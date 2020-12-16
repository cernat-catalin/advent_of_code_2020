import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 {

    public static void main(String[] args) throws Exception {
        final List<String> lines = new ArrayList<>(Files
                .readAllLines(Paths.get("java-solutions", "input", "day13")));

        final int timestamp = Integer.parseInt(lines.get(0));
        final String[] tokens = lines.get(1).split(",");
        final List<Integer> busIds = Arrays.stream(tokens)
                .filter(s -> !s.equals("x"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        final Pair<Integer, Integer> resultOne = busIds.stream()
                .map(id -> new Pair<>(id, id - timestamp % id))
                .min(Comparator.comparing(p -> p.second))
                .orElseThrow();

        System.out.printf("Part one result = %d\n", resultOne.first * resultOne.second);


        final List<Pair<Integer, Integer>> equations = IntStream.range(0, tokens.length)
                .filter(i -> !tokens[i].equals("x"))
                .mapToObj(i -> new Pair<>(Integer.parseInt(tokens[i]), i))
                .map(p -> new Pair<>(p.first, (p.first - p.second) % p.first))
                .collect(Collectors.toList());

        final long[] n = equations.stream()
                .mapToLong(p -> p.first)
                .toArray();

        final long[] a = equations.stream()
                .mapToLong(p -> p.second)
                .toArray();

        final long resultTwo = chineseRemainder(n, a);
        System.out.printf("Part two result = %d\n", resultTwo);
    }

    private static long chineseRemainder(long[] n, long[] a) {

        long prod = Arrays.stream(n).reduce(1, (i, j) -> i * j);

        long p, sm = 0;
        for (int i = 0; i < n.length; i++) {
            p = prod / n[i];
            sm += a[i] * mulInv(p, n[i]) * p;
        }
        return sm % prod;
    }

    private static long mulInv(long a, long b) {
        long b0 = b;
        long x0 = 0;
        long x1 = 1;

        if (b == 1)
            return 1;

        while (a > 1) {
            long q = a / b;
            long amb = a % b;
            a = b;
            b = amb;
            long xqx = x1 - q * x0;
            x1 = x0;
            x0 = xqx;
        }

        if (x1 < 0)
            x1 += b0;

        return x1;
    }
}
