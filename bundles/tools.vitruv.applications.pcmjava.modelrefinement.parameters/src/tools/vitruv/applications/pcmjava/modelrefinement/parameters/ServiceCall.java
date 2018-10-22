package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import java.util.Optional;

import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;

/**
 * Describes a service call record. Other records do not hide behind an interface. Service calls do, because the
 * parameters field of the record needs special parsing.
 * 
 * @author JP
 *
 */
public interface ServiceCall {

    /**
     * Gets the caller id, the id of the {@link ExternalCallAction} which led to the service call.
     * 
     * @return The {@link ExternalCallAction#getId()} of the external call action, or empty if not set.
     */
    Optional<String> getCallerId();

    /**
     * Gets the unique service execution id of the service calling this service.
     * 
     * @return The unique service execution id of the service calling this service.
     */
    String getCallerServiceExecutionId();

    /**
     * Gets the service entry time.
     * 
     * @return The service entry time. Use {@link ServiceCall#timeToSeconds(long)} to convert into seconds.
     */
    long getEntryTime();

    /**
     * Gets the service exit time.
     * 
     * @return The service exit time. Use {@link ServiceCall#timeToSeconds(long)} to convert into seconds.
     */
    long getExitTime();

    /**
     * Gets the service call parameters for this execution.
     * 
     * @return The service call parameters.
     */
    ServiceParameters getParameters();

    /**
     * Gets the service response time.
     * 
     * @return The service response time. Use {@link ServiceCall#timeToSeconds(long)} to convert into seconds.
     */
    long getResponseTime();

    /**
     * Converts the time of the records into seconds.
     * 
     * @param time
     *            A time value of the record.
     * @return The time in seconds.
     */
    double timeToSeconds(long time);

    /**
     * Gets the service response time in seconds.
     * 
     * @return The service response time in seconds.
     */
    double getResponseTimeSeconds();

    /**
     * Gets the unique service execution id for this record. Other monitoring records reference this id.
     * 
     * @return The unique service execution id.
     */
    String getServiceExecutionId();

    /**
     * Gets the id of the {@link ResourceDemandingSEFF} of the service.
     * 
     * @return The {@link ResourceDemandingSEFF#getId()} of the service.
     */
    String getServiceId();
}
