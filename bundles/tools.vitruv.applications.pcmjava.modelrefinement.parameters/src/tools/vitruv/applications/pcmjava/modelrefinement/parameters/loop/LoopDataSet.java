package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop;

import java.util.List;
import java.util.Set;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.LoopRecord;

public interface LoopDataSet {

	List<LoopRecord> getLoopRecords(String loopId);

	Set<String> getLoopIds();

}
