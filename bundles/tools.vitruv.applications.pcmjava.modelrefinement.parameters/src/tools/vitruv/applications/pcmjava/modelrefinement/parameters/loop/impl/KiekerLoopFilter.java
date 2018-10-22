package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl;

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
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.LoopRecord;

/**
 * Implements the {@link LoopDataSet} by filtering loop records, read via Kieker.
 * 
 * @author JP
 *
 */
@Plugin(description = "A filter for loop iteration records.")
public final class KiekerLoopFilter extends AbstractFilterPlugin implements LoopDataSet {

    /**
     * The name of the input port for incoming events.
     */
    public static final String INPUT_PORT_NAME_EVENTS = "inputEvent";

    private final Map<String, List<LoopRecord>> loopIdToRecord;

    /**
     * Initializes a new instance of {@link KiekerLoopFilter}. Each Plugin requires a constructor with a Configuration
     * object and a IProjectContext.
     * 
     * @param configuration
     *            The configuration for this component.
     * @param projectContext
     *            The project context for this component. The component will be registered.
     */
    public KiekerLoopFilter(final Configuration configuration, final IProjectContext projectContext) {
        super(configuration, projectContext);
        this.loopIdToRecord = new HashMap<>();
    }

    @Override
    public Configuration getCurrentConfiguration() {
        return new Configuration();
    }

    @Override
    public Set<String> getLoopIds() {
        return this.loopIdToRecord.keySet();
    }

    @Override
    public List<LoopRecord> getLoopRecords(final String loopId) {
        return this.loopIdToRecord.get(loopId);
    }

    /**
     * This method is called by kieker for each record of the type specified by {@link InputPort}.
     * 
     * @param record
     *            The record of the specified type.
     */
    @InputPort(
            name = INPUT_PORT_NAME_EVENTS,
            description = "Input for loop iteration records.",
            eventTypes = { LoopRecord.class })
    public final void inputEvent(final LoopRecord record) {
        String loopId = record.getLoopId();
        List<LoopRecord> loopRecords = this.loopIdToRecord.get(loopId);
        if (loopRecords == null) {
            loopRecords = new ArrayList<>();
            this.loopIdToRecord.put(loopId, loopRecords);
        }
        loopRecords.add(record);
    }
}