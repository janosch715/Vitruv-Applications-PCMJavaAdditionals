package edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.jamopp2pcm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.internal.events.BuildCommand;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.internal.resources.ProjectDescription;
import org.eclipse.core.internal.resources.ProjectInfo;
import org.eclipse.core.internal.resources.ResourceInfo;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jdt.ui.wizards.NewInterfaceWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.text.edits.ReplaceEdit;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.containers.Package;
import org.junit.Before;

import de.uka.ipd.sdq.pcm.core.entity.NamedElement;
import de.uka.ipd.sdq.pcm.repository.BasicComponent;
import de.uka.ipd.sdq.pcm.repository.CollectionDataType;
import de.uka.ipd.sdq.pcm.repository.CompositeComponent;
import de.uka.ipd.sdq.pcm.repository.CompositeDataType;
import de.uka.ipd.sdq.pcm.repository.DataType;
import de.uka.ipd.sdq.pcm.repository.OperationInterface;
import de.uka.ipd.sdq.pcm.repository.Repository;
import de.uka.ipd.sdq.pcm.repository.RepositoryComponent;
import de.uka.ipd.sdq.pcm.system.System;
import edu.kit.ipd.sdq.vitruvius.casestudies.emf.builder.VitruviusEmfBuilder;
import edu.kit.ipd.sdq.vitruvius.casestudies.emf.util.BuildProjects;
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.PCMJaMoPPNamespace;
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.builder.PCMJavaAddBuilder;
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.builder.PCMJavaBuilder;
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.builder.PCMJavaRemoveBuilder;
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.transformations.PCMJaMoPPTransformationExecuter;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.CorrespondenceInstance;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.VURI;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.interfaces.EMFModelTransformationExecuting;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.interfaces.UserInteracting;
import edu.kit.ipd.sdq.vitruvius.framework.run.propagationengine.EMFModelPropagationEngineImpl;
import edu.kit.ipd.sdq.vitruvius.framework.run.syncmanager.SyncManagerImpl;
import edu.kit.ipd.sdq.vitruvius.framework.run.transformationexecuter.ChangeSynchronizer;
import edu.kit.ipd.sdq.vitruvius.framework.synctransprovider.TransformationExecutingProvidingImpl;
import edu.kit.ipd.sdq.vitruvius.framework.util.bridges.EMFBridge;
import edu.kit.ipd.sdq.vitruvius.framework.util.bridges.EcoreResourceBridge;
import edu.kit.ipd.sdq.vitruvius.framework.util.datatypes.ClaimableMap;
import edu.kit.ipd.sdq.vitruvius.framework.util.datatypes.Pair;
import edu.kit.ipd.sdq.vitruvius.framework.vsum.VSUMImpl;
import edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.PCMJaMoPPTransformationTestBase;
import edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.utils.PCM2JaMoPPTestUtils;
import edu.kit.ipd.sdq.vitruvius.tests.util.TestUtil;

/**
 * Test class that contains utillity methods that can be used by JaMoPP2PCM transformation tests
 *
 */
@SuppressWarnings("restriction")
public class JaMoPP2PCMTransformationTest extends PCMJaMoPPTransformationTestBase {

    private static final Logger logger = Logger.getLogger(JaMoPP2PCMTransformationTest.class.getSimpleName());

    private static final int SELECT_BASIC_COMPONENT = 0;
    private static final int SELECT_COMPOSITE_COMPONENT = 1;
    private static final int SELECT_SYSTEM = 2;
    private static final int SELECT_NOTHING_DECIDE_LATER = 3;

    protected Package mainPackage;
    protected Package secondPackage;
    protected TestUserInteractor testUserInteractor;

    @Before
    public void setUpTest() throws Throwable {
        this.afterTest();
        // remove PCM java builder from Project
        this.testUserInteractor = new TestUserInteractor();
        // add PCM Java Builder to Project under test
        final PCMJavaAddBuilder pcmJavaBuilder = new PCMJavaAddBuilder();
        pcmJavaBuilder.addBuilderToProject(TestUtil.getTestProject());
        // build the project
        BuildProjects.issueIncrementalBuildForAllProjectsWithBuilder(PCMJaMoPPNamespace.BUILDER_ID);
        this.resourceSet = new ResourceSetImpl();
        // set new user interactor
        this.setUserInteractor(this.testUserInteractor);
    }

    @Override
    protected void afterTest() {
        // Remove PCM Java Builder
        final PCMJavaRemoveBuilder pcmJavaRemoveBuilder = new PCMJavaRemoveBuilder();
        pcmJavaRemoveBuilder.removeBuilderFromProject(TestUtil.getTestProject());
    }

    @Override
    protected CorrespondenceInstance getCorrespondenceInstance() throws Throwable {
        final PCMJavaBuilder pcmJavaBuilder = this.getPCMJavaBuilderFromProject();
        if (null == pcmJavaBuilder) {
            return null;
        }
        final VSUMImpl vsum = TestUtil.getFieldFromClass(VitruviusEmfBuilder.class, "vsum", pcmJavaBuilder);
        final VURI jaMoPPVURI = VURI.getInstance(PCMJaMoPPNamespace.JaMoPP.JAMOPP_METAMODEL_NAMESPACE);
        final VURI pcmVURI = VURI.getInstance(PCMJaMoPPNamespace.PCM.PCM_METAMODEL_NAMESPACE);
        final CorrespondenceInstance corresponcenceInstance = vsum.getCorrespondenceInstanceOriginal(pcmVURI,
                jaMoPPVURI);
        return corresponcenceInstance;
    }

    private PCMJavaBuilder getPCMJavaBuilderFromProject() throws Throwable {
        final Project project = (Project) TestUtil.getTestProject();
        final ResourceInfo info = project.getResourceInfo(false, false);
        final ProjectDescription description = ((ProjectInfo) info).getDescription();
        final boolean makeCopy = false;
        for (final ICommand command : description.getBuildSpec(makeCopy)) {
            if (command.getBuilderName().equals(PCMJaMoPPNamespace.BUILDER_ID)) {
                final BuildCommand buildCommand = (BuildCommand) command;
                final IncrementalProjectBuilder ipb = buildCommand.getBuilder(TestUtil.getTestProject()
                        .getActiveBuildConfig());
                final PCMJavaBuilder pcmJavaBuilder = (PCMJavaBuilder) ipb;
                return pcmJavaBuilder;
            }
        }
        logger.warn("Could not find any PCMJavaBuilder");
        return null;
    }

    protected Repository addFirstPackage() throws Throwable {
        this.mainPackage = this.createPackageWithPackageInfo(new String[] { PCM2JaMoPPTestUtils.REPOSITORY_NAME });
        final CorrespondenceInstance ci = this.getCorrespondenceInstance();
        if (null == ci) {
            throw new RuntimeException("Could not get correspondence instance.");
        }
        final Repository repo = ci.claimUniqueCorrespondingEObjectByType(this.mainPackage, Repository.class);
        return repo;
    }

    protected BasicComponent addSecondPackageCorrespondsToBasicComponent() throws Throwable {
        this.testUserInteractor.addNextSelections(SELECT_BASIC_COMPONENT);
        return this.createSecondPackage(BasicComponent.class, PCM2JaMoPPTestUtils.REPOSITORY_NAME,
                PCM2JaMoPPTestUtils.BASIC_COMPONENT_NAME);
    }

    private <T> T createSecondPackage(final Class<T> correspondingType, final String... namespace) throws Throwable {
        this.secondPackage = this.createPackageWithPackageInfo(namespace);
        TestUtil.waitForSynchronization();
        return this.getCorrespondenceInstance().claimUniqueCorrespondingEObjectByType(this.secondPackage,
                correspondingType);
    }

    private void createSecondPackageWithoutCorrespondence(final String... namespace) throws Throwable {
        this.secondPackage = this.createPackageWithPackageInfo(namespace);
        TestUtil.waitForSynchronization();
    }

    protected void createPackage(final String[] namespace) throws Throwable {
        final IPackageFragmentRoot packageRoot = this.getIJavaProject();
        final String namespaceDotted = StringUtils.join(namespace, ".");
        final boolean force = true;
        packageRoot.createPackageFragment(namespaceDotted, force, new NullProgressMonitor());
    }

    protected Package createPackageWithPackageInfo(final String[] namespace) throws Throwable {
        String packageFile = StringUtils.join(namespace, "/");
        packageFile = packageFile + "/package-info.java";
        final Package jaMoPPPackage = ContainersFactory.eINSTANCE.createPackage();
        final List<String> namespaceList = Arrays.asList(namespace);
        jaMoPPPackage.setName(namespaceList.get(namespaceList.size() - 1));
        jaMoPPPackage.getNamespaces().addAll(namespaceList.subList(0, namespaceList.size() - 1));
        final VURI packageVURI = this.createVURIForSrcFile(packageFile);
        final Resource resource = this.resourceSet.createResource(packageVURI.getEMFUri());
        EcoreResourceBridge.saveEObjectAsOnlyContent(jaMoPPPackage, resource);
        TestUtil.waitForSynchronization();
        return jaMoPPPackage;
    }

    protected void renamePackage(final Package packageToRename, String newName) throws Throwable {
        final Resource resource = packageToRename.eResource();
        final IFile iFile = EMFBridge.getIFileForEMFUri(resource.getURI());
        IPath iPath = iFile.getProjectRelativePath();
        iPath = iPath.removeLastSegments(1);
        final String oldPackageName = packageToRename.getName();
        if (oldPackageName.contains(".")) {
            newName = oldPackageName.substring(0, oldPackageName.lastIndexOf(".") + 1) + newName;
        }
        final IFolder iFolder = iFile.getProject().getFolder(iPath);
        final IJavaElement javaPackage = JavaCore.create(iFolder);
        this.refactorRenameJavaElement(newName, javaPackage, IJavaRefactorings.RENAME_PACKAGE);
    }

    private void refactorRenameJavaElement(final String newName, final IJavaElement iJavaElement,
            final String refactorRenameActionName) throws CoreException {
        final RefactoringContribution refacContrib = RefactoringCore
                .getRefactoringContribution(refactorRenameActionName);
        final RefactoringStatus status = new RefactoringStatus();
        final RenameJavaElementDescriptor desc = (RenameJavaElementDescriptor) refacContrib.createDescriptor();
        desc.setUpdateReferences(true);
        desc.setJavaElement(iJavaElement);
        desc.setNewName(newName);
        final NullProgressMonitor monitor = new NullProgressMonitor();
        final Refactoring refactoring = desc.createRefactoring(status);
        refactoring.checkInitialConditions(monitor);
        refactoring.checkFinalConditions(monitor);
        final Change change = refactoring.createChange(monitor);
        change.perform(monitor);
    }

    protected <T> T renameClassifierWithName(final String entityName, final String newName, final Class<T> type)
            throws Throwable {
        try {
            final ICompilationUnit cu = this.findCompilationWithClassName(entityName + ".java");
            cu.becomeWorkingCopy(new NullProgressMonitor());
            final int offset = cu.getBuffer().getContents().indexOf(entityName);
            if (cu.getBuffer() instanceof IBuffer.ITextEditCapability) {
                logger.info(cu.getBuffer());
            }
            final ReplaceEdit edit = new ReplaceEdit(offset, entityName.length(), newName);
            cu.applyTextEdit(edit, null);
            cu.save(new NullProgressMonitor(), true);
            cu.commitWorkingCopy(true, new NullProgressMonitor());
            cu.save(new NullProgressMonitor(), true);
            TestUtil.waitForSynchronization();
            final VURI vuri = VURI.getInstance(cu.getResource());
            final Classifier jaMoPPClass = this.getJaMoPPClassifierForVURI(vuri);
            return this.getCorrespondenceInstance().claimUniqueCorrespondingEObjectByType(jaMoPPClass, type);
        } catch (final Throwable e) {
            logger.warn(e.getMessage());
        }
        return null;

    }

    private ICompilationUnit findCompilationWithClassName(final String entityName) throws Throwable {
        final IJavaProject javaProject = JavaCore.create(TestUtil.getTestProject());
        for (final IPackageFragmentRoot packageFragmentRoot : javaProject.getPackageFragmentRoots()) {
            final IJavaElement[] children = packageFragmentRoot.getChildren();
            for (final IJavaElement iJavaElement : children) {
                if (iJavaElement instanceof IPackageFragment) {
                    final IPackageFragment fragment = (IPackageFragment) iJavaElement;
                    final IJavaElement[] javaElements = fragment.getChildren();
                    for (int k = 0; k < javaElements.length; k++) {
                        final IJavaElement javaElement = javaElements[k];
                        if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
                            final ICompilationUnit compilationUnit = (ICompilationUnit) javaElement;
                            if (compilationUnit.getElementName().equals(entityName)) {
                                return compilationUnit;
                            }
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Could not find a compilation unit with name " + entityName);
    }

    private IPackageFragmentRoot getIJavaProject() throws Throwable {
        final IProject project = TestUtil.getTestProject();
        final IJavaProject javaProject = JavaCore.create(project);
        final IFolder sourceFolder = project.getFolder(TestUtil.SOURCE_FOLDER);
        if (!sourceFolder.exists()) {
            final boolean force = true;
            final boolean local = true;
            sourceFolder.create(force, local, new NullProgressMonitor());
        }
        final IPackageFragmentRoot packageFragment = javaProject.getPackageFragmentRoot(sourceFolder);
        return packageFragment;
    }

    private VURI createVURIForSrcFile(final String srcFilePath) {
        final String vuriKey = super.getProjectPath() + TestUtil.SOURCE_FOLDER + "/" + srcFilePath;
        return VURI.getInstance(vuriKey);
    }

    protected <T> T addClassInSecondPackage(final Class<T> classOfCorrespondingObject) throws Throwable {
        final T createdEObject = this.addClassInPackage(this.secondPackage, classOfCorrespondingObject);
        return createdEObject;
    }

    protected <T> T addClassInPackage(final Package packageForClass, final Class<T> classOfCorrespondingObject)
            throws Throwable {
        final IPackageFragmentRoot packageRoot = this.getIJavaProject();
        final IPackageFragment packageFragment = this.getPackageFragmentToForJaMoPPPackage(packageForClass);
        final NewClassWizardPage classWizard = new NewClassWizardPage();
        classWizard.setPackageFragment(packageFragment, false);
        classWizard.setPackageFragmentRoot(packageRoot, false);
        classWizard.setTypeName(PCM2JaMoPPTestUtils.IMPLEMENTING_CLASS_NAME, true);
        classWizard.createType(new NullProgressMonitor());

        final VURI vuri = this.getVURIForElementInPackage(packageFragment, PCM2JaMoPPTestUtils.IMPLEMENTING_CLASS_NAME);
        final Classifier jaMoPPClass = this.getJaMoPPClassifierForVURI(vuri);
        TestUtil.waitForSynchronization();
        return this.getCorrespondenceInstance().claimUniqueCorrespondingEObjectByType(jaMoPPClass,
                classOfCorrespondingObject);
    }

    private VURI getVURIForElementInPackage(final IPackageFragment packageFragment, final String elementName) {
        String vuriKey = packageFragment.getResource().getFullPath().toString() + "/" + elementName + "."
                + PCMJaMoPPNamespace.JaMoPP.JAVA_FILE_EXTENSION;
        if (vuriKey.startsWith("/")) {
            vuriKey = vuriKey.substring(1, vuriKey.length());
        }
        final VURI vuri = VURI.getInstance(vuriKey);
        return vuri;
    }

    private IPackageFragment getPackageFragmentToForJaMoPPPackage(final Package packageForClass) throws Throwable {
        final IPackageFragmentRoot packageRoot = this.getIJavaProject();
        for (final IJavaElement javaElement : packageRoot.getChildren()) {
            if (javaElement instanceof IPackageFragment
                    && javaElement.getElementName().equals(
                            packageForClass.getNamespacesAsString() + packageForClass.getName())) {
                return (IPackageFragment) javaElement;
            }
        }
        throw new RuntimeException("No packageFragment found for JaMoPP package " + packageForClass);
    }

    private Classifier getJaMoPPClassifierForVURI(final VURI vuri) {
        final Resource resource = EcoreResourceBridge.loadResourceAtURI(vuri.getEMFUri(), this.resourceSet);
        final CompilationUnit cu = (CompilationUnit) resource.getContents().get(0);
        final Classifier jaMoPPClassifier = cu.getClassifiers().get(0);
        return jaMoPPClassifier;
    }

    /**
     * Change user interactor in changeSynchronizer of PCMJaMoPPTransformationExecuter by invoking
     * the setUserInteractor method of the class ChangeSynchronizer
     *
     * @throws Throwable
     */
    private void setUserInteractor(final UserInteracting newUserInteracting) throws Throwable {
        final PCMJavaBuilder pcmJavaBuilder = this.getPCMJavaBuilderFromProject();
        final SyncManagerImpl syncManagerImpl = TestUtil.getFieldFromClass(VitruviusEmfBuilder.class,
                "changeSynchronizing", pcmJavaBuilder);
        final EMFModelPropagationEngineImpl emfModelPropagationEngineImpl = TestUtil.getFieldFromClass(
                SyncManagerImpl.class, "changePropagating", syncManagerImpl);
        final TransformationExecutingProvidingImpl transformationExecutingProvidingImpl = TestUtil.getFieldFromClass(
                EMFModelPropagationEngineImpl.class, "transformationExecutingProviding", emfModelPropagationEngineImpl);
        final ClaimableMap<Pair<VURI, VURI>, EMFModelTransformationExecuting> transformationExecuterMap = TestUtil
                .getFieldFromClass(TransformationExecutingProvidingImpl.class, "transformationExecuterMap",
                        transformationExecutingProvidingImpl);
        PCMJaMoPPTransformationExecuter pcmJaMoPPTransformationExecuter = null;
        for (final EMFModelTransformationExecuting emfModelTransformationExecuting : transformationExecuterMap.values()) {
            if (emfModelTransformationExecuting instanceof PCMJaMoPPTransformationExecuter) {
                pcmJaMoPPTransformationExecuter = (PCMJaMoPPTransformationExecuter) emfModelTransformationExecuting;
                break;
            }
        }
        if (null == pcmJaMoPPTransformationExecuter) {
            throw new RuntimeException("Could not find an PCMJaMoPPTransformationExecuter that is currently active.");
        }
        final ChangeSynchronizer changeSynchronizer = TestUtil.getFieldFromClass(PCMJaMoPPTransformationExecuter.class,
                "changeSynchronizer", pcmJaMoPPTransformationExecuter);
        changeSynchronizer.setUserInteracting(newUserInteracting);
    }

    protected CompositeComponent addSecondPackageCorrespondsToCompositeComponent() throws Throwable {
        this.testUserInteractor.addNextSelections(SELECT_COMPOSITE_COMPONENT);
        return this.createSecondPackage(CompositeComponent.class, PCM2JaMoPPTestUtils.REPOSITORY_NAME,
                PCM2JaMoPPTestUtils.COMPOSITE_COMPONENT_NAME);
    }

    protected de.uka.ipd.sdq.pcm.system.System addSecondPackageCorrespondsToSystem() throws Throwable {
        this.testUserInteractor.addNextSelections(SELECT_SYSTEM);
        return this.createSecondPackage(System.class, PCM2JaMoPPTestUtils.SYSTEM_NAME);
    }

    protected void addSecondPackageCorrespondsWithoutCorrespondences() throws Throwable {
        this.testUserInteractor.addNextSelections(SELECT_NOTHING_DECIDE_LATER);
        this.createSecondPackageWithoutCorrespondence(PCM2JaMoPPTestUtils.REPOSITORY_NAME,
                PCM2JaMoPPTestUtils.BASIC_COMPONENT_NAME);
    }

    protected void assertRepositoryAndPCMName(final Repository repo, final RepositoryComponent repoComponent,
            final String expectedName) {

        assertEquals("Repository of compoennt is not the repository: " + repo, repo.getId(), repoComponent
                .getRepository__RepositoryComponent().getId());

        this.assertPCMNamedElement(repoComponent, expectedName);
    }

    protected void assertRepositoryAndPCMNameForDatatype(final Repository repo, final DataType dt,
            final String expectedName) {

        assertEquals("Repository of compoennt is not the repository: " + repo, repo.getId(), dt
                .getRepository__DataType().getId());
        if (dt instanceof CompositeDataType) {
            this.assertPCMNamedElement((CompositeDataType) dt, expectedName);
        } else if (dt instanceof CollectionDataType) {
            this.assertPCMNamedElement((CollectionDataType) dt, expectedName);
        } else {
            throw new RuntimeException("Primitive data types should not have a correspondence to classes");
        }
    }

    protected void assertPCMNamedElement(final NamedElement pcmNamedElement, final String expectedName) {
        assertTrue("The name of pcm named element is not " + expectedName, pcmNamedElement.getEntityName()
                .equalsIgnoreCase(expectedName));
    }

    protected OperationInterface addInterfaceInContractsPackage() throws Throwable {
        return this.createInterfaceInPackage("contracts", true);
    }

    private OperationInterface createInterfaceInPackage(final String packageName, final boolean throwException)
            throws Throwable, CoreException, InterruptedException {
        final IPackageFragment packageFragment = this.getPackageFragmentToForJaMoPPPackage(this
                .getPackageWithName(packageName));
        final NewInterfaceWizardPage interfaceWizard = new NewInterfaceWizardPage();
        interfaceWizard.setPackageFragment(packageFragment, false);
        interfaceWizard.setPackageFragmentRoot(this.getIJavaProject(), false);
        interfaceWizard.setTypeName(PCM2JaMoPPTestUtils.INTERFACE_NAME, true);
        interfaceWizard.createType(new NullProgressMonitor());
        final VURI vuri = this.getVURIForElementInPackage(packageFragment, PCM2JaMoPPTestUtils.INTERFACE_NAME);
        TestUtil.waitForSynchronization();
        final Classifier jaMoPPIf = this.getJaMoPPClassifierForVURI(vuri);
        if (throwException) {
            return this.getCorrespondenceInstance().claimUniqueCorrespondingEObjectByType(jaMoPPIf,
                    OperationInterface.class);
        } else {
            final Set<EObject> correspondingEObjects = this.getCorrespondenceInstance().getAllCorrespondingEObjects(
                    jaMoPPIf);
            if (null == correspondingEObjects || 0 == correspondingEObjects.size()) {
                return null;
            } else {
                return (OperationInterface) correspondingEObjects.iterator().next();
            }
        }
    }

    protected OperationInterface addInterfaceInSecondPackageWithCorrespondence(final String packageName)
            throws Throwable {
        this.testUserInteractor.addNextSelections(0);
        return this.createInterfaceInPackage(packageName, true);
    }

    protected EObject addInterfaceInSecondPackageWithoutCorrespondence(final String packageName) throws Throwable {
        this.testUserInteractor.addNextSelections(1);
        return this.createInterfaceInPackage(packageName, false);
    }

    protected Package getPackageWithName(final String name) throws Throwable {
        final Set<Package> packages = this.getCorrespondenceInstance().getAllEObjectsInCorrespondencesWithType(
                Package.class);
        for (final Package currentPackage : packages) {
            if (currentPackage.getName().equals(name)) {
                return currentPackage;
            }
        }
        throw new RuntimeException("Could not find datatypes package");
    }

}
