package net.bcarlso.critic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;

public class CriticController extends HttpServlet {

    private Critic critic;

    @Override
    public void init() throws ServletException {
        critic = new Critic();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        List<ContinuousIntegrationData> integrations = critic.findIntegrations(Calendar.getInstance().getTime(), 30);
        try {
            request.getRequestDispatcher("json.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
