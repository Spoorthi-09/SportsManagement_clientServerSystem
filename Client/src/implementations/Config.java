package implementations;

import java.io.*;
import java.util.Properties;

public class Config {
	static Properties properties;
	static File inputStream = new File("resources//config.properties");
    static {
        try {
        	FileReader reader = new FileReader(inputStream);
        	properties = new Properties();
            properties.load(reader);;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getServerAddress() {
        return properties.getProperty("server.address");
    }

    public static int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }
}
