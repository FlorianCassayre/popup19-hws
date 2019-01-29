package popup.hw2;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class EngineeringEnglish {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final Set<String> set = new HashSet<>();

        while(scanner.hasNext()) {
            final String[] line = scanner.nextLine().split(" ");
            for(int i = 0; i < line.length; i++) {
                final String word = line[i], lower = word.toLowerCase();
                if(set.contains(lower)) {
                    System.out.print(".");
                } else {
                    set.add(lower);
                    System.out.print(word);
                }

                if(i < line.length - 1)
                    System.out.print(" ");
                else
                    System.out.println();
            }
        }

        scanner.close();
    }
}
