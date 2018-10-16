package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch;

import java.util.List;
import java.util.Set;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.BranchRecord;

public interface BranchDataSet {

	Set<String> getBranchIds();

	List<BranchRecord> getBranchRecords(String branchId);
	
	String getBranchNotExecutedId();

}