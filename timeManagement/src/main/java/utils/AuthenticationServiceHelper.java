package utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.User;

public class AuthenticationServiceHelper {

	public static Map<String, User> tokens = new HashMap<>();
	public static List<User> loggedUsers = new ArrayList<>();
	private static SecureRandom random = new SecureRandom();

	public static String getToken() {
		return new BigInteger(130, random).toString(32);
	}
	
	
}
