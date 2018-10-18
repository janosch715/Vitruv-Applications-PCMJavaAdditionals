package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParametersUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl.ResourceDemandEstimationImpl;

public class ResourceDemandEstimationTest {

	@BeforeClass
	public static void setUp() {
		LoggingUtil.InitConsoleLogger();
	}

	@Test
	public void estimateAllTest() {
		MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);
		Repository pcmModel = SimpleTestData.loadPcmModel();

		ResourceDemandEstimationImpl rdEstimation = new ResourceDemandEstimationImpl(new LoopPredictionMock(),
				new BranchPredictionMock());
		rdEstimation.update(pcmModel, reader.getServiceCalls(), reader.getResourceUtilizations(),
				reader.getResponseTimes());

		double result1 = rdEstimation.estimateResourceDemand(SimpleTestData.FirstInternalActionId,
				SimpleTestData.ResourceId, ServiceParametersUtil.buildServiceCall("a", 1));
		assertEquals(0.00001, result1, 0.0001);

		double result2 = rdEstimation.estimateResourceDemand(SimpleTestData.FirstInternalActionId,
				SimpleTestData.ResourceId, ServiceParametersUtil.buildServiceCall("a", 8));
		assertEquals(0.00069, result2, 0.0001);
	}

}
