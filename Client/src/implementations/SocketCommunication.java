package implementations;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import interfaces.iSocketCommunication;

public class SocketCommunication implements iSocketCommunication {
	ObjectSerializer objectSerializer = new ObjectSerializer();
	
	public void sendData(Socket socket, int choice, int gameId, Object Inputobject, String outputFileLocation) throws IOException {
	    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
	    if (choice == 1 || choice==2) {
	        dataOutputStream.writeInt(choice);
	    }

	    if (gameId != -1) {
	        dataOutputStream.writeInt(gameId);
	    }

	    if (Inputobject != null) {
		    byte[] objectBytes = objectSerializer.serializeObject(Inputobject);
	        dataOutputStream.writeInt(objectBytes.length);
	        dataOutputStream.write(objectBytes);
	    } else {
	        dataOutputStream.writeInt(0);
	    }

	    if (outputFileLocation != null) {
	        dataOutputStream.writeUTF(outputFileLocation);
	    }
		dataOutputStream.flush();
	}
	
	public void printMessageFromServer(DataInputStream dataInputStream) throws IOException {
		String serverMessage = dataInputStream.readUTF();
    	System.out.println(serverMessage);
	}
}
