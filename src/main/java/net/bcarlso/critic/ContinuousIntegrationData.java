package net.bcarlso.critic;

import java.util.Date;

public class ContinuousIntegrationData implements Comparable<ContinuousIntegrationData> {

    private int pushesReceived;
    private int pullsReceived;
    private Date date;

    public ContinuousIntegrationData(Date date) {
        this.date = date;
    }

    public int pushReceived() {
        return ++pushesReceived;
    }

    public int pullReceived() {
        return ++pullsReceived;
    }

    public Date getDate() {
        return date;
    }

    public int getPulls() {
        return pullsReceived;
    }

    public int getPushes() {
        return pushesReceived;
    }

    public int compareTo(ContinuousIntegrationData continuousIntegrationData) {
        return this.date.compareTo(continuousIntegrationData.getDate());
    }
}
