package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringJoiner;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaServiceParametersModel;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Implements the resource demand parametric dependency estimation by using linear regression from the weka library.
 * This does not imply that only linear dependencies can be detected, because we present different pre-defined possible
 * dependency relations, such as a quadratic dependency, as input. The linear regression then finds the best candidates.
 * 
 * @author JP
 *
 */
public class WekaParametricDependencyEstimationStrategy implements ParametricDependencyEstimationStrategy {

    @Override
    public ResourceDemandModel estimateResourceDemandModel(final String internalActionId, final String resourceId,
            final Map<ServiceParameters, Double> resourceDemands) {
        try {
            return this.internEstimateResourceDemandModel(internalActionId, resourceId, resourceDemands);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ResourceDemandModel internEstimateResourceDemandModel(final String internalActionId,
            final String resourceId,
            final Map<ServiceParameters, Double> resourceDemands) throws Exception {

        // If no service parameters are monitored, we have a constant resource demand.
        if (resourceDemands.size() == 1) {
            double singleValue = resourceDemands.values().iterator().next();
            return new ConstantResourceDemandModel(singleValue);
        }

        ServiceParameters prototypeParameters = resourceDemands.keySet().iterator().next();

        Attribute classAttribute = new Attribute("resourceDemand");
        WekaServiceParametersModel parametersConversion = new WekaServiceParametersModel(prototypeParameters,
                classAttribute);
        Instances dataset = parametersConversion.buildDataSet();

        for (Entry<ServiceParameters, Double> rdEntry : resourceDemands.entrySet()) {
            Instance dataPoint = parametersConversion.buildInstance(rdEntry.getKey(), rdEntry.getValue());
            dataset.add(dataPoint);
        }

        System.out.println("Estimating resource demand for internal action id " + internalActionId + " and resource id "
                + resourceId + ".");

        LinearRegression linReg = new LinearRegression();

        Evaluation evaluation = new Evaluation(dataset);
        int folds = dataset.size() / 10;
        folds = Math.max(folds, 2);
        evaluation.crossValidateModel(linReg, dataset, folds, new Random(1));
        System.out.println(evaluation.toSummaryString());

        linReg.buildClassifier(dataset);
        System.out.println(linReg);

        return new WekaResourceDemandModel(linReg, parametersConversion);
    }

    private static class ConstantResourceDemandModel implements ResourceDemandModel {

        private final double resourceDemand;

        public ConstantResourceDemandModel(final double resourceDemand) {
            this.resourceDemand = resourceDemand;
        }

        @Override
        public double predictResourceDemand(final ServiceCall serviceCall) {
            return this.resourceDemand;
        }

        @Override
        public String getResourceDemandStochasticExpression() {
            return String.valueOf(this.resourceDemand);
        }
    }

    private static class WekaResourceDemandModel implements ResourceDemandModel {

        private final LinearRegression classifier;
        private final WekaServiceParametersModel parametersConversion;

        public WekaResourceDemandModel(final LinearRegression classifier,
                final WekaServiceParametersModel parametersConversion) {
            this.classifier = classifier;
            this.parametersConversion = parametersConversion;
        }

        @Override
        public double predictResourceDemand(final ServiceCall serviceCall) {
            Instance parametersInstance = this.parametersConversion.buildInstance(serviceCall.getParameters(), 0);
            try {
                return this.classifier.classifyInstance(parametersInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getResourceDemandStochasticExpression() {
            StringJoiner result = new StringJoiner(" + (");
            double[] coefficients = this.classifier.coefficients();
            int braces = 0;
            for (int i = 0; i < coefficients.length - 2; i++) {
                if (coefficients[i] == 0.0) {
                    continue;
                }
                StringBuilder coefficientPart = new StringBuilder();
                String paramStoEx = this.parametersConversion.getStochasticExpressionForIndex(i);
                coefficientPart.append(coefficients[i]).append(" * ").append(paramStoEx);
                result.add(coefficientPart.toString());
                braces++;
            }
            result.add(String.valueOf(coefficients[coefficients.length - 1]));
            StringBuilder strBuilder = new StringBuilder().append(result.toString());
            for (int i = 0; i < braces; i++) {
                strBuilder.append(")");
            }
            return strBuilder.toString();
        }
    }

}
