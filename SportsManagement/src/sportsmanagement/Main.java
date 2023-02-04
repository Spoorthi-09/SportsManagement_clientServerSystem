package sportsmanagement;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Game;

public class Main{

	public static void main(String[] args) throws  IOException, ClassNotFoundException{
		Main mainObj = new Main();
		mainObj.start();
	}
	
	public void start() throws StreamWriteException, DatabindException, IOException, ClassNotFoundException {
		
		Scanner sc = new Scanner(System.in);
		int choice;
		
		TeamCreation teamCreationObj = new TeamCreation();
		ObjectMapper mapper = new ObjectMapper();
		Game game = teamCreationObj.mapJsontoClass(mapper);		
		
		do {
			System.out.println("Enter option:\r\n"
					+ " 1. Write team list to Json\r\n"
					+ " 2. Save team list to database\r\n"
					+ " 3. Get Teams\r\n"
					+ " 4. Get input file from client and generate output file");
			choice = sc.nextInt();
			
			switch(choice) {
			case 1: teamCreationObj.writetoJson(mapper, game);
					break;
			case 2: teamCreationObj.saveTeam(teamCreationObj.createTeams(game));
					break;
			case 3: System.out.println("Enter game ID");
					int gameId = sc.nextInt();
					teamCreationObj.getTeams(gameId);
					break;
			case 4: teamCreationObj.serverConnect(mapper);
					break;
			default : System.out.println("default");
			}
		}while(choice<5);
		
		sc.close();
		
	}
}
