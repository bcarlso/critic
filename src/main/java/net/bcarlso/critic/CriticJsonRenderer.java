package net.bcarlso.critic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CriticJsonRenderer {

    public static final SimpleDateFormat MONTH_DAY = new SimpleDateFormat("MM/dd");

    public String render(List<ContinuousIntegrationData> data) {
        StringBuilder json = new StringBuilder("[");
        String separator = "";
        for (ContinuousIntegrationData item : data) {
            json.append(separator);
            json.append(toJsonObject(item));
            separator = ",";
        }
        json.append("]");
        return json.toString();
    }

    private String toJsonObject(ContinuousIntegrationData item) {
        return "{\"date\": \"" + asMonthDay(item.getDate()) + "\"," +
                "\"pushes\": " + item.getPushes() + "," +
                "\"pulls\": " + item.getPulls() + "}";
    }

    private String asMonthDay(Date date) {
        return MONTH_DAY.format(date);
    }
}
