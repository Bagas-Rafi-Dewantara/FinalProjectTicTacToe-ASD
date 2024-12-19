package TicTacToe;

import TicTacToe.AILevel;
import TicTacToe.Board;
import TicTacToe.ComputerAI;

import javax.swing.*;
import java.awt.*;

public class GameMain {
    private Board board;
    private boolean isPlayerVsComputer;
    private AILevel aiLevel;
    private ComputerAI computerAI;
    private JFrame frame;
    private JButton[][] buttons;

    private boolean isPlayer1Turn = true;
    private int turn = 1;

    public GameMain(Board board, boolean isPlayerVsComputer, AILevel aiLevel, JFrame frame) {
        this.board = board;
        this.isPlayerVsComputer = isPlayerVsComputer;
        this.aiLevel = aiLevel;
        this.frame = frame;
        if (isPlayerVsComputer) {
            this.computerAI = new ComputerAI();
            this.computerAI.setLevel(aiLevel);
        }

        SoundEffect.BACKGROUND.loop();
    }

    public void start() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                frame.add(buttons[i][j]);
            }
        }

        frame.setVisible(true);
        SoundEffect.BACKGROUND.loop();
    }

    private void handleMove(int row, int col) {
        if (!board.isValidMove(row, col)) return;

        Token currentPlayerToken = isPlayer1Turn ? Token.X : Token.O;
        board.makeMove(row, col, currentPlayerToken);
        buttons[row][col].setText(currentPlayerToken.toString());

        //Menambahkan bunyi saat CROSS ataupun NOUGH diletakkan
        if (currentPlayerToken == Token.X) {
            SoundEffect.CROSS_SOUND.play();
        } else {
            SoundEffect.NOUGH_SOUND.play();
        }

        if (board.isWinningMove(currentPlayerToken)) {
            String winner = isPlayer1Turn ? "Player 1" : isPlayerVsComputer ? "Computer" : "Player 2";
            if (currentPlayerToken == Token.X) {
                SoundEffect.CROSSWIN_SOUND.play();
            } else {
                SoundEffect.NOUGHWIN_SOUND.play();
            }
            JOptionPane.showMessageDialog(frame, winner + " wins!");
            SoundEffect.BACKGROUND.stop();
            resetGame();
            return;
        }

        if (board.isGameOver()) {
            SoundEffect.DRAW_SOUND.play();
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            SoundEffect.BACKGROUND.stop();
            resetGame();
            return;
        }

        isPlayer1Turn = !isPlayer1Turn;
        turn++;

        if (isPlayerVsComputer && !isPlayer1Turn) {
            computerTurn();
        }
    }

    private void computerTurn() {
        Point move = computerAI.turn(board, Token.O, turn);
        handleMove(move.x, move.y);
    }

    private void resetGame() {
        board.reset();
        isPlayer1Turn = true;
        turn = 1;
        start();
    }
}

