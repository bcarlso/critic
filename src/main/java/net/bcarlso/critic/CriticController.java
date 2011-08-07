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
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Critic critic;
    private CriticJsonRenderer renderer;

    @Override
    public void init() throws ServletException {
        critic = new Critic();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Date date = dateFrom(request);
            int daysBack = periodFrom(request);
            List<ContinuousIntegrationData> integrations = critic.findIntegrations(date, daysBack);
            String content = renderer.render(integrations);
            response.getWriter().append(content);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
