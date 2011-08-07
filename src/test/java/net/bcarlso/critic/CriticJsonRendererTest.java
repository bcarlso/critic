package net.bcarlso.critic;

import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static net.bcarlso.critic.Helpers.july;

public class CriticJsonRendererTest {
    @Test
    public void rendersResultsAsArrayOfJsonObjects() {
        CriticJsonRenderer renderer = new CriticJsonRenderer();

        List<ContinuousIntegrationData> data = Arrays.asList(new ContinuousIntegrationData(july(30)), new ContinuousIntegrationData(july(31)));
        assertEquals("[{\"date\": \"07/30\",\"pushes\": 0,\"pulls\": 0},{\"date\": \"07/31\",\"pushes\": 0,\"pulls\": 0}]", renderer.render(data));
    }

}
