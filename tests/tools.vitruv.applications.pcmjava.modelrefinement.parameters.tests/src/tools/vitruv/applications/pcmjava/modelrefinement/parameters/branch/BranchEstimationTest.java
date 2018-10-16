package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.GuardedBranchTransition;
import org.palladiosimulator.pcm.seff.SeffFactory;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParametersUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.impl.BranchEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;

public class BranchEstimationTest {

	@BeforeClass
	public static void setUp() {
		LoggingUtil.InitConsoleLogger();
	}

	private BranchEstimationImpl branchEstimation;
	private BranchAction branchAction;

	@Before
	public void setUpTest() {
		this.branchEstimation = new BranchEstimationImpl(new Random(1));
		this.branchAction = this.createBranchAction();
	}

	@Test
	public void estimateBranchExecutedTest() {
		MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);

		this.branchEstimation.updateModels(reader.getServiceCalls(), reader.getBranches());

		Optional<AbstractBranchTransition> result = this.branchEstimation.estimateBranch(this.branchAction,
				ServiceParametersUtil.buildServiceCall("a", 6));

		assertTrue(result.isPresent());
		assertEquals(SimpleTestData.FirstBranchTransitionId, result.get().getId());
	}

	@Test
	public void estimateNoBranchExecutedTest() {
		MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);

		this.branchEstimation.updateModels(reader.getServiceCalls(), reader.getBranches());

		Optional<AbstractBranchTransition> result = this.branchEstimation.estimateBranch(this.branchAction,
				ServiceParametersUtil.buildServiceCall("a", 1));

		assertFalse(result.isPresent());
	}

	private BranchAction createBranchAction() {
		BranchAction branchAction = SeffFactory.eINSTANCE.createBranchAction();
		branchAction.setId(SimpleTestData.FirstBranchId);
		GuardedBranchTransition branchTransition = SeffFactory.eINSTANCE.createGuardedBranchTransition();
		branchTransition.setId(SimpleTestData.FirstBranchTransitionId);
		branchAction.getBranches_Branch().add(branchTransition);
		return branchAction;
	}
}
