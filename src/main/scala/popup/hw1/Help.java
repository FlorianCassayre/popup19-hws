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

        scanner.close();
    }

    private static String firstMatch(String sa, String sb)
    {
        final String[] patternA = sa.split(" "), patternB = sb.split(" ");

        if(patternA.length != patternB.length) // Short circuit
            return null;

        final Set<Group> graph = new HashSet<>();

        for(int j = 0; j < patternA.length; j++)
        {
            final String a = patternA[j], b = patternB[j];
            final String ta = extractTag(a), tb = extractTag(b);

            if(ta == null && tb == null && !a.equals(b))
                return null;

            final Group group = new Group();
            group.value = ta == null ? a : (tb == null ? b : null);
            if(ta != null)
                group.constraints.add(new TagConstraint(false, ta));
            if(tb != null)
                group.constraints.add(new TagConstraint(true, tb));

            if(!graphAdd(graph, group))
                return null;
        }

        final List<String> builder = new ArrayList<>(patternA.length);

        for(int i = 0; i < patternA.length; i++)
        {
            final String a = patternA[i], tag = extractTag(a);

            if(tag == null)
                builder.add(a);
            else
            {
                Group group = null;
                for(Group g : graph)
                {
                    final TagConstraint c = new TagConstraint(false, tag);
                    if(g.constraints.contains(c))
                    {
                        group = g;
                        break;
                    }
                }
                builder.add(group.value != null ? group.value : "any");
            }
        }

        return builder.stream().collect(Collectors.joining(" "));
    }

    private static boolean graphAdd(Set<Group> graph, Group group)
    {
        final Iterator<Group> it = graph.iterator();
        while(it.hasNext())
        {
            final Group g = it.next();
            boolean contains = false;
            for(TagConstraint c : g.constraints)
            {
                if(group.constraints.contains(c))
                {
                    contains = true;
                    break;
                }
            }
            if(contains)
            {
                if(g.value != null && group.value != null && !g.value.equals(group.value))
                        return false;
                group.value = g.value != null ? g.value : group.value;
                group.constraints.addAll(g.constraints);

                it.remove();
            }
        }

        graph.add(group);

        return true;
    }

    private static String extractTag(String string)
    {
        if(string.length() >= 2 && string.charAt(0) == '<' && string.charAt(string.length() - 1) == '>')
            return string.substring(1, string.length() - 1);
        else
            return null;
    }

    private static class Group
    {
        public String value = null;
        public final Set<TagConstraint> constraints = new HashSet<>();
    }

    private static class TagConstraint
    {
        public final boolean map;
        public final String tag;
        public TagConstraint(boolean map, String tag)
        {
            this.map = map;
            this.tag = tag;
        }

        @Override
        public boolean equals(Object o)
        {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            TagConstraint that = (TagConstraint) o;
            return map == that.map && Objects.equals(tag, that.tag);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(map, tag);
        }
    }
}
