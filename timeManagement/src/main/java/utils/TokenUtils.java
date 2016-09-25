package utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class TokenUtils {

	  private static SecureRandom random = new SecureRandom();

	public static String getToken(){		
	    return new BigInteger(130, random).toString(32);
	}
	
	
}
