package tools.vitruv.applications.pcmjava.reconstructionintegration.invariantcheckers.pcmjamoppenforcer;

import java.util.ArrayList;
import java.util.List;

import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

import tools.vitruv.applications.pcmjava.reconstructionintegration.invariantcheckers.PcmRepositorytoJavaInvariantEnforcer;

/**
 * PCM Components and Interfaces can have the same name. Forbidden in Java (Class and Interface)
 * Solves this issue by renaming the interface (ORIGINALNAME --> I_ORIGINALNAME).
 *
 * Also checks if two or more interfaces/components have the same name. They should always have
 * genuine name.
 *
 * @author Johannes Hoor
 *
 */
public class PcmToJavaSameIdentifier extends PcmRepositorytoJavaInvariantEnforcer {

    private final List<RepositoryComponent> components = new ArrayList<RepositoryComponent>();

    private final List<Interface> interfaces = new ArrayList<Interface>();

    /**
     * Instantiates a new PC mto ja mo pp same identifier.
     */
    public PcmToJavaSameIdentifier() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * tools.vitruv.integration.invariantChecker.FixedInvariantEnforcer#enforceInvariant
     * ()
     */
    @Override
    public void enforceInvariant() {
        this.identifyConflicts();
        this.solveConflicts();

    }

    /**
     * Identify conflicts.
     */
    protected void identifyConflicts() {
        for (final RepositoryComponent s : this.root.getComponents__Repository()) {
            // EStructuralFeature nameFeature =
            // s.eClass().getEAllStructuralFeatures().get(1);
            // String cname = (String) s.eGet(nameFeature);

            this.components.add(s);

        }

        for (final Interface s : this.root.getInterfaces__Repository()) {
            // EStructuralFeature nameFeature =
            // s.eClass().getEAllStructuralFeatures().get(1);
            // String iname = (String) s.eGet(nameFeature);
            this.interfaces.add(s);

        }
    }

    /**
     * Solve conflicts.
     */
    private void solveConflicts() {
        // TODO: inefficient n^2
        // interface names must be unique
        final int renameCtr = 0;
        for (final Interface i : this.interfaces) {
            for (final Interface j : this.interfaces) {
                if (i.getEntityName().equals(j.getEntityName()) && i != j) {
                    logger.info("Detected Same Identifier at Interfaces: " + i.getEntityName());
                    j.setEntityName(renameCtr + j.getEntityName());
                }
            }
        }

        // TODO: inefficient n^2
        // component names must be unique
        for (final RepositoryComponent c : this.components) {
            for (final RepositoryComponent c2 : this.components) {
                if (c.getEntityName().equals(c2.getEntityName()) && c != c2) {
                    logger.info("Detected Same Identifier at Components: " + c.getEntityName());
                    c2.setEntityName(renameCtr + c2.getEntityName());
                }
            }
        }

        // interface and component name should never be identical
        for (final Interface oi : this.interfaces) {
            for (final RepositoryComponent oc : this.components) {
                if (oi.getEntityName().equals(oc.getEntityName())) {
                    // this.logger.info("-identified@interface " + oi.getEntityName());
                    logger.info("Detected Same Identifier at Component and Interface: " + oi.getEntityName());
                    oi.setEntityName("I_" + oi.getEntityName());
                }
            }
        }

    }

}
