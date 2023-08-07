package gr.aueb.cf.schoolapp.security;

import org.mindrot.jbcrypt.BCrypt;

public class SecUtil {
	
	private SecUtil() {}
	
	public static boolean checkPassword(String inputPasswd, String storedHashedPasswd) {
		return BCrypt.checkpw(inputPasswd, storedHashedPasswd);
	}
	
	public static String hashPassword(String inputPasswd) {
		int workload = 12;
		String salt = BCrypt.gensalt(workload);
		return BCrypt.hashpw(inputPasswd, salt);
	}
}
