package TicTacToe;

import javax.swing.*;
import java.awt.*;

public class Cell {
    private Board board;
    private boolean isPlayerVsComputer;
    private AILevel aiLevel;
    private ComputerAI computerAI;
    private JFrame frame;
    private JButton[][] buttons;

    private boolean isPlayer1Turn = true;
    private int turn = 1;
    private String player1Name;
    private String player2Name;
    private Token player1Token;
    private Token player2Token;

    public Cell(Board board, boolean isPlayerVsComputer, AILevel aiLevel, JFrame frame,
                String player1Name, String player2Name, Token player1Token, Token player2Token) {
        this.board = board;
        this.isPlayerVsComputer = isPlayerVsComputer;
        this.aiLevel = aiLevel;
        this.frame = frame;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Token = player1Token;
        this.player2Token = player2Token;

        if (isPlayerVsComputer) {
            this.computerAI = new ComputerAI();
            this.computerAI.setLevel(aiLevel);
        }

        SoundEffect.BACKGROUND.loop();
    }


    public Cell(Board board, boolean isPlayerVsComputer, AILevel aiLevel, JFrame frame) {
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
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                frame.add(buttons[i][j]);
            }
        }

        frame.setVisible(true);
        SoundEffect.BACKGROUND.loop();
    }

    private void computerTurn() {
        // Dapatkan langkah dari AI
        Point move = computerAI.turn(board, player2Token, turn);

        // Lakukan langkah untuk komputer
        if (move != null) {
            handleMove(move.x, move.y);
        }
    }


    private void resetGame() {
        board.reset();
        isPlayer1Turn = true;
        turn = 1;

        // Reset tombol pada UI
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        JOptionPane.showMessageDialog(frame, "Game has been reset. Let's play again!");
    }


    private void handleMove(int row, int col) {
        if (!board.isValidMove(row, col)) return;

        Token currentPlayerToken = isPlayer1Turn ? player1Token : player2Token;
        board.makeMove(row, col, currentPlayerToken);
        buttons[row][col].setText(currentPlayerToken.toString());

        if (currentPlayerToken == player1Token) {
            SoundEffect.CROSS_SOUND.play();
        } else {
            SoundEffect.NOUGH_SOUND.play();
        }

        if (board.isWinningMove(currentPlayerToken)) {
            String winner = isPlayer1Turn ? player1Name : player2Name;
            JOptionPane.showMessageDialog(frame, winner + " wins!");
            SoundEffect.BACKGROUND.stop();
            resetGame();
            return;
        }

        if (board.isGameOver()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            SoundEffect.BACKGROUND.stop();
            resetGame();
            return;
        }

        isPlayer1Turn = !isPlayer1Turn;

        if (isPlayerVsComputer && !isPlayer1Turn) {
            computerTurn();
        }
    }



}

