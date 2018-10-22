package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.impl.BranchEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl.LoopEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl.ResourceDemandEstimationImpl;

/**
 * This is the main entry point for estimating SEFF parameters, like loop iterations, branch executions and resource
 * demands, based on monitoring data.
 *
 * @author JP
 *
 */
public class SeffParameterEstimation {

    private final LoopEstimationImpl loopEstimation;

    private final BranchEstimationImpl branchEstimation;

    private final ResourceDemandEstimationImpl resourceDemandEstimation;

    /**
     * Initializes a new instance of {@link SeffParameterEstimation}.
     */
    public SeffParameterEstimation() {
        this.loopEstimation = new LoopEstimationImpl();
        this.branchEstimation = new BranchEstimationImpl();
        this.resourceDemandEstimation = new ResourceDemandEstimationImpl(this.loopEstimation, this.branchEstimation);
    }

    /**
     * Updates the SEFF parameters in the PCM based on the monitoring data.
     *
     * @param pcm
     *            The Palladio Component Model Repository, including the SEFFs of the services we will estimate
     *            parameters for.
     * @param monitoringDataSet
     *            We use this monitoring data for estimating the SEFF Parameters.
     */
    public void update(final Repository pcm, final MonitoringDataSet monitoringDataSet) {
        this.loopEstimation.update(pcm, monitoringDataSet.getServiceCalls(), monitoringDataSet.getLoops());
        this.branchEstimation.update(pcm, monitoringDataSet.getServiceCalls(), monitoringDataSet.getBranches());
        this.resourceDemandEstimation.update(pcm, monitoringDataSet.getServiceCalls(),
                monitoringDataSet.getResourceUtilizations(), monitoringDataSet.getResponseTimes());
    }
}
