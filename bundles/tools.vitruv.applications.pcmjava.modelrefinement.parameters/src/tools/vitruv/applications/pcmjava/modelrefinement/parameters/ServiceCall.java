package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

public interface ServiceCall {

    String getCallerId();

    String getCallerServiceExecutionId();

    long getEntryTime();

    long getExitTime();

    ServiceParameters getParameters();

    long getResponseTime();

    double getResponseTimeSeconds();

    String getServiceExecutionId();

    String getServiceId();
}
