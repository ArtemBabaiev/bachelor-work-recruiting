package edu.chnu.recruiting.utils.constants;

public class RegexPatterns {
	public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%_*?&])[A-Za-z\\d@$!%_*?&]{8,}$";
	public static final String USERNAME = "^[a-zA-Z0-9_]+$";
}
