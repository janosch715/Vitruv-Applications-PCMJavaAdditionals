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

@Plugin(description = "A filter for loop iteration records.")
public final class KiekerLoopFilter extends AbstractFilterPlugin implements LoopDataSet {

    /**
     * The name of the input port for incoming events.
     */
    public static final String INPUT_PORT_NAME_EVENTS = "inputEvent";

    private final Map<String, List<LoopRecord>> loopIdToRecord;

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