package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.system.CPUUtilizationRecord;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationDataSet;

@Plugin(description = "A filter for resource utilization records.")
public final class KiekerResourceUtilizationFilter extends AbstractFilterPlugin implements ResourceUtilizationDataSet {

    /**
     * The name of the input port for incoming events.
     */
    public static final String INPUT_PORT_NAME_EVENTS = "inputEvent";

    private final Map<String, SortedMap<Long, Double>> cpuUtilization;

    public KiekerResourceUtilizationFilter(final Configuration configuration, final IProjectContext projectContext) {
        super(configuration, projectContext);
        this.cpuUtilization = new HashMap<>();
    }

    @Override
    public Configuration getCurrentConfiguration() {
        return new Configuration();
    }

    @Override
    public Set<String> getResourceIds() {
        return this.cpuUtilization.keySet();
    }

    @Override
    public SortedMap<Long, Double> getUtilization(final String resourceId) {
        return this.cpuUtilization.get(resourceId);
    }

    @InputPort(
            name = INPUT_PORT_NAME_EVENTS,
            description = "Input for cpu utilization records.",
            eventTypes = { CPUUtilizationRecord.class })
    public final void inputEvent(final CPUUtilizationRecord record) {
        if (record.getCpuID().equals("0") == false) {
            return;
        }
        String cpuId = "_oro4gG3fEdy4YaaT-RYrLQ";

        SortedMap<Long, Double> singleCpuUtilization = this.cpuUtilization.get(cpuId);
        if (singleCpuUtilization == null) {
            singleCpuUtilization = new TreeMap<>();
            this.cpuUtilization.put(cpuId, singleCpuUtilization);
        }
        singleCpuUtilization.put(record.getTimestamp(), record.getTotalUtilization());
    }

    @Override
    public double timeToSeconds(final long time) {
        return time / 1.0e9;
    }
}