package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization;

public interface ResourceUtilizationEstimation {

	ResourceUtilizationDataSet estimateRemainingUtilization(ResourceUtilizationDataSet completeResourceUtilization);

}