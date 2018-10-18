package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import java.util.List;
import java.util.Set;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.LoopRecord;

public interface LoopDataSet {

    Set<String> getLoopIds();

    List<LoopRecord> getLoopRecords(String loopId);

}
