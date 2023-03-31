package implementations;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import interfaces.iSocketInitializer;

public class SocketInitializer implements iSocketInitializer {
	ClientOperations clientOperations = new ClientOperations();
	public void initializeSocket() throws IOException {		   
	    Socket socket = null;
	    try {
	    	String serverAddress = Config.getServerAddress();
	 	    int serverPort = Config.getServerPort();
	 	    socket = new Socket(serverAddress, serverPort);
		    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
		    clientOperations.operations(socket,dataInputStream);
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}    
	}
}
