package interfaces;

import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Game;

public interface iJsonOperations {
	public Game mapJsontoClass(ObjectMapper mapper) throws IOException;
	public void writeOutputtoJson(ObjectMapper mapper, Game game, String outputFileLocation) throws StreamWriteException, DatabindException, IOException;
}
