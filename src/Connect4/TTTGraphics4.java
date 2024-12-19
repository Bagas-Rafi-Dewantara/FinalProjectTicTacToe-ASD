package Connect4;

import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

public class TTTGraphics4 {
    public JFrame frame;
    private Board board;
    private Cell game;
    private String player1Name;
    private String player2Name;
    private Token player1Token;
    private Token player2Token;
    private Image bgImage3;


    public TTTGraphics4() {
        frame = new JFrame("Connect Four");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set ukuran yang lebih besar untuk Connect Four
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        try {
            // Muat gambar background dari folder src atau path lain
            bgImage3 = ImageIO.read(getClass().getResource("image/TTT.jpg"));  // Pastikan path sudah benar
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Gambar background
            g.drawImage(bgImage3, 0, 0, getWidth(), getHeight(), this);
        }
    }


    public void showMenu() {
        BackgroundPanel C4Panel = new BackgroundPanel();
        //C4Panel.setOpaque(false);
        C4Panel.setLayout(new GridBagLayout()); // Menggunakan GridBagLayout untuk kontrol tata letak yang fleksibel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20); // Menambahkan margin antar elemen

        JButton vsHuman = new JButton("Player vs Player");
        JButton vsAI = new JButton("Player vs Computer");

        vsHuman.setFont(new Font("Times New Roman", Font.BOLD, 40));
        vsAI.setFont(new Font("Times New Roman", Font.BOLD, 40));
        vsHuman.setForeground(Color.white);
        vsAI.setForeground(Color.white);

        vsAI.setBackground(new Color(62, 88, 121)); // Ubah warna background sesuai keinginan
        vsHuman.setBackground(new Color(33, 53, 85)); // Ubah warna background sesuai keinginan

        vsHuman.addActionListener(e -> startGame(false)); // Player vs Player
        vsAI.addActionListener(e -> startGame(true)); // Player vs Computer

        gbc.gridy = 1; // Pindahkan tombol ke baris berikutnya
        C4Panel.add(vsHuman, gbc);

        gbc.gridy = 2; // Pindahkan tombol ke baris berikutnya
        C4Panel.add(vsAI, gbc);

        frame.getContentPane().removeAll();
        frame.add(C4Panel, BorderLayout.CENTER); // Tambahkan panel ke frame
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }


    private void startGame(boolean vsAI) {
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
        game = new Cell(board, vsAI, frame, player1Name, player2Name, player1Token, player2Token);
        game.start("/Connect4/image/bc_malam.jpg");
    }

}
