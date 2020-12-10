import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class Day10 {

    public static void main(String[] args) throws Exception {
        final List<Integer> voltages = Files
                .readAllLines(Paths.get("java-solutions", "input", "day10")).stream()
                .map(String::trim)
                .map(Integer::parseInt)
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());
        voltages.add(0, 0);

        final Map<Integer, Long> occ = IntStream.range(1, voltages.size())
                .map(i -> voltages.get(i) - voltages.get(i - 1))
                .boxed()
                .collect(groupingBy(Function.identity(), Collectors.counting()));

        final Long result_one = occ.get(1) * (occ.get(3) + 1);
        System.out.printf("Part one result = %d\n", result_one);

        final int n = voltages.size();
        final long[] arr = new long[n];

        arr[0] = 1;
        for (int i = 1; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                if (voltages.get(i) - voltages.get(j) <= 3) {
                    arr[i] += arr[j];
                }
            }
        }

        System.out.printf("Part two result = %d\n", arr[n - 1]);
    }
}
