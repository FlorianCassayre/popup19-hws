package popup.hw1;

import java.util.Scanner;

public class CheeringGame
{
    public static void main(String[] args)
    {
        final Scanner scanner = new Scanner(System.in);

        final String[] line = scanner.nextLine().split(" ");
        final int n = Integer.parseInt(line[0]), t = Integer.parseInt(line[1]), m = Integer.parseInt(line[2]);

        final int gameLength = 90, consecutive = 5;

        for(int i = 0; i < m; i++)
        {
            final String[] two = scanner.nextLine().split(" ");
            final int a = Integer.parseInt(two[0]), b = Integer.parseInt(two[1]);


        }

        scanner.close();
    }
}
