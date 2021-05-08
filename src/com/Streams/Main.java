import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("sortedOdds:");
        int[] a = {9,10,4,7,3,29,191,1};
        System.out.println(Arrays.toString(a) + "\nSorted:");
        sortedOdds(a);
        System.out.println("Common values btw 2 lists:");
        List<Integer> l1 = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
        List<Integer> l2 = new ArrayList<>(Arrays.asList(5,6,7,8,9));
        System.out.println("L1: " + l1 + " L2: " + l2);
        System.out.println(Arrays.toString(getCommon(l1,l2)));
        System.out.println(Arrays.toString(getCommon2(l1,l2)));
        int limit = 20;
        System.out.println("Pythag:");
        printPythagoreans(limit);
    }

    public static void sortedOdds(int[] arr) {
        Arrays.stream(arr).filter(x->x%2 == 1).sorted().forEach(System.out::println);
    }

    public static int[] getCommon(List<Integer> l1, List<Integer> l2) {
        Set<Integer> s2 = l2.stream().collect(Collectors.toCollection(HashSet::new));
        return l1.stream().mapToInt(x->x).filter(x -> s2.contains(x)).toArray();
    }

    public static int[] getCommon2(List<Integer> l1, List<Integer> l2) {
        return l1.stream().mapToInt(Integer::intValue).filter(i -> l2.stream().anyMatch(j -> i == j)).toArray();
    }

    public static void printPythagoreans(int limit) {
        Stream<Integer> s = IntStream.rangeClosed(1, limit).boxed();
        s.flatMap(i->IntStream.rangeClosed(1, limit).boxed().map(j->"a:" + i + " b: " +j + " c: " + Math.sqrt((i*i) + (j*j)))).forEach(System.out::println);
    }
}
