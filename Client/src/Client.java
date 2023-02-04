import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		String fileName = "C:\\Users\\spoorthi.s.bhat\\Desktop\\Learn and code\\TeamsInputJSON.json";
	    String serverAddress = "localhost";
	    int serverPort = 9000;
	    
	    Socket socket = new Socket(serverAddress, serverPort);
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	    
	    ObjectMapper mapper = new ObjectMapper();
	    Object data = mapper.readValue(new FileReader(new File(fileName)), Object.class);
	    objectOutputStream.writeObject(data);
	    
	    objectOutputStream.close();
	    socket.close();

	}
}
