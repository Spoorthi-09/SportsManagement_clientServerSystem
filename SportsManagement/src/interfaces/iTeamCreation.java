package interfaces;

import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.PlayerDto;
import dto.TeamDto;
import dto.TeamPlayerDto;
import models.Game;
import models.TeamList;

public interface iTeamCreation {
	public Game mapJsontoClass(ObjectMapper mapper) throws IOException;
	public void writetoJson(ObjectMapper mapper, Game game) throws StreamWriteException, DatabindException, IOException;
	public TeamList createTeams(Game game);
	public void saveTeam(TeamList teamList);
	public void insertTeamdb(TeamDto teamdto);
	public void insertPlayerdb(PlayerDto playerdto);
	public void insertTeamPlayerdb(TeamPlayerDto teamPlayerdto);
	public void getTeams(int gameId);
	public void serverConnect(ObjectMapper mapper) throws IOException, ClassNotFoundException;
}
