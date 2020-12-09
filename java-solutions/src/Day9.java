import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {

    public static void main(String[] args) throws Exception {
        final List<BigInteger> values = Files
                .readAllLines(Paths.get("java-solutions", "input", "day9")).stream()
                .map(String::trim)
                .map(BigInteger::new)
                .collect(Collectors.toList());
        final int preamble = 25;

        BigInteger result_one = BigInteger.ONE;
        for (int i = preamble; i < values.size(); ++i) {
            final List<BigInteger> lastNValues = values.subList(i - preamble, i);
            if (!canSum(lastNValues, values.get(i))) {
                result_one = values.get(i);
                System.out.printf("Part one result = %d\n", values.get(i));
                break;
            }
        }

        final Pair<Integer, Integer> interval = findContSum(values, result_one);
        final BigInteger min = values.subList(interval.a, interval.b).stream()
                .min(BigInteger::compareTo)
                .orElse(BigInteger.ZERO);
        final BigInteger max = values.subList(interval.a, interval.b).stream()
                .max(BigInteger::compareTo)
                .orElse(BigInteger.ZERO);
        System.out.printf("Part two result = %d", min.add(max));
    }

    private static boolean canSum(List<BigInteger> values, BigInteger target) {
        for (int i = 0; i < values.size(); ++i) {
            for (int j = i + 1; j < values.size(); ++j) {
                if (!values.get(i).equals(values.get(j))
                        && values.get(i).add(values.get(j)).equals(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Pair<Integer, Integer> findContSum(List<BigInteger> values, BigInteger target) {
        BigInteger acc = BigInteger.ZERO;

        for (int i = 0, j = 0; i < values.size(); ) {
            while (i < values.size() && acc.compareTo(target) < 0) {
                acc = acc.add(values.get(i++));
            }

            while (j < i && acc.compareTo(target) > 0) {
                acc = acc.subtract(values.get(j++));
            }

            if (acc.compareTo(target) == 0) {
                return new Pair<>(j, i);
            }
        }

        return new Pair<>(-1, -1);
    }

    record Pair<T, U>(T a, U b) {
    }
}
