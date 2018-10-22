package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.LoopAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.LoopRecord;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.ServiceCallRecord;

/**
 * Interface for loop iteration estimation implementations, which use {@link ServiceCallRecord} and {@link LoopRecord}
 * to estimate loop iterations and update the PCM.
 * 
 * @author JP
 *
 */
public interface LoopEstimation {

    /**
     * Updates the stochastic expressions of loop iterations of {@link LoopAction}, based on the monitored service call
     * parameters and loop records.
     * 
     * @param pcmModel
     *            The model which will be updated.
     * @param serviceCalls
     *            The monitored service calls, including service call parameters.
     * @param loopIterations
     *            The monitored loop records.
     */
    void update(Repository pcmModel, ServiceCallDataSet serviceCalls, LoopDataSet loopIterations);
}