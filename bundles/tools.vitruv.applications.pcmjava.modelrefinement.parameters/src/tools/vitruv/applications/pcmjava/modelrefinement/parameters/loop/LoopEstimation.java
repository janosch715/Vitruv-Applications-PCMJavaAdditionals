package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;

public interface LoopEstimation {

    void update(Repository pcmModel, ServiceCallDataSet serviceCalls, LoopDataSet loopIterations);
}