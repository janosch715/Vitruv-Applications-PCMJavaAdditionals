package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.GuardedBranchTransition;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchPrediction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class BranchEstimationImpl implements BranchEstimation, BranchPrediction {

	private static final Logger LOGGER = Logger.getLogger(BranchEstimationImpl.class);

	private final Map<String, BranchModel> modelCache;
	private final Random random;

	public BranchEstimationImpl() {
		this(ThreadLocalRandom.current());
	}

	public BranchEstimationImpl(Random random) {
		this.modelCache = new HashMap<String, BranchModel>();
		this.random = random;
	}

	@Override
	public void update(Repository pcmModel, ServiceCallDataSet serviceCalls, BranchDataSet branchExecutions) {
		TreeWekaBranchModelEstimation estimation = new TreeWekaBranchModelEstimation(serviceCalls, branchExecutions,
				this.random);

		Map<String, BranchModel> branchModels = estimation.estimateAll();

		this.modelCache.putAll(branchModels);
		
		this.applyEstimations(pcmModel);
	}

	@Override
	public Optional<AbstractBranchTransition> estimateBranch(BranchAction branch, ServiceCall serviceCall) {
		BranchModel branchModel = this.modelCache.get(branch.getId());
		if (branchModel == null) {
			throw new IllegalArgumentException(
					"An estimation for branch with id " + branch.getId() + " was not found.");
		}
		Optional<String> estimatedBranchId = branchModel.estimateBranchId(serviceCall);

		if (estimatedBranchId.isPresent() == false) {
			return Optional.empty();
		}

		Optional<AbstractBranchTransition> estimatedBranch = branch.getBranches_Branch().stream()
				.filter(transition -> transition.getId().equals(estimatedBranchId.get())).findFirst();

		if (estimatedBranch.isPresent() == false) {
			throw new IllegalArgumentException(
					"The estimated branch transition with id " + estimatedBranchId.get() + " does not exist in SEFF.");
		}

		return Optional.of(estimatedBranch.get());
	}

	private void applyEstimations(Repository pcmModel) {
		List<BranchAction> branches = PcmUtils.getObjects(pcmModel, BranchAction.class);
		for (BranchAction branch : branches) {
			this.applyModel(branch);
		}
	}

	private void applyModel(BranchAction branch) {
		for (AbstractBranchTransition branchTransition : branch.getBranches_Branch()) {
			if (branchTransition instanceof GuardedBranchTransition) {
				this.applyModel(branch.getId(), (GuardedBranchTransition) branchTransition);
			} else {
				LOGGER.warn("A estimation for transition " + branchTransition.getId() + " in branch with id "
						+ branch.getId()
						+ " is not of type GuardedBranchTransition. Nothing is set for this branch transition.");
			}
		}
	}

	private void applyModel(String branchId, GuardedBranchTransition branch) {
		BranchModel branchModel = this.modelCache.get(branchId);
		if (branchModel == null) {
			LOGGER.warn(
					"A estimation for branch with id " + branchId + " was not found. Nothing is set for this branch.");
			return;
		}
		String stoEx = branchModel.getBranchStochasticExpression(branch.getId());
		PCMRandomVariable randomVariable = CoreFactory.eINSTANCE.createPCMRandomVariable();
		randomVariable.setSpecification(stoEx);
		branch.setBranchCondition_GuardedBranchTransition(randomVariable);
	}
}
