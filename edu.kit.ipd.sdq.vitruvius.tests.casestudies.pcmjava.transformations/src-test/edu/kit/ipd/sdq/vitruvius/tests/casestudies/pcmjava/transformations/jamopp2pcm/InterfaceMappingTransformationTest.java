package edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.jamopp2pcm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import de.uka.ipd.sdq.pcm.repository.BasicComponent;
import de.uka.ipd.sdq.pcm.repository.OperationInterface;
import de.uka.ipd.sdq.pcm.repository.Repository;
import edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.utils.PCM2JaMoPPTestUtils;

public class InterfaceMappingTransformationTest extends JaMoPP2PCMTransformationTest {

    /**
     * interface in contracts package --> should automatically be mapped to operation interface
     *
     * @throws Throwable
     */
    @Test
    public void testAddInterfaceInContractsPackage() throws Throwable {
        final Repository repo = super.addFirstPackage();

        final OperationInterface opIf = super.addInterfaceInContractsPackage();

        this.assertOperationInterface(repo, opIf, PCM2JaMoPPTestUtils.INTERFACE_NAME);
    }

    /**
     * interface in non-repository package --> "user" should be asked and decide to add it
     *
     * @throws Exception
     */
    @Test
    public void testAddArchitecturalInterfaceInNonRepositoryPackage() throws Throwable {
        final Repository repo = super.addFirstPackage();
        final BasicComponent bc = super.addSecondPackageCorrespondsToBasicComponent();

        final OperationInterface opInterface = super.addInterfaceInSecondPackageWithCorrespondence(bc.getEntityName());

        this.assertOperationInterface(repo, opInterface, PCM2JaMoPPTestUtils.INTERFACE_NAME);
    }

    /**
     * interface in non-repository package --> "user" should be asked and decide to not add it
     *
     * @throws Exception
     */
    @Test
    public void testAddTechnicalInterfaceInNonRepositoryPackage() throws Throwable {
        super.addFirstPackage();
        final BasicComponent bc = super.addSecondPackageCorrespondsToBasicComponent();

        final EObject eObject = super.addInterfaceInSecondPackageWithoutCorrespondence(bc.getEntityName());

        assertTrue("Corresponding object for interface that is created in non main package is not null: " + eObject,
                null == eObject);
    }

    @Test
    public void testRenameInterfaceWithCorrespondence() throws Throwable {
        final Repository repo = super.addFirstPackage();
        super.addSecondPackageCorrespondsToBasicComponent();
        final OperationInterface opInterface = super.addInterfaceInContractsPackage();

        final OperationInterface newOpInterface = this.renameClassifierWithName(opInterface.getEntityName(),
                PCM2JaMoPPTestUtils.INTERFACE_NAME + PCM2JaMoPPTestUtils.RENAME, OperationInterface.class);

        this.assertOperationInterface(repo, newOpInterface, PCM2JaMoPPTestUtils.INTERFACE_NAME
                + PCM2JaMoPPTestUtils.RENAME);
    }

    private void assertOperationInterface(final Repository repo, final OperationInterface opIf,
            final String expectedName) {
        assertTrue("The created operation interface is null", null != opIf);
        assertEquals("OperationInterface name does not equals the expected interface Name.", opIf.getEntityName(),
                expectedName);
        assertEquals("The created operation interface is not in the repository", repo.getId(), opIf
                .getRepository__Interface().getId());
    }
}
