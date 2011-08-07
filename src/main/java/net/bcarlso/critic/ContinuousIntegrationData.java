package net.bcarlso.critic;

import java.io.Serializable;
import java.util.Date;


public class ContinuousIntegrationData implements Comparable<ContinuousIntegrationData>, Serializable {

    private Date date;
    private int pushesReceived;
    private int pullsReceived;

    public ContinuousIntegrationData(Date date) {
        this.date = date;
    }

    public ContinuousIntegrationData(Date date, int pushesReceived, int pullsReceived) {
        this.date = date;
        this.pushesReceived = pushesReceived;
        this.pullsReceived = pullsReceived;
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
