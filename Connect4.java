// Names: Marcus Quitiquit, Jacques Antoine Vidjanagni
// ID: 1014489, 100989148

import java.util.Scanner;

class OnePlayerMode {
    Scanner userInput = new Scanner(System.in);
    private char[][] board;
    private static final int win = 4;
    private char player;
    private char aiPlayer;
    private String currentName;
    private String playerName;
    private boolean gameset;
    private int turn;

    public OnePlayerMode(String playerName) {
        this.playerName = playerName;
        turn = 1;
        gameset = false;
        player = 'R';
        aiPlayer = 'Y';
        initializeBoard();
    }

    private void initializeBoard() {
        board = new char[6][7];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = ' ';
            }
        }
    }

    // Start Game
    public void playGame() {
        while (!gameset && turn <= 42) {
            if (player == 'R') {
                System.out.println("Player " + playerName + ", choose a column (1-7):");
                connect4Board(board);
                int col = userInput.nextInt() - 1;
                userInput.nextLine();
                if (add(col, player)) {
                    gameset = winCondition(player);
                    player = aiPlayer; 
                    turn++;
                }
            } 
            else {
                int aiMove = getAIMove();
                System.out.println("AI played column: " + (aiMove + 1));
                if (add(aiMove, aiPlayer)) {
                    gameset = winCondition(aiPlayer);
                    player = 'R'; 
                    turn++;
                }
            }
        }
        if (gameset) {
            System.out.println(player == 'R' ? playerName + " Won!" : "AI Won!");
        } 
        else {
            System.out.println("It's a tie!");
        }
    }

    // Connect 4 Board
    public void connect4Board(char[][] board) {
        System.out.println("-----------------------------");
        for (int i = 0; i < board.length; i++) {
            System.out.print("|");
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(" " + board[i][j] + " |");
            }
            System.out.println();
        }
        System.out.println("-----------------------------");
        System.out.println("  1   2   3   4   5   6   7");
    }

    // Add
    public boolean add(int c, char player) {
        if (c < 0 || c >= board[0].length) {
            System.out.println("Invalid column number");
            return false;
        }
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][c] == ' ') {
                board[i][c] = player;
                return true;
            }
        }
        System.out.println("Column is full, choose another one.");
        return false;
    }

    // AI Move
    private int getAIMove() {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < board[0].length; i++) {
            if (board[0][i] == ' ') {
                int score = minimax(board, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, aiPlayer);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    private int minimax(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer, char currentPlayer) {
        if (depth == 0 || winCondition(aiPlayer) || winCondition(player))
            return evaluateBoard(board);

        char opponentPlayer = (currentPlayer == aiPlayer) ? player : aiPlayer;

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < board[0].length; i++) {
                if (board[0][i] == ' ') {
                    char[][] newBoard = cloneBoard(board);
                    add(i, currentPlayer, newBoard);
                    int eval = minimax(newBoard, depth - 1, alpha, beta, false, opponentPlayer);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha)
                        break;
                }
            }
            return maxEval;
        } 
        else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < board[0].length; i++) {
                if (board[0][i] == ' ') {
                    char[][] newBoard = cloneBoard(board);
                    add(i, currentPlayer, newBoard);
                    int eval = minimax(newBoard, depth - 1, alpha, beta, true, opponentPlayer);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha)
                        break;
                }
            }
            return minEval;
        }
    }

    private int evaluateBoard(char[][] board) {
        int score = 0;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (col + 3 < 7) {
                    int aiCount = 0;
                    int playerCount = 0;
                    for (int k = 0; k < 4; k++) {
                        if (board[row][col + k] == aiPlayer)
                            aiCount++;
                        else if (board[row][col + k] == player)
                            playerCount++;
                    }
                    score += aiCount - playerCount;
                }
                if (row + 3 < 6) {
                    int aiCount = 0;
                    int playerCount = 0;
                    for (int k = 0; k < 4; k++) {
                        if (board[row + k][col] == aiPlayer)
                            aiCount++;
                        else if (board[row + k][col] == player)
                            playerCount++;
                    }
                    score += aiCount - playerCount;
                }
                if (row + 3 < 6 && col + 3 < 7) {
                    int aiCount = 0;
                    int playerCount = 0;
                    for (int k = 0; k < 4; k++) {
                        if (board[row + k][col + k] == aiPlayer)
                            aiCount++;
                        else if (board[row + k][col + k] == player)
                            playerCount++;
                    }
                    score += aiCount - playerCount;
                }
                if (row + 3 < 6 && col - 3 >= 0) {
                    int aiCount = 0;
                    int playerCount = 0;
                    for (int k = 0; k < 4; k++) {
                        if (board[row + k][col - k] == aiPlayer)
                            aiCount++;
                        else if (board[row + k][col - k] == player)
                            playerCount++;
                    }
                    score += aiCount - playerCount;
                }
            }
        }
        return score;
    }

    private char[][] cloneBoard(char[][] board) {
        char[][] newBoard = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    private void add(int col, char player, char[][] board) {
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][col] == ' ') {
                board[i][col] = player;
                break;
            }
        }
    }

    // Win Checker
    public boolean checkForWin(char player, int row, int col, int dr, int dc, int count) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length)
            return false;
        if (board[row][col] != player)
            return false;
        if (count == win)
            return true;
        return checkForWin(player, row + dr, col + dc, dr, dc, count + 1);
    }

    // Win Conditions
    public boolean winCondition(char player) {
        // Horizontal
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j <= board[0].length - win; j++) {
                if (checkForWin(player, i, j, 0, 1, 1)) return true;
            }
        }
        // Vertical
        for (int i = 0; i <= board.length - win; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (checkForWin(player, i, j, 1, 0, 1)) return true;
            }
        }
        // Diagonal 1
        for (int i = 0; i <= board.length - win; i++) {
            for (int j = 0; j <= board[0].length - win; j++) {
                if (checkForWin(player, i, j, 1, 1, 1)) return true;
            }
        }
        // Diagonal 2
        for (int i = 0; i <= board.length - win; i++) {
            for (int j = win - 1; j < board[0].length; j++) {
                if (checkForWin(player, i, j, 1, -1, 1)) return true;
            }
        }
        return false;
    }
}

class TwoPlayerMode {
    Scanner userInput = new Scanner(System.in);
    private char[][] board;
    private static final int win = 4;
    private char player;
    private String currentName;
    private String player1Name;
    private String player2Name;
    private boolean gameset;
    private int turn;

    public TwoPlayerMode(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        turn = 1;
        gameset = false;
        currentName = player1Name;
        player = 'R'; // Red Starts
        initializeBoard();
    }

    private void initializeBoard() {
        board = new char[6][7];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = ' ';
            }
        }
    }

    // Start Game
    public void playGame() {
        while (!gameset && turn <= 42) {
            System.out.println("Player " + currentName + ", choose a column (1-7):");
            connect4Board(board);
            int col = userInput.nextInt() - 1;
            userInput.nextLine();
            if (add(col, player)) {
                gameset = winCondition(player);
                if (currentName.equals(player1Name)) {
                    currentName = player2Name;
                } 
                else {
                    currentName = player1Name;
                }
                if (player == 'R') {
                    player = 'Y';
                } 
                else {
                    player = 'R';
                }
                turn++;
            }
        }
        if (gameset) {
            if (player == 'R') {
                System.out.println("Yellow (" + player2Name + ") Won!");
            } 
            else {
                System.out.println("Red (" + player1Name + ") Won!");
            }
        } 
        else {
            System.out.println("It's a tie!");
        }
    }

    // Connect 4 Board
    public void connect4Board(char[][] board) {
        System.out.println("-----------------------------");
        for (int i = 0; i < board.length; i++) {
            System.out.print("|");
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(" " + board[i][j] + " |");
            }
            System.out.println();
        }
        System.out.println("-----------------------------");
        System.out.println("  1   2   3   4   5   6   7");
    }

    // Add or drop a piece on the board
    public boolean add(int c, char player) {
        if (c < 0 || c >= board[0].length) {
            System.out.println("Invalid column number");
            return false;
        }
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][c] == ' ') {
                board[i][c] = player;
                return true;
            }
        }
        System.out.println("Column is full, choose another one.");
        return false;
    }

    // Checks for win conditions
    public boolean checkForWin(char player, int row, int col, int dr, int dc, int count) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length)
            return false;
        if (board[row][col] != player)
            return false;
        if (count == win)
            return true;
        return checkForWin(player, row + dr, col + dc, dr, dc, count + 1);
    }

    // Win Conditions
    public boolean winCondition(char player) {
        // Horizontal
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j <= board[0].length - win; j++) {
                if (checkForWin(player, i, j, 0, 1, 1)) return true;
            }
        }
        // Vertical
        for (int i = 0; i <= board.length - win; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (checkForWin(player, i, j, 1, 0, 1)) return true;
            }
        }
        // Diagonal 1
        for (int i = 0; i <= board.length - win; i++) {
            for (int j = 0; j <= board[0].length - win; j++) {
                if (checkForWin(player, i, j, 1, 1, 1)) return true;
            }
        }
        // Diagonal 2
        for (int i = 0; i <= board.length - win; i++) {
            for (int j = win - 1; j < board[0].length; j++) {
                if (checkForWin(player, i, j, 1, -1, 1)) return true;
            }
        }
        return false;
    }
}

public class Connect4 {
    static Scanner userInput = new Scanner(System.in);

    // Linebreak
    public static void linebreak() {
        System.out.println("---------------------------------");
    }

    // Menu
    public static String menu() {
        System.out.println("Welcome to Connect Four!!!!");
        linebreak();
        System.out.println("Choose a game mode (1-3)");
        System.out.println("1- One Player Mode");
        System.out.println("2- Two Player Mode");
        System.out.println("3- Exit");
        return userInput.nextLine();
    }

    // Main
    public static void main(String[] args) {
        String choice;
        while (true) {
            choice = menu();
            if (choice.equals("1")) {
                System.out.print("Enter your name: ");
                String playerName = userInput.nextLine();
                OnePlayerMode one = new OnePlayerMode(playerName);
                one.playGame();
            } 
            else if (choice.equals("2")) {
                System.out.print("Enter Player 1's name: ");
                String player1Name = userInput.nextLine();
                System.out.print("Enter Player 2's name: ");
                String player2Name = userInput.nextLine();
                TwoPlayerMode two = new TwoPlayerMode(player1Name, player2Name);
                two.playGame();
            } 
            else if (choice.equals("3")) {
                System.out.println("You have exited the program");
                break;
            } 
            else {
                System.out.println("Invalid Input");
            }
        }
    }
}
