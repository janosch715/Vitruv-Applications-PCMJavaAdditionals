package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParametersUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl.KiekerMonitoringReader;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl.ResourceDemandEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class ResourceDemandEstimationTest {

	@BeforeClass
	public static void setUp() {
		LoggingUtil.InitConsoleLogger();
	}

	@Test
	public void estimateAllTest() {
		MonitoringDataSet reader = new KiekerMonitoringReader("./test-data/simple", "session-1");
		Repository pcmModel = PcmUtils.loadModel("./test-data/simple/default.repository");

		ResourceDemandEstimationImpl rdEstimation = new ResourceDemandEstimationImpl(new LoopEstimationMock(),
				new BranchEstimationMock());
		rdEstimation.updateModels(pcmModel, reader.getServiceCalls(), reader.getResourceUtilizations(),
				reader.getResponseTimes());

		double result1 = rdEstimation.estimateResourceDemand("_OkrUMMjSEeiWRYm1yDC5rQ", "_oro4gG3fEdy4YaaT-RYrLQ",
				ServiceParametersUtil.buildServiceCall("a", 1));
		assertEquals(0.00001, result1, 0.0001);

		double result2 = rdEstimation.estimateResourceDemand("_OkrUMMjSEeiWRYm1yDC5rQ", "_oro4gG3fEdy4YaaT-RYrLQ",
				ServiceParametersUtil.buildServiceCall("a", 8));
		assertEquals(0.00069, result2, 0.0001);
	}

}