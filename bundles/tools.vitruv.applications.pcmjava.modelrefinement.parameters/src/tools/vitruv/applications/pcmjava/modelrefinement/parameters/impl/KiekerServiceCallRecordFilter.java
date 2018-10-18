package tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.ServiceCallRecord;

@Plugin(description = "A filter for service call records.")
public final class KiekerServiceCallRecordFilter extends AbstractFilterPlugin implements ServiceCallDataSet {

    private static double TIME_TO_SECONDS = 1.0e-9;

    /**
     * The name of the input port for incoming events.
     */
    public static final String INPUT_PORT_NAME_EVENTS = "inputEvent";

    private final Map<String, ServiceCall> serviceExecutionIdToCall;
    private final Map<String, ArrayList<ServiceCall>> serviceIdToCall;
    private final Map<String, Map<String, ArrayList<ServiceCall>>> callerIdToServiceIdToCall;
    private final List<ServiceCall> allServiceCalls;

    public KiekerServiceCallRecordFilter(final Configuration configuration, final IProjectContext projectContext) {
        super(configuration, projectContext);
        this.serviceExecutionIdToCall = new HashMap<>();
        this.serviceIdToCall = new HashMap<>();
        this.callerIdToServiceIdToCall = new HashMap<>();
        this.allServiceCalls = new ArrayList<>();
    }

    @Override
    public Configuration getCurrentConfiguration() {
        return new Configuration();
    }

    @Override
    public ServiceParameters getParametersOfServiceCall(final String serviceExecutionId) {
        ServiceCall executionItem = this.serviceExecutionIdToCall.get(serviceExecutionId);
        if (executionItem == null) {
            throw new IllegalArgumentException(
                    String.format("The service call with id %s does not exist.", serviceExecutionId));
        }
        return executionItem.getParameters();
    }

    @Override
    public List<ServiceCall> getServiceCalls() {
        return this.allServiceCalls;
    }

    @Override
    public List<ServiceCall> getServiceCalls(final String serviceId) {
        return this.serviceIdToCall.get(serviceId);
    }

    @Override
    public Set<String> getServiceIds() {
        return this.serviceIdToCall.keySet();
    }

    @Override
    public Set<String> getServiceIdsForExternalCallId(final String externalCallId) {
        Map<String, ArrayList<ServiceCall>> serviceIdToCalls = this.callerIdToServiceIdToCall.get(externalCallId);
        if (serviceIdToCalls == null) {
            return Collections.emptySet();
        }
        return serviceIdToCalls.keySet();
    }

    @InputPort(name = INPUT_PORT_NAME_EVENTS, description = "Input for service call records.", eventTypes = {
            ServiceCallRecord.class })
    public final void inputEvent(final ServiceCallRecord record) {
        ServiceCall item = new KiekerServiceCall(record);

        this.allServiceCalls.add(item);

        this.serviceExecutionIdToCall.put(record.getServiceExecutionId(), item);

        ArrayList<ServiceCall> serviceIdToCallList = this.serviceIdToCall.get(record.getServiceId());
        if (serviceIdToCallList == null) {
            serviceIdToCallList = new ArrayList<>();
            this.serviceIdToCall.put(record.getServiceId(), serviceIdToCallList);
        }
        serviceIdToCallList.add(item);

        Map<String, ArrayList<ServiceCall>> callerIdToServiceId = this.callerIdToServiceIdToCall
                .get(record.getCallerId());
        if (callerIdToServiceId == null) {
            callerIdToServiceId = new HashMap<>();
            this.callerIdToServiceIdToCall.put(record.getCallerId(), callerIdToServiceId);
        }

        ArrayList<ServiceCall> callerIdCalls = callerIdToServiceId.get(record.getServiceId());
        if (callerIdCalls == null) {
            callerIdCalls = new ArrayList<>();
            callerIdToServiceId.put(record.getServiceId(), callerIdCalls);
        }
        callerIdCalls.add(item);
    }

    @Override
    public double timeToSeconds(final long time) {
        return time * TIME_TO_SECONDS;
    }

    private static class KiekerServiceCall implements ServiceCall {
        private final ServiceParameters parameters;
        private final ServiceCallRecord record;

        public KiekerServiceCall(final ServiceCallRecord record) {
            this.parameters = ServiceParameters.buildFromJson(record.getParameters());
            this.record = record;
        }

        @Override
        public String getCallerId() {
            return this.record.getCallerId();
        }

        @Override
        public String getCallerServiceExecutionId() {
            return this.record.getCallerServiceExecutionId();
        }

        @Override
        public long getEntryTime() {
            return this.record.getEntryTime();
        }

        @Override
        public long getExitTime() {
            return this.record.getExitTime();
        }

        @Override
        public ServiceParameters getParameters() {
            return this.parameters;
        }

        @Override
        public long getResponseTime() {
            return this.record.getExitTime() - this.record.getEntryTime();
        }

        @Override
        public double getResponseTimeSeconds() {
            return this.getResponseTime() * TIME_TO_SECONDS;
        }

        @Override
        public String getServiceExecutionId() {
            return this.record.getServiceExecutionId();
        }

        @Override
        public String getServiceId() {
            return this.record.getServiceId();
        }
    }
}