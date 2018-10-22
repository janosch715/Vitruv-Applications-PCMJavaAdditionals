package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Helper class for weka model estimations.
 * 
 * @author JP
 *
 */
public class WekaDataSet {

    private final ServiceCallDataSet serviceCalls;

    private final WekaServiceParametersModel parametersConversion;

    private final Instances dataset;

    /**
     * Initializes a new instance of {@link WekaDataSet}.
     * 
     * @param serviceCalls
     *            The service call data set.
     * @param initialServiceExecutionId
     *            A service execution id for getting service parameters. These parameters are used to build the weka
     *            attribute.
     * @param classAttribute
     *            The class attribute for the weka data set.
     */
    public WekaDataSet(
            final ServiceCallDataSet serviceCalls,
            final String initialServiceExecutionId,
            final Attribute classAttribute) {
        this.serviceCalls = serviceCalls;

        ServiceParameters firstRecordParameters = this.serviceCalls
                .getParametersOfServiceCall(initialServiceExecutionId);

        this.parametersConversion = new WekaServiceParametersModel(firstRecordParameters, classAttribute);
        this.dataset = this.parametersConversion.buildDataSet();
    }

    /**
     * Adds a data instance, consisting of the service parameters and the class value.
     * 
     * @param serviceExecutionId
     *            The service execution id is used to get the service parameters.
     * @param classValue
     *            The class value, like resource demand or loop iteration.
     */
    public void addInstance(final String serviceExecutionId, final double classValue) {
        ServiceParameters recordParameters = this.serviceCalls.getParametersOfServiceCall(serviceExecutionId);
        Instance dataPoint = this.parametersConversion.buildInstance(recordParameters, classValue);
        this.dataset.add(dataPoint);
    }

    /**
     * Gets the weka data set.
     * 
     * @return Weka data set.
     */
    public Instances getDataSet() {
        return this.dataset;
    }

    /**
     * Gets the service parameter model.
     * 
     * @return Service parameter model.
     */
    public WekaServiceParametersModel getServiceParametersModel() {
        return this.parametersConversion;
    }
}
