package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//this class holds a util mthods to help the mainworker classes
public class Util {

	private static Pattern pattern;
	private static Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	// check if the string is email
	public static boolean isEmail(String input) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(input);
		return matcher.matches();
	}
}
