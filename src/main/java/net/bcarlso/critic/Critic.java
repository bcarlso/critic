package net.bcarlso.critic;

import java.util.*;

public class Critic {
    private HashMap<Date, ContinuousIntegrationData> integrationsByDate = new HashMap<Date, ContinuousIntegrationData>();

    public int acceptPush(Date date) {
        return integrationsFor(date).pushReceived();
    }

    public int acceptPull(Date date) {
        return integrationsFor(date).pullReceived();
    }

    private ContinuousIntegrationData integrationsFor(Date date) {
        if (!integrationsByDate.containsKey(date)) {
            integrationsByDate.put(date, new ContinuousIntegrationData(date));
        }

        return integrationsByDate.get(date);
    }

    public List<ContinuousIntegrationData> reportIntegrations(Date startingPoint, int daysBack) {
        ArrayList<ContinuousIntegrationData> results = new ArrayList<ContinuousIntegrationData>();
        for (ContinuousIntegrationData integrationData : integrationsByDate.values()) {
            if (dataIsOlderThanRequestedDate(integrationData, startingPoint)) {
                results.add(integrationData);
            }
        }

        Collections.sort(results);
        return resultsLimitedTo(daysBack, results);
    }

    private boolean dataIsOlderThanRequestedDate(ContinuousIntegrationData integrationData, Date date) {
        return integrationData.getDate().compareTo(date) <= 0;
    }

    private List<ContinuousIntegrationData> resultsLimitedTo(int daysBack, ArrayList<ContinuousIntegrationData> temp) {
        return temp.subList(Math.max(0, temp.size() - daysBack), temp.size());
    }
}
