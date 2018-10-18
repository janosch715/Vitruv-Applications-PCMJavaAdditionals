package monitoring.tests.simple2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.sigar.ISigarSamplerFactory;
import kieker.monitoring.sampler.sigar.SigarSamplerFactory;
import kieker.monitoring.sampler.sigar.samplers.CPUsDetailedPercSampler;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class Main {

    final static ISigarSamplerFactory sigarFactory = SigarSamplerFactory.INSTANCE;

    final static CPUsDetailedPercSampler cpuSampler = sigarFactory.createSensorCPUsDetailedPerc();

    public static void main(final String[] args) throws IOException {

        MonitoringController.getInstance().schedulePeriodicSampler(
                cpuSampler, 0, 1, TimeUnit.SECONDS);

        System.out.println("Set processor affinity to CPU 0 and press enter.");
        System.in.read();

        ThreadMonitoringController.setSessionId("session-1");

        for (int x = 0; x < 50; x++) {
            C c = new C();
            c.methodA();
            System.out.println("Next application run finished.");
        }

        System.out.println("Finished session-1.");

        System.out.println("Finished performance monitoring.");
    }
}
