package popup.hw4;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdventureMoving {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int tankVolume = 200;
        final int tankHalfVolume = tankVolume / 2;

        final int distance = scanner.nextInt();
        final List<Station> stations = new ArrayList<>();
        stations.add(new Station(0, 10000000));
        while(scanner.hasNext()) {
            stations.add(new Station(scanner.nextInt(), scanner.nextInt()));
        }
        stations.add(new Station(distance, 10000000));

        // --

        final int[][] costs = new int[stations.size()][tankVolume + 1]; // [station][tank]

        for(int i = 0; i <= tankVolume; i++) { // Base case
            costs[0][i] = Integer.MAX_VALUE;
        }
        costs[0][tankHalfVolume] = 0;

        // Bottom up filling
        for(int i = 1; i < stations.size(); i++) {
            final Station station = stations.get(i);
            for(int j = 0; j <= tankVolume; j++) {
                int min = Integer.MAX_VALUE;

                for(int k = i - 1; k >= 0; k--) {
                    final Station before = stations.get(k);
                    final int difference = station.distance - before.distance;
                    if(j > tankVolume - difference)
                        break;

                    final int cost = costs[k][j + difference];
                    if(cost != Integer.MAX_VALUE) {
                        min = Math.min(cost, min);
                    }
                }

                for(int k = 0; k < j; k++) {
                    final int cost = costs[i][k];
                    if(cost != Integer.MAX_VALUE)
                        min = Math.min(cost + station.price * (j - k), min);
                }

                costs[i][j] = min;
            }
        }
        
        int min = Integer.MAX_VALUE;
        for(int i = tankHalfVolume; i <= tankVolume; i++) {
            min = Math.min(costs[stations.size() - 1][i], min);
        }

        if(min < Integer.MAX_VALUE) { // Solution exists
            System.out.println(min);
        } else { // Impossible
            System.out.println("Impossible");
        }

        scanner.close();
    }

    private static final class Station {
        public final int distance, price;

        private Station(int distance, int price) {
            this.distance = distance;
            this.price = price;
        }
    }
}
