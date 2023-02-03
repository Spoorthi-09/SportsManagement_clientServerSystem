package models;
import java.util.*;

public class TeamList {
	public List<Team> teams;
	public int total;
	
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
	public TeamList(List<Team> teams, int total) {
		super();
		this.teams = teams;
		this.total = total;
	}
}
