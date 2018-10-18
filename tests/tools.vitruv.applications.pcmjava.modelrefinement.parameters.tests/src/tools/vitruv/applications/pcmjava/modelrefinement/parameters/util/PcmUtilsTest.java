package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.LoopAction;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.data.SimpleTestData;

public class PcmUtilsTest {
    @Test
    public void getObjectsTest() {
        Repository pcmModel = SimpleTestData.loadPcmModel();
        List<LoopAction> loops = PcmUtils.getObjects(pcmModel, LoopAction.class);
        List<BranchAction> branches = PcmUtils.getObjects(pcmModel, BranchAction.class);

        assertEquals(1, loops.size());
        assertEquals(1, branches.size());
    }
}
