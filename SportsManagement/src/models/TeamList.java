package models;
import java.util.*;

public class TeamList {
	public List<Team> teams;
	public int total;
	public List<Player> additionalPlayers;
	
	public TeamList() {
		super();
	}
	public List<Team> getTeams() {
		return teams;
	}
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public List<Player> getAdditionalPlayers() {
		return additionalPlayers;
	}
	public void setAdditionalPlayers(List<Player> additionalPlayers) {
		this.additionalPlayers = additionalPlayers;
	}
	public TeamList(List<Team> teams, int total, List<Player> additionalPlayers) {
		super();
		this.teams = teams;
		this.total = total;
		this.additionalPlayers = additionalPlayers;
	}
//	public TeamList(List<Team> teams, int total) {
//		super();
//		this.teams = teams;
//		this.total = total;
//	}
}
