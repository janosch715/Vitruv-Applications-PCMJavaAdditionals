package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.BranchDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.BranchRecord;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaServiceParametersModel;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.j48.C45Split;
import weka.classifiers.trees.j48.ClassifierSplitModel;
import weka.classifiers.trees.j48.ClassifierTree;
import weka.classifiers.trees.j48.NoSplit;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class TreeWekaBranchModelEstimation {

    private final ServiceCallDataSet serviceCalls;

    private final BranchDataSet branchExecutions;

    private final Random random;

    public TreeWekaBranchModelEstimation(final ServiceCallDataSet serviceCalls, final BranchDataSet branchExecutions) {
        this(serviceCalls, branchExecutions, ThreadLocalRandom.current());
    }

    public TreeWekaBranchModelEstimation(final ServiceCallDataSet serviceCalls, final BranchDataSet branchExecutions,
            final Random random) {
        this.serviceCalls = serviceCalls;
        this.branchExecutions = branchExecutions;
        this.random = random;
    }

    public BranchModel estimate(final String branchId) {
        try {
            return this.internEstimate(branchId);
        } catch (Exception e) {
            throw new RuntimeException("Error estimating branch with id " + branchId, e);
        }
    }

    public Map<String, BranchModel> estimateAll() {
        Map<String, BranchModel> returnValue = new HashMap<>();
        for (String branchId : this.branchExecutions.getBranchIds()) {
            returnValue.put(branchId, this.estimate(branchId));
        }
        return returnValue;
    }

    private BranchModel estimate(final String branchId, final Set<String> branchExecutionIds) throws Exception {
        List<BranchRecord> records = this.branchExecutions.getBranchRecords(branchId);

        if (records.size() == 0) {
            throw new IllegalStateException("No records for branch id " + branchId + " found.");
        }

        BranchRecord firstRecord = records.get(0);

        List<String> branchExecutedLabels = branchExecutionIds.stream().collect(Collectors.toList());

        // Check if every time the same branch is executed. Weka cannot handle unary
        // class attributes.
        if (branchExecutedLabels.size() == 0) {
            return new ConstantBranchModel(Optional.empty());
        } else if (branchExecutedLabels.size() == 1) {
            return new ConstantBranchModel(Optional.of(branchExecutedLabels.get(0)));
        }

        Attribute branchExecutedAttribute = new Attribute("branchExecuted", branchExecutedLabels);

        WekaDataSet dataSetBuilder = new WekaDataSet(this.serviceCalls, firstRecord.getServiceExecutionId(),
                branchExecutedAttribute);

        for (BranchRecord record : records) {
            double classValue = branchExecutedLabels.indexOf(record.getExecutedBranchId());
            dataSetBuilder.addInstance(record.getServiceExecutionId(), classValue);
        }

        Instances dataset = dataSetBuilder.getDataSet();

        System.out.println("Estimating branch execution for branch " + branchId);

        StochasticExpressionJ48 tree = new StochasticExpressionJ48();

        Evaluation evaluation = new Evaluation(dataset);
        evaluation.crossValidateModel(tree, dataset, 10, new Random(1));
        System.out.println(evaluation.toSummaryString());

        tree.buildClassifier(dataset);

        return new WekaBranchModel(tree, dataSetBuilder.getParametersConversion(), this.random,
                this.branchExecutions.getBranchNotExecutedId());
    }

    private BranchModel internEstimate(final String branchId) throws Exception {
        List<BranchRecord> records = this.branchExecutions.getBranchRecords(branchId);

        if (records.size() == 0) {
            throw new IllegalStateException("No records for branch id " + branchId + " found.");
        }

        Set<String> executedBranchIds = new HashSet<>();

        for (BranchRecord record : records) {
            executedBranchIds.add(record.getExecutedBranchId());
        }

        return this.estimate(branchId, executedBranchIds);
    }

    private static class ConstantBranchModel implements BranchModel {

        private final Optional<String> transitionId;

        public ConstantBranchModel(final Optional<String> transitionId) {
            this.transitionId = transitionId;
        }

        @Override
        public Optional<String> estimateBranchId(final ServiceCall serviceCall) {
            return this.transitionId;
        }

        @Override
        public String getBranchStochasticExpression(final String transitionId) {
            if (this.transitionId.isPresent() && this.transitionId.get().equals(transitionId)) {
                return "true";
            } else {
                return "false";
            }
        }

    }

    private static class StochasticExpressionJ48 extends J48 {

        /** for serialization */
        private static final long serialVersionUID = -8479361310735737366L;

        public String getBranchStochasticExpression(final int classId, final String[] attributeExpression) {
            StringBuilder result = new StringBuilder();
            this.buildStochasticExpression(this.m_root, classId, attributeExpression, result);
            return result.toString();
        }

        private void buildStochasticExpression(final ClassifierTree tree, final int classId,
                final String[] attributeExpression,
                final StringBuilder result2) {

            // "BoolPMF[(true;p)(false;q)]"

            if (tree.isLeaf()) {
                int maxClass = tree.getLocalModel().distribution().maxClass(0);
                if (maxClass == classId) {
                    result2.append("true");
                } else {
                    result2.append("false");
                }
            } else {
                String opposite = null;
                for (int i = 0; i < tree.getSons().length; i++) {
                    result2.append("(");
                    result2.append(
                            this.toSourceExpression(tree.getLocalModel(), i, attributeExpression,
                                    tree.getTrainingData()))
                            .append(" ? ");

                    if (tree.getSons()[i].isLeaf()) {
                        int maxClass = tree.getLocalModel().distribution().maxClass(i);
                        if (maxClass == classId) {
                            result2.append("true");
                            opposite = "false";
                        } else {
                            result2.append("false");
                            opposite = "true";
                        }
                    } else {
                        result2.append("( ");
                        this.buildStochasticExpression(tree.getSons()[i], classId, attributeExpression, result2);
                        result2.append(" )");
                    }
                    result2.append(" : ");
                }
                result2.append(opposite).append(" ");
                for (int i = 0; i < tree.getSons().length; i++) {
                    result2.append(")");
                }
            }
        }

        private String toSourceExpression(final C45Split splitModel, final int index,
                final String[] attributeExpression,
                final Instances data) {
            StringBuffer expr = new StringBuffer();
            expr.append(attributeExpression[splitModel.attIndex()]);

            if (data.attribute(splitModel.attIndex()).isNominal()) {
                expr.append(" == ").append(data.attribute(splitModel.attIndex()).value(index)).append("\")");
            } else {
                if (index == 0) {
                    expr.append(" <= ").append(splitModel.splitPoint());
                } else {
                    expr.append(" > ").append(splitModel.splitPoint());
                }
            }
            return expr.toString();
        }

        private String toSourceExpression(final ClassifierSplitModel splitModel, final int index,
                final String[] attributeExpression,
                final Instances data) {
            if (splitModel instanceof NoSplit) {
                return "true";
            } else if (splitModel instanceof C45Split) {
                return this.toSourceExpression((C45Split) splitModel, index, attributeExpression, data);
            }
            throw new UnsupportedOperationException();
        }
    }

    private static class WekaBranchModel implements BranchModel {
        private final StochasticExpressionJ48 classifier;
        private final WekaServiceParametersModel parametersModel;
        private final Random random;
        private final String branchNotExecutedId;
        private final String[] attributeExpressions;

        public WekaBranchModel(final StochasticExpressionJ48 classifier,
                final WekaServiceParametersModel parametersConversion,
                final Random random, final String branchNotExecutedId) {
            this.classifier = classifier;
            this.parametersModel = parametersConversion;
            this.random = random;
            this.branchNotExecutedId = branchNotExecutedId;
            this.attributeExpressions = new String[parametersConversion.getInputAttributesCount()];
            for (int i = 0; i < this.attributeExpressions.length; i++) {
                this.attributeExpressions[i] = parametersConversion.getStochasticExpressionForIndex(i);
            }
        }

        @Override
        public Optional<String> estimateBranchId(final ServiceCall serviceCall) {
            Instance parametersInstance = this.parametersModel.buildInstance(serviceCall.getParameters(), 0);
            Instances dataset = this.parametersModel.buildDataSet();
            dataset.add(parametersInstance);
            double[] branchDistribution;
            try {
                branchDistribution = this.classifier.distributionForInstance(dataset.firstInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            double selectedBranchPropability = this.random.nextDouble();
            int selectedBranchIndex = 0;
            double branchPropabilitySum = 0.0;
            while (true) {
                if (selectedBranchIndex >= branchDistribution.length) {
                    throw new IllegalArgumentException("The branch has propability distribution.");
                }
                branchPropabilitySum += branchDistribution[selectedBranchIndex];
                if (selectedBranchPropability < branchPropabilitySum) {
                    break;
                }
                selectedBranchIndex++;
            }

            String result = this.parametersModel.getClassAttribute().value(selectedBranchIndex);

            if (result.equals(this.branchNotExecutedId)) {
                return Optional.empty();
            } else {
                return Optional.of(result);
            }
        }

        @Override
        public String getBranchStochasticExpression(final String transitionId) {
            String expr = this.classifier.getBranchStochasticExpression(0, this.attributeExpressions);
            System.out.println(this.classifier);
            System.out.println(this.classifier.toSummaryString());
            System.out.println(expr);
            return expr;
        }
    }
}
