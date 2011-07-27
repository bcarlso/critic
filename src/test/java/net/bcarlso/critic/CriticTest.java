package net.bcarlso.critic;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class CriticTest {
    private Critic critic;

    @Before
    public void setUp() {
        critic = new Critic();
    }

    @Test
    public void acceptsIncomingChangegroups() {
        assertEquals(1, pushesOn(july(26), 1));
    }

    @Test
    public void incrementsTheCountWhenAnIncomingChangegroupIsReceived() {
        assertEquals(2, pushesOn(july(25), 2));
    }

    @Test
    public void tracksIncomingChangegroupsByDate() {
        assertEquals(2, pushesOn(july(23), 2));
        assertEquals(3, pushesOn(july(24), 3));
    }

    @Test
    public void acceptsOutgoingChangegroups() {
            assertEquals(1, pullsOn(july(26), 1));
    }

    @Test
    public void incrementsTheCountWhenAnOutgoingChangegroupIsReceived() {
        assertEquals(2, pullsOn(july(25), 2));
    }

    @Test
    public void tracksOutgoingChangegroupsByDate() {
        assertEquals(2, pullsOn(july(23), 2));
        assertEquals(3, pullsOn(july(24), 3));
    }

    private int pullsOn(Date date, int numberOfPulls) {
        int totalPulls = 0;
        for (int n = 0; n < numberOfPulls; n++) {
            totalPulls = critic.acceptPull(date);
        }
        return totalPulls;
    }

    private int pushesOn(Date date, int numberOfPushes) {
        int totalPushes = 0;
        for (int n = 0; n < numberOfPushes; n++) {
            totalPushes = critic.acceptPush(date);
        }
        return totalPushes;
    }

    private Date july(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JULY);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }
}
