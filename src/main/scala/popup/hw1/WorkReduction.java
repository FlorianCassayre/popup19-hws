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

                int bestCost = Integer.MAX_VALUE;
                final Map<Integer, Integer> costs = new HashMap<>(), temp = new HashMap<>();
                costs.put(n, 0);
                while(!costs.isEmpty())
                {
                    for(Map.Entry<Integer, Integer> entry : costs.entrySet())
                    {
                        if(entry.getKey() > m) // Branch
                        {
                            temp.merge(entry.getKey() - 1, entry.getValue() + priceUnit, Integer::min);
                            temp.merge(entry.getKey() / 2, entry.getValue() + priceHalf, Integer::min);
                        }
                        else if(entry.getKey() == m) // Goal achieved
                        {
                            bestCost = Math.min(entry.getValue(), bestCost);
                        } // Otherwise ignore
                    }

                    costs.clear();
                    costs.putAll(temp);
                    temp.clear();
                }

                final int optimalCost = bestCost;

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
