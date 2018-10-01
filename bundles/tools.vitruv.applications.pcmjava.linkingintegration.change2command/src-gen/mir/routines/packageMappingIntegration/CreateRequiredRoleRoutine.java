package mir.routines.packageMappingIntegration;

import java.io.IOException;
import mir.routines.packageMappingIntegration.RoutinesFacade;
import org.eclipse.emf.ecore.EObject;
import org.emftext.language.java.members.Field;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;
import tools.vitruv.framework.userinteraction.UserInteractionType;

@SuppressWarnings("all")
public class CreateRequiredRoleRoutine extends AbstractRepairRoutineRealization {
  private CreateRequiredRoleRoutine.ActionUserExecution userExecution;
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public EObject getElement1(final BasicComponent basicComponent, final OperationInterface opInterface, final Field field, final OperationRequiredRole opRequiredRole) {
      return opRequiredRole;
    }
    
    public void update0Element(final BasicComponent basicComponent, final OperationInterface opInterface, final Field field, final OperationRequiredRole opRequiredRole) {
      opRequiredRole.setRequiredInterface__OperationRequiredRole(opInterface);
      opRequiredRole.setRequiringEntity_RequiredRole(basicComponent);
      String _entityName = basicComponent.getEntityName();
      String _plus = ("Create OperationRequiredRole between Component " + _entityName);
      String _plus_1 = (_plus + " and Interface ");
      String _entityName_1 = opInterface.getEntityName();
      String _plus_2 = (_plus_1 + _entityName_1);
      this.userInteracting.showMessage(UserInteractionType.MODAL, _plus_2);
    }
    
    public EObject getElement2(final BasicComponent basicComponent, final OperationInterface opInterface, final Field field, final OperationRequiredRole opRequiredRole) {
      return field;
    }
    
    public EObject getElement3(final BasicComponent basicComponent, final OperationInterface opInterface, final Field field, final OperationRequiredRole opRequiredRole) {
      return opRequiredRole;
    }
  }
  
  public CreateRequiredRoleRoutine(final RoutinesFacade routinesFacade, final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy, final BasicComponent basicComponent, final OperationInterface opInterface, final Field field) {
    super(routinesFacade, reactionExecutionState, calledBy);
    this.userExecution = new mir.routines.packageMappingIntegration.CreateRequiredRoleRoutine.ActionUserExecution(getExecutionState(), this);
    this.basicComponent = basicComponent;this.opInterface = opInterface;this.field = field;
  }
  
  private BasicComponent basicComponent;
  
  private OperationInterface opInterface;
  
  private Field field;
  
  protected boolean executeRoutine() throws IOException {
    getLogger().debug("Called routine CreateRequiredRoleRoutine with input:");
    getLogger().debug("   basicComponent: " + this.basicComponent);
    getLogger().debug("   opInterface: " + this.opInterface);
    getLogger().debug("   field: " + this.field);
    
    org.palladiosimulator.pcm.repository.OperationRequiredRole opRequiredRole = org.palladiosimulator.pcm.repository.impl.RepositoryFactoryImpl.eINSTANCE.createOperationRequiredRole();
    notifyObjectCreated(opRequiredRole);
    
    addCorrespondenceBetween(userExecution.getElement1(basicComponent, opInterface, field, opRequiredRole), userExecution.getElement2(basicComponent, opInterface, field, opRequiredRole), "");
    
    // val updatedElement userExecution.getElement3(basicComponent, opInterface, field, opRequiredRole);
    userExecution.update0Element(basicComponent, opInterface, field, opRequiredRole);
    
    postprocessElements();
    
    return true;
  }
}
