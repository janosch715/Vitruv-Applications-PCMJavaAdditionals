/**
 * Copyright 2005-2009 by SDQ, IPD, University of Karlsruhe, Germany
 */
package org.palladiosimulator.pcm.qosannotations.qos_reliability.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.palladiosimulator.pcm.PcmPackage;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.allocation.impl.AllocationPackageImpl;
import org.palladiosimulator.pcm.core.CorePackage;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.core.composition.impl.CompositionPackageImpl;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.core.entity.impl.EntityPackageImpl;
import org.palladiosimulator.pcm.core.impl.CorePackageImpl;
import org.palladiosimulator.pcm.impl.PcmPackageImpl;
import org.palladiosimulator.pcm.parameter.ParameterPackage;
import org.palladiosimulator.pcm.parameter.impl.ParameterPackageImpl;
import org.palladiosimulator.pcm.protocol.ProtocolPackage;
import org.palladiosimulator.pcm.protocol.impl.ProtocolPackageImpl;
import org.palladiosimulator.pcm.qosannotations.QosannotationsPackage;
import org.palladiosimulator.pcm.qosannotations.impl.QosannotationsPackageImpl;
import org.palladiosimulator.pcm.qosannotations.qos_performance.QosPerformancePackage;
import org.palladiosimulator.pcm.qosannotations.qos_performance.impl.QosPerformancePackageImpl;
import org.palladiosimulator.pcm.qosannotations.qos_reliability.QosReliabilityFactory;
import org.palladiosimulator.pcm.qosannotations.qos_reliability.QosReliabilityPackage;
import org.palladiosimulator.pcm.qosannotations.qos_reliability.SpecifiedReliabilityAnnotation;
import org.palladiosimulator.pcm.qosannotations.qos_reliability.util.QosReliabilityValidator;
import org.palladiosimulator.pcm.reliability.ReliabilityPackage;
import org.palladiosimulator.pcm.reliability.impl.ReliabilityPackageImpl;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.repository.impl.RepositoryPackageImpl;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.resourceenvironment.impl.ResourceenvironmentPackageImpl;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcm.resourcetype.impl.ResourcetypePackageImpl;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.seff.impl.SeffPackageImpl;
import org.palladiosimulator.pcm.seff.seff_performance.SeffPerformancePackage;
import org.palladiosimulator.pcm.seff.seff_performance.impl.SeffPerformancePackageImpl;
import org.palladiosimulator.pcm.seff.seff_reliability.SeffReliabilityPackage;
import org.palladiosimulator.pcm.seff.seff_reliability.impl.SeffReliabilityPackageImpl;
import org.palladiosimulator.pcm.subsystem.SubsystemPackage;
import org.palladiosimulator.pcm.subsystem.impl.SubsystemPackageImpl;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.system.impl.SystemPackageImpl;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;
import org.palladiosimulator.pcm.usagemodel.impl.UsagemodelPackageImpl;

import de.uka.ipd.sdq.identifier.IdentifierPackage;
import de.uka.ipd.sdq.stoex.StoexPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class QosReliabilityPackageImpl extends EPackageImpl implements QosReliabilityPackage {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final String copyright = "Copyright 2005-2015 by palladiosimulator.org";

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass specifiedReliabilityAnnotationEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI
     * value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init
     * init()}, which also performs initialization of the package, or returns the registered
     * package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.palladiosimulator.pcm.qosannotations.qos_reliability.QosReliabilityPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private QosReliabilityPackageImpl() {
        super(eNS_URI, QosReliabilityFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others
     * upon which it depends.
     *
     * <p>
     * This method is used to initialize {@link QosReliabilityPackage#eINSTANCE} when that field is
     * accessed. Clients should not invoke it directly. Instead, they should simply access that
     * field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static QosReliabilityPackage init() {
        if (isInited) {
            return (QosReliabilityPackage) EPackage.Registry.INSTANCE.getEPackage(QosReliabilityPackage.eNS_URI);
        }

        // Obtain or create and register package
        final QosReliabilityPackageImpl theQosReliabilityPackage = (QosReliabilityPackageImpl) (EPackage.Registry.INSTANCE
                .get(eNS_URI) instanceof QosReliabilityPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
                        : new QosReliabilityPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        IdentifierPackage.eINSTANCE.eClass();
        StoexPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        final PcmPackageImpl thePcmPackage = (PcmPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(PcmPackage.eNS_URI) instanceof PcmPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(PcmPackage.eNS_URI) : PcmPackage.eINSTANCE);
        final CorePackageImpl theCorePackage = (CorePackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(CorePackage.eNS_URI) instanceof CorePackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI) : CorePackage.eINSTANCE);
        final EntityPackageImpl theEntityPackage = (EntityPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(EntityPackage.eNS_URI) instanceof EntityPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(EntityPackage.eNS_URI) : EntityPackage.eINSTANCE);
        final CompositionPackageImpl theCompositionPackage = (CompositionPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(CompositionPackage.eNS_URI) instanceof CompositionPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(CompositionPackage.eNS_URI)
                        : CompositionPackage.eINSTANCE);
        final UsagemodelPackageImpl theUsagemodelPackage = (UsagemodelPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(UsagemodelPackage.eNS_URI) instanceof UsagemodelPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(UsagemodelPackage.eNS_URI)
                        : UsagemodelPackage.eINSTANCE);
        final RepositoryPackageImpl theRepositoryPackage = (RepositoryPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(RepositoryPackage.eNS_URI) instanceof RepositoryPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(RepositoryPackage.eNS_URI)
                        : RepositoryPackage.eINSTANCE);
        final ResourcetypePackageImpl theResourcetypePackage = (ResourcetypePackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ResourcetypePackage.eNS_URI) instanceof ResourcetypePackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(ResourcetypePackage.eNS_URI)
                        : ResourcetypePackage.eINSTANCE);
        final ProtocolPackageImpl theProtocolPackage = (ProtocolPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ProtocolPackage.eNS_URI) instanceof ProtocolPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(ProtocolPackage.eNS_URI) : ProtocolPackage.eINSTANCE);
        final ParameterPackageImpl theParameterPackage = (ParameterPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ParameterPackage.eNS_URI) instanceof ParameterPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(ParameterPackage.eNS_URI)
                        : ParameterPackage.eINSTANCE);
        final ReliabilityPackageImpl theReliabilityPackage = (ReliabilityPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ReliabilityPackage.eNS_URI) instanceof ReliabilityPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(ReliabilityPackage.eNS_URI)
                        : ReliabilityPackage.eINSTANCE);
        final SeffPackageImpl theSeffPackage = (SeffPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(SeffPackage.eNS_URI) instanceof SeffPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(SeffPackage.eNS_URI) : SeffPackage.eINSTANCE);
        final SeffPerformancePackageImpl theSeffPerformancePackage = (SeffPerformancePackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(SeffPerformancePackage.eNS_URI) instanceof SeffPerformancePackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(SeffPerformancePackage.eNS_URI)
                        : SeffPerformancePackage.eINSTANCE);
        final SeffReliabilityPackageImpl theSeffReliabilityPackage = (SeffReliabilityPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(SeffReliabilityPackage.eNS_URI) instanceof SeffReliabilityPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(SeffReliabilityPackage.eNS_URI)
                        : SeffReliabilityPackage.eINSTANCE);
        final QosannotationsPackageImpl theQosannotationsPackage = (QosannotationsPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(QosannotationsPackage.eNS_URI) instanceof QosannotationsPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(QosannotationsPackage.eNS_URI)
                        : QosannotationsPackage.eINSTANCE);
        final QosPerformancePackageImpl theQosPerformancePackage = (QosPerformancePackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(QosPerformancePackage.eNS_URI) instanceof QosPerformancePackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(QosPerformancePackage.eNS_URI)
                        : QosPerformancePackage.eINSTANCE);
        final SystemPackageImpl theSystemPackage = (SystemPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(SystemPackage.eNS_URI) instanceof SystemPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(SystemPackage.eNS_URI) : SystemPackage.eINSTANCE);
        final ResourceenvironmentPackageImpl theResourceenvironmentPackage = (ResourceenvironmentPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ResourceenvironmentPackage.eNS_URI) instanceof ResourceenvironmentPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentPackage.eNS_URI)
                        : ResourceenvironmentPackage.eINSTANCE);
        final AllocationPackageImpl theAllocationPackage = (AllocationPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(AllocationPackage.eNS_URI) instanceof AllocationPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(AllocationPackage.eNS_URI)
                        : AllocationPackage.eINSTANCE);
        final SubsystemPackageImpl theSubsystemPackage = (SubsystemPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(SubsystemPackage.eNS_URI) instanceof SubsystemPackageImpl
                        ? EPackage.Registry.INSTANCE.getEPackage(SubsystemPackage.eNS_URI)
                        : SubsystemPackage.eINSTANCE);

        // Create package meta-data objects
        theQosReliabilityPackage.createPackageContents();
        thePcmPackage.createPackageContents();
        theCorePackage.createPackageContents();
        theEntityPackage.createPackageContents();
        theCompositionPackage.createPackageContents();
        theUsagemodelPackage.createPackageContents();
        theRepositoryPackage.createPackageContents();
        theResourcetypePackage.createPackageContents();
        theProtocolPackage.createPackageContents();
        theParameterPackage.createPackageContents();
        theReliabilityPackage.createPackageContents();
        theSeffPackage.createPackageContents();
        theSeffPerformancePackage.createPackageContents();
        theSeffReliabilityPackage.createPackageContents();
        theQosannotationsPackage.createPackageContents();
        theQosPerformancePackage.createPackageContents();
        theSystemPackage.createPackageContents();
        theResourceenvironmentPackage.createPackageContents();
        theAllocationPackage.createPackageContents();
        theSubsystemPackage.createPackageContents();

        // Initialize created meta-data
        theQosReliabilityPackage.initializePackageContents();
        thePcmPackage.initializePackageContents();
        theCorePackage.initializePackageContents();
        theEntityPackage.initializePackageContents();
        theCompositionPackage.initializePackageContents();
        theUsagemodelPackage.initializePackageContents();
        theRepositoryPackage.initializePackageContents();
        theResourcetypePackage.initializePackageContents();
        theProtocolPackage.initializePackageContents();
        theParameterPackage.initializePackageContents();
        theReliabilityPackage.initializePackageContents();
        theSeffPackage.initializePackageContents();
        theSeffPerformancePackage.initializePackageContents();
        theSeffReliabilityPackage.initializePackageContents();
        theQosannotationsPackage.initializePackageContents();
        theQosPerformancePackage.initializePackageContents();
        theSystemPackage.initializePackageContents();
        theResourceenvironmentPackage.initializePackageContents();
        theAllocationPackage.initializePackageContents();
        theSubsystemPackage.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put(theQosReliabilityPackage, new EValidator.Descriptor() {
            @Override
            public EValidator getEValidator() {
                return QosReliabilityValidator.INSTANCE;
            }
        });

        // Mark meta-data to indicate it can't be changed
        theQosReliabilityPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(QosReliabilityPackage.eNS_URI, theQosReliabilityPackage);
        return theQosReliabilityPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getSpecifiedReliabilityAnnotation() {
        return this.specifiedReliabilityAnnotationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getSpecifiedReliabilityAnnotation_ExternalFailureOccurrenceDescriptions__SpecifiedReliabilityAnnotation() {
        return (EReference) this.specifiedReliabilityAnnotationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public QosReliabilityFactory getQosReliabilityFactory() {
        return (QosReliabilityFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on
     * any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void createPackageContents() {
        if (this.isCreated) {
            return;
        }
        this.isCreated = true;

        // Create classes and their features
        this.specifiedReliabilityAnnotationEClass = this.createEClass(SPECIFIED_RELIABILITY_ANNOTATION);
        this.createEReference(this.specifiedReliabilityAnnotationEClass,
                SPECIFIED_RELIABILITY_ANNOTATION__EXTERNAL_FAILURE_OCCURRENCE_DESCRIPTIONS_SPECIFIED_RELIABILITY_ANNOTATION);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have
     * no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void initializePackageContents() {
        if (this.isInitialized) {
            return;
        }
        this.isInitialized = true;

        // Initialize package
        this.setName(eNAME);
        this.setNsPrefix(eNS_PREFIX);
        this.setNsURI(eNS_URI);

        // Obtain other dependent packages
        final QosannotationsPackage theQosannotationsPackage = (QosannotationsPackage) EPackage.Registry.INSTANCE
                .getEPackage(QosannotationsPackage.eNS_URI);
        final ReliabilityPackage theReliabilityPackage = (ReliabilityPackage) EPackage.Registry.INSTANCE
                .getEPackage(ReliabilityPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.specifiedReliabilityAnnotationEClass.getESuperTypes()
                .add(theQosannotationsPackage.getSpecifiedQoSAnnotation());

        // Initialize classes and features; add operations and parameters
        this.initEClass(this.specifiedReliabilityAnnotationEClass, SpecifiedReliabilityAnnotation.class,
                "SpecifiedReliabilityAnnotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(
                this.getSpecifiedReliabilityAnnotation_ExternalFailureOccurrenceDescriptions__SpecifiedReliabilityAnnotation(),
                theReliabilityPackage.getExternalFailureOccurrenceDescription(),
                theReliabilityPackage
                        .getExternalFailureOccurrenceDescription_SpecifiedReliabilityAnnotation__ExternalFailureOccurrenceDescription(),
                "externalFailureOccurrenceDescriptions__SpecifiedReliabilityAnnotation", null, 1, -1,
                SpecifiedReliabilityAnnotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
                !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        EOperation op = this.addEOperation(this.specifiedReliabilityAnnotationEClass, this.ecorePackage.getEBoolean(),
                "SpecifiedReliabilityAnnotationMustReferenceRequiredRoleOfASystem", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEDiagnosticChain(), "diagnostics", 0, 1, IS_UNIQUE, IS_ORDERED);
        EGenericType g1 = this.createEGenericType(this.ecorePackage.getEMap());
        EGenericType g2 = this.createEGenericType(this.ecorePackage.getEJavaObject());
        g1.getETypeArguments().add(g2);
        g2 = this.createEGenericType(this.ecorePackage.getEJavaObject());
        g1.getETypeArguments().add(g2);
        this.addEParameter(op, g1, "context", 0, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.specifiedReliabilityAnnotationEClass, this.ecorePackage.getEBoolean(),
                "SumOfReliabilityAnnotationFailureProbabilitiesMustNotExceed1", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEDiagnosticChain(), "diagnostics", 0, 1, IS_UNIQUE, IS_ORDERED);
        g1 = this.createEGenericType(this.ecorePackage.getEMap());
        g2 = this.createEGenericType(this.ecorePackage.getEJavaObject());
        g1.getETypeArguments().add(g2);
        g2 = this.createEGenericType(this.ecorePackage.getEJavaObject());
        g1.getETypeArguments().add(g2);
        this.addEParameter(op, g1, "context", 0, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.specifiedReliabilityAnnotationEClass, this.ecorePackage.getEBoolean(),
                "MultipleExternalOccurrenceDescriptionsPerFailureTypeNotAllowed", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEDiagnosticChain(), "diagnostics", 0, 1, IS_UNIQUE, IS_ORDERED);
        g1 = this.createEGenericType(this.ecorePackage.getEMap());
        g2 = this.createEGenericType(this.ecorePackage.getEJavaObject());
        g1.getETypeArguments().add(g2);
        g2 = this.createEGenericType(this.ecorePackage.getEJavaObject());
        g1.getETypeArguments().add(g2);
        this.addEParameter(op, g1, "context", 0, 1, IS_UNIQUE, IS_ORDERED);
    }

} // QosReliabilityPackageImpl
