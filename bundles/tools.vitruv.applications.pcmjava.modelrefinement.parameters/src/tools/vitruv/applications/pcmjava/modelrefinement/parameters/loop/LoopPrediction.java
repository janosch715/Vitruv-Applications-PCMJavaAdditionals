package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import org.palladiosimulator.pcm.seff.LoopAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

public interface LoopPrediction {

	double estimateIterations(LoopAction loop, ServiceCall serviceCall);

}