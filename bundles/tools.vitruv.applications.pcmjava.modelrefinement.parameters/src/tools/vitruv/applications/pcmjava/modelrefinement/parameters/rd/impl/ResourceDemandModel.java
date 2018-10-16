package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

public interface ResourceDemandModel {

	double estimate(ServiceCall serviceCall);

	String getResourceDemandStochasticExpression();
}