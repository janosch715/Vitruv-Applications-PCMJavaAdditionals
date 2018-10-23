package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.BranchPredictionMock;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.LoopPredictionMock;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization.impl.ResourceUtilizationEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class ResourceUtilizationEstimationTest {

    @Test
    public void checkIgnoreInternalActionsTest() {
        MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);
        Repository pcmModel = SimpleTestData.loadPcmModel();

        Set<String> allInternalActionIds = reader.getResponseTimes().getInternalActionIds();

        ResourceUtilizationEstimationImpl estimation = new ResourceUtilizationEstimationImpl(allInternalActionIds,
                pcmModel, reader.getServiceCalls(), new LoopPredictionMock(), new BranchPredictionMock(),
                new ResourceDemandPredictionMock());

        ResourceUtilizationDataSet results = estimation.estimateRemainingUtilization(reader.getResourceUtilizations());

        for (String resourceId : reader.getResourceUtilizations().getResourceIds()) {
            assertEquals(reader.getResourceUtilizations().getUtilization(resourceId),
                    results.getUtilization(resourceId));
        }
    }

    @Test
    public void checkResourceId() {
        Repository pcmModel = SimpleTestData.loadPcmModel();
        List<ParametricResourceDemand> rds = PcmUtils.getObjects(pcmModel, ParametricResourceDemand.class);
        List<ProcessingResourceType> resourceTypes = rds.stream()
                .map(a -> a.getRequiredResource_ParametricResourceDemand()).collect(Collectors.toList());

        assertEquals(2, resourceTypes.size());
        assertEquals(SimpleTestData.ResourceId, resourceTypes.get(0).getId());
    }

    @Test
    public void estimationTest() {
        MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);
        Repository pcmModel = SimpleTestData.loadPcmModel();

        ResourceUtilizationEstimationImpl estimation = new ResourceUtilizationEstimationImpl(Collections.emptySet(),
                pcmModel, reader.getServiceCalls(), new LoopPredictionMock(), new BranchPredictionMock(),
                new ResourceDemandPredictionMock());

        ResourceUtilizationDataSet results = estimation.estimateRemainingUtilization(reader.getResourceUtilizations());

        assertEquals(1.0, reader.getResourceUtilizations().getUtilization(SimpleTestData.ResourceId)
                .get(1539699172429520175L), 0.00001);
        assertEquals(0.0, results.getUtilization(SimpleTestData.ResourceId).get(1539699172429520175L),
                0.00001);
    }
}
