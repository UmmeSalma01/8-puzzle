
public class PuzzleState implements Comparable<PuzzleState> {

    int[][] board;
    int x, y, cost, heuristic;
    PuzzleState parent;

    public PuzzleState(int[][] board, int x, int y, int cost, PuzzleState parent) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.parent = parent;
        this.heuristic = manhattan();
    }

    int manhattan() {
        int d = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                int v = board[i][j];
                if (v != 0) {
                    int tx = (v - 1) / 3;
                    int ty = (v - 1) % 3;
                    d += Math.abs(i - tx) + Math.abs(j - ty);
                }
            }
        return d;
    }

    public int compareTo(PuzzleState o) {
        return (cost + heuristic) - (o.cost + o.heuristic);
    }
}
