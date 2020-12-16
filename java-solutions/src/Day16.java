import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) throws Exception {
        final String content = Files
                .readString(Paths.get("java-solutions", "input", "day16"));

        final String[] tokens = content.split("\n\n");
        final List<Rule> rules = Arrays.stream(tokens[0].split("\n"))
                .map(Rule::valueOf)
                .collect(Collectors.toList());

        final List<Integer> myTicket = parseTicket(tokens[1].split("\n")[1]);
        final List<List<Integer>> tickets = Arrays.stream(tokens[2].split("\n"))
                .skip(1)
                .map(Day16::parseTicket)
                .collect(Collectors.toList());

        {
            int resultOne = tickets.stream()
                    .flatMap(Collection::stream)
                    .filter(value -> !isInRules(value, rules))
                    .reduce(0, Integer::sum);
            System.out.printf("Part one result = %d\n", resultOne);
        }

        {
            final List<List<Integer>> validTickets = tickets.stream()
                    .filter(ticket -> ticket.stream().allMatch(value -> isInRules(value, rules)))
                    .collect(Collectors.toList());

            final int m = validTickets.get(0).size();
            final List<List<Rule>> potentialRules = new ArrayList<>();
            for (int j = 0; j < m; ++j) {
                final List<Integer> values = columnValues(validTickets, j);
                final List<Rule> potentials = new ArrayList<>();

                for (Rule rule : rules) {
                    if (isRuleValid(values, rule)) {
                        potentials.add(rule);
                    }
                }

                potentialRules.add(potentials);
            }

            final List<Pair<Integer, Rule>> finalRules = new ArrayList<>();
            for (int i = 0; i < m; ++i) {
                final Pair<Integer, Rule> finalRule = iterateOnce(potentialRules);
                finalRules.add(finalRule);
            }

            final Long resultTwo = finalRules.stream()
                    .filter(p -> p.second.name.startsWith("departure"))
                    .map(p -> myTicket.get(p.first).longValue())
                    .reduce(1L, (acc, x) -> acc * x);
            System.out.printf("Part two result = %d\n", resultTwo);
        }
    }

    private static Pair<Integer, Rule> iterateOnce(List<List<Rule>> potentialRules) {
        for (int i = 0; i < potentialRules.size(); ++i) {
            if (potentialRules.get(i).size() == 1) {
                final Pair<Integer, Rule> result = new Pair<>(i, potentialRules.get(i).get(0));
                removeRule(potentialRules, result.second);
                return result;
            }
        }

        throw new RuntimeException("No rule found!");
    }

    private static void removeRule(List<List<Rule>> potentialRules, Rule rule) {
        for (List<Rule> potentials : potentialRules) {
            potentials.removeIf(r -> r.name.equals(rule.name));
        }
    }

    private static boolean isRuleValid(List<Integer> values, Rule rule) {
        return values.stream()
                .allMatch(value ->
                        ((rule.range1.first <= value && value <= rule.range1.second)
                                || (rule.range2.first <= value && value <= rule.range2.second)));
    }

    private static List<Integer> columnValues(List<List<Integer>> tickets, int j) {
        return tickets.stream()
                .map(ticket -> ticket.get(j))
                .collect(Collectors.toList());
    }

    private static boolean isInRules(Integer value, List<Rule> rules) {
        return rules.stream()
                .anyMatch(rule -> (rule.range1.first <= value && value <= rule.range1.second)
                        || (rule.range2.first <= value && value <= rule.range2.second));
    }

    private static List<Integer> parseTicket(String str) {
        return Arrays.stream(str.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    record Rule(String name, Pair<Integer, Integer>range1, Pair<Integer, Integer>range2) {
        public static Rule valueOf(String ruleStr) {
            final String[] tokens1 = ruleStr.split(":");
            final String[] tokens2 = tokens1[1].split(" or ");

            return new Rule(tokens1[0], parseRange(tokens2[0]), parseRange(tokens2[1]));
        }

        private static Pair<Integer, Integer> parseRange(String str) {
            final String[] tokens = str.split("-");
            return new Pair<>(Integer.parseInt(tokens[0].trim()), Integer.parseInt(tokens[1]));
        }
    }
}