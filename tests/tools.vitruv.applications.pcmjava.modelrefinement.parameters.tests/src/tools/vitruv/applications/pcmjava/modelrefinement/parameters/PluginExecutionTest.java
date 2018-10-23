package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.osgi.framework.Bundle;

public class PluginExecutionTest {

    @Test
    public void checkIfExecutedAsPlugin() {
        Bundle b = org.osgi.framework.FrameworkUtil.getBundle(PluginExecutionTest.class);
        if (b == null) {
            fail("ERROR Execute tests as plugin unit tests, else tests will fail.");
        }
    }
}
