package interfaces;

import dto.PlayerDto;
import dto.TeamDto;
import dto.TeamPlayerDto;
import models.Game;
import models.TeamList;

public interface iTeamPersistence {
	
	public void saveTeam(TeamList teamList);
	public void insertTeamdb(TeamDto teamdto);
	public void insertPlayerdb(PlayerDto playerdto);
	public void insertTeamPlayerdb(TeamPlayerDto teamPlayerdto);
	public String getTeams(int gameId);
	public String checkInputGame(Game game);
	public boolean checkPlayerExistsForGame(int playerId, int gameType);
}
