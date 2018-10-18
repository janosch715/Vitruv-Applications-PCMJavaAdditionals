package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd.utilization;

import java.util.Set;
import java.util.SortedMap;

public interface ResourceUtilizationDataSet {
    Set<String> getResourceIds();

    SortedMap<Long, Double> getUtilization(String resourceId);

    double timeToSeconds(long time);
}
