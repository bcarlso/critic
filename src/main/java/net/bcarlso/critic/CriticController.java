package net.bcarlso.critic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CriticController extends HttpServlet {

    public static interface Parameters {
        String DATE = "date";
        String PERIOD = "report_period";
        String ACTION = "action";
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Critic critic;
    private CriticJsonRenderer renderer;

    @Override
    public void init() throws ServletException {
        critic = new Critic();
        renderer = new CriticJsonRenderer();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Date date = dateFrom(request);
            System.out.println("date = " + date);
            int daysBack = periodFrom(request);
            List<ContinuousIntegrationData> integrations = critic.findIntegrations(date, daysBack);
            String content = renderer.render(integrations);
            response.getWriter().append(content);
        } catch (ParseException e) {
            throw wrapped(e);
        } catch (IOException e) {
            throw wrapped(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Date date = DATE_FORMAT.parse(request.getParameter(Parameters.DATE));
            String actionPerformed = request.getParameter(Parameters.ACTION);
            if ("push".equals(actionPerformed)) {
                critic.acceptPush(date);
            } else if ("pull".equals(actionPerformed)) {
                critic.acceptPull(date);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (ParseException e) {
            throw wrapped(e);
        }
    }

    private RuntimeException wrapped(Exception e) {
        return new RuntimeException(e);
    }

    private int periodFrom(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter(Parameters.PERIOD));
    }

    private Date dateFrom(HttpServletRequest request) throws ParseException {
        return DATE_FORMAT.parse(request.getParameter(Parameters.DATE));
    }

    public void setRenderer(CriticJsonRenderer renderer) {
        this.renderer = renderer;
    }

    public void setCritic(Critic critic) {
        this.critic = critic;
    }
}
