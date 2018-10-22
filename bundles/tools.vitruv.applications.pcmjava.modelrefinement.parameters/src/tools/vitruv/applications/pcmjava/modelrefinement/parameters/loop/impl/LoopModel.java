package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

/**
 * Estimated model for a loop.
 * 
 * @author JP
 *
 */
public interface LoopModel {

    /**
     * Predicts the number of loop iterations for this loop based on a service call context.
     * 
     * @param serviceCall
     *            Context information, like service call parameters and service execution ID.
     * @return A predicted number of loop iterations for this loop.
     */
    double predictIterations(ServiceCall serviceCall);

    /**
     * Gets the stochastic expression of this estimated model.
     * 
     * @return The stochastic expression string.
     */
    String getIterationsStochasticExpression();

}