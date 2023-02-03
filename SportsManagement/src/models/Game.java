package models;
import java.util.*;
public class Game {
	public int gameType;
	public List<Player> players;
	
	public Game() {
		super();
	}
	public int getGameType() {
		return gameType;
	}
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public Game(int gameType, List<Player> playerList) {
		super();
		this.gameType = gameType;
		this.players = playerList;
	}
	
}
