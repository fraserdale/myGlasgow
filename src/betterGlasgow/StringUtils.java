package betterGlasgow;

public class StringUtils {
	public static String center(String s, int size) {
		return center(s, size, ' ');
	}

	public static String center(String s, int size, char pad) {
		if (s == null || size <= s.length())
			return s;

		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < (size - s.length()) / 2; i++) {
			sb.append(pad);
		}
		sb.append(s);
		while (sb.length() < size) {
			sb.append(pad);
		}
		return sb.toString();
	}

	public static void println(String s, int n) {
		if (n > 0) {
			System.out.print(s);
			println(s, n - 1);
		} else if (n == 0) {
			System.out.print("\n");
		}
	}
}
