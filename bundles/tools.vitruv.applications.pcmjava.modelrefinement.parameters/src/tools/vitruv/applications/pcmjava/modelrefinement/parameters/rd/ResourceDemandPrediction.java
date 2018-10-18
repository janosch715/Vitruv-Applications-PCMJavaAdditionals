package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

public interface ResourceDemandPrediction {

    double estimateResourceDemand(String internalActionId, String resourceId, ServiceCall serviceCall);

}