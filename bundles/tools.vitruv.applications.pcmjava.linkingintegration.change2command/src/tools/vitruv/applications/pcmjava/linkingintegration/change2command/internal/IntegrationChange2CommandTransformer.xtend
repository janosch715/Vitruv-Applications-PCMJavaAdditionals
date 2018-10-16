package tools.vitruv.applications.pcmjava.linkingintegration.change2command.internal

import tools.vitruv.framework.tuid.Tuid
import tools.vitruv.framework.userinteraction.UserInteractionType
import tools.vitruv.framework.userinteraction.UserInteracting
import tools.vitruv.framework.correspondence.Correspondence
import tools.vitruv.framework.util.bridges.CollectionBridge
import java.util.ArrayList
import java.util.HashSet
import java.util.Set
import java.util.concurrent.Callable
import org.eclipse.emf.ecore.EObject
import org.emftext.language.java.classifiers.Class
import org.emftext.language.java.classifiers.Interface
import org.emftext.language.java.containers.CompilationUnit

import org.palladiosimulator.pcm.core.entity.NamedElement
import tools.vitruv.framework.change.echange.EChange
import tools.vitruv.framework.change.echange.root.InsertRootEObject
import tools.vitruv.framework.change.echange.feature.reference.InsertEReference
import tools.vitruv.framework.change.echange.root.RemoveRootEObject
import tools.vitruv.framework.change.echange.feature.FeatureEChange
import tools.vitruv.framework.correspondence.CorrespondenceModel
import tools.vitruv.extensions.integration.correspondence.integration.IntegrationCorrespondence;
import mir.reactions.packageMappingIntegration.ReactionsExecutor;
//import mir.reactions.reactionsJavaToPcm.packageMappingIntegration.ExecutorJavaToPcm
import tools.vitruv.framework.util.command.ResourceAccess

class IntegrationChange2CommandTransformer {
	
	private UserInteracting userInteracting
	
	new(UserInteracting userInteracting) {
		this.userInteracting = userInteracting
	}
	
	def compute(EChange change, CorrespondenceModel correspondenceModel, ResourceAccess resourceAccess) {
		return executeIntegration(change, correspondenceModel, resourceAccess)
	}
	
	private def boolean executeIntegration(EChange change, CorrespondenceModel correspondenceModel, ResourceAccess resourceAccess) {
		// Since all correspondences are considered (not only IntegrationCorrespondences),
		// only return reaction commands, if one of the other 2 checks are successful
		val existsReaction = doesReactionHandleChange(change, correspondenceModel); 
		val newClassOrInterfaceInIntegratedAreaCommand = createNewClassOrInterfaceInIntegratedAreaCommand(
			change, correspondenceModel)
    	if (newClassOrInterfaceInIntegratedAreaCommand !== null) {
    		if (existsReaction) {
				executeReactions(change, correspondenceModel, resourceAccess);
				return true;
			}
			newClassOrInterfaceInIntegratedAreaCommand.call()
    		return true;
    	}
    	val defaultIntegrationChangeCommand = getDefaultIntegrationChangeCommand(change, correspondenceModel)
    	if (defaultIntegrationChangeCommand !== null) {
    		if (existsReaction) {
				executeReactions(change, correspondenceModel, resourceAccess);
				return true;
			}
    		defaultIntegrationChangeCommand.call()
    		return true
    	}
    	return false
	}
	
	def doesReactionHandleChange(EChange change, CorrespondenceModel correspondenceModel) {
		//val executor = new ExecutorJavaToPcm()
		val executor = new ReactionsExecutor();
		executor.userInteracting = userInteracting
		return executor.doesHandleChange(change, correspondenceModel);
	}
	
	def executeReactions(EChange change, CorrespondenceModel correspondenceModel, ResourceAccess resourceAccess) {
		//val executor = new ExecutorJavaToPcm()
		val executor = new ReactionsExecutor();
		executor.userInteracting = userInteracting
		executor.propagateChange(change, correspondenceModel, resourceAccess)
	}
	
	private def createNewClassOrInterfaceInIntegratedAreaCommand(EChange eChange, CorrespondenceModel correspondenceModel) {
        if (eChange instanceof InsertEReference<?,?> && (eChange as InsertEReference<?,?>).isContainment()) { 
        	//Check if this is a creation of a class or interface on file level.
        	//In this case we need to check if any siblings in the package have been integrated
        	val change = eChange as InsertEReference<?,?>
        	val classOfAffected = change.getAffectedEObject().eClass().getInstanceClass()
        	val classOfCreated = change.getNewValue().eClass().getInstanceClass()
        	if (classOfAffected == typeof(CompilationUnit) && 
        			(classOfCreated.equals(typeof(Class)) || 
        			 classOfCreated.equals(typeof(Interface)))) {
        		val cu = change.getAffectedEObject() as CompilationUnit
        		//TODO use IntegrationCorrespondence view of InternalCorrespondenceModel which is
        		//statically typed to Correspondence right now and needs to support views like 
        		//CorrespondenceModel
                val ci = correspondenceModel
                val newCompilationUnitTuid = ci.calculateTuidFromEObject(cu)
                val packagePartOfNewTuid = getPackagePart(newCompilationUnitTuid)
    			for (Correspondence corr : ci.getAllCorrespondences()) {
    				if (corr instanceof IntegrationCorrespondence) {
    					val integrationCorr = corr as IntegrationCorrespondence
	    				if (integrationCorr.isCreatedByIntegration()) {
	    					val allTuids = new ArrayList<Tuid>()
	    					allTuids.addAll(corr.getATuids())
	    					allTuids.addAll(corr.getBTuids())
	    					for (Tuid tuid : allTuids) {
	    						val packagePart = getPackagePart(tuid)
	    						if (packagePartOfNewTuid.startsWith(packagePart)) {
	    							val command = new Callable<Void>() {
										override call() throws Exception {
											showNewTypeInIntegratedAreaDialog()
											return null
										}
									}
	    							return command
	    						}
	    					}
	    				}
    				}
    			}
        	}
    	} 
        return null
	}
	
	private def String getPackagePart(Tuid newCompilationUnitTuid) {
		val originalTuidAsString = newCompilationUnitTuid.toString()
		val lastSlashIndex = originalTuidAsString.lastIndexOf("/")
		return originalTuidAsString.substring(0, lastSlashIndex)
	}
	
	private def showNewTypeInIntegratedAreaDialog() {
		val buffer = new StringBuffer()
		buffer.append("Created class or interface in area with integrated objects.\n")
		buffer.append("Please handle consistency manually.")
		userInteracting.showMessage(UserInteractionType.MODAL, buffer.toString())
	}
	
	private def getDefaultIntegrationChangeCommand(EChange eChange, CorrespondenceModel correspondenceModel) {
        val correspondingIntegratedEObjects = getCorrespondingEObjectsIfIntegrated(eChange, correspondenceModel)
        if (correspondingIntegratedEObjects !== null) {
	    	val buffer = new StringBuffer()
	    	buffer.append("Elements in change were integrated into Vitruvius.\n")
	    	buffer.append("Please fix manually. Corresponding object(s):\n")
	    	for (EObject obj : correspondingIntegratedEObjects) {
	    		val name = getReadableName(obj)
	    		buffer.append("\n")
	    		buffer.append(name)
	    	}
			val command = new Callable<Void>() {
					override call() throws Exception {
						userInteracting.showMessage(UserInteractionType.MODAL, buffer.toString())
						return null
					}
				}
	    	return command
        } 
        return null
	}
	
	private def getReadableName(EObject obj) {
    	var name = getDirectNameIfNamed(obj)
		val className = obj.eClass().getName()
    	var container = obj.eContainer()
    	while (name === null) {
    		if (container === null) {
    			name = className
    		} else {
    			val containerName = getDirectNameIfNamed(container)
    			if (containerName !== null) {
    				val containerClassName = container.eClass().getName()
					name = className + " in " + containerClassName + ": " + containerName
    			} else {
    				container = container.eContainer()
    			}
    		}
    	}
		return name
	}

	private def getDirectNameIfNamed(EObject obj) {
		var String name = null
		val className = obj.eClass().getName()
		if (obj instanceof NamedElement) {
			val named = obj as NamedElement
			name =  className + ": " + named.getEntityName()
		} else if (obj instanceof org.emftext.language.java.commons.NamedElement) {
			val named = obj as org.emftext.language.java.commons.NamedElement
			name =  className + ": " + named.getName()
		}
		return name
	}

	/**
     * 
     * @return set of corresponding EObjects if integrated, else null
     */
    private def getCorrespondingEObjectsIfIntegrated(EChange eChange,
            CorrespondenceModel correspondenceModel) {
        val ci = correspondenceModel
        var EObject eObj = null
        if (eChange instanceof FeatureEChange<?,?>) {
            eObj = eChange.getAffectedEObject() as EObject
        } else if (eChange instanceof InsertRootEObject<?>) {
            eObj = eChange.getNewValue() as EObject
        } else if (eChange instanceof RemoveRootEObject<?>) {
            eObj = eChange.getOldValue() as EObject
        }

        val  integrationView = ci.getView(typeof(IntegrationCorrespondence))
        if (eObj !== null) {
            val set = CollectionBridge.toSet(eObj)
            val Set<IntegrationCorrespondence> correspondences = integrationView
                    .getCorrespondencesThatInvolveAtLeast(set)
            val correspondingObjects = new HashSet<EObject>()
            for (integratedCorrespondence : correspondences) {
                if (integratedCorrespondence.isCreatedByIntegration()) {
                	val aList = integratedCorrespondence.getAs()
                	val bList = integratedCorrespondence.getAs()
                	if (aList.contains(eObj)) {
                		correspondingObjects.addAll(bList)
                	} else {
                		correspondingObjects.addAll(aList)
                	}
                	return correspondingObjects
                }
            }
        }
        return null
    }
	
}
