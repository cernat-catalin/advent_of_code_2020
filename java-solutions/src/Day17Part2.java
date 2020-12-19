import java.nio.file.Files;
import java.nio.file.Paths;

public class Day17Part2 {
    public static void main(String[] args) throws Exception {
        final String[] lines = Files
                .readAllLines(Paths.get("java-solutions", "input", "day17"))
                .toArray(String[]::new);

        final int xLines = lines.length;
        final int yLines = lines[0].length();

        final int n = 50;
        final int m = n / 2;
        final boolean[][][][] arr = new boolean[n][n][n][n];

        for (int i = 0; i < xLines; ++i) {
            for (int j = 0; j < yLines; ++j) {
                arr[m - xLines / 2 + i][m - yLines / 2 + j][m][m] = (lines[i].charAt(j) == '#');
            }
        }

        final Area area = new Area(new Pair<>(m - xLines / 2, m - xLines / 2 + xLines),
                new Pair<>(m - yLines / 2, m - yLines / 2 + yLines),
                new Pair<>(m, m + 1), new Pair<>(m, m + 1));

        for (int i = 0; i < 6; ++i) {
            iterate(arr, area);
        }

        final int resultOne = sumActive(arr, area);
        System.out.printf("Part one result = %d\n", resultOne);
    }

    private static void iterate(boolean[][][][] arr, Area area) {
        area.rangeX.first -= 1;
        area.rangeX.second += 1;
        area.rangeY.first -= 1;
        area.rangeY.second += 1;
        area.rangeZ.first -= 1;
        area.rangeZ.second += 1;
        area.rangeW.first -= 1;
        area.rangeW.second += 1;

        final int rx = area.rangeX.second - area.rangeX.first;
        final int ry = area.rangeY.second - area.rangeY.first;
        final int rz = area.rangeZ.second - area.rangeZ.first;
        final int rw = area.rangeW.second - area.rangeW.first;
        final boolean[][][][] arrT = new boolean[rx][ry][rz][rw];

        for (int i = 0; i < rx; ++i) {
            for (int j = 0; j < ry; ++j) {
                for (int k = 0; k < rz; ++k) {
                    for (int u = 0; u < rz; ++u) {
                        final int x = area.rangeX.first + i;
                        final int y = area.rangeY.first + j;
                        final int z = area.rangeZ.first + k;
                        final int w = area.rangeW.first + u;
                        final int activeNeighbours = countNeighbours(arr, x, y, z, w);

                        if (arr[x][y][z][w]) {
                            arrT[i][j][k][u] = (activeNeighbours == 2 || activeNeighbours == 3);
                        } else {
                            arrT[i][j][k][u] = (activeNeighbours == 3);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < rx; ++i) {
            for (int j = 0; j < ry; ++j) {
                for (int k = 0; k < rz; ++k) {
                    for (int u = 0; u < rz; ++u) {
                        final int x = area.rangeX.first + i;
                        final int y = area.rangeY.first + j;
                        final int z = area.rangeZ.first + k;
                        final int w = area.rangeW.first + u;
                        arr[x][y][z][w] = arrT[i][j][k][u];
                    }
                }
            }
        }
    }

    private static int countNeighbours(boolean[][][][] arr, int x, int y, int z, int w) {
        int activeNeighbours = 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                for (int k = -1; k <= 1; ++k) {
                    for (int u = -1; u <= 1; ++u) {
                        if (arr[x + i][y + j][z + k][w + u]) {
                            activeNeighbours += 1;
                        }
                    }
                }
            }
        }

        if (arr[x][y][z][w]) {
            activeNeighbours -= 1;
        }

        return activeNeighbours;
    }

    private static int sumActive(boolean[][][][] arr, Area area) {
        int active = 0;
        for (int i = area.rangeX.first; i < area.rangeX.second; ++i) {
            for (int j = area.rangeY.first; j < area.rangeY.second; ++j) {
                for (int k = area.rangeZ.first; k < area.rangeZ.second; ++k) {
                    for (int u = area.rangeW.first; u < area.rangeW.second; ++u) {
                        if (arr[i][j][k][u]) {
                            active += 1;
                        }
                    }
                }
            }
        }

        return active;
    }


    // [a, b)
    record Area(Pair<Integer, Integer>rangeX, Pair<Integer, Integer>rangeY,
                Pair<Integer, Integer>rangeZ, Pair<Integer, Integer>rangeW) {
    }
}
