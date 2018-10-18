package tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.impl;

import java.util.Optional;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;

public interface BranchModel {

    Optional<String> estimateBranchId(ServiceCall serviceCall);

    String getBranchStochasticExpression(String transitionId);

}