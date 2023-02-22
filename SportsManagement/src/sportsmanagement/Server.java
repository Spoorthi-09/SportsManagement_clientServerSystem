package sportsmanagement;

import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) throws IOException{
	    serverConnection();
	  }
	public static void serverConnection() throws IOException {
		ServerSocket serverSocket = new ServerSocket(9000);
	    System.out.println("Server started on port 9000");
	    
	    while (true) {
	      Socket clientSocket = serverSocket.accept();
	      System.out.println("Client connected");
	      new Thread(new ClientHandler(clientSocket)).start();
	    }
	}
}
