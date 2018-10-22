package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.impl;

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
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.BranchRecord;

/**
 * Implements the {@link BranchDataSet} by filtering branch records, read via Kieker.
 * 
 * @author JP
 *
 */
@Plugin(description = "A filter for loop iteration records.")
public final class KiekerBranchFilter extends AbstractFilterPlugin implements BranchDataSet {

    /**
     * The name of the input port for incoming events.
     */
    public static final String INPUT_PORT_NAME_EVENTS = "inputEvent";

    private final Map<String, List<BranchRecord>> branchIdToRecord;

    /**
     * Initializes a new instance of {@link KiekerBranchFilter}. Each Plugin requires a constructor with a Configuration
     * object and a IProjectContext.
     * 
     * @param configuration
     *            The configuration for this component.
     * @param projectContext
     *            The project context for this component. The component will be registered.
     */
    public KiekerBranchFilter(final Configuration configuration, final IProjectContext projectContext) {
        super(configuration, projectContext);
        this.branchIdToRecord = new HashMap<>();
    }

    @Override
    public Set<String> getBranchIds() {
        return this.branchIdToRecord.keySet();
    }

    @Override
    public String getBranchNotExecutedId() {
        return BranchRecord.EXECUTED_BRANCH_ID;
    }

    @Override
    public List<BranchRecord> getBranchRecords(final String branchId) {
        return this.branchIdToRecord.get(branchId);
    }

    @Override
    public Configuration getCurrentConfiguration() {
        return new Configuration();
    }

    /**
     * This method is called by kieker for each record of the type specified by {@link InputPort}.
     * 
     * @param record
     *            The record of the specified type.
     */
    @InputPort(
            name = INPUT_PORT_NAME_EVENTS,
            description = "Input for branch records.",
            eventTypes = { BranchRecord.class })
    public final void inputEvent(final BranchRecord record) {
        String branchId = record.getBranchId();
        List<BranchRecord> branchRecords = this.branchIdToRecord.get(branchId);
        if (branchRecords == null) {
            branchRecords = new ArrayList<>();
            this.branchIdToRecord.put(branchId, branchRecords);
        }
        branchRecords.add(record);
    }
}