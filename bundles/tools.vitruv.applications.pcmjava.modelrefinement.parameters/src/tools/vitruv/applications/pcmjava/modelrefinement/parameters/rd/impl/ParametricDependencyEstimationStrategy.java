package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl;

import java.util.Map;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;

public interface ParametricDependencyEstimationStrategy {

	ResourceDemandModel estimateResourceDemandModel(String internalActionId, String resourceId,
			Map<ServiceParameters, Double> resourceDemands);

}