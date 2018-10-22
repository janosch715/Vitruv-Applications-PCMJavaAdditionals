package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import java.util.List;
import java.util.Set;

import org.palladiosimulator.pcm.usagemodel.Loop;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.LoopRecord;

/**
 * The monitoring data of loops. Every time a monitored loop is executed, a {@link LoopRecord} record is stored.
 *
 * @author JP
 *
 */
public interface LoopDataSet {

    /**
     * Gets the IDs of all monitored loops.
     * 
     * @return A set of all loop IDs.
     */
    Set<String> getLoopIds();

    /**
     * Gets all records for a specific loop ID. The ID is the value returned by {@link Loop#getId()}.
     * 
     * @param loopId
     *            The ID of the loop.
     * @return All records for the specified ID.
     */
    List<LoopRecord> getLoopRecords(String loopId);

}
