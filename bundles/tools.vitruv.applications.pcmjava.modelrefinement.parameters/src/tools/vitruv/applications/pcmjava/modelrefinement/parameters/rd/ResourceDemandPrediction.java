package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

/**
 * Interface for general resource demand prediction.
 * 
 * @author JP
 *
 */
public interface ResourceDemandPrediction {

    /**
     * Gets a {@link ParametricResourceDemand} prediction for a resource demand and service call.
     * 
     * @param rd
     *            The resource demand a prediction is made for.
     * @param serviceCall
     *            Context information, like service call parameters and service execution ID.
     * @return The predicted resource demand.
     */
    double predictResourceDemand(ParametricResourceDemand rd, ServiceCall serviceCall);

}