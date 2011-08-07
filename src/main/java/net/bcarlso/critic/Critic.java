package net.bcarlso.critic;

import java.util.*;

public class Critic {
    protected HashMap<Date, ContinuousIntegrationData> integrationsByDate = new HashMap<Date, ContinuousIntegrationData>();
    private CriticRepository repository = new CriticRepository("integrations.properties");

    public int acceptPush(Date date) {
        int updatedTotal = integrationsFor(date).pushReceived();
        repository.save(integrationsByDate);
        return updatedTotal;
    }

    public int acceptPull(Date date) {
        int updatedTotal = integrationsFor(date).pullReceived();
        repository.save(integrationsByDate);
        return updatedTotal;
    }

    private ContinuousIntegrationData integrationsFor(Date date) {
        if (!integrationsByDate.containsKey(date)) {
            integrationsByDate.put(date, new ContinuousIntegrationData(date));
        }

        return integrationsByDate.get(date);
    }

    public List<ContinuousIntegrationData> findIntegrations(Date startingPoint, int daysBack) {
        integrationsByDate = repository.load();
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

    public void setRepository(CriticRepository repository) {
        this.repository = repository;
    }
}