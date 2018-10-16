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
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.impl.KiekerCpuUtilizationFilter;

public class KiekerMonitoringReader implements MonitoringDataSet {

	private KiekerResponseTimeFilter responseTimeFilter;
	private KiekerServiceCallRecordFilter callRecordFilter;
	private KiekerCpuUtilizationFilter cpuFilter;
	private KiekerLoopFilter loopFilter;
	private KiekerBranchFilter branchFilter;

	public KiekerMonitoringReader(String kiekerRecordsDirectoryPath, String sessionId) {
		this.read(kiekerRecordsDirectoryPath, Optional.of(sessionId));
	}

	/*public KiekerMonitoringReader(String kiekerRecordsDirectoryPath) {
		this.read(kiekerRecordsDirectoryPath, Optional.empty());
	}*/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseTimeDataSet getResponseTimes() {
		return responseTimeFilter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServiceCallDataSet getServiceCalls() {
		return callRecordFilter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceUtilizationDataSet getResourceUtilizations() {
		return cpuFilter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoopDataSet getLoops() {
		return loopFilter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BranchDataSet getBranches() {
		return branchFilter;
	}

	private void read(String kiekerRecordsDirectoryPath, Optional<String> sessionId) {
		try {
			this.internRead(kiekerRecordsDirectoryPath, sessionId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void internRead(String kiekerRecordsDirectoryPath, Optional<String> sessionId) throws Exception {
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
		analysisInstance.connect(sessionFilter, KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS, responseTimeFilter,
				KiekerResponseTimeFilter.INPUT_PORT_NAME_EVENTS);

		this.callRecordFilter = new KiekerServiceCallRecordFilter(emptyFilterConfig, analysisInstance);

		// Connect the output of the session filter with the input of the filter.
		analysisInstance.connect(sessionFilter, KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS, callRecordFilter,
				KiekerServiceCallRecordFilter.INPUT_PORT_NAME_EVENTS);

		this.cpuFilter = new KiekerCpuUtilizationFilter(emptyFilterConfig, analysisInstance);

		// Connect the output of the reader with the input of the filter.
		analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, cpuFilter,
				KiekerCpuUtilizationFilter.INPUT_PORT_NAME_EVENTS);

		this.loopFilter = new KiekerLoopFilter(emptyFilterConfig, analysisInstance);

		// Connect the output of the session filter with the input of the filter.
		analysisInstance.connect(sessionFilter, KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS, loopFilter,
				KiekerLoopFilter.INPUT_PORT_NAME_EVENTS);

		this.branchFilter = new KiekerBranchFilter(emptyFilterConfig, analysisInstance);

		// Connect the output of the session filter with the input of the filter.
		analysisInstance.connect(sessionFilter, KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS, branchFilter,
				KiekerBranchFilter.INPUT_PORT_NAME_EVENTS);

		// Start reading all records.
		analysisInstance.run();
	}
}
