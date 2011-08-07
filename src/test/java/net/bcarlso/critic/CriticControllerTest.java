package net.bcarlso.critic;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static net.bcarlso.critic.Helpers.july;
import static org.mockito.Mockito.*;

public class CriticControllerTest {

    public static final List<ContinuousIntegrationData> CI_REPORT = Arrays.asList();
    private HttpServletRequest request;
    private Critic critic;
    private CriticJsonRenderer renderer;
    private CriticController controller;
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        critic = mock(Critic.class);
        renderer = mock(CriticJsonRenderer.class);

        controller = new CriticController();
        controller.setCritic(critic);
        controller.setRenderer(renderer);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void getRendersReportBasedOnDateAndTimespan() throws IOException {
        PrintWriter writer = mock(PrintWriter.class);

        when(request.getParameter(CriticController.Parameters.DATE)).thenReturn("2011-07-31");
        when(request.getParameter(CriticController.Parameters.PERIOD)).thenReturn("10");
        when(critic.findIntegrations(july(31), 10)).thenReturn(CI_REPORT);
        when(renderer.render(CI_REPORT)).thenReturn("result from renderer");
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);

        verify(writer).append("result from renderer");
    }

    @Test
    public void postRecordsPullRequests() throws IOException, ServletException {
        when(request.getParameter(CriticController.Parameters.DATE)).thenReturn("2011-07-31");
        when(request.getParameter(CriticController.Parameters.ACTION)).thenReturn("pull");
        controller.doPost(request, response);
        verify(critic).acceptPull(july(31));
    }

    @Test
    public void postRecordsPushRequests() throws IOException, ServletException {
        when(request.getParameter(CriticController.Parameters.DATE)).thenReturn("2011-07-31");
        when(request.getParameter(CriticController.Parameters.ACTION)).thenReturn("push");
        controller.doPost(request, response);
        verify(critic).acceptPush(july(31));
    }

    @Test
    public void returnsBadRequestWhenInvalidActionSpecified() throws IOException, ServletException {
        when(request.getParameter(CriticController.Parameters.DATE)).thenReturn("2011-07-31");
        when(request.getParameter(CriticController.Parameters.ACTION)).thenReturn("invalid-action");

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
