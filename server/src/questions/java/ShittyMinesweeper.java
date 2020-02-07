package questions.java;

import java.util.*;

public class ShittyMinesweeper {

    private final boolean[][] actual;
    private final int[][] seen;
    private final int size;

    public ShittyMinesweeper(boolean[][] actual) {
        this.actual = actual;
        this.size = actual.length;
        this.seen = new int[size][size];
        for (int[] ints : seen) {
            Arrays.fill(ints, -1);
        }
    }

    public final boolean doIDieNow(int x, int y) {
        if (seen[x][y] != -1) {
            return false;
        }
        if (actual[x][y]) {
            System.out.println("YOU DIE");
            return true;
        } else {
            seen[x][y] = getNumSurrounding(x, y);
            if (seen[x][y] == 0) {
                List<Point> todo = getUnopenedSurrounding(x, y);
                while (!todo.isEmpty()) {
                    Point p = todo.get(0);
                    todo.remove(0);
                    if (seen[p.x][p.y] == -1) {
                        seen[p.x][p.y] = getNumSurrounding(p);
                        if (seen[p.x][p.y] == 0) {
                            todo.addAll(getUnopenedSurrounding(p));
                        }
                    }
                }
            }
            return false;
        }

    }

    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("Board:\n");
        for (int[] ints : seen) {
            sb.append(Arrays.toString(ints)).append("\n");
        }
        System.out.println(sb.toString());
    }

    private int getNumSurrounding(Point p) {
        return getNumSurrounding(p.x, p.y);
    }

    private List<Point> getUnopenedSurrounding(Point p) {
        return getUnopenedSurrounding(p.x, p.y);
    }

    private final int getNumSurrounding(int x, int y) {
        int n = 0;
        for (int i = Math.max(0, x - 1); i <= Math.min(size - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(size - 1, y + 1); j++) {
                if (actual[i][j]) {
                    n++;
                }
            }
        }
        return n;
    }

    private final List<Point> getUnopenedSurrounding(int x, int y) {
        List<Point> todo = new ArrayList<>();
        for (int i = Math.max(0, x - 1); i <= Math.min(size - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(size - 1, y + 1); j++) {
                if (seen[i][j] == -1) {
                    todo.add(new Point(i, j));
                }
            }
        }
        return todo;
    }

    private static final class Point {
        private final int x;
        private final int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }
    }

    public static void main(String[] args) {
        boolean[][] board = new boolean[][] {
                {
                    false, false, true, true, false
                },
                {
                        false, false, false, true, false
                },
                {
                        true, false, true, true, false
                },
                {
                        false, false, false, false, false
                },
                {
                        false, false, false, true, true
                }
        };
        ShittyMinesweeper sm = new ShittyMinesweeper(board);
        sm.doIDieNow(4,1);
        sm.printBoard();
        sm.doIDieNow(0,2);
        sm.printBoard();


    }

}
