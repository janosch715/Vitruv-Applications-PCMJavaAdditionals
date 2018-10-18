package tools.vitruv.applications.pcmjava.modelrefinement.parameters.rd;

import java.util.List;
import java.util.Set;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.ResponseTimeRecord;

public interface ResponseTimeDataSet {

    Long getEarliestEntry();

    Set<String> getInternalActionIds();

    Long getLatestEntry();

    Set<String> getResourceIds(String internalActionId);

    List<ResponseTimeRecord> getResponseTimes(String internalActionId, String resourceId);

    double timeToSeconds(long time);

}