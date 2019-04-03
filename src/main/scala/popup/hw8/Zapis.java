package popup.hw8;

import java.util.*;

public class Zapis {
    private static final long MOD = 1_000_000, MOD_DISP = 100_000;
    private static final String OPENING = "([{", CLOSING = ")]}";
    private static final Map<Character, Character> OPPOSITE = Collections.unmodifiableMap(new HashMap<Character, Character>() {{
        for(int i = 0; i < OPENING.length(); i++)
            put(OPENING.charAt(i), CLOSING.charAt(i));
    }});
    private static final char JOKER = '?';


    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = Integer.parseInt(scanner.nextLine());
        final String string = scanner.nextLine();

        final Long[][] cache = new Long[n][n + 1];

        final long result = recurrence(0, n, string, cache);

        if(result % MOD_DISP == result) {
            System.out.println(result);
        } else {
            final String str = String.valueOf(result);
            System.out.println(str.substring(1));
        }

        scanner.close();
    }

    private static long recurrence(int from, int until, String string, Long[][] cache) {

        if(until < from)
            return 0;
        if(from == until)
            return 1;

        if(cache[from][until - 1] != null)
            return cache[from][until - 1];

        final long value;

        if(CLOSING.indexOf(string.charAt(from)) == -1) {
            final Character opposite = OPPOSITE.get(string.charAt(from));

            long sum = 0;

            for(int i = from + 1; i < until; i++) {
                if(string.charAt(i) == JOKER || (CLOSING.indexOf(string.charAt(i)) != -1 && (string.charAt(from) == JOKER || string.charAt(i) == opposite))) {
                    final int p = string.charAt(from) == JOKER && string.charAt(i) == JOKER ? 3 : 1;
                    sum = (sum + p * recurrence(from + 1, i, string, cache) * recurrence(i + 1, until, string, cache)) % MOD;
                }
            }

            value = sum;
        } else { // Unbalanced
            value = 0;
        }

        cache[from][until - 1] = value;

        return value;
    }
}
