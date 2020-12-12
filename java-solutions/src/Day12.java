import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 {

    public static void main(String[] args) throws Exception {
        final List<Instr> instrs = Files
                .readAllLines(Paths.get("java-solutions", "input", "day12")).stream()
                .map(Instr::valueOf)
                .collect(Collectors.toList());

        final Pair<Integer, Integer> ship = new Pair<>(0, 0);
        // 0 - N; 1 - E; 2 - S; 3 - V
        int direction = 1;
        for (Instr instr : instrs) {
            direction = updatePos(ship, direction, instr);
        }

        System.out.printf("Part one result = %d\n", mDistance(new Pair<>(0, 0), ship));


        final Pair<Integer, Integer> ship2 = new Pair<>(0, 0);
        final Pair<Integer, Integer> waypoint = new Pair<>(10, 1);
        for (Instr instr : instrs) {
            updatePos(ship2, waypoint, instr);
        }

        System.out.printf("Part two result = %d\n", mDistance(new Pair<>(0, 0), ship2));
    }

    public static int updatePos(Pair<Integer, Integer> pos, int direction, Instr instr) {
        switch (instr.action) {
            case 'N' -> pos.second += instr.value;
            case 'S' -> pos.second -= instr.value;
            case 'E' -> pos.first += instr.value;
            case 'W' -> pos.first -= instr.value;
            case 'L' -> direction = updateDir(direction, -instr.value);
            case 'R' -> direction = updateDir(direction, instr.value);
            case 'F' -> direction = updatePos(pos, direction, Instr.valueOf(direction, instr.value));
        }

        return direction;
    }

    public static void updatePos(Pair<Integer, Integer> ship, Pair<Integer, Integer> waypoint, Instr instr) {
        switch (instr.action) {
            case 'N' -> waypoint.second += instr.value;
            case 'S' -> waypoint.second -= instr.value;
            case 'E' -> waypoint.first += instr.value;
            case 'W' -> waypoint.first -= instr.value;
            case 'L' -> rotateWaypoint(waypoint, -instr.value);
            case 'R' -> rotateWaypoint(waypoint, instr.value);
            case 'F' -> {
                ship.first += (instr.value * waypoint.first);
                ship.second += (instr.value * waypoint.second);
            }
        }
    }

    public static void rotateWaypoint(Pair<Integer, Integer> waypoint, int degrees) {
        for (int i = 0; i < Math.floorDiv(Math.abs(degrees), 90); ++i) {
            final int tmp = waypoint.first;
            if (degrees > 0) {
                waypoint.first = waypoint.second;
                waypoint.second = tmp * -1;
            } else {
                waypoint.first = waypoint.second * -1;
                waypoint.second = tmp;
            }
        }
    }

    public static int updateDir(int direction, int degrees) {
        return (direction + Math.floorDiv(degrees, 90) + 4) % 4;
    }

    public static int mDistance(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        return (Math.abs(a.first - b.first) + Math.abs(a.second - b.second));
    }

    record Instr(char action, int value) {
        public static Instr valueOf(String s) {
            final char action = s.charAt(0);
            final int value = Integer.parseInt(s.substring(1));
            return new Instr(action, value);
        }

        public static Instr valueOf(int dir, int value) {
            final char action = switch (dir) {
                case 0 -> 'N';
                case 1 -> 'E';
                case 2 -> 'S';
                default -> 'W';
            };

            return new Instr(action, value);
        }
    }
}
