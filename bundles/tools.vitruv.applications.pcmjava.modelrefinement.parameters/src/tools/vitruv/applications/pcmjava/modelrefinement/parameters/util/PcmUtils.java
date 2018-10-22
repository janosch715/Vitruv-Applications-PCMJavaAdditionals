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

/**
 * PCM specific utility functions.
 * 
 * @author JP
 *
 */
public class PcmUtils {

    /**
     * Gets all objects in a {@link Repository} of a specific type.
     * 
     * @param <T>
     *            The type of the objects to find.
     * @param pcmModel
     *            The repository which is searched.
     * @param type
     *            The type of the objects to find.
     * @return A list of all found objects or an empty list.
     */
    @SuppressWarnings("unchecked")
    public static <T extends EObject> List<T> getObjects(final Repository pcmModel, final Class<T> type) {
        List<T> results = new ArrayList<>();
        TreeIterator<EObject> it = pcmModel.eAllContents();
        while (it.hasNext()) {
            EObject eo = it.next();
            if (type.isInstance(eo)) {
                results.add((T) eo);
            }
        }
        return results;
    }

    /**
     * Loads a {@link Repository} form a file.
     * 
     * @param filePath
     *            The repository file.
     * @return The loaded repository.
     */
    public static Repository loadModel(final String filePath) {
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

    /**
     * Saves the repository into a file.
     * 
     * @param filePath
     *            The file for the repository.
     * @param repository
     *            The repository which will be saved.
     */
    public static void saveModel(final String filePath, final Repository repository) {
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
}
