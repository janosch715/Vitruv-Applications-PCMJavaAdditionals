package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.LoopAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopEstimation;

public class LoopEstimationMock implements LoopEstimation {
	@Override
	public double estimateIterations(LoopAction loop, ServiceCall serviceCall) {
		return 0;
	}

	@Override
	public void applyEstimations(Repository pcmModel) {
	}
}