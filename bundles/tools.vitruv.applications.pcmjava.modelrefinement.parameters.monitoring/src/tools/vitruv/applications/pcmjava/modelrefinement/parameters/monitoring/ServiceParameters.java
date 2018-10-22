package tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring;

/**
 * Service parameter serialization.
 *
 * @author JP
 *
 */
public class ServiceParameters {

    /**
     * Empty service parameters.
     */
    public static ServiceParameters EMPTY = new ServiceParameters();

    private final StringBuilder stringBuilder;

    /**
     * Initializes a new instance of {@link ServiceParameters} class.
     */
    public ServiceParameters() {
        this.stringBuilder = new StringBuilder();
    }

    /**
     * Appends an float parameter.
     *
     * @param name
     *            Parameter name.
     * @param value
     *            Parameter value.
     */
    public void addFloat(final String name, final double value) {
        this.stringBuilder.append("\"").append(name).append("\":").append(value).append(",");
    }

    /**
     * Appends an integer parameter.
     *
     * @param name
     *            Parameter name.
     * @param value
     *            Parameter value.
     */
    public void addInt(final String name, final int value) {
        this.stringBuilder.append("\"").append(name).append("\":").append(value).append(",");
    }

    /**
     * Gets the serialized parameters.
     */
    @Override
    public String toString() {
        return "{" + this.stringBuilder.toString() + "}";
    }
}
