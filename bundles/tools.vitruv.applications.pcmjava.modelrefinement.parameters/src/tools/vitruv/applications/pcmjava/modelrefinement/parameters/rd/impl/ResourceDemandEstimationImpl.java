package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchPrediction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopPrediction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResourceDemandEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResourceDemandPrediction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResponseTimeDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.impl.ResourceUtilizationEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class ResourceDemandEstimationImpl implements ResourceDemandEstimation, ResourceDemandPrediction {

	private static final Logger LOGGER = Logger.getLogger(ResourceDemandEstimationImpl.class);
	private final Map<String, Map<String, ResourceDemandModel>> modelCache;
	private final ParametricDependencyEstimationStrategy parametricDependencyEstimationStrategy;
	private final LoopPrediction loopEstimation;
	private final BranchPrediction branchEstimation;

	public ResourceDemandEstimationImpl(LoopPrediction loopEstimation, BranchPrediction branchEstimation) {
		this.modelCache = new HashMap<String, Map<String, ResourceDemandModel>>();
		this.parametricDependencyEstimationStrategy = new WekaParametricDependencyEstimationStrategy();
		this.loopEstimation = loopEstimation;
		this.branchEstimation = branchEstimation;
	}

	@Override
	public void update(Repository pcmModel, ServiceCallDataSet serviceCalls,
			ResourceUtilizationDataSet resourceUtilizations, ResponseTimeDataSet responseTimes) {

		Set<String> internalActionsToEstimate = responseTimes.getInternalActionIds();

		if (internalActionsToEstimate.isEmpty()) {
			LOGGER.info("No internal action records in data set. So resource demand estimation is skipped.");
			return;
		}

		ResourceUtilizationEstimation resourceUtilizationEstimation = new ResourceUtilizationEstimationImpl(
				internalActionsToEstimate, pcmModel, serviceCalls, this.loopEstimation, this.branchEstimation, this);

		ResourceUtilizationDataSet remainingResourceUtilization = resourceUtilizationEstimation
				.estimateRemainingUtilization(resourceUtilizations);

		LibredeResourceDemandEstimation estimation = new LibredeResourceDemandEstimation(
				this.parametricDependencyEstimationStrategy, remainingResourceUtilization, responseTimes, serviceCalls);

		Map<String, Map<String, ResourceDemandModel>> newModels = estimation.estimateResourceDemandModels();

		modelCache.putAll(newModels);

		this.applyEstimations(pcmModel);
	}

	@Override
	public double estimateResourceDemand(String internalActionId, String resourceId, ServiceCall serviceCall) {
		Map<String, ResourceDemandModel> resourceModels = this.modelCache.get(internalActionId);
		if (resourceModels == null) {
			throw new IllegalArgumentException("An estimation for resource demand with internal action id "
					+ internalActionId + " was not found.");
		}
		ResourceDemandModel rdModel = resourceModels.get(resourceId);
		if (rdModel == null) {
			throw new IllegalArgumentException("An estimation for resource demand for resource id " + resourceId
					+ " for internal action id " + internalActionId + " was not found.");
		}
		return rdModel.estimate(serviceCall);
	}

	private void applyEstimations(Repository pcmModel) {
		List<InternalAction> internalActions = PcmUtils.getObjects(pcmModel, InternalAction.class);
		for (InternalAction internalAction : internalActions) {
			this.applyModel(internalAction);
		}
	}

	private void applyModel(InternalAction internalAction) {
		for (ParametricResourceDemand rd : internalAction.getResourceDemand_Action()) {
			this.applyModel(internalAction.getId(), rd);
		}
	}

	private void applyModel(String internalActionId, ParametricResourceDemand rd) {
		Map<String, ResourceDemandModel> internalActionModel = this.modelCache.get(internalActionId);
		if (internalActionModel == null) {
			LOGGER.warn("A estimation for internal action with id " + internalActionId
					+ " was not found. Nothing is set for this internal action.");
			return;
		}

		// TODO: use actual rd id.
		String resourceId = "_oro4gG3fEdy4YaaT-RYrLQ";
		ResourceDemandModel rdModel = internalActionModel.get(resourceId);
		if (rdModel == null) {
			LOGGER.warn("A estimation for internal action with id " + internalActionId + " and resource type id "
					+ resourceId + " was not found. Nothing is set for this resource demand.");
			return;
		}

		String stoEx = rdModel.getResourceDemandStochasticExpression();
		PCMRandomVariable randomVariable = CoreFactory.eINSTANCE.createPCMRandomVariable();
		randomVariable.setSpecification(stoEx);
		rd.setSpecification_ParametericResourceDemand(randomVariable);
	}
}
