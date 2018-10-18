package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class WekaServiceParametersModel {

    private static final Logger LOGGER = Logger.getLogger(WekaServiceParametersModel.class);

    private final Attribute classAttribute;

    private final ArrayList<Attribute> attributes;

    private final List<WekaServiceParameter> parameters;

    private final Map<String, List<WekaServiceParameter>> parametersToAttributes;

    public WekaServiceParametersModel(final ServiceParameters basedOnParameters, final Attribute classAttribute) {
        this.attributes = new ArrayList<>();
        this.parameters = new ArrayList<>();
        this.parametersToAttributes = new HashMap<>();

        for (Entry<String, Object> parameter : basedOnParameters.getParameters().entrySet()) {
            this.addParameter(parameter.getKey(), parameter.getValue());
        }

        this.attributes.add(classAttribute);
        this.classAttribute = classAttribute;
    }

    public Instances buildDataSet() {
        Instances instances = new Instances("dataset", this.attributes, 0);
        instances.setClass(this.classAttribute);
        return instances;
    }

    public Instance buildInstance(final ServiceParameters serviceParameters, final double classValue) {
        double[] values = new double[this.parameters.size() + 1];

        for (Entry<String, Object> parameter : serviceParameters.getParameters().entrySet()) {
            List<WekaServiceParameter> wekaParameters = this.parametersToAttributes.get(parameter.getKey());
            for (WekaServiceParameter wekaServiceParameter : wekaParameters) {
                wekaServiceParameter.setValue(parameter.getValue(), values);
            }
        }

        values[this.parameters.size()] = classValue;
        return new DenseInstance(1.0, values);
    }

    public ArrayList<Attribute> getAttributes() {
        return this.attributes;
    }

    public Attribute getClassAttribute() {
        return this.classAttribute;
    }

    public int getInputAttributesCount() {
        return this.attributes.size() - 1;
    }

    public String getStochasticExpressionForIndex(final int idx) {
        return this.parameters.get(idx).getStochasticExpression();
    }

    private void addNumericParameter(final String name) {
        List<WekaServiceParameter> newParameters = new ArrayList<>();
        int index = this.parameters.size();
        NumericWekaServiceParameter numeric = new NumericWekaServiceParameter(name, index);
        newParameters.add(numeric);
        this.parameters.add(numeric);
        this.attributes.add(numeric.getWekaAttribute());
        index++;
        QuadraticNumericWekaServiceParameter numeric2 = new QuadraticNumericWekaServiceParameter(name, index);
        newParameters.add(numeric2);
        this.parameters.add(numeric2);
        this.attributes.add(numeric2.getWekaAttribute());

        this.parametersToAttributes.put(name, newParameters);
    }

    private void addParameter(final String name, final Object value) {
        if (value instanceof Integer || value instanceof Double) {
            this.addNumericParameter(name);
        } else {
            LOGGER.warn("Handling parameter of type " + value.getClass().getSimpleName() + " is not implemented.");
        }
    }

    private static class NumericWekaServiceParameter extends WekaServiceParameter {

        public NumericWekaServiceParameter(final String parameterName, final int index) {
            super(parameterName, index, new Attribute(parameterName));
        }

        @Override
        public String getStochasticExpression() {
            return this.getParameterName() + ".VALUE";
        }

        @Override
        public void setValue(final Object value, final double[] result) {
            double castedValue = 0.0;
            if (value instanceof Integer) {
                castedValue = (Integer) value;
            } else if (value instanceof Double) {
                castedValue = (Double) value;
            }

            result[this.getIndex()] = castedValue;
        }

    }

    private static class QuadraticNumericWekaServiceParameter extends WekaServiceParameter {

        public QuadraticNumericWekaServiceParameter(final String parameterName, final int index) {
            super(parameterName, index, new Attribute(parameterName + "²"));
        }

        @Override
        public String getStochasticExpression() {
            return "(" + this.getParameterName() + ".VALUE ^ 2)";
        }

        @Override
        public void setValue(final Object value, final double[] result) {
            double castedValue = 0.0;
            if (value instanceof Integer) {
                castedValue = (Integer) value;
            } else if (value instanceof Double) {
                castedValue = (Double) value;
            }

            result[this.getIndex()] = Math.pow(castedValue, 2.0);
        }

    }

    private static abstract class WekaServiceParameter {
        private final int index;
        private final String parameterName;
        private final Attribute wekaAttribute;

        public WekaServiceParameter(final String parameterName, final int index, final Attribute wekaAttribute) {
            this.index = index;
            this.parameterName = parameterName;
            this.wekaAttribute = wekaAttribute;
        }

        public int getIndex() {
            return this.index;
        }

        public String getParameterName() {
            return this.parameterName;
        }

        public abstract String getStochasticExpression();

        public Attribute getWekaAttribute() {
            return this.wekaAttribute;
        }

        public abstract void setValue(Object value, double[] result);
    }
}
