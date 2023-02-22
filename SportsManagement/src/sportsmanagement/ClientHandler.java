package sportsmanagement;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                    TeamCreation teamCreationObj = new TeamCreation();
                    switch (choice) {               
                        case 1: 
                            System.out.println("Inside switch "+choice);
                            createTeamsOperations(inputJsonObject,teamCreationObj,outputFileLocation,dataOutputStream);
                            break;
                        case 2:
                            System.out.println("Inside switch "+choice);
                            getTeamsOperations(gameId,teamCreationObj,dataOutputStream);
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

    private static void createTeamsOperations(Object inputJsonObject,TeamCreation teamCreationObj,String outputFileLocation,DataOutputStream dataOutputStream) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        String jsonDataString = mapper.writeValueAsString(inputJsonObject);
        Game game = mapper.readValue(jsonDataString, Game.class);
        
    	String message = teamCreationObj.checkInputGame(game);
    	if(message.contains("Success")) {
    		teamCreationObj.writeOutputtoJson(mapper, game, outputFileLocation); 
    		teamCreationObj.saveTeam(teamCreationObj.createTeams(game));
    	}
    	
    	dataOutputStream.writeUTF(message);
    	dataOutputStream.flush();
    }
    
    private static void getTeamsOperations(int gameId, TeamCreation teamCreationObj,DataOutputStream dataOutputStream) throws IOException {
	        String clientMessage = teamCreationObj.getTeams(gameId);
	        dataOutputStream.writeUTF(clientMessage);
	      	dataOutputStream.flush();
	          
    }
 }



