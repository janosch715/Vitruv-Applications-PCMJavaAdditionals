package mir.routines.parserIntegrationReaction;

import java.io.IOException;
import mir.routines.parserIntegrationReaction.RoutinesFacade;
import org.eclipse.emf.ecore.EObject;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.members.Method;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.impl.RepositoryFactoryImpl;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;
import tools.vitruv.framework.correspondence.CorrespondenceModelUtil;

@SuppressWarnings("all")
public class AddedMethodEventParserRoutine extends AbstractRepairRoutineRealization {
  private RoutinesFacade actionsFacade;
  
  private AddedMethodEventParserRoutine.ActionUserExecution userExecution;
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public EObject getCorrepondenceSourceOpInterface(final ConcreteClassifier clazz, final Method method) {
      return clazz;
    }
    
    public void updateOpSigElement(final ConcreteClassifier clazz, final Method method, final OperationInterface opInterface, final OperationSignature opSig) {
      String _name = method.getName();
      opSig.setEntityName(_name);
      opSig.setInterface__OperationSignature(opInterface);
      CorrespondenceModelUtil.createAndAddCorrespondence(this.correspondenceModel, opSig, method);
    }
  }
  
  public AddedMethodEventParserRoutine(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy, final ConcreteClassifier clazz, final Method method) {
    super(reactionExecutionState, calledBy);
    this.userExecution = new mir.routines.parserIntegrationReaction.AddedMethodEventParserRoutine.ActionUserExecution(getExecutionState(), this);
    this.actionsFacade = new mir.routines.parserIntegrationReaction.RoutinesFacade(getExecutionState(), this);
    this.clazz = clazz;this.method = method;
  }
  
  private ConcreteClassifier clazz;
  
  private Method method;
  
  protected void executeRoutine() throws IOException {
    getLogger().debug("Called routine AddedMethodEventParserRoutine with input:");
    getLogger().debug("   ConcreteClassifier: " + this.clazz);
    getLogger().debug("   Method: " + this.method);
    
    OperationInterface opInterface = getCorrespondingElement(
    	userExecution.getCorrepondenceSourceOpInterface(clazz, method), // correspondence source supplier
    	OperationInterface.class,
    	(OperationInterface _element) -> true, // correspondence precondition checker
    	null);
    if (opInterface == null) {
    	return;
    }
    registerObjectUnderModification(opInterface);
    OperationSignature opSig = RepositoryFactoryImpl.eINSTANCE.createOperationSignature();
    userExecution.updateOpSigElement(clazz, method, opInterface, opSig);
    
    postprocessElements();
  }
}
