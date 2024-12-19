package TicTacToe;

import javax.swing.*;
import java.awt.*;

public class TTTGraphics {
    private JFrame frame;
    private Board board;
    private Cell game;
    private String player1Name;
    private String player2Name;
    private Token player1Token;
    private Token player2Token;


    public TTTGraphics() {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);  // Ukuran frame, bisa disesuaikan
        frame.setLayout(new BorderLayout());
    }

    public void showMenu() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JLabel label = new JLabel("Choose Game Mode:", SwingConstants.CENTER);
        JButton vsHuman = new JButton("Player vs Player");
        JButton vsAI = new JButton("Player vs Computer");

        label.setFont(new Font("Arial", Font.BOLD, 40));  // Ukuran font lebih besar

        // Menetapkan font yang lebih besar untuk tombol
        vsHuman.setFont(new Font("Arial", Font.BOLD, 40));
        vsAI.setFont(new Font("Arial", Font.BOLD, 40));

        vsHuman.addActionListener(e -> startGame(false, null));
        vsAI.addActionListener(e -> showAIModeMenu());

        panel.add(label);
        panel.add(vsHuman);
        panel.add(vsAI);

        frame.getContentPane().removeAll();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Menambahkan pengaturan lokasi agar frame muncul di tengah layar
        frame.setLocationRelativeTo(null);  // Menampilkan JFrame di tengah
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

        // Menambahkan pengaturan lokasi agar frame muncul di tengah layar
        frame.setLocationRelativeTo(null);  // Menampilkan JFrame di tengah
    }

    private void startGame(boolean vsAI, AILevel aiLevel) {
        // Input nama pemain
        player1Name = JOptionPane.showInputDialog(frame, "Enter Player 1 Name:");
        if (!vsAI) {
            player2Name = JOptionPane.showInputDialog(frame, "Enter Player 2 Name:");
        } else {
            player2Name = "Computer";
        }

        // Input simbol pemain
        Object[] options = {Token.X, Token.O};
        player1Token = (Token) JOptionPane.showInputDialog(
                frame, "Choose Player 1 Token:", "Token Selection",
                JOptionPane.PLAIN_MESSAGE, null, options, Token.X
        );

        player2Token = (player1Token == Token.X) ? Token.O : Token.X;

        board = new Board();
        game = new Cell(board, vsAI, aiLevel, frame, player1Name, player2Name, player1Token, player2Token);
        game.start();
    }

}
