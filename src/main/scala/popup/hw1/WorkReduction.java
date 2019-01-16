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

                int[] costs = new int[n - m + 1];
                costs[costs.length - 1] = 0;
                for(int k = 0; k < costs.length - 1; k++)
                    costs[k] = Integer.MAX_VALUE;
                for(int k = costs.length - 1; k >= 0; k--)
                {
                    final int currentCost = costs[k];
                    final int unitIndex = k - 1, halfIndex = (k + m) / 2 - m;

                    if(unitIndex >= 0)
                        costs[unitIndex] = Math.min(currentCost + priceUnit, costs[unitIndex]);
                    if(halfIndex >= 0)
                        costs[halfIndex] = Math.min(currentCost + priceHalf, costs[halfIndex]);
                }

                final int optimalCost = costs[0]; // TODO

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
            return Integer.compare(this.optimalCost, that.optimalCost);
        }
    }
}
