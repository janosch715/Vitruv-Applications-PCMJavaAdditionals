package tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.adapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emftext.language.java.members.Method;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;
import tools.vitruv.framework.correspondence.CorrespondenceModel;
import tools.vitruv.framework.correspondence.CorrespondenceModelUtil;

public class CodeInstrumentation {

	private static final Logger LOGGER = Logger.getLogger(CodeInstrumentation.class);
	private final CorrespondenceModel cm;
	private final Repository pcmModel;
	private final SeffChangesAdapter seffChangesAdapter;

	public CodeInstrumentation(CorrespondenceModel cm, Repository pcmModel) {
		this.cm = cm;
		this.pcmModel = pcmModel;
		this.seffChangesAdapter = new SeffChangesAdapter();
		this.pcmModel.eAdapters().add(seffChangesAdapter);
	}

	public void generateInstrumentedCode() {
		List<ResourceDemandingSEFF> allSeffs = PcmUtils.getObjects(this.pcmModel, ResourceDemandingSEFF.class);
	}

	private void instrumentServiceCall(ResourceDemandingSEFF seff) {
		Set<Method> correspondingServiceMethod = CorrespondenceModelUtil.getCorrespondingEObjectsByType(this.cm,
				seff, Method.class);

		if (correspondingServiceMethod == null || correspondingServiceMethod.size() != 1) {
			LOGGER.warn("Could not instrument seff " + seff.getId() + ".");
			return;
		}
		
		this.instrumentServiceCall(seff, correspondingServiceMethod.iterator().next());
	}
	
	private void instrumentServiceCall(ResourceDemandingSEFF seff, Method method) {
		String methodId = EcoreUtil.getIdentification(method);
		//EcoreUtil.getRelativeURIFragmentPath(ancestorEObject, descendantEObject)
	}

	public void reset() {

	}

	private static class SeffChangesAdapter extends EContentAdapter {

		private final Set<ResourceDemandingSEFF> changedSeffs;

		public Set<ResourceDemandingSEFF> getChangedSeffs() {
			return this.changedSeffs;
		}

		public void reset() {
			this.changedSeffs.clear();
		}

		public SeffChangesAdapter() {
			this.changedSeffs = new HashSet<ResourceDemandingSEFF>();
		}

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);

			if (notification.getNotifier() instanceof ResourceDemandingSEFF == false) {
				return;
			}

			switch (notification.getEventType()) {
			case Notification.SET:
			case Notification.ADD:
				changedSeffs.add((ResourceDemandingSEFF) notification.getNotifier());
				break;
			default:
				break;
			}
		}
	}
}
