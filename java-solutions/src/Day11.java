import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day11 {

    public static void main(String[] args) throws Exception {
        final char[][] seats = Files
                .readAllLines(Paths.get("java-solutions", "input", "day11")).stream()
                .map(String::trim)
                .map(String::toCharArray)
                .toArray(char[][]::new);


        final int part_one_result = part_one(cloneArray(seats));
        System.out.printf("Part one result = %d\n", part_one_result);

        final int part_two_result = part_two(cloneArray(seats));
        System.out.printf("Part two result = %d\n", part_two_result);
    }

    private static int part_one(char[][] seats) {
        final char[][] seats_copy = cloneArray(seats);
        final int n = seats.length;
        final int m = seats[0].length;

        boolean changed = true;
        while (changed) {
            changed = false;

            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < m; ++j) {
                    final int occupied_neighbours = count_occupied_neighbours(seats, i, j);

                    if (seats[i][j] == 'L' && occupied_neighbours == 0) {
                        seats_copy[i][j] = '#';
                        changed = true;
                    } else if (seats[i][j] == '#' && occupied_neighbours >= 4) {
                        seats_copy[i][j] = 'L';
                        changed = true;
                    } else {
                        seats_copy[i][j] = seats[i][j];
                    }
                }
            }

            copyArray(seats_copy, seats);
        }

        return count_occupied(seats);
    }

    private static int count_occupied_neighbours(char[][] seats, int x, int y) {
        final int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
        final int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};

        final int n = seats.length;
        final int m = seats[0].length;

        int occupied_neighbours = 0;
        for (int k = 0; k < 8; ++k) {
            final int x2 = x + dx[k];
            final int y2 = y + dy[k];
            if (0 <= x2 && x2 < n && 0 <= y2 && y2 < m && seats[x2][y2] == '#') {
                occupied_neighbours += 1;
            }
        }

        return occupied_neighbours;
    }

    private static int part_two(char[][] seats) {
        final char[][] seats_copy = cloneArray(seats);
        final int n = seats.length;
        final int m = seats[0].length;

        boolean changed = true;
        while (changed) {
            changed = false;

            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < m; ++j) {
                    final int occupied_neighbours = count_occupied_neighbours_fov(seats, i, j);

                    if (seats[i][j] == 'L' && occupied_neighbours == 0) {
                        seats_copy[i][j] = '#';
                        changed = true;
                    } else if (seats[i][j] == '#' && occupied_neighbours >= 5) {
                        seats_copy[i][j] = 'L';
                        changed = true;
                    } else {
                        seats_copy[i][j] = seats[i][j];
                    }
                }
            }

            copyArray(seats_copy, seats);
        }

        return count_occupied(seats);
    }

    private static int count_occupied_neighbours_fov(char[][] seats, int x, int y) {
        final int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
        final int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};

        final int n = seats.length;
        final int m = seats[0].length;

        int occupied_neighbours = 0;
        for (int k = 0; k < 8; ++k) {
            int x2 = x + dx[k];
            int y2 = y + dy[k];

            while (0 <= x2 && x2 < n && 0 <= y2 && y2 < m) {
                if (seats[x2][y2] == '.') {
                    x2 = x2 + dx[k];
                    y2 = y2 + dy[k];
                } else if (seats[x2][y2] == '#') {
                    ++occupied_neighbours;
                    break;
                } else {
                    break;
                }
            }
        }

        return occupied_neighbours;
    }

    private static int count_occupied(char[][] seats) {
        int empty_seats = 0;
        for (char[] seat : seats)
            for (char chr : seat)
                if (chr == '#')
                    ++empty_seats;

        return empty_seats;
    }

    private static void print(char[][] seats) {
        for (char[] seat : seats) {
            System.out.printf("%s\n", String.valueOf(seat));
        }
    }

    private static char[][] cloneArray(char[][] array) {
        return Arrays.stream(array)
                .map(char[]::clone)
                .toArray(char[][]::new);
    }

    private static void copyArray(char[][] src, char[][] dest) {
        for (int i = 0; i < src.length; ++i) {
            System.arraycopy(src[i], 0, dest[i], 0, src[i].length);
        }
    }
}
