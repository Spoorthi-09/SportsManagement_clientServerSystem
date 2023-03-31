package interfaces;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

public interface iClientOperations {
	public void operations(Socket socket,DataInputStream dataInputStream) throws IOException;
	public void createTeamsOperations(Socket socket, Scanner scanner, int choice) throws StreamReadException, DatabindException, FileNotFoundException, IOException;
	public void getTeamsOperations(Socket socket, Scanner scanner, int choice) throws IOException;
}
