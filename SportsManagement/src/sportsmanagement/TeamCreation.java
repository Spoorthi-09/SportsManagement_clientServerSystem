package sportsmanagement;

import java.io.*;

import java.net.*;
import java.sql.*;
import java.util.*;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dbconnection.DBConnection;
import dto.PlayerDto;
import dto.TeamDto;
import dto.TeamPlayerDto;
import interfaces.iTeamCreation;
import models.Game;
import models.Player;
import models.Team;
import models.TeamList;

public class TeamCreation implements iTeamCreation{
	
	Connection con = DBConnection.createDBConnection();
	int execution=0;
	
	@Override
	public Game mapJsontoClass(ObjectMapper mapper) throws IOException {
		File jsonFile = new File("C:\\Users\\spoorthi.s.bhat\\Desktop\\Learn and code\\TeamsInputJSON.json");
		Game game = mapper.readValue(jsonFile,Game.class);
		return game;
	}
	
	@Override
	public void writeOutputtoJson(ObjectMapper mapper, Game game, String outputFileLocation) throws StreamWriteException, DatabindException, IOException {
		File outputFile = new File(outputFileLocation);
		mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, createTeams(game));
	}
	
	public String checkInputGame(Game game) {
		int gameType = game.gameType;
	    List<Player> players = game.players;
	    
		int numberOfPlayers=0;
		int participants=game.players.size();

		if(game.gameType == 1) {
			numberOfPlayers = 11;
		}else if(game.gameType == 2) {
			numberOfPlayers = 2;
		}else if(game.gameType == 3) {
			numberOfPlayers = 1;
		}		
	    String clientOutput = "Success: teams can be created";

	    if(participants < numberOfPlayers) {
	    	clientOutput = "Error : Not enough players to form a team";
	    	return clientOutput;
	    }
	    
	 // Check if any player ids are present for the same game type in team and team-player table
	    for (Player player : players) {
	        int playerId = player.playerId;
	        String playerName = player.name;

	        if (playerId == 0 || playerName == null || playerName.isEmpty()) {
	        	clientOutput = "Error: Player id or name cannot be null or empty.";
	            System.out.println(clientOutput);
	            return clientOutput;
	        }

	        if (checkPlayerExistsForGame(playerId, gameType)) {
	        	clientOutput = "Error: Player with id " + playerId + " and name " + playerName 
		                + " already exists in the database for game type " + gameType + ".";
	            System.out.println(clientOutput);
	            return clientOutput;
	        }
	    }

	    // If all checks pass, continue with creating teams and saving to the database
	    System.out.println(clientOutput);
	    return clientOutput;
	    
	}
	
	private boolean checkPlayerExistsForGame(int playerId, int gameType) {
	    boolean exists = false;

	    // Check if player exists in team_player table for the given game type
	    String query = "SELECT COUNT(*) FROM team_player tp INNER JOIN team t ON tp.teamId = t.teamId " 
	        + "WHERE tp.playerId = ? AND t.gameId = ?";
	    try (PreparedStatement stmt = con.prepareStatement(query)) {
	        stmt.setInt(1, playerId);
	        stmt.setInt(2, gameType);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            exists = true;
	        }
	    } catch (SQLException e) {
	        System.out.println("Error checking if player exists in team_player table: " + e.getMessage());
	    }

	    if (exists) {
	        return true;
	    }

	    // Check if player exists in team table for the given game type
	    query = "SELECT COUNT(*) FROM team WHERE id IN " 
	        + "(SELECT DISTINCT teamId FROM team_player WHERE playerId = ?) AND gameId = ?";
	    try (PreparedStatement stmt = con.prepareStatement(query)) {
	        stmt.setInt(1, playerId);
	        stmt.setInt(2, gameType);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            exists = true;
	        }
	    } catch (SQLException e) {
	        System.out.println("Error checking if player exists in team table: " + e.getMessage());
	    }

	    return exists;
	}
	
	@Override
	public TeamList createTeams(Game game) {
		int numberOfPlayers=0;
		int participants=game.players.size();
		int total =0;
		int teamId =0;
		List<Team> excludedTotalTeamList = new ArrayList<>();
		List<Player> additionalPlayers = new ArrayList<>();
		
		if(game.gameType == 1) {
			numberOfPlayers = 11;
		}else if(game.gameType == 2) {
			numberOfPlayers = 2;
		}else if(game.gameType == 3) {
			numberOfPlayers = 1;
		}
				
		int numAdditionalPlayers = participants % numberOfPlayers;
	    if (numAdditionalPlayers > 0) {
	        additionalPlayers = game.players.subList(participants - numAdditionalPlayers, participants);
	        participants -= numAdditionalPlayers;
	    }
	    
		for(int i=0;i<participants;i+=numberOfPlayers) {
			int endIndex = i + numberOfPlayers;
			endIndex = endIndex < game.players.size() ? endIndex : game.players.size();
			total+=1;
			teamId = 100 * total*game.gameType;
			excludedTotalTeamList.add(new Team(teamId,"Team - "+teamId,game.gameType,game.players.subList(i, endIndex)));
		}

		TeamList teamList = new TeamList(excludedTotalTeamList,total,additionalPlayers);
		return teamList;
	}
	
@Override
	public void saveTeam(TeamList teamList) {
		for(Team team : teamList.teams) {
			TeamDto teamdto = new TeamDto();
			teamdto.id = team.id;
			teamdto.name = team.name;
			teamdto.gameId = team.gameType;
			
			insertTeamdb(teamdto);
			
			if(execution != 0) {
				for(Player player : team.playerList) {
					PlayerDto playerdto = new PlayerDto();
					playerdto.id = player.playerId;
					playerdto.name = player.name;
					
					insertPlayerdb(playerdto);
					
					TeamPlayerDto teamPlayerDto = new TeamPlayerDto();
					teamPlayerDto.playerId = player.playerId;
					teamPlayerDto.teamId = team.id;
					
					insertTeamPlayerdb(teamPlayerDto);
				}
			}
		}
		if(execution != 0) {
			System.out.println("Teams uploaded successfully");
		}else {
			System.out.println("There is an error uploading teams");
		}
	}

	@Override
	public void insertTeamdb(TeamDto teamdto) {
		String query = "insert into team(teamId,name,gameId) values(?,?,?)";
		try {
			PreparedStatement prepstmt = con.prepareStatement(query);
			prepstmt.setInt(1, teamdto.id);
			prepstmt.setString(2, teamdto.name);
			prepstmt.setInt(3, teamdto.gameId);
			
			execution = prepstmt.executeUpdate();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void insertPlayerdb(PlayerDto playerdto) {
		
		String query = "SELECT COUNT(*) FROM player WHERE playerId = ?";
	    try {
	        PreparedStatement prepstmt = con.prepareStatement(query);
	        prepstmt.setInt(1, playerdto.id);
	        ResultSet rs = prepstmt.executeQuery();

	        if (rs.next() && rs.getInt(1) > 0) {
	            System.out.println("Player with ID " + playerdto.id + " already exists in the database.");
	        } else {
	            query = "INSERT INTO player(playerId, name) VALUES (?, ?)";
	            prepstmt = con.prepareStatement(query);
	            prepstmt.setInt(1, playerdto.id);
	            prepstmt.setString(2, playerdto.name);

	            execution = prepstmt.executeUpdate();
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
		
//		String query = "insert into player(playerId,name) values(?,?)";
//		try {
//			PreparedStatement prepstmt = con.prepareStatement(query);
//			prepstmt.setInt(1, playerdto.id);
//			prepstmt.setString(2, playerdto.name);
//
//			execution = prepstmt.executeUpdate();	
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
	}

	@Override
	public void insertTeamPlayerdb(TeamPlayerDto teamPlayerdto) {
		String query = "insert into team_player(playerId, teamId) values(?,?)";
		
		try {
			PreparedStatement prepstmt = con.prepareStatement(query);
			prepstmt.setInt(1,teamPlayerdto.playerId);
			prepstmt.setInt(2,teamPlayerdto.teamId);
			
			execution = prepstmt.executeUpdate();	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	@Override
	public String getTeams(int gameId) {
		String clientMessage = "No teams for the game";
		
		String query = "SELECT sportsmanagement.team.name AS teamName\r\n"
				+ "	,sportsmanagement.player.name AS playerName\r\n"
				+ "	,sportsmanagement.game.gameType\r\n"
				+ "FROM sportsmanagement.team_player\r\n"
				+ "INNER JOIN sportsmanagement.team ON sportsmanagement.team_player.teamId = sportsmanagement.team.teamId\r\n"
				+ "INNER JOIN sportsmanagement.player ON sportsmanagement.team_player.playerId = sportsmanagement.player.playerId\r\n"
				+ "INNER JOIN sportsmanagement.game ON sportsmanagement.team.gameId = sportsmanagement.game.id\r\n"
				+ "WHERE sportsmanagement.team.gameId = "+gameId;
				
		try {
			PreparedStatement prepstmt = con.prepareStatement(query);
			ResultSet resultSet = prepstmt.executeQuery(query);
			clientMessage = "TeamName\tPlayerName\tGameType";
			System.out.println("TeamName\tPlayerName\tGameType");
			
			while(resultSet.next()) {
				clientMessage = clientMessage.concat("\n"+resultSet.getString(1)+"\t"+resultSet.getString(2)+"\t\t"+resultSet.getString(3));
				System.out.print(resultSet.getString(1));
				System.out.print("\t"+resultSet.getString(2));
				System.out.print("\t\t"+resultSet.getString(3));
				System.out.println();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return clientMessage;
	}	
}
