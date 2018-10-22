package tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl;

import java.util.Optional;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.RecordWithSession;

/**
 * Kieker monitoring records filter, which filters for a specific session id.
 * 
 * @author JP
 *
 */
@Plugin(description = "Filter for a session id.", outputPorts = {
        @OutputPort(name = KiekerSessionFilter.OUTPUT_PORT_NAME_EVENTS,
                description = "Outputs response times exceeding the threshold", eventTypes = {
                        RecordWithSession.class }) },
        configuration = {
                @Property(name = KiekerSessionFilter.CONFIG_PROPERTY_NAME_SESSION_ID, defaultValue = "") })
public class KiekerSessionFilter extends AbstractFilterPlugin {

    /**
     * The name of the input port for incoming events.
     */
    public static final String INPUT_PORT_NAME_EVENTS = "inputEvent";

    /**
     * The name of the output port for outgoing events.
     */
    public static final String OUTPUT_PORT_NAME_EVENTS = "outputEvent";

    /**
     * The name of the input port for incoming events.
     */
    public static final String CONFIG_PROPERTY_NAME_SESSION_ID = "SessionId";

    /**
     * The session id to filter for. If empty, nothing is filtered.
     */
    private final Optional<String> sessionId;

    /**
     * Initializes a new instance of {@link KiekerSessionFilter}. Each Plugin requires a constructor with a
     * Configuration object and a IProjectContext.
     * 
     * @param configuration
     *            The configuration for this component.
     * @param projectContext
     *            The project context for this component. The component will be registered.
     */
    public KiekerSessionFilter(final Configuration configuration, final IProjectContext projectContext) {
        super(configuration, projectContext);
        String sessionIdConfig = configuration.getStringProperty(CONFIG_PROPERTY_NAME_SESSION_ID);
        if (sessionIdConfig == null || sessionIdConfig.isEmpty()) {
            this.sessionId = Optional.empty();
        } else {
            this.sessionId = Optional.of(sessionIdConfig);
        }
    }

    @Override
    public Configuration getCurrentConfiguration() {
        Configuration config = new Configuration();
        config.putIfAbsent(CONFIG_PROPERTY_NAME_SESSION_ID, this.sessionId);
        return config;
    }

    /**
     * This method is called by kieker for each record of the type specified by {@link InputPort}.
     * 
     * @param record
     *            The record of the specified type.
     */
    @InputPort(name = INPUT_PORT_NAME_EVENTS, description = "Input for records.", eventTypes = {
            RecordWithSession.class })
    public final void inputEvent(final RecordWithSession record) {
        if (this.sessionId.isPresent() == false || this.sessionId.get().equals(record.getSessionId())) {
            super.deliver(OUTPUT_PORT_NAME_EVENTS, record);
        }
    }
}
