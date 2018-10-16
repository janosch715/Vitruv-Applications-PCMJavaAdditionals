package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;

public class ExportUtils {
	public static void exportResponseTimeCsv(ServiceCallDataSet dataSet, String serviceId, String filePath) throws FileNotFoundException {
		List<ServiceCall> serviceCalls = dataSet.getServiceCalls(serviceId);
		PrintWriter pw = new PrintWriter(new File(filePath));
		pw.write("time, response time \n");
		for (ServiceCall serviceCall : serviceCalls) {
			StringBuilder sb = new StringBuilder();
	        sb.append(serviceCall.getEntryTime());
	        sb.append(',');
	        sb.append(serviceCall.getResponseTimeSeconds());
	        sb.append('\n');
	        pw.write(sb.toString());
		}
        pw.close();
	}
}
