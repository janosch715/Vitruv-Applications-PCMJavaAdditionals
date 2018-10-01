package mir.reactions.packageMappingIntegration;

import mir.routines.packageMappingIntegration.RoutinesFacade;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractReactionRealization;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;
import tools.vitruv.framework.change.echange.EChange;
import tools.vitruv.framework.change.echange.eobject.CreateEObject;
import tools.vitruv.framework.change.echange.feature.reference.InsertEReference;

@SuppressWarnings("all")
public class ChangeMethodModifierEventReaction extends AbstractReactionRealization {
  private CreateEObject<EObject> createChange;
  
  private InsertEReference<Method, AnnotationInstanceOrModifier> insertChange;
  
  private int currentlyMatchedChange;
  
  public ChangeMethodModifierEventReaction(final RoutinesFacade routinesFacade) {
    super(routinesFacade);
  }
  
  public void executeReaction(final EChange change) {
    if (!checkPrecondition(change)) {
    	return;
    }
    org.emftext.language.java.members.Method affectedEObject = insertChange.getAffectedEObject();
    EReference affectedFeature = insertChange.getAffectedFeature();
    org.emftext.language.java.modifiers.AnnotationInstanceOrModifier newValue = insertChange.getNewValue();
    int index = insertChange.getIndex();
    				
    getLogger().trace("Passed complete precondition check of Reaction " + this.getClass().getName());
    				
    mir.reactions.packageMappingIntegration.ChangeMethodModifierEventReaction.ActionUserExecution userExecution = new mir.reactions.packageMappingIntegration.ChangeMethodModifierEventReaction.ActionUserExecution(this.executionState, this);
    userExecution.callRoutine1(insertChange, affectedEObject, affectedFeature, newValue, index, this.getRoutinesFacade());
    
    resetChanges();
  }
  
  private void resetChanges() {
    createChange = null;
    insertChange = null;
    currentlyMatchedChange = 0;
  }
  
  private boolean matchCreateChange(final EChange change) {
    if (change instanceof CreateEObject<?>) {
    	CreateEObject<org.eclipse.emf.ecore.EObject> _localTypedChange = (CreateEObject<org.eclipse.emf.ecore.EObject>) change;
    	if (!(_localTypedChange.getAffectedEObject() instanceof org.eclipse.emf.ecore.EObject)) {
    		return false;
    	}
    	this.createChange = (CreateEObject<org.eclipse.emf.ecore.EObject>) change;
    	return true;
    }
    
    return false;
  }
  
  public boolean checkPrecondition(final EChange change) {
    if (currentlyMatchedChange == 0) {
    	if (!matchCreateChange(change)) {
    		resetChanges();
    		return false;
    	} else {
    		currentlyMatchedChange++;
    	}
    	return false; // Only proceed on the last of the expected changes
    }
    if (currentlyMatchedChange == 1) {
    	if (!matchInsertChange(change)) {
    		resetChanges();
    		checkPrecondition(change); // Reexecute to potentially register this as first change
    		return false;
    	} else {
    		currentlyMatchedChange++;
    	}
    }
    
    return true;
  }
  
  private boolean matchInsertChange(final EChange change) {
    if (change instanceof InsertEReference<?, ?>) {
    	InsertEReference<org.emftext.language.java.members.Method, org.emftext.language.java.modifiers.AnnotationInstanceOrModifier> _localTypedChange = (InsertEReference<org.emftext.language.java.members.Method, org.emftext.language.java.modifiers.AnnotationInstanceOrModifier>) change;
    	if (!(_localTypedChange.getAffectedEObject() instanceof org.emftext.language.java.members.Method)) {
    		return false;
    	}
    	if (!_localTypedChange.getAffectedFeature().getName().equals("annotationsAndModifiers")) {
    		return false;
    	}
    	if (!(_localTypedChange.getNewValue() instanceof org.emftext.language.java.modifiers.AnnotationInstanceOrModifier)) {
    		return false;
    	}
    	this.insertChange = (InsertEReference<org.emftext.language.java.members.Method, org.emftext.language.java.modifiers.AnnotationInstanceOrModifier>) change;
    	return true;
    }
    
    return false;
  }
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public void callRoutine1(final InsertEReference insertChange, final Method affectedEObject, final EReference affectedFeature, final AnnotationInstanceOrModifier newValue, final int index, @Extension final RoutinesFacade _routinesFacade) {
      _routinesFacade.changedMethodModifierEvent(affectedEObject, newValue);
    }
  }
}
