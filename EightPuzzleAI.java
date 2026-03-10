import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class EightPuzzleAI extends JFrame {

    JButton[][] tiles = new JButton[3][3];
    JTextArea output = new JTextArea(10, 25);
    JLabel stepLabel = new JLabel("User Steps: 0");

    int[][] board = new int[3][3];
    int blankX = 2, blankY = 2;
    int userSteps = 0;

    int[][] goal = {{1,2,3},{4,5,6},{7,8,0}};

    JComboBox<String> difficultyBox;

    public EightPuzzleAI() {

        setTitle("8 Puzzle AI – Play + A* Solver");
        setSize(500, 550);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel grid = new JPanel(new GridLayout(3,3));
        Font f = new Font("Arial", Font.BOLD, 20);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j] = new JButton();
                tiles[i][j].setFont(f);
                int r = i, c = j;

                tiles[i][j].addActionListener(e -> moveTile(r,c));
                grid.add(tiles[i][j]);
            }
        }

        String[] levels = {"Easy", "Medium", "Hard"};
        difficultyBox = new JComboBox<>(levels);

        JButton randomBtn = new JButton("Random");
        JButton solveBtn = new JButton("Solve");

        randomBtn.addActionListener(e -> {
            String level = (String) difficultyBox.getSelectedItem();
            generateByDifficulty(level);
        });

        solveBtn.addActionListener(e -> solvePuzzle());

        JPanel top = new JPanel();
        top.add(new JLabel("Difficulty:"));
        top.add(difficultyBox);
        top.add(randomBtn);
        top.add(solveBtn);
        top.add(stepLabel);

        output.setEditable(false);
        JScrollPane scroll = new JScrollPane(output);

        add(top, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        generateByDifficulty("Easy");
        setVisible(true);
    }

    void generateByDifficulty(String level) {

        int moves = 10;
        if (level.equals("Medium")) moves = 25;
        if (level.equals("Hard")) moves = 50;

        board = copy(goal);
        blankX = 2;
        blankY = 2;

        Random rand = new Random();

        int[] dx = {0,0,1,-1};
        int[] dy = {1,-1,0,0};

        for (int i = 0; i < moves; i++) {

            List<Integer> validMoves = new ArrayList<>();

            for (int j = 0; j < 4; j++) {
                int nx = blankX + dx[j];
                int ny = blankY + dy[j];

                if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {
                    validMoves.add(j);
                }
            }

            int move = validMoves.get(rand.nextInt(validMoves.size()));
            int nx = blankX + dx[move];
            int ny = blankY + dy[move];

            board[blankX][blankY] = board[nx][ny];
            board[nx][ny] = 0;
            blankX = nx;
            blankY = ny;
        }

        userSteps = 0;
        stepLabel.setText("User Steps: 0");
        updateBoard();
        output.setText("");
    }

    void moveTile(int r, int c) {

        if ((Math.abs(r - blankX) + Math.abs(c - blankY)) == 1) {

            board[blankX][blankY] = board[r][c];
            board[r][c] = 0;
            blankX = r;
            blankY = c;

            userSteps++;
            stepLabel.setText("User Steps: " + userSteps);

            updateBoard();

            if (Arrays.deepEquals(board, goal)) {
                JOptionPane.showMessageDialog(this,
                        "You solved it! \nYour Steps: " + userSteps);
            }
        }
    }

    void updateBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0)
                    tiles[i][j].setText("");
                else
                    tiles[i][j].setText(String.valueOf(board[i][j]));
            }
    }

    void solvePuzzle() {

        java.util.List<PuzzleState> path =
                AStarSolver.solve(board, blankX, blankY);

        if (path == null) {
            output.setText("No solution found.");
            return;
        }

        output.setText("A* Solution Path:\n\n");

        for (PuzzleState s : path) {
            for (int[] row : s.board) {
                for (int v : row) output.append(v + " ");
                output.append("\n");
            }
            output.append("\n");
        }

        int optimalSteps = path.size() - 1;

        output.append("Optimal Steps = " + optimalSteps + "\n");
        output.append("Your Steps = " + userSteps + "\n");
    }

    int[][] copy(int[][] b) {
        int[][] n = new int[3][3];
        for (int i = 0; i < 3; i++)
            n[i] = b[i].clone();
        return n;
    }

    public static void main(String[] args) {
        new EightPuzzleAI();
    }
}
