package mir.routines.packageMappingIntegration;

import java.io.IOException;
import mir.routines.packageMappingIntegration.RoutinesFacade;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emftext.language.java.members.Method;
import org.palladiosimulator.pcm.repository.OperationSignature;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;

@SuppressWarnings("all")
public class RemovedMethodEventRoutine extends AbstractRepairRoutineRealization {
  private RemovedMethodEventRoutine.ActionUserExecution userExecution;
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public EObject getElement1(final Method method, final OperationSignature opSig) {
      return opSig;
    }
    
    public void update0Element(final Method method, final OperationSignature opSig) {
      EcoreUtil.remove(opSig);
    }
    
    public EObject getElement2(final Method method, final OperationSignature opSig) {
      return method;
    }
    
    public EObject getElement3(final Method method, final OperationSignature opSig) {
      return opSig;
    }
    
    public EObject getCorrepondenceSourceOpSig(final Method method) {
      return method;
    }
  }
  
  public RemovedMethodEventRoutine(final RoutinesFacade routinesFacade, final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy, final Method method) {
    super(routinesFacade, reactionExecutionState, calledBy);
    this.userExecution = new mir.routines.packageMappingIntegration.RemovedMethodEventRoutine.ActionUserExecution(getExecutionState(), this);
    this.method = method;
  }
  
  private Method method;
  
  protected boolean executeRoutine() throws IOException {
    getLogger().debug("Called routine RemovedMethodEventRoutine with input:");
    getLogger().debug("   method: " + this.method);
    
    org.palladiosimulator.pcm.repository.OperationSignature opSig = getCorrespondingElement(
    	userExecution.getCorrepondenceSourceOpSig(method), // correspondence source supplier
    	org.palladiosimulator.pcm.repository.OperationSignature.class,
    	(org.palladiosimulator.pcm.repository.OperationSignature _element) -> true, // correspondence precondition checker
    	null, 
    	false // asserted
    	);
    if (opSig == null) {
    	return false;
    }
    registerObjectUnderModification(opSig);
    removeCorrespondenceBetween(userExecution.getElement1(method, opSig), userExecution.getElement2(method, opSig), "");
    
    // val updatedElement userExecution.getElement3(method, opSig);
    userExecution.update0Element(method, opSig);
    
    postprocessElements();
    
    return true;
  }
}
