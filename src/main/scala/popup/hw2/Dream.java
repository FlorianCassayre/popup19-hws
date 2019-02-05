package popup.hw2;

import java.util.*;

public class Dream {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();

        final Map<String, Integer> events = new HashMap<>();

        final List<BitSet> happened = new ArrayList<>();
        happened.add(new BitSet());

        for(int i = 0; i < n; i++) {
            final String type = scanner.next();

            switch(type.charAt(0)) {
                case 'E':
                    final String e = scanner.next();

                    final int id;
                    if(!events.containsKey(e)) {
                        id = events.size();
                        events.put(e, events.size());
                    } else {
                        id = events.get(e);
                    }

                    final BitSet previous = (BitSet) happened.get(happened.size() - 1).clone();
                    previous.set(id);
                    happened.add(previous);

                    break;
                case 'D':
                    final int r = scanner.nextInt();

                    for(int j = 0; j < r; j++)
                        happened.remove(happened.size() - 1);

                    break;

                case 'S':
                    final int k = scanner.nextInt();

                    final BitSet in = new BitSet(), out = new BitSet();

                    boolean impossible = false;
                    for(int j = 0; j < k && !impossible; j++) {
                        final String token = scanner.next();
                        final boolean isNot = token.charAt(0) == '!';
                        final String event;
                        if(isNot) {
                            event = token.substring(1);
                        } else {
                            event = token;
                        }

                        final Integer get = events.get(event);

                        if(get != null) {
                            if(isNot)
                                out.set(get);
                            else
                                in.set(get);
                        } else if(get == null && !isNot) {
                            impossible = true;
                        }

                    }

                    if(impossible) {
                        System.out.println("Plot Error");
                    } else {
                        boolean possible = false;
                        for(int j = happened.size() - 1; j >= 0 && !possible; j--) {
                            final BitSet set = happened.get(j);
                            final BitSet copy = (BitSet) set.clone();
                            // TODO
                            //if(copy)
                        }

                        if(!possible) {
                            System.out.println("Plot Error");
                        }
                    }

                    break;
            }


        }

        scanner.close();
    }
}
