package net.bcarlso.critic;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.internal.matchers.Any;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static net.bcarlso.critic.Helpers.july;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CriticTest {
    private Critic critic;
    private CriticRepository repository;

    @Before
    public void setUp() {
        critic = new Critic();
        repository = mock(CriticRepository.class);
        critic.setRepository(repository);
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

    @Test
    public void reportsOnSpecifiedNumberOfDays() {
        critic.acceptPull(july(29));

        when(repository.load()).thenReturn(critic.integrationsByDate);

        List<ContinuousIntegrationData> dataSet = critic.findIntegrations(july(29), 1);

        assertEquals(1, dataSet.size());
        assertEquals(july(29), dataSet.get(0).getDate());
        assertEquals(1, dataSet.get(0).getPulls());
        assertEquals(0, dataSet.get(0).getPushes());
    }

    @Test
    public void loadsReportsFromRepository() {
        critic.findIntegrations(july(29), 1);
        verify(repository).load();
    }

    @Test
    public void savesOnPullEvents() {
        critic.acceptPull(july(30));
        verify(repository).save(Matchers.<HashMap<Date, ContinuousIntegrationData>>any());
    }

    @Test
    public void savesOnPushEvents() {
        critic.acceptPush(july(30));
        verify(repository).save(Matchers.<HashMap<Date,ContinuousIntegrationData>>any());
    }

    @Test
    public void reportsOnSpecifiedNumberOfDaysAscendingByDate() {
        critic.acceptPull(july(28));
        critic.acceptPull(july(29));
        critic.acceptPush(july(30));
        critic.acceptPull(july(31));

        when(repository.load()).thenReturn(critic.integrationsByDate);

        List<ContinuousIntegrationData> dataSet = critic.findIntegrations(july(30), 3);

        assertEquals(3, dataSet.size());
        assertEquals(july(28), dataSet.get(0).getDate());
        assertEquals(july(29), dataSet.get(1).getDate());
        assertEquals(july(30), dataSet.get(2).getDate());
    }

    @Test
    public void basesReportingOffOfTodaysDate() {
        critic.acceptPull(july(28));
        critic.acceptPull(july(29));
        critic.acceptPush(july(30));
        critic.acceptPush(july(31));

        when(repository.load()).thenReturn(critic.integrationsByDate);

        List<ContinuousIntegrationData> dataSet = critic.findIntegrations(july(30), 2);

        assertEquals(2, dataSet.size());
        assertEquals(july(29), dataSet.get(0).getDate());
        assertEquals(july(30), dataSet.get(1).getDate());
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
}
