package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import java.util.HashMap;
import java.util.Map;

public class ServiceParametersUtil {
    public static ServiceParameters buildParameters(final String name, final Object value) {
        Map<String, Object> testParameters = new HashMap<>();
        testParameters.put(name, value);
        return ServiceParameters.build(testParameters);
    }

    public static ServiceCall buildServiceCall(final String parameterName, final Object parameterValue) {
        return new ServiceCallMock(buildParameters(parameterName, parameterValue));
    }
}
