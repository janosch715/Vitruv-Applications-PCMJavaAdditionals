package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class WekaDataSet {

    private final ServiceCallDataSet serviceCallRepository;

    private final WekaServiceParametersModel parametersConversion;

    private final Instances dataset;

    public WekaDataSet(
            final ServiceCallDataSet serviceCallRepository,
            final String initialServiceExecutionId,
            final Attribute classAttribute) {
        this.serviceCallRepository = serviceCallRepository;

        ServiceParameters firstRecordParameters = this.serviceCallRepository
                .getParametersOfServiceCall(initialServiceExecutionId);

        this.parametersConversion = new WekaServiceParametersModel(firstRecordParameters, classAttribute);
        this.dataset = this.parametersConversion.buildDataSet();
    }

    public void addInstance(final String serviceExecutionId, final double classValue) {
        ServiceParameters recordParameters = this.serviceCallRepository.getParametersOfServiceCall(serviceExecutionId);
        Instance dataPoint = this.parametersConversion.buildInstance(recordParameters, classValue);
        this.dataset.add(dataPoint);
    }

    public Instances getDataSet() {
        return this.dataset;
    }

    public WekaServiceParametersModel getParametersConversion() {
        return this.parametersConversion;
    }
}
