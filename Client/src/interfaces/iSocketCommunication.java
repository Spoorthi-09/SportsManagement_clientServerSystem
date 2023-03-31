package interfaces;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public interface iSocketCommunication {
	public void sendData(Socket socket, int choice, int gameId, Object Inputobject, String outputFileLocation) throws IOException;
	public void printMessageFromServer(DataInputStream dataInputStream) throws IOException;
}
