package popup.hw10;

import java.util.*;

public class AnimalClassification {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = Integer.parseInt(scanner.nextLine());

        final String s1 = scanner.nextLine(), s2 = scanner.nextLine();
        final List<Integer> order1 = new ArrayList<>(2 * n - 1), order2 = new ArrayList<>(2 * n - 1);

        final int[] t1 = parse(s1, n, order1), t2 = parse(s2, n, order2);

        final Random random = new Random(42);
        final long[] dictionary = new long[n];
        for(int i = 0; i < n; i++)
            dictionary[i] = random.nextLong();

        final Set<Long> set = new HashSet<>();

        accumulate(t1, order1, n, set, dictionary);
        final int count = accumulate(t2, order2, n, set, dictionary);

        System.out.println(count);

        scanner.close();
    }

    private static int accumulate(int[] tree, List<Integer> order, int n, Set<Long> set, long[] dictionary) {
        final long[] hashes = new long[n];
        int count = 0;
        for(int i = order.size() - 1; i >= 0; i--) {
            final int last = order.get(i);
            final long hash;
            if(last < n)
                hash = dictionary[last];
            else
                hash = hashes[last - n];

            if(!set.add(hash))
                count++;

            final int next = tree[last];
            if(next != -1) // If not the root
                hashes[next - n] ^= hash;
        }

        return count;
    }

    private static int[] parse(String string, int n, List<Integer> sorted) {
        int i = 0;

        final int[] tree = new int[2 * n - 1];
        int ptr = n;
        final ArrayDeque<Integer> stack = new ArrayDeque<>(), first = new ArrayDeque<>(), second = new ArrayDeque<>();
        ArrayDeque<Boolean> order = new ArrayDeque<>();
        boolean isFirst = true;
        while(i < string.length()) {
            final char peek = string.charAt(i);

            if(peek == '(') {
                order.push(isFirst);
                isFirst = true;
                sorted.add(ptr);
                stack.push(ptr++);
                i++;
            } else if(peek == ')') {
                isFirst = order.pop();
                final int a = first.pop(), b = second.pop(), c = stack.pop();
                tree[a] = c;
                tree[b] = c;
                tree[c] = -1;
                if(isFirst)
                    first.push(c);
                else
                    second.push(c);
                i++;
            } else if(peek == ',') {
                isFirst = false;
                i++;
            } else {
                int k = 0;
                while(Character.isDigit(string.charAt(i))) {
                    k *= 10;
                    k += string.charAt(i++) - '0';
                }
                k--; // 0 indexed
                sorted.add(k);
                if(isFirst) {
                    first.push(k);
                } else {
                    second.push(k);
                }
            }
        }

        return tree;
    }
}
