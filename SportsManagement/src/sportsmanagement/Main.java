package sportsmanagement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dbconnection.DBConnection;
import models.Game;
import models.Team;
import models.TeamList;
import utility.TeamCreation;

public class Main{

	public static void main(String[] args) throws  IOException{
		Main mainObj = new Main();
		mainObj.start();
	}
	
	public void start() throws StreamWriteException, DatabindException, IOException {
		
		Scanner sc = new Scanner(System.in);
		int choice;
		
		TeamCreation teamCreationObj = new TeamCreation();
		ObjectMapper mapper = new ObjectMapper();
		Game game = teamCreationObj.mapJsontoClass(mapper);		
		
		do {
			System.out.println("Enter option:\n 1. Write team list to Json\n 2. Save team list to database\n 3. Get Teams");
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
			default : System.out.println("default");
			}
		}while(choice<4);
		
	}
	
	

}
