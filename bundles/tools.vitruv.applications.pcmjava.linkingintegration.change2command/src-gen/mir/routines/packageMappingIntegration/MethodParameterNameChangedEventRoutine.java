package mir.routines.packageMappingIntegration;

import java.io.IOException;
import mir.routines.packageMappingIntegration.RoutinesFacade;
import org.eclipse.emf.ecore.EObject;
import org.emftext.language.java.parameters.Parameter;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;
import tools.vitruv.framework.userinteraction.UserInteractionType;

@SuppressWarnings("all")
public class MethodParameterNameChangedEventRoutine extends AbstractRepairRoutineRealization {
  private MethodParameterNameChangedEventRoutine.ActionUserExecution userExecution;
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public EObject getElement1(final Parameter parameter, final String oldParameterName, final String newParameterName, final org.palladiosimulator.pcm.repository.Parameter pcmParam) {
      return pcmParam;
    }
    
    public void update0Element(final Parameter parameter, final String oldParameterName, final String newParameterName, final org.palladiosimulator.pcm.repository.Parameter pcmParam) {
      this.userInteracting.showMessage(UserInteractionType.MODAL, ((("Renamed method parameter " + oldParameterName) + " to ") + newParameterName));
      pcmParam.setParameterName(newParameterName);
    }
    
    public EObject getCorrepondenceSourcePcmParam(final Parameter parameter, final String oldParameterName, final String newParameterName) {
      return parameter;
    }
  }
  
  public MethodParameterNameChangedEventRoutine(final RoutinesFacade routinesFacade, final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy, final Parameter parameter, final String oldParameterName, final String newParameterName) {
    super(routinesFacade, reactionExecutionState, calledBy);
    this.userExecution = new mir.routines.packageMappingIntegration.MethodParameterNameChangedEventRoutine.ActionUserExecution(getExecutionState(), this);
    this.parameter = parameter;this.oldParameterName = oldParameterName;this.newParameterName = newParameterName;
  }
  
  private Parameter parameter;
  
  private String oldParameterName;
  
  private String newParameterName;
  
  protected boolean executeRoutine() throws IOException {
    getLogger().debug("Called routine MethodParameterNameChangedEventRoutine with input:");
    getLogger().debug("   parameter: " + this.parameter);
    getLogger().debug("   oldParameterName: " + this.oldParameterName);
    getLogger().debug("   newParameterName: " + this.newParameterName);
    
    org.palladiosimulator.pcm.repository.Parameter pcmParam = getCorrespondingElement(
    	userExecution.getCorrepondenceSourcePcmParam(parameter, oldParameterName, newParameterName), // correspondence source supplier
    	org.palladiosimulator.pcm.repository.Parameter.class,
    	(org.palladiosimulator.pcm.repository.Parameter _element) -> true, // correspondence precondition checker
    	null, 
    	false // asserted
    	);
    if (pcmParam == null) {
    	return false;
    }
    registerObjectUnderModification(pcmParam);
    // val updatedElement userExecution.getElement1(parameter, oldParameterName, newParameterName, pcmParam);
    userExecution.update0Element(parameter, oldParameterName, newParameterName, pcmParam);
    
    postprocessElements();
    
    return true;
  }
}
