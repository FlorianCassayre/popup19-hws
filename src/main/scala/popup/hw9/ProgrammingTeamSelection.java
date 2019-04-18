package popup.hw9;

import java.util.*;

public class ProgrammingTeamSelection {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        while(true) {
            final int m = Integer.parseInt(scanner.nextLine());

            if(m == 0)
                break;

            final Map<String, Integer> map = new HashMap<>();
            final Integer[][] pairs = new Integer[m][2];
            for(int i = 0; i < m; i++) {
                final String[] names = scanner.nextLine().split(" ");
                for(int j = 0; j < names.length; j++)
                    pairs[i][j] = getId(names[j], map);
            }

            final int n = map.size();

            final boolean[][] matrix = new boolean[n][n];
            for(int i = 0; i < m; i++) {
                final int a = pairs[i][0], b = pairs[i][1];
                matrix[Math.min(a, b)][Math.max(a, b)] = true;
            }

            System.out.println(isPossible(matrix) ? "possible" : "impossible");
        }


        scanner.close();
    }

    private static boolean isPossible(boolean[][] matrix) {
        final int n = matrix.length;
        if(n % 3 != 0)
            return false;

        final List<Triple> triples = new ArrayList<>();
        for(int i = 0; i < n; i++)
            for(int j = i + 1; j < n; j++)
                for(int k = j + 1; k < n; k++)
                    if(matrix[i][j] && matrix[j][k] && matrix[i][k])
                        triples.add(new Triple(i, j, k));
        final BitSet done = new BitSet(n);

        return isPossible2(triples, done, n, n / 3);
    }

    private static boolean isPossible2(List<Triple> triples, BitSet done, int n, int goal) {
        int count = 0;

        while(count < goal) {
            final int[] counts = new int[n];
            for(Triple triple : triples) {
                counts[triple.a]++;
                counts[triple.b]++;
                counts[triple.c]++;
            }
            final Set<Integer> alone = new HashSet<>();
            for(int i = 0; i < n; i++)
                if(!done.get(i))
                    if(counts[i] == 0)
                        return false;
                    else if(counts[i] == 1)
                        alone.add(i);
            final Iterator<Triple> it = triples.iterator();
            boolean removed = false;
            while(it.hasNext()) {
                final Triple triple = it.next();
                final boolean overlapping = done.get(triple.a) || done.get(triple.b) || done.get(triple.c);
                if(alone.contains(triple.a) || alone.contains(triple.b) || alone.contains(triple.c)) {
                    if(overlapping)
                        return false;
                    done.set(triple.a);
                    done.set(triple.b);
                    done.set(triple.c);
                    count++;
                    it.remove();
                    removed = true;
                } else if(overlapping) {
                    it.remove();
                    removed = true;
                }
            }

            if(!removed) { // Well, fuck
                int index = -1; // Heuristic
                for(int i = 0; i < n; i++)
                    if(!done.get(i) && (index == -1 || counts[i] < counts[index]))
                        index = i;

                for(int i = 0; i < triples.size(); i++) {
                    final Triple triple = triples.get(i);
                    if(triple.a == index || triple.b == index || triple.c == index) {
                        final List<Triple> copy = new ArrayList<>(triples);
                        copy.remove(i);
                        final BitSet newDone = (BitSet) done.clone();
                        newDone.set(triple.a);
                        newDone.set(triple.b);
                        newDone.set(triple.c);
                        if(isPossible2(copy, newDone, n, goal - 1))
                            return true;
                    }
                }
                return false;
            }
        }

        return true;
    }

    private static int getId(String name, Map<String, Integer> map) {
        if(map.containsKey(name))
            return map.get(name);
        map.put(name, map.size());
        return map.size() - 1;
    }

    private static final class Triple {
        public final int a, b, c;

        private Triple(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
