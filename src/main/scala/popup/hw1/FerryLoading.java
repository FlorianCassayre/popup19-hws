package popup.hw1;

import java.util.*;

public class FerryLoading
{
    public static void main(String[] args)
    {
        final Scanner scanner = new Scanner(System.in);
        final int c = Integer.parseInt(scanner.nextLine());
        for(int i = 0; i < c; i++)
        {
            final String[] params = scanner.nextLine().split(" ");
            final int n = Integer.parseInt(params[0]), t = Integer.parseInt(params[1]), m = Integer.parseInt(params[2]);
            final Deque<Car> left = new ArrayDeque<>(), right = new ArrayDeque<>();
            for(int j = 0; j < m; j++)
            {
                final String[] carpar = scanner.nextLine().split(" ");
                final int time = Integer.parseInt(carpar[0]);
                final String directionString = carpar[1];
                final boolean direction;
                if(directionString.equals("left"))
                {
                    direction = false;
                }
                else if(directionString.equals("right"))
                {
                    direction = true;
                }
                else
                    throw new IllegalStateException();
                final Car car = new Car(j, time, direction);
                if(direction) right.add(car); else left.add(car);
            }

            // --

            boolean position = false;
            int time = 0;
            final Queue<Car> traveling = new ArrayDeque<>(n);
            final int[] arrivals = new int[m];
            while(!left.isEmpty() || !right.isEmpty() || !traveling.isEmpty())
            {
                while(!traveling.isEmpty())
                {
                    arrivals[traveling.poll().id] = time;
                }

                final Deque<Car> that = position ? right : left, other = position ? left : right;

                int removed = 0;
                while(!that.isEmpty() && that.peek().arrivalTime <= time && removed < n)
                {
                    traveling.add(that.poll());
                    removed++;
                }

                if(removed == 0) // No cars on this side
                {
                    if(!other.isEmpty() && other.peek().arrivalTime <= time) // Cars on the other side
                    {
                        position = !position;
                        time += t;
                    }
                    else // Wait until a car arrives (jump in time)
                    {
                        if(!that.isEmpty())
                            time = that.peek().arrivalTime;
                        if(!other.isEmpty())
                            time = Math.min(time, other.peek().arrivalTime);
                    }
                }
                else
                {
                    position = !position;
                    time += t;
                }

            }

            for(int j = 0; j < m; j++)
            {
                System.out.println(arrivals[j]);
            }

            if(i < c - 1)
                System.out.println();
        }

        scanner.close();
    }

    private static class Car
    {
        public final int id, arrivalTime;
        public final boolean side;
        public Car(int id, int arrivalTime, boolean side)
        {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.side = side;
        }
    }
}
