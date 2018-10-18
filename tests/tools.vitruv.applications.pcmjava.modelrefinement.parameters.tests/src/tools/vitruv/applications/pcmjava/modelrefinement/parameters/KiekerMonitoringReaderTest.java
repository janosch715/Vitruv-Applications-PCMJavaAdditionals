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

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;

public class KiekerMonitoringReaderTest {
	
	@BeforeClass
	public static void setUp() {
		// create appender
		ConsoleAppender console = new ConsoleAppender(new PatternLayout("%d [%p|%c|%C{1}] %m%n")); 
		console.setThreshold(Level.ALL);
		console.activateOptions();
		Logger.getRootLogger().addAppender(console);
	}
	
	@Test
	public void kiekerReadTest() {
		MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);
		
		// Check service ids
		Set<String> serviceIds = reader.getServiceCalls().getServiceIds();
		
		Set<String> expectedServiceIds = new HashSet<String>();
		expectedServiceIds.add(SimpleTestData.A1ServiceSeffId);
		expectedServiceIds.add(SimpleTestData.B1ServiceSeffId);
		
		assertEquals(expectedServiceIds, serviceIds);
		
		// Check loop ids
		Set<String> loopIds = reader.getLoops().getLoopIds();
		
		Set<String> expectedLoopIds = new HashSet<String>();
		expectedLoopIds.add(SimpleTestData.LoopId);
		
		assertEquals(expectedLoopIds, loopIds);
		
		// Check branch ids
		Set<String> branchIds = reader.getBranches().getBranchIds();
		
		Set<String> expectedBranchIds = new HashSet<String>();
		expectedBranchIds.add(SimpleTestData.FirstBranchId);
		
		assertEquals(expectedBranchIds, branchIds);
		
		// Check resource demand ids
		Set<String> resourceDemandIds = reader.getResponseTimes().getInternalActionIds();
		
		Set<String> expectedResourceDemandIds = new HashSet<String>();
		expectedResourceDemandIds.add(SimpleTestData.FirstInternalActionId);
		expectedResourceDemandIds.add(SimpleTestData.SecondInternalActionId);
		
		assertEquals(expectedResourceDemandIds, resourceDemandIds);
	}
}
