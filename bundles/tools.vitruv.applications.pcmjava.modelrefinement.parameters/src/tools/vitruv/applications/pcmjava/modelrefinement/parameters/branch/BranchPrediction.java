package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch;

import java.util.Optional;

import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

/**
 * Interface for general branch prediction.
 * 
 * @author JP
 *
 */
public interface BranchPrediction {

    /**
     * Gets a {@link AbstractBranchTransition} prediction for a branch and service call.
     * 
     * @param branch
     *            The branch a prediction is made for.
     * @param serviceCall
     *            Context information, like service call parameters and service execution ID.
     * @return The predicted branch transition or empty, if the prediction is, that no branch transition is executed.
     */
    Optional<AbstractBranchTransition> predictTransition(BranchAction branch, ServiceCall serviceCall);

}