package net.bcarlso.critic;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class CriticRepository {
    public static final String FILE_COMMENT = "Continuous Integration Critic Data. Format: [integration-date]=[pulls],[pushes]";
    public static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private String filename;

    public CriticRepository(String filename) {
        this.filename = filename;
    }

    public void save(HashMap<Date, ContinuousIntegrationData> data) {
        try {
            Properties intermediary = buildPropertiesFrom(data);
            intermediary.store(getOutputFile(), FILE_COMMENT);
        } catch (IOException e) {
            throw wrapped(e);
        }
    }

    public HashMap<Date, ContinuousIntegrationData> load() {
        try {
            Properties properties = new Properties();
            properties.load(getInputFile());
            return unmarshal(properties);
        } catch (IOException e) {
            throw wrapped(e);
        } catch (ParseException e) {
            throw wrapped(e);
        }
    }

    private RuntimeException wrapped(Exception e) {
        return new RuntimeException(e);
    }

    private FileInputStream getInputFile() throws FileNotFoundException {
        return new FileInputStream(filename);
    }

    private HashMap<Date, ContinuousIntegrationData> unmarshal(Properties properties) throws ParseException {
        HashMap<Date, ContinuousIntegrationData> map = new HashMap<Date, ContinuousIntegrationData>();
        for (String entry : properties.stringPropertyNames()) {
            Date integrationDate = FILE_DATE_FORMAT.parse(entry);
            String csv = properties.getProperty(entry);
            String[] values = csv.split(",");
            int pushes = Integer.parseInt(values[1]);
            int pulls = Integer.parseInt(values[0]);
            map.put(integrationDate, new ContinuousIntegrationData(integrationDate, pushes, pulls));
        }
        return map;
    }

    private PrintWriter getOutputFile() throws FileNotFoundException {
        return new PrintWriter(new File(filename));
    }

    private Properties buildPropertiesFrom(HashMap<Date, ContinuousIntegrationData> data) {
        Properties intermediary = new Properties();
        for (ContinuousIntegrationData entry : data.values()) {
            intermediary.setProperty(FILE_DATE_FORMAT.format(entry.getDate()), entry.getPulls() + "," + entry.getPushes());
        }
        return intermediary;
    }
}
