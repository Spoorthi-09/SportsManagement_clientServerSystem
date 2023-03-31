package dbconnection;
import java.sql.Connection;
import java.sql.DriverManager;

import sportsmanagement.Config;

public class DBConnection {
	static Connection con;
	public static Connection createDBConnection() {
		
		try {
			//load driver
			Class.forName(Config.getDriver());
		
			//get connection
			String url=Config.getDatabaseURL();
			String username=Config.getDatabaseUsername();
			String password=Config.getDatabasePassword();
			con = DriverManager.getConnection(url,username,password);
			
//			if(con!=null) {
//				System.out.println("success connection");
//			}else {
//				System.out.println("failed connection");
//			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
}
