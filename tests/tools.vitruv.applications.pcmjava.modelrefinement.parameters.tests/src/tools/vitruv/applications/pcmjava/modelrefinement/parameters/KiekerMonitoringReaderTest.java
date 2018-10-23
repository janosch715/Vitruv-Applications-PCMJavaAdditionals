package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.junit.Test;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;

public class KiekerMonitoringReaderTest {

    @Test
    public void kiekerReadTest() {
        MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);

        // Check service ids
        Set<String> serviceIds = reader.getServiceCalls().getServiceIds();

        Set<String> expectedServiceIds = new HashSet<>();
        expectedServiceIds.add(SimpleTestData.A1ServiceSeffId);
        expectedServiceIds.add(SimpleTestData.B1ServiceSeffId);

        assertEquals(expectedServiceIds, serviceIds);

        // Check loop ids
        Set<String> loopIds = reader.getLoops().getLoopIds();

        Set<String> expectedLoopIds = new HashSet<>();
        expectedLoopIds.add(SimpleTestData.LoopId);

        assertEquals(expectedLoopIds, loopIds);

        // Check branch ids
        Set<String> branchIds = reader.getBranches().getBranchIds();

        Set<String> expectedBranchIds = new HashSet<>();
        expectedBranchIds.add(SimpleTestData.FirstBranchId);

        assertEquals(expectedBranchIds, branchIds);

        // Check resource demand ids
        Set<String> resourceDemandIds = reader.getResponseTimes().getInternalActionIds();

        Set<String> expectedResourceDemandIds = new HashSet<>();
        expectedResourceDemandIds.add(SimpleTestData.FirstInternalActionId);
        expectedResourceDemandIds.add(SimpleTestData.SecondInternalActionId);

        assertEquals(expectedResourceDemandIds, resourceDemandIds);
    }
}
