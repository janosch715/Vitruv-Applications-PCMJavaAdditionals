package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.PcmPackage;
import org.palladiosimulator.pcm.repository.Repository;

public class PcmUtils {
	
	@SuppressWarnings("unchecked")
	public static <T extends EObject> List<T> getObjects(Repository pcmModel, Class<T> type) {
		List<T> results = new ArrayList<T>();
		TreeIterator<EObject> it = pcmModel.eAllContents();
		while(it.hasNext()) {
			EObject eo = it.next();
			if (type.isInstance(eo)) {
				results.add((T)eo);
			}
		}
		return results;
	}
	
	public static void saveModel(String filePath, Repository repository) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException e1) {
		}
		// Initialize package.
		PcmPackage.eINSTANCE.eClass();
				
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				Resource.Factory.Registry.DEFAULT_EXTENSION,
				new XMIResourceFactoryImpl());

		URI filePathUri = URI.createFileURI(filePath);
		Resource resource = resourceSet.createResource(filePathUri);
		resource.getContents().add(repository);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Repository loadModel(String filePath) {
		// Initialize package.
		PcmPackage.eINSTANCE.eClass();
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				Resource.Factory.Registry.DEFAULT_EXTENSION,
				new XMIResourceFactoryImpl());

		URI filePathUri = org.eclipse.emf.common.util.URI.createFileURI(filePath);
		
		Resource resource = resourceSet.getResource(filePathUri, true);
        return (Repository) resource.getContents().get(0);
	}
}
