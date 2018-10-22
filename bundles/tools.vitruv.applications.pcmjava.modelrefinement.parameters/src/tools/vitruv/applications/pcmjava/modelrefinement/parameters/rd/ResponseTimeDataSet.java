package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import java.util.List;
import java.util.Set;

import org.palladiosimulator.pcm.resourcetype.ResourceType;
import org.palladiosimulator.pcm.seff.InternalAction;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.ResponseTimeRecord;

/**
 * The monitoring data of response times. Every time a internal action is executed, a {@link ResponseTimeRecord} record
 * is stored.
 *
 * @author JP
 *
 */
public interface ResponseTimeDataSet {

    /**
     * Gets the time of the earliest entry.
     * 
     * @return The time of the earliest entry.
     */
    Long getEarliestEntry();

    /**
     * Gets the time of the latest entry.
     * 
     * @return The time of the latest entry.
     */
    Long getLatestEntry();

    /**
     * Gets the IDs of all monitored internal actions.
     * 
     * @return A set of all internal action IDs.
     */
    Set<String> getInternalActionIds();

    /**
     * Gets the IDs of all monitored resource types for an internal action.
     * 
     * @param internalActionId
     *            The id of the internal action.
     * 
     * @return A set of all resource type IDs.
     */
    Set<String> getResourceIds(String internalActionId);

    /**
     * Gets all records for a specific internal action ID and resource type ID. The internal action ID is the value
     * returned by {@link InternalAction#getId()} and the resource type ID the value returned by
     * {@link ResourceType#getId()}.
     * 
     * @param internalActionId
     *            The ID of the internal action.
     * @param resourceId
     *            The ID of the resource type.
     * @return All records for the specified IDs.
     */
    List<ResponseTimeRecord> getResponseTimes(String internalActionId, String resourceId);

    /**
     * Converts the time of type long, used by the records, into seconds.
     *
     * @param time
     *            The input for the conversion.
     * @return The input time in seconds.
     */
    double timeToSeconds(long time);
}