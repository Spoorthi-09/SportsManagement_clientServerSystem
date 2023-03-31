package implementations;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import interfaces.iInputParser;

public class InputParser implements iInputParser {
	public Matcher isValidFormat(String command) {
        String pattern = "-a (\\S+) -i (\\S+) -o (\\S+)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(command);
        return matcher;
    }
	
	public String[] getFileLocations(String command) {
		Matcher matcher = isValidFormat(command);
		if(matcher.find()) {
			String inputFile = matcher.group(2);
			String outputFile = matcher.group(3);
		    return new String[] {inputFile, outputFile};
		}else {
			System.out.println("Please use the format : -a <action> -i <input-file-location> -o <output-file-location>");
		}
		return null;
	}
}
