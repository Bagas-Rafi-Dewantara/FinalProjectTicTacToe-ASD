/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #11
 * 1 - 5026231018 - Izzuddin Hamadi Faiz
 * 2 - 5026231091 - Bagas Rafi Dewantara
 * 3 - 5026231116 - I Putu Febryan Khrisyantara
 */

package Connect4;

public class Board {
    private Token[][] board;
    public static final int ROWS = 6;
    public static final int COLS = 7;

    public Board() {
        board = new Token[ROWS][COLS];
        reset();
    }

    public void reset() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = Token.Empty;
            }
        }
    }

    public boolean isValidMove(int col) {
        return board[0][col] == Token.Empty; // Cek jika kolom masih memiliki ruang
    }

    public int makeMove(int col, Token token) {
        for (int row = ROWS - 1; row >= 0; row--) { // Cari baris kosong dari bawah ke atas
            if (board[row][col] == Token.Empty) {
                board[row][col] = token;
                return row; // Return baris tempat token ditempatkan
            }
        }
        return -1; // Jika kolom penuh
    }


    public boolean isWinningMove(Token token, int row, int col) {
        return checkDirection(token, row, col, 1, 0) // Horizontal
                || checkDirection(token, row, col, 0, 1) // Vertikal
                || checkDirection(token, row, col, 1, 1) // Diagonal kanan bawah
                || checkDirection(token, row, col, 1, -1); // Diagonal kiri bawah
    }

    private boolean checkDirection(Token token, int row, int col, int rowDelta, int colDelta) {
        int count = 0;
        for (int d = -3; d <= 3; d++) { // Cek 3 langkah ke kiri/atas dan 3 ke kanan/bawah
            int newRow = row + d * rowDelta;
            int newCol = col + d * colDelta;
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && board[newRow][newCol] == token) {
                count++;
                if (count == 4) return true; // Jika 4 token berurutan ditemukan
            } else {
                count = 0;
            }
        }
        return false;
    }

    public boolean isGameOver() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == Token.Empty) return false; // Masih ada ruang di board
        }
        return true;
    }

    public Token getCellType(int row, int col) {
        return board[row][col];
    }
}
