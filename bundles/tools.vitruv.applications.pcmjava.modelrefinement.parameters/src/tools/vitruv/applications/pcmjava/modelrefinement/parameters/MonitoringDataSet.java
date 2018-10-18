package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResponseTimeDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationDataSet;

public interface MonitoringDataSet {

    BranchDataSet getBranches();

    LoopDataSet getLoops();

    ResourceUtilizationDataSet getResourceUtilizations();

    ResponseTimeDataSet getResponseTimes();

    ServiceCallDataSet getServiceCalls();

}