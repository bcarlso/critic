package net.bcarlso.critic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Random;

import static net.bcarlso.critic.Helpers.july;

public class SampleFileGenerator {
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage: SampleFileGenerator [number_of_days=30");
        }
        int x = 30;
        if (args.length == 1) {
            x = Integer.parseInt(args[0]);
        }
        Properties properties = new Properties();
        for (int y = 1; y <= x; y++) {
            properties.setProperty(String.format("2011-01-%02d", y), String.format("%s,%s", new Random().nextInt(8),new Random().nextInt(8)));
        }

        FileWriter fileWriter = new FileWriter("integrations.properties");
        properties.store(fileWriter, "Generated Sample File");
    }
}
