package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.SeffFactory;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParametersUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl.LoopEstimationImpl;

public class LoopIterationEstimationTest {

	@BeforeClass
	public static void setUp() {
		LoggingUtil.InitConsoleLogger();
	}

	private LoopEstimationImpl loopEstimation;
	private LoopAction loopAction;
	private Repository repository;

	@Before
	public void setUpTest() {
		this.loopEstimation = new LoopEstimationImpl();
		this.loopAction = this.createLoopAction();
		this.repository = RepositoryFactory.eINSTANCE.createRepository();
	}

	@Test
	public void estimateLoopIterationTest() {
		MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);

		this.loopEstimation.update(this.repository, reader.getServiceCalls(), reader.getLoops());

		double loopEstimationResult = this.loopEstimation.estimateIterations(this.loopAction,
				ServiceParametersUtil.buildServiceCall("a", 12));

		assertEquals(12.0, loopEstimationResult, 10e-5);
	}
	
	private LoopAction createLoopAction() {
		LoopAction loopAction = SeffFactory.eINSTANCE.createLoopAction();
		loopAction.setId(SimpleTestData.LoopId);
		return loopAction;
	}
}
