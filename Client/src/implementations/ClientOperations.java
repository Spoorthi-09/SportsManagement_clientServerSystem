package implementations;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import interfaces.iClientOperations;

public class ClientOperations implements iClientOperations {
	SocketCommunication socketCommunication = new SocketCommunication();
	InputParser inputParser = new InputParser();
	
	public void operations(Socket socket,DataInputStream dataInputStream) throws IOException {
		Scanner scanner = new Scanner(System.in);
		int choice;
        do {
        	System.out.println("Enter option:\r\n"
                    + " 1. Write team list to Json and save to database\r\n"
                    + " 2. Get Teams\n"
                    + " 3. Exit");
            choice = scanner.nextInt();
            
            if(choice == 1) {
            	createTeamsOperations(socket, scanner,choice);            	
            }else if (choice == 2) {
            	getTeamsOperations(socket,scanner,choice);
            }else if(choice == 3) {
            	System.out.println("Exiting program.");
                socket.close();
                System.exit(0);
            }
            socketCommunication.printMessageFromServer(dataInputStream);
          }while(choice<=3);
        System.out.println("Closing connection to server");
	    socket.close();
        }
	
	public void createTeamsOperations(Socket socket, Scanner scanner, int choice) throws StreamReadException, DatabindException, FileNotFoundException, IOException {
		System.out.println("Enter command");
    	scanner.nextLine();
    	String command = scanner.nextLine();
    	String[] fileLocations = inputParser.getFileLocations(command);
    	String inputFileLocation = fileLocations[0];
    	String outputFileLocation = fileLocations[1];
    	
    	ObjectMapper mapper = new ObjectMapper();
    	Object data = mapper.readValue(new FileReader(new File(inputFileLocation)), Object.class);
    	
    	socketCommunication.sendData(socket,choice,0,data,outputFileLocation);
	}

	public void getTeamsOperations(Socket socket, Scanner scanner, int choice) throws IOException {
		System.out.println("Enter game ID");
        int gameId = scanner.nextInt();
        socketCommunication.sendData(socket,choice,gameId,null,null);
	}

}
