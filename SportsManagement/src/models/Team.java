package models;
import java.util.*;

public class Team {
	public int id;
	public String name;
	public int gameType;
	public List<Player> playerList;
	
	public Team(int id, String name, int gameType, List<Player> playerList) {
		super();
		this.id = id;
		this.name = name;
		this.gameType = gameType;
		this.playerList = playerList;
	}
	
}
