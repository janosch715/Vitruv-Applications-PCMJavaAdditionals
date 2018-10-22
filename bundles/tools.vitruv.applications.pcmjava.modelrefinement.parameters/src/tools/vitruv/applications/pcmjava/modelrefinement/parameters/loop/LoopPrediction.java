package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import org.palladiosimulator.pcm.seff.LoopAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

/**
 * Interface for general loop prediction.
 * 
 * @author JP
 *
 */
public interface LoopPrediction {

    /**
     * Gets a {@link LoopAction} iteration prediction for a loop and service call.
     * 
     * @param loop
     *            The loop a prediction is made for.
     * @param serviceCall
     *            Context information, like service call parameters and service execution ID.
     * @return The predicted loop iterations.
     */
    double estimateIterations(LoopAction loop, ServiceCall serviceCall);

}