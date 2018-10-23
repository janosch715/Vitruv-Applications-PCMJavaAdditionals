package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchPrediction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopPrediction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResourceDemandPrediction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationEstimation;

/**
 * Logic for estimating the utilization of monitored and not monitored internal actions. All service calls are
 * considered and for each service call its SEFF is traversed to predict the resource demand of the service call. The
 * resulting resource demands are used to estimate the monitored and not monitored resource utilization.
 * 
 * @author JP
 *
 */
public class ResourceUtilizationEstimationImpl implements ResourceUtilizationEstimation {

    private final Set<String> ignoredInternalActionIds;

    private final Repository pcm;

    private final Map<String, ResourceDemandingSEFF> seffIdToSeff;

    private final ServiceCallDataSet serviceCalls;

    private final LoopPrediction loopPredictionn;

    private final BranchPrediction branchPrediction;

    private final ResourceDemandPrediction rdPrediction;

    private final SortedMap<Long, List<Map<String, Double>>> estimations;

    private long firstServiceCallOn = Long.MAX_VALUE;

    private long lastServiceCallOn = Long.MIN_VALUE;

    /**
     * Initializes a new instance of {@link ResourceUtilizationEstimationImpl}.
     * 
     * @param ignoredInternalActionIds
     *            Monitored internal action ids.
     * @param pcm
     *            The PCM repository.
     * 
     * @param serviceCalls
     *            The service call data set.
     * @param loopPredictionn
     *            The loop prediction.
     * @param branchPrediction
     *            The branch prediction.
     * @param rdPrediction
     *            The resource demand prediction.
     */
    public ResourceUtilizationEstimationImpl(final Set<String> ignoredInternalActionIds, final Repository pcm,
            final ServiceCallDataSet serviceCalls, final LoopPrediction loopPredictionn,
            final BranchPrediction branchPrediction,
            final ResourceDemandPrediction rdPrediction) {

        this.ignoredInternalActionIds = ignoredInternalActionIds;
        this.pcm = pcm;
        this.serviceCalls = serviceCalls;
        this.loopPredictionn = loopPredictionn;
        this.branchPrediction = branchPrediction;
        this.rdPrediction = rdPrediction;
        this.estimations = new TreeMap<>();

        this.seffIdToSeff = this.pcm.getComponents__Repository().stream()
                .filter(BasicComponent.class::isInstance).map(component -> (BasicComponent) component)
                .flatMap(component -> component.getServiceEffectSpecifications__BasicComponent().stream())
                .filter(ResourceDemandingSEFF.class::isInstance).map(component -> (ResourceDemandingSEFF) component)
                .collect(Collectors.toMap(seff -> seff.getId(), seff -> seff));

        this.estimateAllResourceDemands();
    }

    @Override
    public ResourceUtilizationDataSet estimateRemainingUtilization(
            final ResourceUtilizationDataSet completeResourceUtilization) {
        ResourceUtilizationDataSetImpl results = new ResourceUtilizationDataSetImpl(
                completeResourceUtilization::timeToSeconds);

        for (String resourceId : completeResourceUtilization.getResourceIds()) {
            SortedMap<Long, Double> remainingUtilization = this.estimateRemainingUtilization(resourceId,
                    completeResourceUtilization.getUtilization(resourceId), completeResourceUtilization::timeToSeconds);
            results.put(resourceId, remainingUtilization);
        }

        return results;
    }

    private SortedMap<Long, Double> estimateRemainingUtilization(final String resourceId,
            final SortedMap<Long, Double> completeResourceUtilization, final Function<Long, Double> timeToSeconds) {

        SortedMap<Long, Double> monitoredActionsUtilization = new TreeMap<>();

        Entry<Long, Double> lastUtilizationRecord = null;
        for (Entry<Long, Double> utilizationRecord : completeResourceUtilization.entrySet()) {

            double currentUtilization = utilizationRecord.getValue();

            if (lastUtilizationRecord != null) {
                Map<String, Double> currentNotMonitoredUtilization = this
                        .estimateUtilization(lastUtilizationRecord.getKey(), utilizationRecord.getKey(), timeToSeconds);

                Double utilization = currentNotMonitoredUtilization.get(resourceId);
                if (utilization != null) {
                    currentUtilization = currentUtilization - utilization;
                    currentUtilization = Math.max(currentUtilization, 0.0);
                }
            }

            monitoredActionsUtilization.put(utilizationRecord.getKey(), currentUtilization);
            lastUtilizationRecord = utilizationRecord;
        }

        return monitoredActionsUtilization;
    }

    private Map<String, Double> estimateUtilization(final long fromInclusive, final long toExclusive,
            final Function<Long, Double> timeToSeconds) {
        Map<String, Double> utilization = new HashMap<>();
        for (Entry<Long, List<Map<String, Double>>> serviceCallRdEstimations : this.estimations
                .subMap(fromInclusive, toExclusive).entrySet()) {
            for (Map<String, Double> serviceCallRdEstimation : serviceCallRdEstimations.getValue()) {
                addResourceDemands(utilization, serviceCallRdEstimation);
            }
        }
        long interval = toExclusive - fromInclusive;
        double intervalSeconds = timeToSeconds.apply(interval);
        multiplyResourceDemands(utilization, 1.0 / intervalSeconds);
        return utilization;
    }

    private void estimateAllResourceDemands() {
        for (ServiceCall serviceCall : this.serviceCalls.getServiceCalls()) {
            Map<String, Double> estimatedRds = this.estimateResourceDemand(serviceCall);

            long entryTime = serviceCall.getEntryTime();
            List<Map<String, Double>> rdsForTime = this.estimations.get(entryTime);
            if (rdsForTime == null) {
                rdsForTime = new ArrayList<>();
                this.estimations.put(entryTime, rdsForTime);
            }
            rdsForTime.add(estimatedRds);

            this.firstServiceCallOn = Math.min(this.firstServiceCallOn, entryTime);

            this.lastServiceCallOn = Math.max(this.lastServiceCallOn, entryTime);
        }
    }

    private AbstractAction estimateBranchResourceDemand(final BranchAction branchAction, final ServiceCall serviceCall,
            final Map<String, Double> resourceDemands) {
        Optional<AbstractBranchTransition> estimatedBranch = this.branchPrediction.predictTransition(branchAction,
                serviceCall);
        if (estimatedBranch.isPresent()) {
            ResourceDemandingBehaviour branchSeff = estimatedBranch.get().getBranchBehaviour_BranchTransition();
            this.estimateSeffResourceDemand(branchSeff, serviceCall, resourceDemands);
        }
        return branchAction.getSuccessor_AbstractAction();
    }

    private AbstractAction estimateInternalActionResourceDemand(final InternalAction internalAction,
            final ServiceCall serviceCall,
            final Map<String, Double> resourceDemands) {
        if (this.ignoredInternalActionIds.contains(internalAction.getId()) == false) {
            for (ParametricResourceDemand rd : internalAction.getResourceDemand_Action()) {
                double estimatedRd = this.rdPrediction.predictResourceDemand(rd, serviceCall);
                addResourceDemands(resourceDemands, rd.getRequiredResource_ParametricResourceDemand().getId(),
                        estimatedRd);
            }
        }
        return internalAction.getSuccessor_AbstractAction();
    }

    private AbstractAction estimateLoopResourceDemand(final LoopAction loopAction, final ServiceCall serviceCall,
            final Map<String, Double> resourceDemands) {
        double iterations = this.loopPredictionn.estimateIterations(loopAction, serviceCall);
        Map<String, Double> innerLoopResourceDemands = new HashMap<>();
        ResourceDemandingBehaviour loopSeff = loopAction.getBodyBehaviour_Loop();
        this.estimateSeffResourceDemand(loopSeff, serviceCall, innerLoopResourceDemands);
        multiplyResourceDemands(innerLoopResourceDemands, iterations);
        addResourceDemands(resourceDemands, innerLoopResourceDemands);
        return loopAction.getSuccessor_AbstractAction();
    }

    private void estimateResourceDemand(final AbstractAction action, final ServiceCall serviceCall,
            final Map<String, Double> resourceDemands) {
        AbstractAction currentAction = action;
        while (true) {
            if (currentAction instanceof BranchAction) {
                currentAction = this.estimateBranchResourceDemand((BranchAction) currentAction, serviceCall,
                        resourceDemands);
            } else if (currentAction instanceof LoopAction) {
                currentAction = this.estimateLoopResourceDemand((LoopAction) currentAction, serviceCall,
                        resourceDemands);
            } else if (currentAction instanceof InternalAction) {
                currentAction = this.estimateInternalActionResourceDemand((InternalAction) currentAction, serviceCall,
                        resourceDemands);
            } else if (currentAction instanceof ExternalCallAction) {
                currentAction = currentAction.getSuccessor_AbstractAction();
            } else if (currentAction instanceof StopAction) {
                return;
            } else {
                throw new UnsupportedOperationException(
                        "Dont know how to handle " + currentAction.eClass().getName() + ".");
            }
        }
    }

    private Map<String, Double> estimateResourceDemand(final ServiceCall serviceCall) {
        ResourceDemandingBehaviour seff = this.seffIdToSeff.get(serviceCall.getServiceId());
        if (seff == null) {
            throw new IllegalArgumentException("No seff for service id " + serviceCall.getServiceId() + " found.");
        }
        Map<String, Double> resourceDemands = new HashMap<>();
        this.estimateSeffResourceDemand(seff, serviceCall, resourceDemands);
        return resourceDemands;
    }

    private void estimateSeffResourceDemand(final ResourceDemandingBehaviour seff, final ServiceCall serviceCall,
            final Map<String, Double> resourceDemands) {
        StartAction startAction = findStartAction(seff);
        this.estimateResourceDemand(startAction.getSuccessor_AbstractAction(), serviceCall, resourceDemands);
    }

    private static void addResourceDemands(final Map<String, Double> rds, final Map<String, Double> rdsToAdd) {
        for (Entry<String, Double> rd : rdsToAdd.entrySet()) {
            addResourceDemands(rds, rd.getKey(), rd.getValue());
        }
    }

    private static void addResourceDemands(final Map<String, Double> rds, final String resourceId,
            final double resourceDemand) {
        Double rdValue = rds.get(resourceId);
        if (rdValue == null) {
            rdValue = 0.0;
        }
        rdValue += resourceDemand;
        rds.put(resourceId, rdValue);
    }

    private static StartAction findStartAction(final ResourceDemandingBehaviour seff) {
        List<StartAction> foundStartActions = seff.getSteps_Behaviour().stream().filter(StartAction.class::isInstance)
                .map(startAction -> (StartAction) startAction).collect(Collectors.toList());

        if (foundStartActions.size() == 0) {
            throw new IllegalArgumentException("The seff " + seff.getId() + " does not have a start action.");
        }

        if (foundStartActions.size() > 1) {
            throw new IllegalArgumentException("The seff " + seff.getId() + " has multiple start actions.");
        }

        return foundStartActions.get(0);
    }

    private static void multiplyResourceDemands(final Map<String, Double> rds, final double factor) {
        for (Entry<String, Double> rd : rds.entrySet()) {
            rds.put(rd.getKey(), rd.getValue() * factor);
        }
    }

    private static class ResourceUtilizationDataSetImpl implements ResourceUtilizationDataSet {

        private final Function<Long, Double> timeToSeconds;
        private final Map<String, SortedMap<Long, Double>> utilization;

        public ResourceUtilizationDataSetImpl(final Function<Long, Double> timeToSeconds) {
            this.utilization = new HashMap<>();
            this.timeToSeconds = timeToSeconds;
        }

        @Override
        public Set<String> getResourceIds() {
            return this.utilization.keySet();
        }

        @Override
        public SortedMap<Long, Double> getUtilization(final String resourceId) {
            return this.utilization.get(resourceId);
        }

        public void put(final String resourceId, final SortedMap<Long, Double> utilization) {
            this.utilization.put(resourceId, utilization);
        }

        @Override
        public double timeToSeconds(final long time) {
            return this.timeToSeconds.apply(time);
        }
    }
}
