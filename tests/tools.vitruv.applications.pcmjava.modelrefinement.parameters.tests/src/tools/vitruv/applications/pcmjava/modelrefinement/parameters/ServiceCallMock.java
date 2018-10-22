package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import java.util.Optional;

public class ServiceCallMock implements ServiceCall {

    private ServiceParameters parameters = ServiceParameters.EMPTY;
    private String serviceExecutionId = "not set";
    private String serviceId = "not set";
    private String callerServiceExecutionId = "not set";
    private String callerId = "not set";
    private long entryTime = 0;
    private long exitTime = 0;

    public ServiceCallMock() {
    }

    public ServiceCallMock(final ServiceParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public Optional<String> getCallerId() {
        return Optional.of(this.callerId);
    }

    @Override
    public String getCallerServiceExecutionId() {
        return this.callerServiceExecutionId;
    }

    @Override
    public long getEntryTime() {
        return this.entryTime;
    }

    @Override
    public long getExitTime() {
        return this.exitTime;
    }

    @Override
    public ServiceParameters getParameters() {
        return this.parameters;
    }

    @Override
    public long getResponseTime() {
        return 0;
    }

    @Override
    public double getResponseTimeSeconds() {
        return 0;
    }

    @Override
    public String getServiceExecutionId() {
        return this.serviceExecutionId;
    }

    @Override
    public String getServiceId() {
        return this.serviceId;
    }

    public void setCallerId(final String callerId) {
        this.callerId = callerId;
    }

    public void setCallerServiceExecutionId(final String callerServiceExecutionId) {
        this.callerServiceExecutionId = callerServiceExecutionId;
    }

    public void setEntryTime(final long entryTime) {
        this.entryTime = entryTime;
    }

    public void setExitTime(final long exitTime) {
        this.exitTime = exitTime;
    }

    public void setParameters(final ServiceParameters parameters) {
        this.parameters = parameters;
    }

    public void setServiceExecutionId(final String serviceExecutionId) {
        this.serviceExecutionId = serviceExecutionId;
    }

    public void setServiceId(final String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public double timeToSeconds(long time) {
        return 0;
    }

}
