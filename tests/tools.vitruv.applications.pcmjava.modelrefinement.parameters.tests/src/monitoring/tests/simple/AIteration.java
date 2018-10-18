package monitoring.tests.simple;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class AIteration {

    private final B externalB;

    public AIteration(final B externalB) {
        this.externalB = externalB;
    }

    public int methodA(final int a) {
        // Monitoring actions start
        ServiceParameters serviceParameters = new ServiceParameters();
        serviceParameters.addInt("a", a);
        ThreadMonitoringController.getInstance().enterService("_XYJcUMjPEeiWRYm1yDC5rQ", serviceParameters);
        try {
            // Monitoring actions end

            this.computation(a);

            int result = 0;
            for (int i = 0; i < a; i++) {

                this.computation(5);

                if (a > 5) {
                    result += this.externalB.methodB1();
                }
            }

            // Monitoring actions start
            String ___executedBranchId_1 = null;
            // Monitoring actions end

            if (a < 7) {

                // Monitoring actions start
                ___executedBranchId_1 = "_Lakg4MwNEeiWXYGpzxFH0A";
                // Monitoring actions end

                // Monitoring actions start
                long ___startTime_1 = ThreadMonitoringController.getInstance().getTime();
                // Monitoring actions end

                this.computation(a + a / 2);

                // Monitoring actions start
                ThreadMonitoringController.getInstance().logResponseTime("_SANpEMwWEeiWXYGpzxFH0A",
                        "_oro4gG3fEdy4YaaT-RYrLQ", ___startTime_1);
                // Monitoring actions end

                // Monitoring actions start
                ThreadMonitoringController.getInstance().setCurrentCallerId("_Sd2WYMwNEeiWXYGpzxFH0A");
                // Monitoring actions end

                this.externalB.methodB2();
            }

            // Monitoring actions start
            ThreadMonitoringController.getInstance().logBranchExecution("_8icPAMwMEeiWXYGpzxFH0A",
                    ___executedBranchId_1);
            // Monitoring actions end

            return result;

            // Monitoring actions start
        } finally {
            ThreadMonitoringController.getInstance().exitService();
        }
        // Monitoring actions end
    }

    private void computation(final int param) {
        double[] array = new double[1000 * param];
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.sqrt(i);
            for (int a = 0; a < array.length; a++) {
                array[i] += array[a];
            }
        }
    }
}
