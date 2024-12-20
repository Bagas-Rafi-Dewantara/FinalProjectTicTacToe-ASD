package Connect4;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Cell {
    private Board board;
    private boolean isPlayer1Turn = true;
    private JButton[][] buttons;
    private JFrame frame;
    private String player1Name, player2Name;
    private Token player1Token, player2Token;
    private Image backgroundImage;
    private ComputerAI computerAI;
    private ImageIcon xIcon; // Gambar untuk X
    private ImageIcon oIcon; // Gambar untuk O

    public Cell(Board board, boolean isPlayerVsComputer, JFrame frame, String player1Name, String player2Name, Token player1Token, Token player2Token) {
        this.board = board;
        this.frame = frame;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Token = player1Token;
        this.player2Token = player2Token;

        if (isPlayerVsComputer) {
            this.computerAI = new ComputerAI(); // AI langsung menggunakan logika default
        }

        SoundEffect.BACKGROUND.loop();
    }


    private void loadBackgroundImage(String imagePath) {
        try {
            // Ubah ke gambar latar belakang baru yang sesuai dengan style Tic Tac Toe
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (NullPointerException e) {
            System.err.println("Background image not found: " + imagePath);
        }
    }


    private void loadIcons(String xImagePath, String oImagePath) {
        try {
            // Muat gambar langsung sebagai ImageIcon
            xIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(xImagePath)));
            oIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(oImagePath)));
            xIcon = resizeIcon(xImagePath, 80, 80);
            oIcon = resizeIcon(oImagePath, 80, 80);

        } catch (NullPointerException e) {
            System.err.println("Error loading icon images: " + e.getMessage());
        }
    }

    private ImageIcon resizeIcon(String imagePath, int width, int height) {
        try {
            Image img = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (NullPointerException e) {
            System.err.println("Error resizing icon: " + e.getMessage());
            return null;
        }
    }



    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public void start(String backgroundPath) {
        // Muat gambar latar belakang
        loadBackgroundImage(backgroundPath);

        // Muat ikon X dan O
        loadIcons("/Connect4/image/crossc4.png", "/Connect4/image/noughc4.png");

        // Bersihkan semua komponen sebelumnya
        frame.getContentPane().removeAll();

        // Buat panel untuk grid Connect Four
        BackgroundPanel panels = new BackgroundPanel();
        panels.setLayout(new GridLayout(Board.ROWS, Board.COLS, 5, 5)); // Grid 6x7 dengan jarak antar sel
        buttons = new JButton[Board.ROWS][Board.COLS];

        // Membuat tombol-tombol untuk grid
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                buttons[row][col].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                buttons[row][col].setEnabled(true); // Disable semua tombol kecuali baris paling atas

                // Border kosong di tombol
                buttons[row][col].setOpaque(true);
                buttons[row][col].setBackground(new Color(255, 255, 255, 50)); // Warna tombol sesuai tema

                int finalCol = col;
                buttons[row][col].addActionListener(e -> handleMove(finalCol));
                //buttons[row][col].setForeground(Color.WHITE); // Warna teks pada tombol

                buttons[row][col].setContentAreaFilled(false);
                buttons[row][col].setBorderPainted(true);

                panels.add(buttons[row][col]);
            }
        }

        // Menambahkan panel ke frame
        frame.add(panels, BorderLayout.CENTER); // Tambahkan grid ke frame

        // Enable baris atas untuk menerima input
        for (int col = 0; col < Board.COLS; col++) {
            buttons[0][col].setEnabled(true);
        }


        // Refresh frame
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void handleMove(int col) {
        if (!board.isValidMove(col)) return;

        Token currentPlayerToken = isPlayer1Turn ? player1Token : player2Token;
        int row = board.makeMove(col, currentPlayerToken);

        // Update UI
        buttons[row][col].setIcon(currentPlayerToken == Token.X ? xIcon : oIcon);
        buttons[row][col].setEnabled(false); // Disable tombol yang terisi

        if (board.isWinningMove(currentPlayerToken, row, col)) {
            String winner = isPlayer1Turn ? player1Name : player2Name;
            JOptionPane.showMessageDialog(frame, winner + " wins!");
            resetGame();
            return;
        }

        if (board.isGameOver()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetGame();
            return;
        }

        // Switch turns
        isPlayer1Turn = !isPlayer1Turn;
    }

    private void resetGame() {
        board.reset();
        isPlayer1Turn = true;

        for (int i = 0; i < board.ROWS; i++) {
            for (int j = 0; j < board.COLS; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setIcon(null); // Hapus ikon dari tombol
            }
        }
        JOptionPane.showMessageDialog(frame, "Game has been reset. Let's play again!");

    }
}
