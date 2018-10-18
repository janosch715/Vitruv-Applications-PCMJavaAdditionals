package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import java.util.List;
import java.util.Set;

/**
 * The monitoring data of service calls. Every time a service is called a {@link ServiceCall} record is stored.
 *
 * @author JP
 *
 */
public interface ServiceCallDataSet {

    /**
     * Gets the parameters of a service call.
     *
     * @param serviceExecutionId
     *            The unique service execution ID.
     * @return The parameters of the service call.
     */
    ServiceParameters getParametersOfServiceCall(String serviceExecutionId);

    /**
     * Gets all service calls.
     *
     * @return All service call records.
     */
    List<ServiceCall> getServiceCalls();

    /**
     * Gets the service call records for a SEFF ID.
     *
     * @param serviceId
     *            The SEFF ID for a service.
     * @return The service call records for the specified SEFF ID, or an empty list.
     */
    List<ServiceCall> getServiceCalls(String serviceId);

    /**
     * Gets the SEFF IDs of the called services.
     *
     * @return The SEFF IDs of the called services.
     */
    Set<String> getServiceIds();

    /**
     * Gets the service SEFF IDs for an external call ID.
     *
     * @param externalCallId
     *            The id of a an external call.
     * @return The service SEFF IDs for an external call ID, or an empty set.
     */
    Set<String> getServiceIdsForExternalCallId(String externalCallId);

    /**
     * Converts the time of type long, used by the records, into seconds.
     *
     * @param time
     *            The input for the conversion.
     * @return The input time in seconds.
     */
    double timeToSeconds(long time);

}