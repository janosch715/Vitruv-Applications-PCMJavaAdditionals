package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch;

import java.util.Optional;

import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

public interface BranchPrediction {

	Optional<AbstractBranchTransition> estimateBranch(BranchAction branch, ServiceCall serviceCall);

}