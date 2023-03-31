package sportsmanagement;

import java.util.ArrayList;
import java.util.List;

import interfaces.iTeamCreator;
import models.Game;
import models.Player;
import models.Team;
import models.TeamList;

public class TeamCreator implements iTeamCreator {
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
}
