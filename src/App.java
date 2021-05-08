import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class App {
    public static void main(String[] args) throws Exception {
        int[] a = {9,10,4,7,3,29,191,1};
        System.out.println("Sorting odd numbers from: " + Arrays.toString(a) + "\nSorted:");
        sortedOdds(a);
        System.out.println("*******\nCommon values btw 2 lists:");
        List<Integer> l1 = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
        List<Integer> l2 = new ArrayList<>(Arrays.asList(5,6,7,8,9));
        System.out.println("L1: " + l1 + " L2: " + l2);
        System.out.println(Arrays.toString(getCommon(l1,l2)));
        int limit = 20;
        System.out.println("Pythagorean triples:");
        printPythagoreans(limit);
        System.out.println("First " + limit + "fibonacci numbers: " + Arrays.toString(fibs(limit)));
        System.out.println("Numbers up to " + limit + " partitioned by primality: " + primesAndNot(limit));
        System.out.println("Words by Length: " + wordsByLength("test.txt"));
        System.out.println("Top 3 frequently used words: " + top3Words("test.txt"));
        String word = "dcbra";
        System.out.println("Sorting " + word + ": " + sortStringWithCountingSort(word));
    }

    public static void sortedOdds(int[] arr) {
        Arrays.stream(arr).filter(x->x%2 == 1).sorted().forEach(System.out::println);
    }

    public static int[] getCommonFaster(List<Integer> l1, List<Integer> l2) {
        Set<Integer> s2 = new HashSet<>();
        s2.addAll(l2);
        return l1.stream()
                .mapToInt(Integer::valueOf)
                .filter(x -> s2.contains(x))
                .toArray();
    }

    public static int[] getCommon(List<Integer> l1, List<Integer> l2) {
        return l1.stream()
                .mapToInt(Integer::intValue)
                .filter(i -> l2.stream()
                        .anyMatch(j -> i == j))
                .toArray();
    }

    public static void printPythagoreans(int limit) {
        IntStream.rangeClosed(1, limit)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a, limit)
                        .boxed()
                        .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                        .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}))
                .forEach(t -> System.out.println(Arrays.stream(t)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(", ")
                        )));
    }

    public static List<String> findAllAnagrams(String word, List<String> dictionary) {
        return makeWordSignatures(dictionary).getOrDefault(word, new ArrayList<>());
    }

    public static Map<String, List<String>> makeWordSignatures(List<String> words) {
        return words.stream()
                .collect(groupingBy(
                        word->sortStringWithCountingSort(word)
                        ));
    }

    public static String sortStringWithCountingSort(String s) {
        int alphabetSize = 26;
        int ASCIIValueOf_a = 97;
        int[] charCount = new int[alphabetSize];
        s = s.toLowerCase();
        s.chars().forEach(charVal->charCount[charVal-97]++); // counting sort by associating each index with number of times it appears
        return IntStream.range(0, charCount.length) // generates a stream of indices into the arr map
                .mapToObj(offsetFromA-> IntStream.range(0, charCount[offsetFromA]) // map each index i to it's ascii value, and use a stream of that chars count to repeat the char "count"-times
                        .mapToObj(x->Character.toString((char)offsetFromA+ASCIIValueOf_a)) // maps the ints to the char val, then converts the char to string
                        .collect(Collectors.joining(""))) // Join the repetitions of the char
                .collect(Collectors.joining("")); // join the collection of repeated characters

    }

    public static int[] fibs(int limit) {
        return Stream.iterate(new int[]{0,1}, f-> new int[]{f[1],f[0]+f[1]})
                .mapToInt(x->x[0])
                .limit(limit)
                .toArray();
    }

    public static Map<Boolean, List<Integer>> primesAndNot(int n) {
        return IntStream.rangeClosed(0, n).boxed().collect(Collectors.partitioningBy(App::isPrime));
    }

    public static boolean isPrime(int n) {
        return !IntStream.rangeClosed(1, (int)Math.sqrt(n))
                .mapToObj(i-> IntStream.range(i, n).boxed() // stream multipliers from i to n
                        .filter(j-> j*i == n))// filter if not prime
                .flatMap(i->i)// map stream of ints to ints
                .findAny() // if there is stil a value, it is not prime
                .isPresent(); // return true if present (inverted in the return to return false)
    }

    public static List<String> top3Words(String path) throws IOException {
        Map<String, List<String>> m = Files.lines(Paths.get(path))
                .flatMap(line->Arrays.stream(line.split(" ")))
                .collect(Collectors.groupingBy(word->word));

        return m.keySet().stream()
                .sorted(Comparator.comparing(wordList->m.get(wordList).size() * -1))
                .limit(3)
                .collect(Collectors.toList());
    }


    public static Map<Integer,List<String>> wordsByLength(String file)
            throws IOException {
        return Files.lines(Paths.get(file))
                .map(x->x.split(" "))
                .flatMap(x->Arrays.stream(x))
                .collect(Collectors.groupingBy(String::length));
    }

    public static int numberOfHackathons(int year, List<Hackathon> hackathons) {
        return (int)hackathons.stream()
                .filter(hackathon -> hackathon.getYear() == year)
                .count();
    }

    public static int highestSponsorshipAmount(List<Hackathon> hackathons) {
        return hackathons.stream()
                .mapToInt(Hackathon::getSponsorship)
                .max()
                .orElse(-1);
    }

    public static String schoolWithLargestNumberOfParticipants(int year, List<Hackathon> hackathons) {
        return hackathons.stream()
                .filter(hackathon -> hackathon.getYear() == year)
                .max(Comparator.comparing(Hackathon::getParticipants))
                .map(Hackathon::getSchool)
                .orElse("None");
    }

    public static Map<Integer, Long> numberOfHackathonsLargerThanN(int n, List<Hackathon> hackathons) {
        return hackathons.stream()
                .filter(hackathon -> hackathon.getParticipants() > 500)
                .collect(Collectors.groupingBy(
                        Hackathon::getYear,
                        Collectors.counting()
                ));
    }
    public static Map<Integer, Set<String>> schoolsWithHackathonsLargerThanN(int n, List<Hackathon> hackathons) {
        return hackathons.stream()
                .filter(hackathon -> hackathon.getParticipants() > 500)
                .sorted(Comparator.comparing(Hackathon::getParticipants).reversed())
                .collect(Collectors.groupingBy(
                        Hackathon::getYear,
                        mapping(Hackathon::getSchool, toSet())
                ));
    }
}

class Hackathon {
    private String school;
    private int year;
    private int month;   // 1..12
    private int points;       // awarded by site, out of 1000
    private int participants;

    public String getSchool() {
        return school;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getPoints() {
        return points;
    }

    public int getParticipants() {
        return participants;
    }

    public int getSponsorship() {
        return sponsorship;
    }

    private int sponsorship;  // $$ value


    public String toString() { return school; }
}