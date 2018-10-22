package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.SeffFactory;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;
import org.palladiosimulator.pcm.seff.seff_performance.SeffPerformanceFactory;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParametersUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl.ResourceDemandEstimationImpl;

public class ResourceDemandEstimationTest {

    @Test
    public void estimateAllTest() {
        MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);
        Repository pcmModel = SimpleTestData.loadPcmModel();

        ResourceDemandEstimationImpl rdEstimation = new ResourceDemandEstimationImpl(new LoopPredictionMock(),
                new BranchPredictionMock());
        rdEstimation.update(pcmModel, reader.getServiceCalls(), reader.getResourceUtilizations(),
                reader.getResponseTimes());

        ProcessingResourceType prt = ResourcetypeFactory.eINSTANCE.createProcessingResourceType();
        prt.setId(SimpleTestData.ResourceId);

        InternalAction firstInternalAction = SeffFactory.eINSTANCE.createInternalAction();
        firstInternalAction.setId(SimpleTestData.FirstInternalActionId);

        ParametricResourceDemand firstRd = SeffPerformanceFactory.eINSTANCE.createParametricResourceDemand();
        firstRd.setAction_ParametricResourceDemand(firstInternalAction);
        firstRd.setRequiredResource_ParametricResourceDemand(prt);

        double result1 = rdEstimation.predictResourceDemand(firstRd, ServiceParametersUtil.buildServiceCall("a", 1));
        assertEquals(0.0011, result1, 0.0001);

        double result2 = rdEstimation.predictResourceDemand(firstRd, ServiceParametersUtil.buildServiceCall("a", 8));
        assertEquals(0.0707, result2, 0.0001);
    }

    @BeforeClass
    public static void setUp() {
        LoggingUtil.InitConsoleLogger();
    }

}
