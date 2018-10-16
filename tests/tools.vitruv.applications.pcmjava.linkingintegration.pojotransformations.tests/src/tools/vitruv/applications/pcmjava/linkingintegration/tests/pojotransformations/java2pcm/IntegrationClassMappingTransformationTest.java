package tools.vitruv.applications.pcmjava.linkingintegration.tests.pojotransformations.java2pcm;

import org.junit.Test;
import org.palladiosimulator.pcm.repository.BasicComponent;

import tools.vitruv.applications.pcmjava.linkingintegration.tests.CodeIntegrationTestCBSNamespace;
import tools.vitruv.applications.pcmjava.pojotransformations.java2pcm.Java2PcmUserSelection;

public class IntegrationClassMappingTransformationTest extends Java2PcmPackageIntegrationMappingTransformationTest {

	@Test
	public void testAddClassInIntegratedArea() throws Throwable {
		this.createClassInPackage(NAME_OF_INTEGRATED_CLASS,
				CodeIntegrationTestCBSNamespace.PACKAGE_NAME_OF_DISPLAY_COMPONENT);

		assertMessage(1, "Created class or interface in area with integrated object");
		assertNoComponentWithName(NAME_OF_INTEGRATED_CLASS);
	}

	@Test
	public void testAddClassInNonIntegratedArea() throws Throwable {
		this.getUserInteractor().addNextSelections(Java2PcmUserSelection.SELECT_BASIC_COMPONENT.getSelection());
		final BasicComponent bc = super.createSecondPackage(BasicComponent.class, NAME_OF_NOT_INTEGRATED_PACKAGE);

		// add Class in package that should correspond to a basic component
		// after it has been added
		this.getUserInteractor().addNextSelections(0);
		BasicComponent bcForClass = super.addClassInSecondPackage(BasicComponent.class);

		super.assertRepositoryAndPCMName(bc.getRepository__RepositoryComponent(), bc, bcForClass.getEntityName());
		assertNoUserInteractingMessage();
	}

}