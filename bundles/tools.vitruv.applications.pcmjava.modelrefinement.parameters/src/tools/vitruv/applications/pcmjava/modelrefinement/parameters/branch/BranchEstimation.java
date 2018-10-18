package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch;

import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;

public interface BranchEstimation {

    void update(Repository pcmModel, ServiceCallDataSet serviceCalls, BranchDataSet branchExecutions);
}