import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 {

    public static void main(String[] args) throws Exception {
        final List<Instruction> instructions = Files
                .readAllLines(Paths.get("java-solutions", "input", "day8")).stream()
                .map(Instruction::parseString)
                .collect(Collectors.toList());

        final Result result = runProgram(instructions);
        System.out.printf("Part one result = %d\n", result.accumulator);

        for (int i = 0; i < instructions.size(); ++i) {
            instructions.set(i, flipOP(instructions.get(i)));

            final Result result2 = runProgram(instructions);
            if (result2.exitSuccess) {
                System.out.printf("Part two result = %d\n", result2.accumulator);
                break;
            }

            instructions.set(i, flipOP(instructions.get(i)));
        }
    }

    private static Instruction flipOP(Instruction instr) {
        final Instruction.OP op = switch (instr.op) {
            case JMP -> Instruction.OP.NOOP;
            case NOOP -> Instruction.OP.JMP;
            default -> instr.op;
        };

        return new Instruction(op, instr.arg);
    }

    private static Result runProgram(List<Instruction> instructions) {
        final int n = instructions.size();
        final boolean[] instrExecuted = new boolean[n];

        int accumulator = 0;
        int instrIndex = 0;
        while (instrIndex < n && !instrExecuted[instrIndex]) {
            instrExecuted[instrIndex] = true;
            final Instruction instruction = instructions.get(instrIndex);

            switch (instruction.op) {
                case ACC -> {
                    accumulator += instruction.arg;
                    instrIndex += 1;
                }
                case JMP -> instrIndex += instruction.arg;
                case NOOP -> instrIndex += 1;
                case ERR -> throw new RuntimeException("Found an ERR instruction in line: [%s]");
            }
        }

        return new Result(accumulator, instrIndex == n);
    }

    private record Result(int accumulator, boolean exitSuccess) {
    }

    private record Instruction(OP op, int arg) {
        public static Instruction parseString(String line) {
            final OP op = switch (line.substring(0, 3)) {
                case "nop" -> OP.NOOP;
                case "acc" -> OP.ACC;
                case "jmp" -> OP.JMP;
                default -> OP.ERR;
            };
            final int arg = Integer.parseInt(line.substring(4).trim());
            return new Instruction(op, arg);
        }

        enum OP {NOOP, ACC, JMP, ERR}
    }
}
