import java.util.*;

public class AStarSolver {

    static int[][] goal = {{1,2,3},{4,5,6},{7,8,0}};

    public static List<PuzzleState> solve(int[][] start, int blankX, int blankY) {

        PriorityQueue<PuzzleState> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        pq.add(new PuzzleState(copy(start), blankX, blankY, 0, null));

        while (!pq.isEmpty()) {

            PuzzleState curr = pq.poll();
            String key = Arrays.deepToString(curr.board);

            if (visited.contains(key)) continue;
            visited.add(key);

            if (Arrays.deepEquals(curr.board, goal)) {
                return buildPath(curr);
            }

            int[] dx = {0,0,1,-1};
            int[] dy = {1,-1,0,0};

            for (int i = 0; i < 4; i++) {
                int nx = curr.x + dx[i];
                int ny = curr.y + dy[i];

                if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {

                    int[][] newBoard = copy(curr.board);

                    newBoard[curr.x][curr.y] = newBoard[nx][ny];
                    newBoard[nx][ny] = 0;

                    pq.add(new PuzzleState(newBoard, nx, ny,
                            curr.cost + 1, curr));
                }
            }
        }

        return null;
    }

    static List<PuzzleState> buildPath(PuzzleState goalState) {
        List<PuzzleState> path = new ArrayList<>();

        while (goalState != null) {
            path.add(goalState);
            goalState = goalState.parent;
        }

        Collections.reverse(path);
        return path;
    }

    static int[][] copy(int[][] b) {
        int[][] n = new int[3][3];
        for (int i = 0; i < 3; i++)
            n[i] = b[i].clone();
        return n;
    }
}
