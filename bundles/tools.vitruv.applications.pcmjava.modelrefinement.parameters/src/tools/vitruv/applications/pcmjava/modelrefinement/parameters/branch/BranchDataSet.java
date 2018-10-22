package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch;

import java.util.List;
import java.util.Set;

import org.palladiosimulator.pcm.usagemodel.Branch;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.BranchRecord;

/**
 * The monitoring data of branches. Every time a monitored branch is executed, a {@link BranchRecord} record is stored.
 *
 * @author JP
 *
 */
public interface BranchDataSet {

    /**
     * Gets the IDs of all monitored branches.
     * 
     * @return A set of all branch IDs.
     */
    Set<String> getBranchIds();

    /**
     * Gets the branch execution ID which is used if no branch path was taken.
     * 
     * @return The branch execution ID which is used if no branch path was taken.
     */
    String getBranchNotExecutedId();

    /**
     * Gets all records for a specific branch ID. The ID is the value returned by {@link Branch#getId()}.
     * 
     * @param branchId
     *            The ID of the branch.
     * @return All records for the specified ID.
     */
    List<BranchRecord> getBranchRecords(String branchId);

}