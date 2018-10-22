package tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl;

import java.util.Optional;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.impl.KiekerBranchFilter;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.LoopDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl.KiekerLoopFilter;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResponseTimeDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl.KiekerResponseTimeFilter;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.impl.KiekerResourceUtilizationFilter;

/**
 * Implementation of the {@link MonitoringDataSet} by using kieker as reader of monitoring data.
 * 
 * @author JP
 *
 */
public class KiekerMonitoringReader implements MonitoringDataSet {

    private KiekerResponseTimeFilter responseTimeFilter;
    private KiekerServiceCallRecordFilter callRecordFilter;
    private KiekerResourceUtilizationFilter cpuFilter;
    private KiekerLoopFilter loopFilter;
    private KiekerBranchFilter branchFilter;

    /**
     * Initializes a new instance of {@link KiekerMonitoringReader}.
     * 
     * @param kiekerRecordsDirectoryPath
     *            The path of the directory for kieker monitoring data.
     * @param sessionId
     *            The session id for which the monitoring data will be filtered during read.
     */
    public KiekerMonitoringReader(final String kiekerRecordsDirectoryPath, final String sessionId) {
        this.read(kiekerRecordsDirectoryPath, Optional.of(sessionId));
    }

    /**
     * Initializes a new instance of {@link KiekerMonitoringReader}. The records are not filtered by a session id.
     * 
     * @param kiekerRecordsDirectoryPath
     *            The path of the directory for kieker monitoring data.
     */
    public KiekerMonitoringReader(String kiekerRecordsDirectoryPath) {
        this.read(kiekerRecordsDirectoryPath, Optional.empty());
    }

    @Override
    public BranchDataSet getBranches() {
        return this.branchFilter;
    }

    @Override
    public LoopDataSet getLoops() {
        return this.loopFilter;
    }

    @Override
    public ResourceUtilizationDataSet getResourceUtilizations() {
        return this.cpuFilter;
    }

    @Override
    public ResponseTimeDataSet getResponseTimes() {
        return this.responseTimeFilter;
    }

    @Override
    public ServiceCallDataSet getServiceCalls() {
        return this.callRecordFilter;
    }

    private void internRead(final String kiekerRecordsDirectoryPath, final Optional<String> sessionId)
            throws Exception {
        // Create Kieker Analysis instance
        final IAnalysisController analysisInstance = new AnalysisController();

        // Set file system monitoring log input directory for our analysis
        final Configuration fsReaderConfig = new Configuration();
        fsReaderConfig.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, kiekerRecordsDirectoryPath);
        final FSReader reader = new FSReader(fsReaderConfig, analysisInstance);

        Configuration recordFilterConfig = new Configuration();
        if (sessionId.isPresent()) {
            recordFilterConfig.putIfAbsent("SessionId", sessionId.get());
        }

        KiekerSessionFilter sessionFilter = new KiekerSessionFilter(recordFilterConfig, analysisInstance);

        // Connect the output of the reader with the input of the filter.
        analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, sessionFilter,
                KiekerSessionFilter.INPUT_PORT_NAME_EVENTS);

        Configuration emptyFilterConfig = new Configuration();

        // Create the session filter.
        this.responseTimeFilter = new KiekerResponseTimeFilter(emptyFilterConfig, analysisInstance);

        // Connect the output of the session filter with the input of the filter.
        analysisInstance.connect(sessionFilter, KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS, this.responseTimeFilter,
                KiekerResponseTimeFilter.INPUT_PORT_NAME_EVENTS);

        this.callRecordFilter = new KiekerServiceCallRecordFilter(emptyFilterConfig, analysisInstance);

        // Connect the output of the session filter with the input of the filter.
        analysisInstance.connect(sessionFilter, KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS, this.callRecordFilter,
                KiekerServiceCallRecordFilter.INPUT_PORT_NAME_EVENTS);

        this.cpuFilter = new KiekerResourceUtilizationFilter(emptyFilterConfig, analysisInstance);

        // Connect the output of the reader with the input of the filter.
        analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, this.cpuFilter,
                KiekerResourceUtilizationFilter.INPUT_PORT_NAME_EVENTS);

        this.loopFilter = new KiekerLoopFilter(emptyFilterConfig, analysisInstance);

        // Connect the output of the session filter with the input of the filter.
        analysisInstance.connect(sessionFilter, KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS, this.loopFilter,
                KiekerLoopFilter.INPUT_PORT_NAME_EVENTS);

        this.branchFilter = new KiekerBranchFilter(emptyFilterConfig, analysisInstance);

        // Connect the output of the session filter with the input of the filter.
        analysisInstance.connect(sessionFilter, KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS, this.branchFilter,
                KiekerBranchFilter.INPUT_PORT_NAME_EVENTS);

        // Start reading all records.
        analysisInstance.run();
    }

    private void read(final String kiekerRecordsDirectoryPath, final Optional<String> sessionId) {
        try {
            this.internRead(kiekerRecordsDirectoryPath, sessionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
