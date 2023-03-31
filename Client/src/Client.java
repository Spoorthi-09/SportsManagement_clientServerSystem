import java.io.*;

import implementations.SocketInitializer;

public class Client {
    
	public static void main(String[] args) throws IOException{
		SocketInitializer socketInitializer = new SocketInitializer();
		socketInitializer.initializeSocket();
	}
}
