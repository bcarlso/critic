package net.bcarlso.critic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import static net.bcarlso.critic.Helpers.july;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CriticRepositoryIntegrationTest {

    public static final String FILENAME = "integrations.properties";
    public static final File REPOSITORY_FILE = new File(FILENAME);

    @Before
    @After
    public void setUp() {
        REPOSITORY_FILE.delete();
    }

    @Test
    public void savesDataAsPropertiesFile() {
        CriticRepository repository = new CriticRepository(FILENAME);
        HashMap<Date, ContinuousIntegrationData> data = new HashMap<Date, ContinuousIntegrationData>();

        ContinuousIntegrationData thirtyJuly = new ContinuousIntegrationData(july(30));
        thirtyJuly.pushReceived();
        thirtyJuly.pullReceived();
        thirtyJuly.pullReceived();

        data.put(july(30), thirtyJuly);
        repository.save(data);
        assertTrue(REPOSITORY_FILE.exists());
        HashMap<Date, ContinuousIntegrationData> actual = repository.load();
        assertTrue(actual.containsKey(july(30)));
        assertEquals(2, actual.get(july(30)).getPulls());
        assertEquals(1, actual.get(july(30)).getPushes());
    }

}
