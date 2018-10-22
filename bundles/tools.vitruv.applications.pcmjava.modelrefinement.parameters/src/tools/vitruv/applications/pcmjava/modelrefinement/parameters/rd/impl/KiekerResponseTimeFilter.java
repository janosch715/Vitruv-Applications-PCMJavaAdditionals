package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.ResponseTimeRecord;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResponseTimeDataSet;

/**
 * Implements the {@link ResponseTimeDataSet} by filtering response time records, read via Kieker.
 * 
 * @author JP
 *
 */
@Plugin(description = "A filter for response time records.")
public final class KiekerResponseTimeFilter extends AbstractFilterPlugin implements ResponseTimeDataSet {

    private static double TIME_TO_SECONDS = 1.0e-9;

    /**
     * The name of the input port for incoming events.
     */
    public static final String INPUT_PORT_NAME_EVENTS = "inputEvent";

    private final Map<String, Map<String, ArrayList<ResponseTimeRecord>>> internalActionIdAndReosurceIdToResponseTimeRecord;

    private Long earliestEntry = Long.MAX_VALUE;

    private Long latestEntry = Long.MIN_VALUE;

    /**
     * Initializes a new instance of {@link KiekerResponseTimeFilter}. Each Plugin requires a constructor with a
     * Configuration object and a IProjectContext.
     * 
     * @param configuration
     *            The configuration for this component.
     * @param projectContext
     *            The project context for this component. The component will be registered.
     */
    public KiekerResponseTimeFilter(final Configuration configuration, final IProjectContext projectContext) {
        super(configuration, projectContext);
        this.internalActionIdAndReosurceIdToResponseTimeRecord = new HashMap<>();
    }

    @Override
    public Configuration getCurrentConfiguration() {
        return new Configuration();
    }

    @Override
    public Long getEarliestEntry() {
        return this.earliestEntry;
    }

    @Override
    public Set<String> getInternalActionIds() {
        return this.internalActionIdAndReosurceIdToResponseTimeRecord.keySet();
    }

    @Override
    public Long getLatestEntry() {
        return this.latestEntry;
    }

    @Override
    public Set<String> getResourceIds(final String internalActionId) {
        return this.internalActionIdAndReosurceIdToResponseTimeRecord.get(internalActionId).keySet();
    }

    @Override
    public List<ResponseTimeRecord> getResponseTimes(final String internalActionId, final String resourceId) {
        return this.internalActionIdAndReosurceIdToResponseTimeRecord.get(internalActionId).get(resourceId);
    }

    /**
     * This method is called by kieker for each record of the type specified by {@link InputPort}.
     * 
     * @param record
     *            The record of the specified type.
     */
    @InputPort(
            name = INPUT_PORT_NAME_EVENTS,
            description = "Input for response time records.",
            eventTypes = { ResponseTimeRecord.class })
    public final void inputEvent(final ResponseTimeRecord record) {
        String internalActionId = record.getInternalActionId();
        Map<String, ArrayList<ResponseTimeRecord>> resourceToResponseTimeRecord = this.internalActionIdAndReosurceIdToResponseTimeRecord
                .get(internalActionId);
        if (resourceToResponseTimeRecord == null) {
            resourceToResponseTimeRecord = new HashMap<>();
            this.internalActionIdAndReosurceIdToResponseTimeRecord.put(internalActionId, resourceToResponseTimeRecord);
        }

        String resourceId = record.getResourceId();
        ArrayList<ResponseTimeRecord> responseTimeRecords = resourceToResponseTimeRecord.get(resourceId);
        if (responseTimeRecords == null) {
            responseTimeRecords = new ArrayList<>();
            resourceToResponseTimeRecord.put(resourceId, responseTimeRecords);
        }
        responseTimeRecords.add(record);

        this.earliestEntry = Math.min(this.earliestEntry, record.getStartTime());
        this.latestEntry = Math.max(this.latestEntry, record.getStartTime());
    }

    @Override
    public double timeToSeconds(final long time) {
        return time * TIME_TO_SECONDS;
    }
}