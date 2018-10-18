package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationDataSet;

public interface ResourceDemandEstimation {
    void update(Repository pcmRepository, ServiceCallDataSet serviceCalls,
            ResourceUtilizationDataSet resourceUtilizations, ResponseTimeDataSet responseTimes);
}