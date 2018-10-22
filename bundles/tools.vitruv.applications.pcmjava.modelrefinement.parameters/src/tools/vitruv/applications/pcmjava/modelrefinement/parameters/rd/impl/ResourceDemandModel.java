package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

/**
 * Estimated model for a resource demand.
 * 
 * @author JP
 *
 */
public interface ResourceDemandModel {

    /**
     * Predicts the resource demand in seconds for this resource demand based on a service call context.
     * 
     * @param serviceCall
     *            Context information, like service call parameters and service execution ID.
     * @return A predicted resource demand in seconds for this resource demand.
     */
    double predictResourceDemand(ServiceCall serviceCall);

    /**
     * Gets the stochastic expression of this estimated model.
     * 
     * @return The stochastic expression string.
     */
    String getResourceDemandStochasticExpression();
}