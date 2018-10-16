package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

public interface LoopModel {

	double estimateIterations(ServiceCall serviceCall);

	String getIterationsStochasticExpression();

}