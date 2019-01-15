package popup.hw1;

import java.util.*;

public class JCardTrick
{
    public static void main(String[] args)
    {
        final Scanner scanner = new Scanner(System.in);

        final int t = scanner.nextInt();

        for(int i = 0; i < t; i++)
        {
            final int n = scanner.nextInt();

            final Deque<Integer> queue = new ArrayDeque<>();
            for(int k = n; k > 0; k--)
            {
                queue.add(k);
                for(int j = 0; j < k; j++)
                    queue.add(queue.poll());
            }

            boolean first = true;
            while(!queue.isEmpty())
            {
                if(!first)
                    System.out.print(" ");
                System.out.print(queue.removeLast());
                first = false;
            }
            System.out.println();
        }

        scanner.close();
    }
}
