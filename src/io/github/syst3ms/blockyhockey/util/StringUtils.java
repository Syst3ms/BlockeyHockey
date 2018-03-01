package io.github.syst3ms.blockyhockey.util;

public class StringUtils {

	public static String ordinal(int num) {
		if (num > 10 && num < 14) {
			return num + "th";
		}
		int mod = num % 10;
		switch (mod) {
			case 1:
				return num + "st";
			case 2:
				return num + "nd";
			case 3:
				return num + "rd";
			default:
				return num + "th";
		}
	}
}
