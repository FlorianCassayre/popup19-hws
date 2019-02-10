package popup.hw3;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Cantor {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        String input;
        while(!(input = scanner.next()).equals("END")) {
            final String[] split = input.split("\\.");

            final String integer = split[0], decimals;
            if(split.length == 1)
                decimals = "0";
            else
                decimals = split[1];

            System.out.println(isMember(integer, decimals) ? "MEMBER" : "NON-MEMBER");
        }

        scanner.close();
    }

    private static boolean isMember(String integer, String decimals) {

        if(integer.equals("1") || (integer.equals("0") && Integer.parseInt(decimals) == 0)) { // 0.0 and 1.0
            return true;
        }

        final String[] base3 = toBase3(decimals);
        final String fixed3 = base3[0], repeating3 = base3[1];

        final BigInteger n = new BigInteger("0" + fixed3, 3);

        if(repeating3.equals("0")) {
            return valid(fixed3) || valid(n.subtract(BigInteger.ONE).toString(3));
        } else if(repeating3.equals("2")) {
            return valid(fixed3) || valid(n.add(BigInteger.ONE).toString(3));
        } else {
            return valid(fixed3) && valid(repeating3);
        }
    }

    private static boolean valid(String str) {
        return !str.contains("1");
    }

    private static String[] toBase3(String string) {
        final int base = 3;
        final int divider = BigInteger.TEN.pow(string.length()).intValue();
        int current = Integer.parseInt(string) * base;
        final Set<Integer> seenSet = new HashSet<>();
        final List<Integer> seen = new ArrayList<>(), decimals = new ArrayList<>();
        int index;
        do {
            final int n = current / divider;
            final int rest = current - n * divider;
            if(seenSet.contains(rest)) {
                index = seen.indexOf(rest);
            } else {
                index = -1;
            }
            if(index == -1) {
                seen.add(rest);
                decimals.add(n);
                seenSet.add(rest);
                current = rest * base;
            }
        } while(index == -1);

        final String total = decimals.stream().map(String::valueOf).collect(Collectors.joining());

        return new String[] {total.substring(0, index), total.substring(index)};
    }
}
