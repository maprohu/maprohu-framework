package hu.mapro.gwtui.shared.access;

import org.springframework.security.access.AccessDeniedException;

public class Access {

	public static void check(boolean condition) {
		check(condition, "Access Denied");
	}
	
	public static void check(boolean condition, String msg) {
		if (!condition) {
			throw new AccessDeniedException(msg);
		}
	}
	
}
