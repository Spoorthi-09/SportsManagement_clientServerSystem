package dbconnection;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	static Connection con;
	public static Connection createDBConnection() {
		
		try {
			//load driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			//get connection
			String url="jdbc:mysql://localhost:3306/sportsmanagement";
			String username="root";
			String password="LnC@root09";
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
