package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResponseTimeDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationDataSet;

public interface MonitoringDataSet {

	ResponseTimeDataSet getResponseTimes();

	ServiceCallDataSet getServiceCalls();

	ResourceUtilizationDataSet getResourceUtilizations();

	LoopDataSet getLoops();

	BranchDataSet getBranches();

}