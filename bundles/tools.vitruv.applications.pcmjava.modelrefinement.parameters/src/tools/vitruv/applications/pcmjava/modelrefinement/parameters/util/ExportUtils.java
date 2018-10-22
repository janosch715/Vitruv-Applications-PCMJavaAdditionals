package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;

/**
 * Utilities for exporting data in order to do further analysis.
 * 
 * @author JP
 *
 */
public class ExportUtils {

    /**
     * Exports the response times of a specific service id. The first column <i>time</i> contains the time when the
     * service was called. The second column contains the response time in seconds.
     * 
     * @param dataSet
     *            The service call data set including the response times of the service calls.
     * @param serviceId
     *            The id of the service of which the response times are exported.
     * @param filePath
     *            The path of the exported csv file.
     * @throws FileNotFoundException
     *             If the given file object does not denote an existing, writable regular file and a new regular file of
     *             that name cannot becreated, or if some other error occurs while opening or creating the file
     */
    public static void exportResponseTimeCsv(final ServiceCallDataSet dataSet, final String serviceId,
            final String filePath) throws FileNotFoundException {
        List<ServiceCall> serviceCalls = dataSet.getServiceCalls(serviceId);
        PrintWriter pw = new PrintWriter(new File(filePath));
        pw.write("time,response time\n");
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
