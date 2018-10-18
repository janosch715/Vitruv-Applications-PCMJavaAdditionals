package monitoring.tests.simple;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class B {
    public int methodB1() {
        // Monitoring actions start
        ThreadMonitoringController.getInstance().enterService("_T_bNAMjPEeiWRYm1yDC5rQ");
        try {
            // Monitoring actions end

            return 1;

            // Monitoring actions start
        } finally {
            ThreadMonitoringController.getInstance().exitService();
        }
        // Monitoring actions end
    }

    public void methodB2() {
        // Monitoring actions start
        ThreadMonitoringController.getInstance().enterService("_1d51QMwMEeiWXYGpzxFH0A");
        try {
            // Monitoring actions end

            // Monitoring actions start
        } finally {
            ThreadMonitoringController.getInstance().exitService();
        }
        // Monitoring actions end
    }
}
