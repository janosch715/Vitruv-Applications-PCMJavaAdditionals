package mir.reactions;

import tools.vitruv.framework.change.processing.impl.CompositeDecomposingChangePropagationSpecification;
import tools.vitruv.framework.change.processing.ChangePropagationSpecification;
import tools.vitruv.domains.java.JavaDomainProvider;
import java.util.List;
import java.util.function.Supplier;
import tools.vitruv.domains.pcm.PcmDomainProvider;
import java.util.Arrays;

/**
 * The {@link class tools.vitruv.framework.change.processing.impl.CompositeDecomposingChangePropagationSpecification} for transformations between the metamodels Java and PCM.
 * To add further change processors override the setup method.
 *
 * <p> This file is generated! Do not edit it but extend it by inheriting from it!
 * 
 * <p> Generated from template version 1
 */
public class JavaToPcmChangePropagationSpecification extends CompositeDecomposingChangePropagationSpecification {
	
	private final List<Supplier<? extends ChangePropagationSpecification>> executors = Arrays.asList(
		// begin generated executor list
		mir.reactions.reactionsJavaToPcm.packageMappingIntegration.ExecutorJavaToPcm::new,
		
		mir.reactions.reactionsJavaToPcm.parserIntegrationReaction.ExecutorJavaToPcm::new
		// end generated executor list
	);
	
	public JavaToPcmChangePropagationSpecification() {
		super(new JavaDomainProvider().getDomain(), 
			new PcmDomainProvider().getDomain());
		setup();
	}
	
	/**
	 * Adds the reactions change processors to this {@link JavaToPcmChangePropagationSpecification}.
	 * For adding further change processors overwrite this method and call the super method at the right place.
	 */
	protected void setup() {
		for (final Supplier<? extends ChangePropagationSpecification> executorSupplier : executors) {
			this.addChangeMainprocessor(executorSupplier.get());
		}	
	}
}
