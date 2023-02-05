import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
	private static final String ACTION_OPTION = "-a";
    private static final String INPUT_OPTION = "-i";
    private static final String OUTPUT_OPTION = "-o";
    
	public static void main(String[] args) throws IOException{
		readCommand(args);
	}
	
	public static void readCommand(String[] args) throws IOException {
		String action = null;
        String inputFileLocation = null;
        String outputFileLocation = null;
        
        for (int i = 0; i < args.length; i++) {
            if (ACTION_OPTION.equals(args[i])) {
                action = args[++i];
            } else if (INPUT_OPTION.equals(args[i])) {
            	inputFileLocation = args[++i];
            } else if (OUTPUT_OPTION.equals(args[i])) {
            	outputFileLocation = args[++i];
            }
        }
        
        if (action == null || inputFileLocation == null || outputFileLocation == null) {
            System.out.println("Usage:-a <action> -i <json_input_file> -o <json_output_file>");
            System.exit(1);
        }else {
    		initializeSocket(action,inputFileLocation,outputFileLocation);
        }
	}
	
	public  static void initializeSocket(String action, String inputFileLocation, String outputFileLocation) throws IOException {		
	    String serverAddress = "localhost";
	    int serverPort = 9000;
	    
	    Socket socket = new Socket(serverAddress, serverPort);
	    
	    if(action.toLowerCase().equals("create_team")) {
		    sendObjectToServer(socket,inputFileLocation,outputFileLocation);
	    }
	    
	    socket.close();
	}
	
	public static void sendObjectToServer(Socket socket, String inputFileLocation, String outputFileLocation) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
	    Object data = mapper.readValue(new FileReader(new File(inputFileLocation)), Object.class);
		
	    //serialize the object to a byte array
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
	    objectOutputStream.writeObject(data);
	    byte[] objectBytes = byteArrayOutputStream.toByteArray();

	    //send object and string to server
	    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
	    dataOutputStream.writeInt(objectBytes.length);
	    dataOutputStream.write(objectBytes);
	    dataOutputStream.writeUTF(outputFileLocation);
	    
	    objectOutputStream.close();
	}
}
