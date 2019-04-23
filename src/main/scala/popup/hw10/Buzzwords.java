package popup.hw10;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Buzzwords {
    private static final int M = 1_000_003, B = 29;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        while(true) {
            final String string = scanner.nextLine().replace(" ", "");

            if(string.isEmpty())
                break;

            final int[] values = new int[string.length()];
            for(int i = 0; i < string.length(); i++)
                values[i] = string.charAt(i) - 'A';

            for(int l = 1; l < string.length(); l++) {
                final Map<Wrapper, Integer> map = new HashMap<>();

                long bl = 1;
                for(int i = 0; i < l; i++)
                    bl = (bl * B) % M;

                Wrapper current = new Wrapper(values, l);
                for(int i = 1; i <= string.length() - l + 1; i++) {
                    map.merge(current, 1, (a, b) -> a + b);

                    if(i <= string.length() - l) { // Next
                        current = new Wrapper(values, i, l, current.hash, (int) bl);
                    }
                }

                final int max = map.values().stream().max(Integer::compareTo).get();

                if(max > 1) {
                    System.out.println(max);
                } else
                    break; // Stop
            }

            System.out.println();
        }

        scanner.close();
    }

    private static final class Wrapper {
        private final int[] s;
        private final int i, len;
        private final int hash;

        private Wrapper(int[] s, int i, int len, int previous, int bl) {
            this.s = s;
            this.i = i;
            this.len = len;

            this.hash = (int) (((((long) previous * B - s[i - 1] * bl + s[i + len - 1]) % M) + M) % M);
        }

        private Wrapper(int[] s, int len) {
            this.s = s;
            this.i = 0;
            this.len = len;

            int bl = 1;
            long h = 0;
            for(int j = 0; j < len; j++) {
                h = (h + s[len - 1 - j] * bl) % M;
                bl = (bl * B) % M;
            }

            this.hash = (int) h;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;
            Wrapper wrapper = (Wrapper) o;
            for(int j = 0; j < len; j++) {
                if(s[i + j] != s[wrapper.i + j])
                    return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
