package tools.vitruv.applications.pcmjava.seffstatements.parameters;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.seffstatements.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.seffstatements.parameters.ServiceParametersUtil;
import tools.vitruv.applications.pcmjava.seffstatements.parameters.impl.KiekerMonitoringReader;
import tools.vitruv.applications.pcmjava.seffstatements.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.seffstatements.parameters.rd.impl.ResourceDemandEstimationImpl;
import tools.vitruv.applications.pcmjava.seffstatements.parameters.util.ExportUtils;
import tools.vitruv.applications.pcmjava.seffstatements.parameters.util.PcmUtils;

public class SeffParameterEstimationTest {

	@BeforeClass
	public static void setUp() {
		LoggingUtil.InitConsoleLogger();
	}

	@Test
	public void simpleEvaluationTest() throws Exception {
		MonitoringDataSet reader = new KiekerMonitoringReader("./test-data/simple", "session-1");
		Repository pcmModel = PcmUtils.loadModel("./test-data/simple/default.repository");

		MonitoringDataSet reader2 = new KiekerMonitoringReader("./test-data/simple", "session-2");
		Repository pcmModel2 = PcmUtils.loadModel("./test-data/simple-iteration/default2.repository");

		SeffParameterEstimation estimation = new SeffParameterEstimation();
		estimation.updateModels(pcmModel, reader);
		estimation.updateModels(pcmModel2, reader2);
		
		estimation.applyEstimations(pcmModel2);
		PcmUtils.saveModel("./test-data/simple-iteration/temp.repository", pcmModel2);
		
		ExportUtils.exportResponseTimeCsv(reader2.getServiceCalls(), "_XYJcUMjPEeiWRYm1yDC5rQ", "./test-data/simple-iteration/temp-service-a-response-times.csv");
		// Without iteration
		/*
		MonitoringDataSet reader3 = new KiekerMonitoringReader("./test-data/simple-complete");
		Repository pcmModel3 = PcmUtils.loadModel("./test-data/simple-iteration/default2.repository");
		SeffParameterEstimation estimation3 = new SeffParameterEstimation();
		estimation3.updateModels(pcmModel3, reader3);
		
		estimation3.applyEstimations(pcmModel3);
		PcmUtils.saveModel("./test-data/simple-complete/temp.repository", pcmModel3);
		*/
	}
	
	@Test
	public void simple2EvaluationTest() throws Exception {
		MonitoringDataSet reader = new KiekerMonitoringReader("./test-data/simple2", "session-1");
		Repository pcmModel = PcmUtils.loadModel("./test-data/simple2/default.repository");

		SeffParameterEstimation estimation = new SeffParameterEstimation();
		estimation.updateModels(pcmModel, reader);
		
		estimation.applyEstimations(pcmModel);
		PcmUtils.saveModel("./test-data/simple2/temp.repository", pcmModel);
		
		ExportUtils.exportResponseTimeCsv(reader.getServiceCalls(), "_SVoyANChEeiG9v0ZHxeEbQ", "./test-data/simple2/temp-service-c-response-times.csv");
	}
}
