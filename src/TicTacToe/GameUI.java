package TicTacToe;

import TicTacToe.AILevel;
import TicTacToe.Board;

import javax.swing.*;
import java.awt.*;

public class GameUI {
    private JFrame frame;
    private Board board;
    private GameMain game;

    public GameUI() {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());
    }

    public void showMenu() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JLabel label = new JLabel("Choose Game Mode:", SwingConstants.CENTER);
        JButton vsHuman = new JButton("Player vs Player");
        JButton vsAI = new JButton("Player vs Computer");

        vsHuman.addActionListener(e -> startGame(false, null));
        vsAI.addActionListener(e -> showAIModeMenu());

        panel.add(label);
        panel.add(vsHuman);
        panel.add(vsAI);

        frame.getContentPane().removeAll();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void showAIModeMenu() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JLabel label = new JLabel("Choose AI Difficulty:", SwingConstants.CENTER);
        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");

        easy.addActionListener(e -> startGame(true, AILevel.Easy));
        medium.addActionListener(e -> startGame(true, AILevel.Medium));
        hard.addActionListener(e -> startGame(true, AILevel.Hard));

        panel.add(label);
        panel.add(easy);
        panel.add(medium);
        panel.add(hard);

        frame.getContentPane().removeAll();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void startGame(boolean vsAI, AILevel aiLevel) {
        board = new Board();
        game = new GameMain(board, vsAI, aiLevel, frame);
        game.start();
    }
}

