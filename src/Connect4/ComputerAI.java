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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class ComputerAI {

    private Random r;

    public ComputerAI() {
        this.r = new Random(); // Initialize random for AI
    }

    public Point turn(Board b, Token t) {
        Point p = win(b, t); // Try to win

        if (p == null) {
            p = blockWin(b, t); // Block opponent's winning move
        }

        if (p == null) {
            p = chooseStrategicMove(b, t); // Choose strategic move
        }

        if (p == null) {
            p = random(b, t); // Random move as fallback
        }

        System.out.println("AI move: " + p.x + "," + p.y);
        return p;
    }

    private Point win(Board b, Token t) {
        // Implement the logic to identify winning moves for Connect Four
        return null; // Placeholder for actual implementation
    }

    private Point blockWin(Board b, Token t) {
        // Implement the logic to block opponent's winning moves for Connect Four
        return null; // Placeholder for actual implementation
    }

    private Point chooseStrategicMove(Board b, Token t) {
        // AI will try to take the center column if available
        int centerColumn = b.COLS / 2;
        for (int i = b.ROWS - 1; i >= 0; i--) {
            if (b.getCellType(i, centerColumn) == Token.Empty) {
                return new Point(i, centerColumn);
            }
        }
        // If center is not available, pick a random column
        return chooseRandomColumn(b, t);
    }

    private Point chooseRandomColumn(Board b, Token t) {
        ArrayList<Point> options = new ArrayList<>();
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 6; i++) {
                if (b.getCellType(i, j) == Token.Empty) {
                    options.add(new Point(i, j));
                    break; // Only consider the lowest empty spot in the column
                }
            }
        }

        if (!options.isEmpty()) {
            return options.get(this.r.nextInt(options.size()));
        }
        return null;
    }

    private Point random(Board b, Token t) {
        ArrayList<Point> options = new ArrayList<>();
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 6; i++) {
                if (b.getCellType(i, j) == Token.Empty) {
                    options.add(new Point(i, j));
                    break; // Only consider the lowest empty spot in the column
                }
            }
        }

        return options.get(this.r.nextInt(options.size()));
    }

    private Token getOpponent(Token t) {
        return t == Token.X ? Token.O : Token.X;
    }
}
