package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import org.palladiosimulator.pcm.seff.LoopAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopPrediction;

public class LoopPredictionMock implements LoopPrediction {
    @Override
    public double estimateIterations(final LoopAction loop, final ServiceCall serviceCall) {
        return 0;
    }
}