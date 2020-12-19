import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Day18 {
    public static void main(String[] args) throws Exception {
        final String[] lines = Files
                .readAllLines(Paths.get("java-solutions", "input", "day18"))
                .toArray(String[]::new);

        long resultOne = 0;
        for (String line : lines) {
            final List<Long> expression = parseExpression(line, false);
            resultOne += evalExpression(expression);
        }

        System.out.printf("Part one result = %d\n", resultOne);

        long resultTwo = 0;
        for (String line : lines) {
            final List<Long> expression = parseExpression(line, true);
            resultTwo += evalExpression(expression);
        }

        System.out.printf("Part two result = %d\n", resultTwo);
    }

    public static Long evalExpression(List<Long> expression) {
        final Stack<Long> evalStack = new Stack<>();

        for (int i = 0; i < expression.size(); ++i) {
            if (expression.get(i) >= 0) {
                evalStack.add(expression.get(i));
            } else {
                final long right = evalStack.pop();
                final long left = evalStack.pop();
                if (expression.get(i) == -1L) {
                    evalStack.add(left + right);
                } else {
                    evalStack.add(left * right);
                }
            }
        }

        return evalStack.pop();
    }

    // Parse expression into reverse polish notation with Shunting-yard algorithm
    // -1 = +; -2 = *; -3 = (; -4 = )
    public static List<Long> parseExpression(String line, boolean withPrecedence) {
        final List<Long> output = new ArrayList<>();
        final Stack<Long> operatorStack = new Stack<>();
        final char[] chrs = line.toCharArray();

        int i = 0;
        while (i < chrs.length) {
            if (Character.isDigit(chrs[i])) {
                long n = 0;
                while (i < chrs.length && '0' <= chrs[i] && chrs[i] <= '9') {
                    n = n * 10 + chrs[i] - '0';
                    ++i;
                }
                output.add(n);
            } else if (chrs[i] == '+' || chrs[i] == '*') {
                final long op = chrs[i] == '+' ? -1L : -2L;
                if (withPrecedence) {
                    while (operatorStack.size() > 0 &&
                            (operatorStack.peek() != -3L && op < operatorStack.peek())) {
                        final long top = operatorStack.pop();
                        output.add(top);
                    }
                    operatorStack.add(op);
                } else {
                    while (operatorStack.size() > 0 && operatorStack.peek() != -3L) {
                        final Long top = operatorStack.pop();
                        output.add(top);
                    }
                    operatorStack.add(chrs[i] == '+' ? -1L : -2L);
                }
                ++i;
            } else if (chrs[i] == '(') {
                operatorStack.push(-3L);
                ++i;
            } else if (chrs[i] == ')') {
                while (operatorStack.peek() != -3L) {
                    output.add(operatorStack.pop());
                }
                operatorStack.pop();
                ++i;
            } else if (chrs[i] == ' ') {
                ++i;
            }
        }

        while (operatorStack.size() > 0) {
            output.add(operatorStack.pop());
        }

        return output;
    }
}
