package sportsmanagement;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    public static String getDriver() {
        return properties.getProperty("driver");
    }
    
    public static String getDatabaseURL() {
        return properties.getProperty("db.url");
    }
    
    public static String getDatabaseUsername() {
        return properties.getProperty("db.username");
    } 

    public static String getDatabasePassword() {
        return properties.getProperty("db.password");
    }

    public static int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }
    
    public static String getInsertTeamQuery() {
        return properties.getProperty("db.insertTeam");
    }
    
    public static String getCountPlayerQuery() {
        return properties.getProperty("db.countPlayer");
    }
    
    public static String getInsertPlayerQuery() {
        return properties.getProperty("db.insertPlayer");
    }
    
    public static String getInsertTeamPlayerQuery() {
        return properties.getProperty("db.insertTeamPlayer");
    }
    
    public static String getTeamQuery() {
        return properties.getProperty("db.getTeam");
    }
    
    public static String getCountPlayerInTeamPlayerQuery() {
        return properties.getProperty("db.countPlayerInTeamPlayer");
    }
    
    public static String getCountPlayerInTeamQuery() {
        return properties.getProperty("db.countPlayerInTeam");
    }
}

