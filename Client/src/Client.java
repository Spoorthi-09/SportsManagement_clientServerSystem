import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    
	public static void main(String[] args) throws IOException{
		initializeSocket();
	}
	
	public  static void initializeSocket() throws IOException {		   

	    Socket socket = null;
	    try {
	    	 String serverAddress = "localhost";
	 	    int serverPort = 9000;
	 	    socket = new Socket(serverAddress, serverPort);
//		    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
		    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
	 	   	clientOperations(socket,dataInputStream);
//	 	   dataOutputStream.close();
//	 	   dataInputStream.close();
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}    
	}
	
	private static void clientOperations(Socket socket,DataInputStream dataInputStream) throws IOException {
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
            printMessageFromServer(dataInputStream);
          }while(choice<=3);
        System.out.println("Closing connection to server");
	    socket.close();
        }
	
	public static void createTeamsOperations(Socket socket, Scanner scanner, int choice) throws StreamReadException, DatabindException, FileNotFoundException, IOException {
		System.out.println("Enter command");
    	scanner.nextLine();
    	String command = scanner.nextLine();
    	String[] fileLocations = getFileLocations(command);
    	String inputFileLocation = fileLocations[0];
    	String outputFileLocation = fileLocations[1];
    	
    	ObjectMapper mapper = new ObjectMapper();
    	Object data = mapper.readValue(new FileReader(new File(inputFileLocation)), Object.class);
    	
    	sendData(socket,choice,0,data,outputFileLocation);
	}
	
	public static void getTeamsOperations(Socket socket, Scanner scanner, int choice) throws IOException {
		System.out.println("Enter game ID");
        int gameId = scanner.nextInt();
        sendData(socket,choice,gameId,null,null);
	}
	
	public static Matcher isValidFormat(String command) {
        String pattern = "-a (\\S+) -i (\\S+) -o (\\S+)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(command);
        return matcher;
    }
	
	public static String[] getFileLocations(String command) {
		Matcher matcher = isValidFormat(command);
		if(matcher.find()) {
			String inputFile = matcher.group(2);
			String outputFile = matcher.group(3);
		    return new String[] {inputFile, outputFile};
		}else {
			System.out.println("Please use the format : -a <action> -i <input-file-location> -o <output-file-location>");
		}
		return null;
	}
	
	public static byte[] serializeObject(Object object) throws IOException {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
	    objectOutputStream.writeObject(object);
	    byteArrayOutputStream.close();
	    objectOutputStream.close();
	    return byteArrayOutputStream.toByteArray();
	}
	
	public static void sendData(Socket socket, int choice, int gameId, Object Inputobject, String outputFileLocation) throws IOException {
	    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
	    if (choice == 1 || choice==2) {
	        dataOutputStream.writeInt(choice);
	    }

	    if (gameId != -1) {
	        dataOutputStream.writeInt(gameId);
	    }

	    if (Inputobject != null) {
		    byte[] objectBytes = serializeObject(Inputobject);
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
	
	public static void printMessageFromServer(DataInputStream dataInputStream) throws IOException {
		String serverMessage = dataInputStream.readUTF();
    	System.out.println(serverMessage);
	}
}
