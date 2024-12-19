public class Board {
    private Token[][] board;

    public Board() {
        board = new Token[3][3];
        reset();
    }

    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = Token.Empty;
            }
        }
    }

    public boolean isValidMove(int row, int col) {
        return board[row][col] == Token.Empty;
    }

    public void makeMove(int row, int col, Token token) {
        board[row][col] = token;
    }

    public boolean isWinningMove(Token token) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == token && board[i][1] == token && board[i][2] == token) ||
                    (board[0][i] == token && board[1][i] == token && board[2][i] == token)) {
                return true;
            }
        }

        return (board[0][0] == token && board[1][1] == token && board[2][2] == token) ||
                (board[0][2] == token && board[1][1] == token && board[2][0] == token);
    }

    public boolean isGameOver() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Token.Empty) return false;
            }
        }
        return true;
    }

    public Token getCellType(int row, int col) {
        return board[row][col];
    }
}
