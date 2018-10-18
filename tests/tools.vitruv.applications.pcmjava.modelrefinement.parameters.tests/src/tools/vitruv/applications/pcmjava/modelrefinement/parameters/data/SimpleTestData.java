package tools.vitruv.applications.pcmjava.modelrefinement.parameters.data;

import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl.KiekerMonitoringReader;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class SimpleTestData {

    public static final String ResourceId = "_oro4gG3fEdy4YaaT-RYrLQ";

    public static final String FirstSessionId = "session-1";

    public static final String SecondSessionId = "session-2";

    public static final String ThirdSessionId = "session-3";

    public static final String LoopId = "_SSitEMjSEeiWRYm1yDC5rQ";

    public static final String FirstInternalActionId = "_OkrUMMjSEeiWRYm1yDC5rQ";

    public static final String SecondInternalActionId = "_dhstIMjSEeiWRYm1yDC5rQ";

    public static final String ThirdInternalActionId = "_SANpEMwWEeiWXYGpzxFH0A";

    public static final String FirstBranchId = "_VTWLoMjSEeiWRYm1yDC5rQ";

    public static final String FirstBranchTransitionId = "_HbtQ8MjTEeiWRYm1yDC5rQ";

    public static final String SecondBranchId = "_8icPAMwMEeiWXYGpzxFH0A";

    public static final String SecondBranchTransitionId = "_Lakg4MwNEeiWXYGpzxFH0A";

    public static final String TempDirectoryPath = "./test-data/simple-iteration/";

    public static final String A1ServiceSeffId = "_XYJcUMjPEeiWRYm1yDC5rQ";

    public static final String B1ServiceSeffId = "_T_bNAMjPEeiWRYm1yDC5rQ";

    public static final String NotSetId = "<not set>";

    public static final String FirstExternalCallerId = "_P1p-cMjTEeiWRYm1yDC5rQ";

    public static KiekerMonitoringReader getReader(final String sessionId) {
        return new KiekerMonitoringReader("./test-data/simple", sessionId);
    }

    public static Repository loadIterationPcmModel() {
        return PcmUtils.loadModel("./test-data/simple-iteration/default2.repository");
    }

    public static Repository loadPcmModel() {
        return PcmUtils.loadModel("./test-data/simple/default.repository");
    }
}
