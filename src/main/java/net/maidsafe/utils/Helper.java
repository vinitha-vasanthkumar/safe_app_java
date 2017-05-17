package net.maidsafe.utils;

public class Helper {

	public static boolean isMockEnvironment() {
		return System.getenv("MOCK_ENV") != null;
	}

}
