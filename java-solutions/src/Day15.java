import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day15 {

    public static void main(String[] args) throws Exception {
        final Integer[] xs = Arrays.stream(Files
                .readAllLines(Paths.get("java-solutions", "input", "day15")).get(0).split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

        final Map<Integer, Integer> occurrences = new HashMap<>();

        for (int i = 0; i < xs.length - 1; ++i) {
            occurrences.put(xs[i], i);
        }

        int lastSpoken = xs[xs.length - 1];
        for (int i = xs.length - 1; i < 30000000 - 1; ++i) {
            final int newNumber = occurrences.containsKey(lastSpoken) ? i - occurrences.get(lastSpoken) : 0;
            occurrences.put(lastSpoken, i);
            lastSpoken = newNumber;
        }

        System.out.printf("Part one result = %d\n", lastSpoken);
    }
}
