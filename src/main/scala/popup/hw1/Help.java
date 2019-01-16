package popup.hw1;

import java.util.*;
import java.util.stream.Collectors;

public class Help
{
    public static void main(String[] args)
    {
        final Scanner scanner = new Scanner(System.in);

        final int n = Integer.parseInt(scanner.nextLine());
        for(int i = 0; i < n; i++)
        {
            final String v = firstMatch(scanner.nextLine(), scanner.nextLine());

            if(v != null)
                System.out.println(v);
            else
                System.out.println("-");
        }
    }

    private static String firstMatch(String sa, String sb)
    {
        final String[] patternA = sa.split(" "), patternB = sb.split(" ");

        if(patternA.length != patternB.length) // Short circuit
            return null;

        final Map<String, String> mapA = new HashMap<>(), mapB = new HashMap<>();

        for(int j = 0; j < patternA.length; j++)
        {
            final String a = patternA[j], b = patternB[j];
            final String ta = extractTag(a), tb = extractTag(b);

            if(ta == null && tb == null)
            {
                if(!a.equals(b))
                    return null;
            }
            else if(ta != null && tb == null)
            {
                mapA.putIfAbsent(ta, b);
                if(!b.equals(mapA.get(ta)))
                    return null;
            }
            else if(ta == null && tb != null)
            {
                mapB.putIfAbsent(tb, a);
                if(!a.equals(mapB.get(tb)))
                    return null;
            }
            else
            {
                final String ma = mapA.get(ta), mb = mapB.get(tb);
                if(ma != null && mb == null)
                    mapB.put(tb, ma);
                else if(ma == null && mb != null)
                    mapA.put(ta, mb);
                else if(ma != null && mb != null && !ma.equals(mb))
                    return null;
            }
        }

        final List<String> builder = new ArrayList<>(patternA.length);

        for(int i = 0; i < patternA.length; i++)
        {
            final String a = patternA[i], tag = extractTag(a), get = mapA.get(tag);

            if(tag == null)
                builder.add(a);
            else if(get != null)
                builder.add(get);
            else
                builder.add("*");
        }

        return builder.stream().collect(Collectors.joining(" "));
    }

    private static String extractTag(String string)
    {
        if(string.length() >= 2 && string.charAt(0) == '<' && string.charAt(string.length() - 1) == '>')
            return string.substring(1, string.length() - 1);
        else
            return null;
    }
}
