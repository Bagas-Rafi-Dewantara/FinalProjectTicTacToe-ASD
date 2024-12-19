package TicTacToe;

import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

public class TTTGraphics {
    public JFrame frame;
    private Board board;
    private Cell game;
    private String player1Name;
    private String player2Name;
    private Token player1Token;
    private Token player2Token;
    private Image bgImage;
    private Image bgImageAI;

    public TTTGraphics() {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);  // Ukuran frame, bisa disesuaikan
        frame.setLayout(new BorderLayout());

        frame.setLocationRelativeTo(null);

        try {
            // Muat gambar background dari folder src atau path lain
            bgImage = ImageIO.read(getClass().getResource("TTT.jpg"));  // Pastikan path sudah benar
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Kelas untuk menggambar background
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Gambar background
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void showMenu() {
        BackgroundPanel PanelTicTacToe = new BackgroundPanel();
        PanelTicTacToe.setLayout(new GridBagLayout());  // Menggunakan GridBagLayout untuk kontrol yang lebih baik

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20);  // Menambahkan jarak antara elemen dan panel

        JButton vsHuman = new JButton("Player vs Player");
        JButton vsAI = new JButton("Player vs Computer");

        vsHuman.setFont(new Font("Times New Roman", Font.BOLD, 40));
        vsAI.setFont(new Font("Times New Roman", Font.BOLD, 40));
        vsHuman.setForeground(Color.white);
        vsAI.setForeground(Color.white);

        vsAI.setBackground(new Color(62, 88, 121));  // Ubah warna background sesuai keinginan
        vsHuman.setBackground(new Color(33, 53, 85));  // Ubah warna background sesuai keinginan

        vsHuman.addActionListener(e -> startGame(false, null));
        vsAI.addActionListener(e -> showAIModeMenu());

        gbc.gridy = 1;  // Pindahkan tombol ke baris berikutnya
        PanelTicTacToe.add(vsHuman, gbc);

        gbc.gridy = 2;  // Pindahkan tombol ke baris berikutnya
        PanelTicTacToe.add(vsAI, gbc);

        frame.getContentPane().removeAll();
        frame.add(PanelTicTacToe, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private class BackgroundPanelAI extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Gambar background
            g.drawImage(bgImageAI, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void showAIModeMenu() {
        JFrame frame = new JFrame("AI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);  // Ukuran frame, bisa disesuaikan
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        try {
            // Muat gambar background dari folder src atau path lain
            bgImageAI = ImageIO.read(getClass().getResource("TTT.jpg"));  // Pastikan path sudah benar
        } catch (IOException e) {
            e.printStackTrace();
        }

        BackgroundPanelAI PanelAI = new BackgroundPanelAI();
        PanelAI.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20);  // Menambahkan jarak antara elemen dan panel

        JLabel label = new JLabel("Choose AI Difficulty:", SwingConstants.CENTER);
        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");

        label.setFont(new Font("Times New Roman", Font.BOLD, 30));
        easy.setFont(new Font("Times New Roman", Font.BOLD, 30));
        medium.setFont(new Font("Times New Roman", Font.BOLD, 30));
        hard.setFont(new Font("Times New Roman", Font.BOLD, 30));

        label.setForeground(Color.white);
        easy.setForeground(Color.WHITE);
        medium.setForeground(Color.WHITE);
        hard.setForeground(Color.WHITE);

        easy.setBackground(new Color(33, 53, 85));
        medium.setBackground(new Color(62, 88, 121));
        hard.setBackground(new Color(62, 88, 121));

        easy.addActionListener(e -> startGame(true, AILevel.Easy));
        medium.addActionListener(e -> startGame(true, AILevel.Medium));
        hard.addActionListener(e -> startGame(true, AILevel.Hard));

        PanelAI.add(label, gbc);

        gbc.gridy = 1;
        PanelAI.add(easy, gbc);

        gbc.gridy = 2;
        PanelAI.add(medium, gbc);

        gbc.gridy = 3;
        PanelAI.add(hard, gbc);

        frame.getContentPane().removeAll();
        frame.add(PanelAI, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
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

        // Membuat dan memulai game
        board = new Board();
        game = new Cell(board, vsAI, aiLevel, frame, player1Name, player2Name, player1Token, player2Token);
        game.start("/TicTacToe/image/bc_malam.jpg");
    }
}
