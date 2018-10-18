package monitoring.tests.simple;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class A {

    private final B externalB;

    public A(final B externalB) {
        this.externalB = externalB;
    }

    public int methodA(final int a) {
        // Monitoring actions start
        ServiceParameters serviceParameters = new ServiceParameters();
        serviceParameters.addInt("a", a);
        ThreadMonitoringController.getInstance().enterService("_XYJcUMjPEeiWRYm1yDC5rQ", serviceParameters);
        try {
            // Monitoring actions end

            // Monitoring actions start
            long ___startTime_1 = ThreadMonitoringController.getInstance().getTime();
            // Monitoring actions end

            this.computation(a);

            // Monitoring actions start
            ThreadMonitoringController.getInstance().logResponseTime("_OkrUMMjSEeiWRYm1yDC5rQ",
                    "_oro4gG3fEdy4YaaT-RYrLQ", ___startTime_1);
            // Monitoring actions end

            // Monitoring actions start
            long ___loopIterationsCount_1 = 0;
            // Monitoring actions end

            int result = 0;
            for (int i = 0; i < a; i++) {

                // Monitoring actions start
                ___loopIterationsCount_1++;
                // Monitoring actions end

                // Monitoring actions start
                long ___startTime_2 = ThreadMonitoringController.getInstance().getTime();
                // Monitoring actions end

                this.computation(5);

                // Monitoring actions start
                ThreadMonitoringController.getInstance().logResponseTime("_dhstIMjSEeiWRYm1yDC5rQ",
                        "_oro4gG3fEdy4YaaT-RYrLQ", ___startTime_2);
                // Monitoring actions end

                // Monitoring actions start
                String ___executedBranchId_1 = null;
                // Monitoring actions end

                if (a > 5) {
                    // Monitoring actions start
                    ___executedBranchId_1 = "_HbtQ8MjTEeiWRYm1yDC5rQ";
                    // Monitoring actions end

                    // Monitoring actions start
                    ThreadMonitoringController.getInstance().setCurrentCallerId("_P1p-cMjTEeiWRYm1yDC5rQ");
                    // Monitoring actions end

                    result += this.externalB.methodB1();
                }

                // Monitoring actions start
                ThreadMonitoringController.getInstance().logBranchExecution("_VTWLoMjSEeiWRYm1yDC5rQ",
                        ___executedBranchId_1);
                // Monitoring actions end
            }

            // Monitoring actions start
            ThreadMonitoringController.getInstance().logLoopIterationCount("_SSitEMjSEeiWRYm1yDC5rQ",
                    ___loopIterationsCount_1);
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
