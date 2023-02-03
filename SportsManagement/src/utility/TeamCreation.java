package utility;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
	public void writetoJson(ObjectMapper mapper, Game game) throws StreamWriteException, DatabindException, IOException {
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File("C:\\Users\\spoorthi.s.bhat\\Desktop\\Learn and code\\OutputJSON.json"), createTeams(game));
	}
	
	@Override
	public TeamList createTeams(Game game) {
		int numberOfPlayers=0;
		int participants=game.players.size();
		int total =0;
		List<Team> excludedTotalTeamList = new ArrayList<>();
		
		if(game.gameType == 1) {
			numberOfPlayers = 11;
		}else if(game.gameType == 2) {
			numberOfPlayers = 2;
		}else if(game.gameType == 3) {
			numberOfPlayers = 1;
		}
		
		for(int i=0;i<participants;i+=numberOfPlayers) {
			int endIndex = i + numberOfPlayers;
			endIndex = endIndex < game.players.size() ? endIndex : game.players.size();
			total+=1;
			excludedTotalTeamList.add(new Team(total,"Team - "+total,game.gameType,game.players.subList(i, endIndex)));
		}
		TeamList teamList = new TeamList(excludedTotalTeamList,total);
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
		String query = "insert into player(playerId,name) values(?,?)";
		try {
			PreparedStatement prepstmt = con.prepareStatement(query);
			prepstmt.setInt(1, playerdto.id);
			prepstmt.setString(2, playerdto.name);

			execution = prepstmt.executeUpdate();	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
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
	public void getTeams(int gameId) {
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
			System.out.println("TeamName\tPlayerName\tGameType");
			
			while(resultSet.next()) {
				System.out.print(resultSet.getString(1));
				System.out.print("\t"+resultSet.getString(2));
				System.out.print("\t\t"+resultSet.getString(3));
				System.out.println();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	
	
	
}
