package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization;

import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.ResourceDemandPrediction;

public class ResourceDemandPredictionMock implements ResourceDemandPrediction {
    @Override
    public double predictResourceDemand(final ParametricResourceDemand rd, final ServiceCall serviceCall) {
        return 1.0;
    }
}