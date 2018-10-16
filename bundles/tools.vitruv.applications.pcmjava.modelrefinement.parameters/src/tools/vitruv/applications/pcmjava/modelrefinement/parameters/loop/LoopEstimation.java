package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.LoopAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

public interface LoopEstimation {

	double estimateIterations(LoopAction loop, ServiceCall serviceCall);

	void applyEstimations(Repository pcmModel);

}