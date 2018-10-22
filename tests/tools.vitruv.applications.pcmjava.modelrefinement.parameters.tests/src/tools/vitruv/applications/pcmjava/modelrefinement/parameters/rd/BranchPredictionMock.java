package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import java.util.Optional;

import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchPrediction;

public class BranchPredictionMock implements BranchPrediction {
    @Override
    public Optional<AbstractBranchTransition> predictTransition(final BranchAction branch, final ServiceCall serviceCall) {
        return Optional.empty();
    }
}