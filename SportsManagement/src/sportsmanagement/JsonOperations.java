package sportsmanagement;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import interfaces.iJsonOperations;
import models.Game;

public class JsonOperations implements iJsonOperations {
	TeamCreator teamCreator = new TeamCreator();
	@Override
	public Game mapJsontoClass(ObjectMapper mapper) throws IOException {
		File jsonFile = new File("C:\\Users\\spoorthi.s.bhat\\Desktop\\Learn and code\\TeamsInputJSON.json");
		Game game = mapper.readValue(jsonFile,Game.class);
		return game;
	}
	
	@Override
	public void writeOutputtoJson(ObjectMapper mapper, Game game, String outputFileLocation) throws StreamWriteException, DatabindException, IOException {
		File outputFile = new File(outputFileLocation);
		mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, teamCreator.createTeams(game));
	}

}
