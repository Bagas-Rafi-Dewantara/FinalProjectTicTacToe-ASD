package TicTacToe;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class ComputerAI {

    private Random r;
    private AILevel level;

    public ComputerAI(){
        this.r = new Random();
        this.level = AILevel.Hard;
    }

    public void setLevel(AILevel l){
        this.level = l;
    }

    public Point turn(Board b, Token t, int turn){
        Point p;
        if(this.level == AILevel.Easy){
            p = this.easy(b, t, turn);
        } else if(this.level == AILevel.Medium){
            p = this.medium(b, t, turn);
        } else {
            p = this.hard(b, t, turn);
        }
        return p;
    }

    private Point easy(Board b, Token t, int turn) {
        Point p = win(b, t); // Coba menang

        if (p == null) {
            p = blockWin(b, t); // Blokir kemenangan lawan
        }

        if (p == null) {
            p = findClosestEmpty(b); // Cari ruang kosong terdekat
        }

        System.out.println("Easy mode selected, AI move: " + p.x + "," + p.y);
        return p;
    }

    // Tambahkan metode untuk mencari ruang kosong terdekat
    private Point findClosestEmpty(Board b) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b.getCellType(i, j) == Token.Empty) {
                    return new Point(i, j); // Pilih kotak kosong pertama yang ditemukan
                }
            }
        }
        return null; // Tidak ada ruang kosong
    }

    private Point medium(Board b, Token t, int turn) {
        Point p = win(b, t); // Coba menang

        if (p == null) {
            p = blockWin(b, t); // Blokir kemenangan lawan
        }

        if (p == null) {
            p = defenceDiagnalAttack(b, t); // Serangan diagonal
        }

        if (p == null) {
            p = chooseStrategicMove(b, t); // Pilih langkah strategis
        }

        if (p == null) {
            p = random(b, t); // Langkah acak jika tidak ada pilihan lain
        }

        System.out.println("Medium mode selected, AI move: " + p.x + "," + p.y);
        return p;
    }

    // Metode untuk langkah strategis di Medium Mode
    private Point chooseStrategicMove(Board b, Token t) {
        // AI akan mencoba mengambil tengah jika kosong
        if (b.getCellType(1, 1) == Token.Empty) {
            return new Point(1, 1);
        }

        // Jika tengah tidak tersedia, pilih sudut
        return chooseRandomCorner(b, t);
    }


    private Point hard(Board b, Token t, int turn) {
        Point p = win(b, t); // Prioritas menang

        if (p == null) {
            p = blockWin(b, t); // Blokir kemenangan lawan
        }

        if (p == null) {
            p = minimaxMove(b, t, true); // Pilih langkah terbaik menggunakan Minimax
        }

        if (p == null) {
            p = random(b, t); // Langkah acak sebagai fallback
        }

        System.out.println("Hard mode selected, AI move: " + p.x + "," + p.y);
        return p;
    }

    // Implementasi Minimax untuk Hard Mode
    private Point minimaxMove(Board b, Token t, boolean isMaximizing) {
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Point bestMove = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b.getCellType(i, j) == Token.Empty) {
                    b.makeMove(i, j, isMaximizing ? t : getOpponent(t)); // Simulasi langkah

                    int score = minimax(b, t, 0, !isMaximizing); // Hitung skor langkah ini

                    b.makeMove(i, j, Token.Empty); // Batalkan simulasi langkah

                    // Pilih langkah terbaik berdasarkan skor
                    if (isMaximizing ? score > bestScore : score < bestScore) {
                        bestScore = score;
                        bestMove = new Point(i, j);
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(Board b, Token t, int depth, boolean isMaximizing) {
        if (b.isWinningMove(t)) {
            return 10 - depth; // Semakin cepat menang, semakin baik
        }
        if (b.isWinningMove(getOpponent(t))) {
            return depth - 10; // Semakin cepat kalah, semakin buruk
        }
        if (b.isGameOver()) {
            return 0; // Seri
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b.getCellType(i, j) == Token.Empty) {
                    b.makeMove(i, j, isMaximizing ? t : getOpponent(t));

                    int score = minimax(b, t, depth + 1, !isMaximizing);

                    b.makeMove(i, j, Token.Empty);

                    bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }

    private Token getOpponent(Token t) {
        return t == Token.X ? Token.O : Token.X;
    }


    private boolean equalsOpponent(Token boardToken, Token t){
        return boardToken != t && boardToken != Token.Empty;
    }

    private Point defenceDiagnalAttack(Board b, Token t){
        if(this.equalsOpponent(b.getCellType(0, 0), t) && this.equalsOpponent(b.getCellType(2, 2), t) ||
                this.equalsOpponent(b.getCellType(0, 2), t) && this.equalsOpponent(b.getCellType(2, 0), t)) {
            ArrayList<Point> options = new ArrayList<>();
            for(int k = -1; k < 2; k += 2){
                if(1 + k >= 0 && 1 + k < 3) {
                    if(b.getCellType(1, 1 + k) == Token.Empty){
                        options.add(new Point(1, 1 + k));
                    }

                    if(b.getCellType(1 + k, 1) == Token.Empty){
                        options.add(new Point(1 + k, 1));
                    }
                }
            }

            if(!options.isEmpty()) {
                return options.get(this.r.nextInt(options.size()));
            }
        }
        return null;
    }


    private Point chooseRandomCorner(Board b, Token t){
        ArrayList<Point> options = new ArrayList<>();
        for(int i = 0; i < 3; i += 2){
            for(int j = 0; j < 3; j += 2){
                if(b.getCellType(i, j) == Token.Empty){
                    options.add(new Point(i, j));
                }
            }
        }

        if(!options.isEmpty()) {
            return options.get(this.r.nextInt(options.size()));
        }
        return null;
    }

    private Point win(Board b, Token t){
        // Logic remains the same, using b.getCellType instead of c.getType()
        return null; // Placeholder for actual implementation
    }

    Point blockWin(Board b, Token t) {
        // Logic remains the same, using b.getCellType instead of c.getType()
        return null; // Placeholder for actual implementation
    }

    Point random(Board b, Token t) {
        ArrayList<Point> options = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(b.getCellType(i, j) == Token.Empty){
                    options.add(new Point(i, j));
                }
            }
        }

        return options.get(this.r.nextInt(options.size()));
    }
}
