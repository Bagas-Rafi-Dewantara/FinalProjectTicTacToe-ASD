package Connect4;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class ComputerAI {
    private Random r;

    public ComputerAI() {
        this.r = new Random(); // Inisialisasi random untuk AI
    }

    public Point turn(Board b, Token t, int turn) {
        // Langsung gunakan logika "hard" sebagai default
        return this.hard(b, t, turn);
    }

    private Point hard(Board b, Token t, int turn) {
        Point p = win(b, t);

        if (p == null) {
            p = blockWin(b, t);
        }

        if (turn % 2 == 0) {
            if (p == null && turn == 4) {
                p = this.defenceDiagnalAttack(b, t);
            }

            if (p == null && turn == 4) {
                p = defenceCornerChooser(b, t);
            }

            if (p == null) {
                p = defenceMoves(b, t);
            }
        } else {
            if (p == null) {
                p = this.chooseRandomCorner(b, t);
            }
        }

        if (p == null) {
            p = this.random(b, t);
        }
        System.out.println("Hard mode AI move: " + p.x + "," + p.y);
        return p;
    }
    private boolean equalsOpponent(Connect4.Token boardToken, Connect4.Token t){
        return boardToken != t && boardToken != Connect4.Token.Empty;
    }

    private Point defenceDiagnalAttack(Connect4.Board b, Connect4.Token t){
        if(this.equalsOpponent(b.getCellType(0, 0), t) && this.equalsOpponent(b.getCellType(2, 2), t) ||
                this.equalsOpponent(b.getCellType(0, 2), t) && this.equalsOpponent(b.getCellType(2, 0), t)) {
            ArrayList<Point> options = new ArrayList<>();
            for(int k = -1; k < 2; k += 2){
                if(1 + k >= 0 && 1 + k < 3) {
                    if(b.getCellType(1, 1 + k) == Connect4.Token.Empty){
                        options.add(new Point(1, 1 + k));
                    }

                    if(b.getCellType(1 + k, 1) == Connect4.Token.Empty){
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

    private Point defenceCornerChooser(Connect4.Board b, Connect4.Token t){
        ArrayList<Point> options = new ArrayList<>();
        int sum;
        for(int i = 0; i < 3; i += 2){
            for(int j = 0; j < 3; j += 2){
                sum = 0;
                if(b.getCellType(i, j) == Connect4.Token.Empty){
                    for(int k = -1; k < 2; k += 2){
                        if(j + k >= 0 && j + k < 3) {
                            if(b.getCellType(i, j + k) == t){
                                sum--;
                            } else if(b.getCellType(i, j + k) != Connect4.Token.Empty){
                                sum++;
                            }
                        }
                    }

                    for(int k = -1; k < 2; k += 2){
                        if(i + k >= 0 && i + k < 3) {
                            if(b.getCellType(i + k, j) == t){
                                sum--;
                            } else if(b.getCellType(i + k, j) != Connect4.Token.Empty){
                                sum++;
                            }
                        }
                    }
                    if(sum == 2){
                        options.add(new Point(i, j));
                    }
                }
            }
        }

        if(!options.isEmpty()) {
            return options.get(this.r.nextInt(options.size()));
        }
        return null;
    }

    private Point defenceMoves(Connect4.Board b, Connect4.Token t){
        if(b.getCellType(1, 1) == Connect4.Token.Empty){
            return new Point(1, 1);
        } else {
            return this.chooseRandomCorner(b, t);
        }
    }

    private Point chooseRandomCorner(Connect4.Board b, Connect4.Token t){
        ArrayList<Point> options = new ArrayList<>();
        for(int i = 0; i < 3; i += 2){
            for(int j = 0; j < 3; j += 2){
                if(b.getCellType(i, j) == Connect4.Token.Empty){
                    options.add(new Point(i, j));
                }
            }
        }

        if(!options.isEmpty()) {
            return options.get(this.r.nextInt(options.size()));
        }
        return null;
    }

    private Point win(Connect4.Board b, Connect4.Token t){
        // Logic remains the same, using b.getCellType instead of c.getType()
        return null; // Placeholder for actual implementation
    }

    Point blockWin(Connect4.Board b, Connect4.Token t) {
        // Logic remains the same, using b.getCellType instead of c.getType()
        return null; // Placeholder for actual implementation
    }

    Point random(Connect4.Board b, Connect4.Token t) {
        ArrayList<Point> options = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(b.getCellType(i, j) == Connect4.Token.Empty){
                    options.add(new Point(i, j));
                }
            }
        }

        return options.get(this.r.nextInt(options.size()));
    }

    // Semua metode pendukung lainnya tetap sama
}
