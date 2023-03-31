package sportsmanagement;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Game;

public class ClientHandler implements Runnable {
	private Socket clientSocket;
    
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run() {
        try (DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());) {
            int choice;
            do {
                try {
                    choice = dataInputStream.readInt();
                    int gameId = dataInputStream.readInt();
                    
                    Object inputJsonObject = null;
                    int objectLength = dataInputStream.readInt();
                    if (objectLength > 0) {
                        byte[] objectBytes = new byte[objectLength];
                        dataInputStream.readFully(objectBytes);
                        inputJsonObject = deserializeObject(objectBytes);
                    }
                    
                    String outputFileLocation = null;
                    if(choice == 1) {
                        outputFileLocation = dataInputStream.readUTF();
                    }               
                    TeamPersistence teamPersistence = new TeamPersistence();
                    switch (choice) {               
                        case 1: 
                            System.out.println("Inside switch "+choice);
                            createTeamsOperations(inputJsonObject,teamPersistence,outputFileLocation,dataOutputStream);
                            break;
                        case 2:
                            System.out.println("Inside switch "+choice);
                            getTeamsOperations(gameId,teamPersistence,dataOutputStream);
                            break;
                        default:
                            System.out.println("Invalid choice");
                            break;
                    }
                } catch (EOFException e) {
                    System.err.println("Client disconnected");
                    break;
                }
            } while (choice <= 2);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    private static Object deserializeObject(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();
        }
    }

    private static void createTeamsOperations(Object inputJsonObject,TeamPersistence teamPersistence,String outputFileLocation,DataOutputStream dataOutputStream) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        JsonOperations jsonOperations = new JsonOperations();
        TeamCreator teamCreator = new TeamCreator();
        String jsonDataString = mapper.writeValueAsString(inputJsonObject);
        Game game = mapper.readValue(jsonDataString, Game.class);

    	String message = teamPersistence.checkInputGame(game);
    	if(message.contains("Success")) {
    		jsonOperations.writeOutputtoJson(mapper, game, outputFileLocation); 
    		teamPersistence.saveTeam(teamCreator.createTeams(game));
    	}else {
    		System.out.println("can't invoke");
    	}
    	
    	dataOutputStream.writeUTF(message);
    	dataOutputStream.flush();
    }
    
    private static void getTeamsOperations(int gameId, TeamPersistence teamPersistence,DataOutputStream dataOutputStream) throws IOException {
	        String clientMessage = teamPersistence.getTeams(gameId);
	        dataOutputStream.writeUTF(clientMessage);
	      	dataOutputStream.flush();
	          
    }
 }

