package popup.hw1;

import java.util.*;

public class WorkReduction
{
    public static void main(String[] args)
    {
        final Scanner scanner = new Scanner(System.in);
        final int c = Integer.parseInt(scanner.nextLine());
        for(int i = 1; i <= c; i++)
        {
            final String[] line = scanner.nextLine().split(" ");
            final int n = Integer.parseInt(line[0]), m = Integer.parseInt(line[1]), l = Integer.parseInt(line[2]);

            final List<Agency> agencies = new ArrayList<>(l);
            for(int j = 0; j < l; j++)
            {
                final String[] specs = scanner.nextLine().split(":");
                final String name = specs[0];
                final String[] prices = specs[1].split(",");
                final int priceUnit = Integer.parseInt(prices[0]), priceHalf = Integer.parseInt(prices[1]);

                // --

                // Should work on most cases, if not all, but is very efficient (in fact it is logarithmic in the
                // worst-case scenario, and tends to be constant in practice) compared to the previous dynamic programming
                // approach that was linear.

                int work = n, cost = 0;
                while(work > m)
                {
                    final int halved = work / 2;
                    if(halved >= m && priceUnit * (work - halved) >= priceHalf) // Still cheaper (and possible) to halve
                    {
                        cost += priceHalf;
                        work = halved;
                    }
                    else // Otherwise reach the goal with unit steps (can be computed directly)
                    {
                        cost += priceUnit * (work - m);
                        work = m;
                    }
                }

                final int optimalCost = cost;

                // --

                agencies.add(new Agency(name, optimalCost));
            }

            Collections.sort(agencies);

            System.out.println("Case " + i);
            for(Agency agency : agencies)
            {
                System.out.println(agency.name + " " + agency.optimalCost);
            }
        }

        scanner.close();
    }

    private static class Agency implements Comparable<Agency>
    {
        public final String name;
        public final int optimalCost;

        private Agency(String name, int optimalCost)
        {
            this.name = name;
            this.optimalCost = optimalCost;
        }

        @Override
        public int compareTo(Agency that)
        {
            if(this.optimalCost != that.optimalCost)
                return Integer.compare(this.optimalCost, that.optimalCost);
            else
                return this.name.compareTo(that.name);
        }
    }
}
