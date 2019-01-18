package popup.hw1;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Homework {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final String s = scanner.nextLine(), s1 = scanner.nextLine(), s2 = scanner.nextLine();
        assert s.length() == s1.length() + s2.length();

        final Pair start = new Pair(0, 0), goal = new Pair(s1.length(), s2.length());
        final Set<Pair> seqs = new HashSet<>(), temp = new HashSet<>();
        seqs.add(start);

        int i = 0;
        while(i < s.length() && !seqs.isEmpty()) {
            temp.clear();

            for(Pair p : seqs) {
                if(p.x < s1.length() && s1.charAt(p.x) == s.charAt(i))
                    temp.add(new Pair(p.x + 1, p.y));
                if(p.y < s2.length() && s2.charAt(p.y) == s.charAt(i))
                    temp.add(new Pair(p.x, p.y + 1));
            }

            i++;

            seqs.clear();
            seqs.addAll(temp);
        }

        if(seqs.contains(goal))
            System.out.println("yes");
        else
            System.out.println("no");

        scanner.close();
    }

    private static class Pair {
        public final int x, y;
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;
            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
