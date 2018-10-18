package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl.KiekerMonitoringReader;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.ExportUtils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class SeffParameterEstimationTest {

    @Test
    public void simple2EvaluationTest() throws Exception {
        MonitoringDataSet reader = new KiekerMonitoringReader("./test-data/simple2", "session-1");
        Repository pcmModel = PcmUtils.loadModel("./test-data/simple2/default.repository");

        SeffParameterEstimation estimation = new SeffParameterEstimation();
        estimation.update(pcmModel, reader);

        PcmUtils.saveModel("./test-data/simple2/temp.repository", pcmModel);

        ExportUtils.exportResponseTimeCsv(reader.getServiceCalls(), "_SVoyANChEeiG9v0ZHxeEbQ",
                "./test-data/simple2/temp-service-c-response-times.csv");
    }

    @Test
    public void simpleEvaluationTest() throws Exception {
        MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);
        Repository pcmModel = SimpleTestData.loadPcmModel();

        MonitoringDataSet reader2 = SimpleTestData.getReader(SimpleTestData.SecondSessionId);
        Repository pcmModel2 = SimpleTestData.loadIterationPcmModel();

        SeffParameterEstimation estimation = new SeffParameterEstimation();
        estimation.update(pcmModel, reader);
        estimation.update(pcmModel2, reader2);

        PcmUtils.saveModel(SimpleTestData.TempDirectoryPath + "temp.repository", pcmModel2);

        ExportUtils.exportResponseTimeCsv(reader2.getServiceCalls(), SimpleTestData.A1ServiceSeffId,
                SimpleTestData.TempDirectoryPath + "temp-service-a-response-times.csv");

        // Without iteration

        MonitoringDataSet reader3 = SimpleTestData.getReader(SimpleTestData.ThirdSessionId);
        Repository pcmModel3 = SimpleTestData.loadIterationPcmModel();
        SeffParameterEstimation estimation3 = new SeffParameterEstimation();
        estimation3.update(pcmModel3, reader3);

        PcmUtils.saveModel(SimpleTestData.TempDirectoryPath + "temp2.repository", pcmModel3);
    }

    @BeforeClass
    public static void setUp() {
        LoggingUtil.InitConsoleLogger();
    }
}
