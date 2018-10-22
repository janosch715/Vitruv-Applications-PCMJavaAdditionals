package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParametersUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl.LibredeResourceDemandEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl.ResourceDemandModel;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl.WekaParametricDependencyEstimationStrategy;

public class LibredeResourceDemandEstimationTest {

    private WekaParametricDependencyEstimationStrategy modelEstimationStartegy;

    @Before
    public void before() {
        this.modelEstimationStartegy = new WekaParametricDependencyEstimationStrategy();
    }

    @Test
    public void buildConfigTest() {
        MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);

        LibredeResourceDemandEstimation estimation = new LibredeResourceDemandEstimation(this.modelEstimationStartegy,
                reader.getResourceUtilizations(), reader.getResponseTimes(), reader.getServiceCalls());

        estimation.saveConfig(SimpleTestData.TempDirectoryPath + "temp.librede");
    }

    @Test
    public void estimateResourceDemandModelsTest() {
        MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);

        LibredeResourceDemandEstimation estimation = new LibredeResourceDemandEstimation(this.modelEstimationStartegy,
                reader.getResourceUtilizations(), reader.getResponseTimes(), reader.getServiceCalls());

        Map<String, Map<String, ResourceDemandModel>> results = estimation.estimateAll();

        assertEquals(2, results.size());
        assertEquals(1, results.get(SimpleTestData.FirstInternalActionId).size());
        assertEquals(1, results.get(SimpleTestData.SecondInternalActionId).size());

        ResourceDemandModel rds1 = results.get(SimpleTestData.FirstInternalActionId).get(SimpleTestData.ResourceId);
        assertEquals(0.0, rds1.predictResourceDemand(ServiceParametersUtil.buildServiceCall("a", 0)), 0.01);
        assertEquals(0.087, rds1.predictResourceDemand(ServiceParametersUtil.buildServiceCall("a", 9)), 0.01);

        ResourceDemandModel rds2 = results.get(SimpleTestData.SecondInternalActionId).get(SimpleTestData.ResourceId);
        assertEquals(0.03, rds2.predictResourceDemand(ServiceParametersUtil.buildServiceCall("a", 1)), 0.01);
        assertEquals(0.03, rds2.predictResourceDemand(ServiceParametersUtil.buildServiceCall("a", 9)), 0.01);
    }

    @Test
    public void estimateResourceDemandsTest() {
        MonitoringDataSet reader = SimpleTestData.getReader(SimpleTestData.FirstSessionId);

        LibredeResourceDemandEstimation estimation = new LibredeResourceDemandEstimation(this.modelEstimationStartegy,
                reader.getResourceUtilizations(), reader.getResponseTimes(), reader.getServiceCalls());

        Map<String, Map<String, Map<ServiceParameters, Double>>> results = estimation.estimateResourceDemands();

        assertEquals(2, results.size());
        assertEquals(1, results.get(SimpleTestData.FirstInternalActionId).size());
        assertEquals(1, results.get(SimpleTestData.SecondInternalActionId).size());

        Map<ServiceParameters, Double> rds1 = results.get(SimpleTestData.FirstInternalActionId)
                .get(SimpleTestData.ResourceId);
        assertEquals(10, rds1.size());
        assertEquals(0.0, rds1.get(ServiceParametersUtil.buildParameters("a", 0)), 0.001);
        assertEquals(0.089, rds1.get(ServiceParametersUtil.buildParameters("a", 9)), 0.001);

        Map<ServiceParameters, Double> rds2 = results.get(SimpleTestData.SecondInternalActionId)
                .get(SimpleTestData.ResourceId);
        assertEquals(9, rds2.size());
        assertEquals(0.03, rds2.get(ServiceParametersUtil.buildParameters("a", 1)), 0.01);
        assertEquals(0.03, rds2.get(ServiceParametersUtil.buildParameters("a", 9)), 0.01);
    }

    @BeforeClass
    public static void setUp() {
        LoggingUtil.InitConsoleLogger();
    }
}
