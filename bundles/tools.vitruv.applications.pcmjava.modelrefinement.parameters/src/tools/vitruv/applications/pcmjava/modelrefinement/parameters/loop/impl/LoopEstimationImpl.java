package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.LoopAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopPrediction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class LoopEstimationImpl implements LoopEstimation, LoopPrediction {
	
	private static final Logger LOGGER = Logger.getLogger(LoopEstimationImpl.class);
	private final Map<String, LoopModel> modelCache;
	
	public LoopEstimationImpl() {
		this.modelCache = new HashMap<String, LoopModel>();
	}
	
	@Override
	public void update(Repository pcmModel, ServiceCallDataSet serviceCalls,
			LoopDataSet loopIterations) {
		
		WekaLoopModelEstimation estimation = 
				new WekaLoopModelEstimation(serviceCalls, loopIterations);
		
		Map<String, LoopModel> loopModels = estimation.estimateAll();
		
		this.modelCache.putAll(loopModels);
		
		this.applyEstimations(pcmModel);
	}
	
	@Override
	public double estimateIterations(LoopAction loop, ServiceCall serviceCall) {
		LoopModel loopModel = this.modelCache.get(loop.getId());
		if (loopModel == null) {
			throw new IllegalArgumentException("A estimation for loop with id " + loop.getId() + " was not found.");
		}
		return loopModel.estimateIterations(serviceCall);
	}
	
	private void applyEstimations(Repository pcmModel) {
		List<LoopAction> loops = PcmUtils.getObjects(pcmModel, LoopAction.class);
		for (LoopAction loopAction : loops) {
			this.applyModel(loopAction);
		}
	}
	
	private void applyModel(LoopAction loop) {
		LoopModel loopModel = this.modelCache.get(loop.getId());
		if (loopModel == null) {
			LOGGER.warn("A estimation for loop with id " + loop.getId() + " was not found. Nothing is set for this loop.");
			return;
		}
		String stoEx = loopModel.getIterationsStochasticExpression();
		PCMRandomVariable randomVariable = CoreFactory.eINSTANCE.createPCMRandomVariable();
		randomVariable.setSpecification(stoEx);
		loop.setIterationCount_LoopAction(randomVariable);
	}
}
