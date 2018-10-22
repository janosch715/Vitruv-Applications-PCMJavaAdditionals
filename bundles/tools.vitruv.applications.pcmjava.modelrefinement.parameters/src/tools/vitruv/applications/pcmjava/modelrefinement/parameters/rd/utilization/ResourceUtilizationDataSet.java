package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization;

import java.util.Set;
import java.util.SortedMap;

/**
 * The monitoring data of resource utilization.
 * 
 * @author JP
 *
 */
public interface ResourceUtilizationDataSet {

    /**
     * Gets all resource type IDs.
     * 
     * @return Set of resource type IDs.
     */
    Set<String> getResourceIds();

    /**
     * Gets the utilization for a resource type id.
     * 
     * @param resourceId
     *            The resource type id the utilization is returned for.
     * @return A sorted map, which maps time to resource utilization.
     */
    SortedMap<Long, Double> getUtilization(String resourceId);

    /**
     * Converts the time of type long, used by the records, into seconds.
     *
     * @param time
     *            The input for the conversion.
     * @return The input time in seconds.
     */
    double timeToSeconds(long time);
}
