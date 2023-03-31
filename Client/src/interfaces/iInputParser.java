package interfaces;

import java.util.regex.Matcher;

public interface iInputParser {
	public Matcher isValidFormat(String command);
	public String[] getFileLocations(String command);
}
