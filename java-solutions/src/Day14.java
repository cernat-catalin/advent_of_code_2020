import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day14 {

    public static void main(String[] args) throws Exception {
        final List<String> lines = new ArrayList<>(Files
                .readAllLines(Paths.get("java-solutions", "input", "day14")));

        {
            final Map<Integer, Long> memory = new HashMap<>();
            Pair<Long, Long> lastMask = null;
            for (String line : lines) {
                if (line.startsWith("mask")) {
                    lastMask = decodeMask(line);
                } else {
                    final Pair<Integer, Long> instr = parseUpdateInstr(line);
                    final long maskedValue = ((instr.second & lastMask.first) | lastMask.second);
                    memory.put(instr.first, maskedValue);
                }
            }

            final Long resultOne = memory.values().stream().reduce(0L, Long::sum);
            System.out.printf("Part one result = %d\n", resultOne);
        }

        {
            final Map<Long, Long> memory = new HashMap<>();
            Pair<Long, List<Integer>> lastMask = null;
            for (String line : lines) {
                if (line.startsWith("mask")) {
                    lastMask = decodeMask2(line);
                } else {
                    final Pair<Integer, Long> instr = parseUpdateInstr(line);
                    final long initialOutcome = instr.first | lastMask.first;
                    final List<Long> addresses = collapse(Collections.singletonList(initialOutcome), lastMask.second);
                    for (Long address : addresses) {
                        memory.put(address, instr.second);
                    }
                }
            }

            final Long resultTwo = memory.values().stream().reduce(0L, Long::sum);
            System.out.printf("Part two result = %d\n", resultTwo);
        }
    }

    private static Pair<Long, Long> decodeMask(String maskLine) {
        final String mask = maskLine.split(" = ")[1].trim();
        long zeroMask = Long.MAX_VALUE;
        long oneMask = 0L;

        for (int i = 0; i < mask.length(); ++i) {
            final char c = mask.charAt(mask.length() - i - 1);
            if (c == '0') {
                zeroMask = zeroMask & ~(1L << i);
            } else if (c == '1') {
                oneMask = oneMask | (1L << i);
            }
        }

        return new Pair<>(zeroMask, oneMask);
    }

    private static Pair<Long, List<Integer>> decodeMask2(String maskLine) {
        final String mask = maskLine.split(" = ")[1].trim();
        long oneMask = 0L;
        final List<Integer> superpositions = new ArrayList<>();

        for (int i = 0; i < mask.length(); ++i) {
            final char c = mask.charAt(mask.length() - i - 1);
            if (c == '1') {
                oneMask = oneMask | (1L << i);
            } else if (c == 'X') {
                superpositions.add(i);
            }
        }

        return new Pair<>(oneMask, superpositions);
    }

    private static List<Long> collapse(List<Long> outcomes, List<Integer> superpositions) {
        if (superpositions.size() == 0) {
            return outcomes;
        }

        final List<Long> newOutcomes = new ArrayList<>();
        for (Long outcome : outcomes) {
            final Integer superposition = superpositions.get(0);
            newOutcomes.add(outcome | (1L << superposition));
            newOutcomes.add(outcome & ~(1L << superposition));
        }

        return collapse(newOutcomes, superpositions.subList(1, superpositions.size()));
    }

    private static Pair<Integer, Long> parseUpdateInstr(String instrLine) {
        final String[] tokens = instrLine.split(" = ");
        final String addrStr = tokens[0].trim().substring(4, tokens[0].length() - 1);
        return new Pair<>(Integer.parseInt(addrStr), Long.parseLong(tokens[1].trim()));
    }
}
