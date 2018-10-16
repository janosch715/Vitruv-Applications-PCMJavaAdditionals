package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl.KiekerMonitoringReader;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.BranchEstimationMock;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.LoopEstimationMock;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.ResourceUtilizationDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.impl.ResourceUtilizationEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class ResourceUtilizationEstimationTest {

	@Test
	@Ignore
	public void checkResourceId() {
		Repository pcmModel = PcmUtils.loadModel("./test-data/simple/default.repository");
		List<ParametricResourceDemand> rds = PcmUtils.getObjects(pcmModel, ParametricResourceDemand.class);
		List<ProcessingResourceType> resourceTypes = rds.stream()
				.map(a -> a.getRequiredResource_ParametricResourceDemand()).collect(Collectors.toList());
		
		assertEquals(1, resourceTypes.size());
		assertEquals("_oro4gG3fEdy4YaaT-RYrLQ", resourceTypes.get(0).getId());
	}

	@Test
	public void estimationTest() {
		MonitoringDataSet reader = new KiekerMonitoringReader("./test-data/simple", "session-1");
		Repository pcmModel = PcmUtils.loadModel("./test-data/simple/default.repository");

		ResourceUtilizationEstimationImpl estimation = new ResourceUtilizationEstimationImpl(Collections.emptySet(),
				pcmModel, reader.getServiceCalls(), new LoopEstimationMock(), new BranchEstimationMock(),
				new ResourceDemandEstimationMock());

		ResourceUtilizationDataSet results = estimation.estimateRemainingUtilization(reader.getResourceUtilizations());

		assertEquals(1.0, (double) reader.getResourceUtilizations().getUtilization("_oro4gG3fEdy4YaaT-RYrLQ")
				.get(1539590223478864089L), 0.00001);
		assertEquals(0.0, (double) results.getUtilization("_oro4gG3fEdy4YaaT-RYrLQ").get(1539590223478864089L),
				0.00001);
	}

	@Test
	public void checkIgnoreInternalActionsTest() {
		MonitoringDataSet reader = new KiekerMonitoringReader("./test-data/simple", "session-1");
		Repository pcmModel = PcmUtils.loadModel("./test-data/simple/default.repository");

		Set<String> allInternalActionIds = reader.getResponseTimes().getInternalActionIds();

		ResourceUtilizationEstimationImpl estimation = new ResourceUtilizationEstimationImpl(allInternalActionIds,
				pcmModel, reader.getServiceCalls(), new LoopEstimationMock(), new BranchEstimationMock(),
				new ResourceDemandEstimationMock());

		ResourceUtilizationDataSet results = estimation.estimateRemainingUtilization(reader.getResourceUtilizations());

		for (String resourceId : reader.getResourceUtilizations().getResourceIds()) {
			assertEquals(reader.getResourceUtilizations().getUtilization(resourceId),
					results.getUtilization(resourceId));
		}
	}
}
