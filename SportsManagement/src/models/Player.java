package models;

public class Player {
	public int playerId;
	public String name;
	
	public Player() {
		super();
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Player(int playerId, String name) {
		super();
		this.playerId = playerId;
		this.name = name;
	}
	
}
