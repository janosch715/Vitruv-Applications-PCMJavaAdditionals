package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResourceDemandPrediction;

public class ResourceDemandPredictionMock implements ResourceDemandPrediction {
	@Override
	public double estimateResourceDemand(String internalActionId, String resourceId, ServiceCall serviceCall) {
		return 1.0;
	}
}