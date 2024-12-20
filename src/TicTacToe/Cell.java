/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #11
 * 1 - 5026231018 - Izzuddin Hamadi Faiz
 * 2 - 5026231091 - Bagas Rafi Dewantara
 * 3 - 5026231116 - I Putu Febryan Khrisyantara
 */

package TicTacToe;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

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

    private int player1Points = 0; // Tambahkan variabel untuk poin
    private int player2Points = 0; // Tambahkan variabel untuk poin
    private int totalGames = 0; // Total jumlah permainan

    private JLabel pointsLabel; // Label untuk menampilkan poin

    private Image backgroundImage;
    private ImageIcon xIcon; // Gambar untuk X
    private ImageIcon oIcon; // Gambar untuk O

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

    private void loadBackgroundImage(String imagePath) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (NullPointerException e) {
            System.err.println("Background image not found: " + imagePath);
        }
    }

    private void loadIcons(String xImagePath, String oImagePath) {
        try {
            // Ubah ukuran ikon agar lebih kecil
            Image xImage = new ImageIcon(getClass().getResource(xImagePath)).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            Image oImage = new ImageIcon(getClass().getResource(oImagePath)).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            xIcon = new ImageIcon(xImage);
            oIcon = new ImageIcon(oImage);
        } catch (NullPointerException e) {
            System.err.println("Icon image not found: " + xImagePath + " or " + oImagePath);
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
        loadIcons("/TicTacToe/image/cross.png", "/TicTacToe/image/nough.png");

        // Hapus konten sebelumnya
        frame.getContentPane().removeAll();

        // Buat panel dengan latar belakang
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new BorderLayout());

        // Tambahkan panel untuk judul
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout()); // Judul berada di tengah
        titlePanel.setOpaque(true);
        titlePanel.setBackground(new Color(33, 53, 85)); // Warna latar belakang judul

        // Tambahkan label untuk judul
        JLabel titleLabel = new JLabel("Tic Tac Toe Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false); // Transparan untuk mengikuti warna panel
        titlePanel.add(titleLabel, BorderLayout.CENTER); // Tambahkan judul ke panel

        // Tambahkan panel untuk skor
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Horizontal dengan jarak antar elemen
        scorePanel.setOpaque(true);
        scorePanel.setBackground(new Color(33, 53, 85)); // Warna latar belakang sama dengan judul

        // Tambahkan label untuk menampilkan poin
        pointsLabel = new JLabel(getPointsText());
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setOpaque(false); // Transparan untuk mengikuti warna panel
        scorePanel.add(pointsLabel); // Tambahkan label skor ke panel skor

        // Gabungkan panel judul dan skor ke dalam panel vertikal
        JPanel titleAndScorePanel = new JPanel();
        titleAndScorePanel.setLayout(new BorderLayout());
        titleAndScorePanel.add(titlePanel, BorderLayout.NORTH); // Tambahkan judul ke atas
        titleAndScorePanel.add(scorePanel, BorderLayout.CENTER); // Tambahkan skor di bawahnya

        // Tambahkan panel vertikal ke bagian atas
        panel.add(titleAndScorePanel, BorderLayout.NORTH);

        // Panel grid untuk tombol Tic Tac Toe
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gridPanel.setOpaque(false); // Biarkan latar belakang tetap terlihat
        buttons = new JButton[3][3];

        // Tambahkan tombol ke panel grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                buttons[i][j].setBackground(new Color(255, 255, 255, 50));
                buttons[i][j].setOpaque(true);
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));

                // Atur transparansi tombol
                buttons[i][j].setContentAreaFilled(false);
                buttons[i][j].setBorderPainted(true);

                // Tambahkan tombol ke panel grid
                gridPanel.add(buttons[i][j]);
            }
        }

        // Tambahkan panel grid ke panel utama
        panel.add(gridPanel, BorderLayout.CENTER);



        // Tambahkan tombol reset di bagian bawah
        JButton resetButton = new JButton("Reset Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 20));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(new Color(33, 53, 85));
        resetButton.setOpaque(true);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        resetButton.addActionListener(e -> resetGameWithResult());
        panel.add(resetButton, BorderLayout.SOUTH);

        // Tambahkan panel utama ke frame
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        SoundEffect.BACKGROUND.loop();
    }

    private String getPointsText() { // Format teks horizontal untuk skor
        return String.format(
                "%s: %d    |    %s: %d    |    Games Played: %d",
                player1Name, player1Points,
                player2Name, player2Points,
                totalGames
        );
    }


    private void handleMove(int row, int col) {
        if (!board.isValidMove(row, col)) return;

        Token currentPlayerToken = isPlayer1Turn ? player1Token : player2Token;
        board.makeMove(row, col, currentPlayerToken);

        // Gunakan ikon gambar sebagai representasi token
        if (currentPlayerToken == Token.X) {
            buttons[row][col].setIcon(xIcon);
            SoundEffect.CROSS_SOUND.play();
        } else {
            buttons[row][col].setIcon(oIcon);
            SoundEffect.NOUGH_SOUND.play();
        }

        if (board.isWinningMove(currentPlayerToken)) {
            String winner = isPlayer1Turn ? player1Name : player2Name;
            if (isPlayer1Turn) {
                player1Points++; // Tambahkan poin untuk pemain 1
                SoundEffect.CROSSWIN_SOUND.play();
                SoundEffect.BACKGROUND.stop();
            } else {
                player2Points++; // Tambahkan poin untuk pemain 2
                SoundEffect.NOUGHWIN_SOUND.play();
                SoundEffect.BACKGROUND.stop();
            }
            JOptionPane.showMessageDialog(frame, winner + " wins!");
            totalGames++; // Tambahkan jumlah permainan
            resetBoard();
            return;
        }

        if (board.isGameOver()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            SoundEffect.DRAW_SOUND.play();
            SoundEffect.BACKGROUND.stop();
            totalGames++; // Tambahkan jumlah permainan
            resetBoard();
            return;
        }

        isPlayer1Turn = !isPlayer1Turn;

        if (isPlayerVsComputer && !isPlayer1Turn) {
            computerTurn();
        }
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
                buttons[i][j].setIcon(null); // Hapus ikon dari tombol
            }
        }

        pointsLabel.setText(getPointsText()); // Perbarui label poin
    }

    private void resetGameWithResult() { // [Modified] Metode untuk tombol reset game
        // Tentukan hasil berdasarkan poin
        String result;
        if (player1Points > player2Points) {
            result = String.format("%s wins the series!\nScore: %d - %d", player1Name, player1Points, player2Points);
        } else if (player1Points < player2Points) {
            result = String.format("%s wins the series!\nScore: %d - %d", player2Name, player2Points, player1Points);
        } else {
            result = String.format("The series is a draw!\nScore: %d - %d", player1Points, player2Points);
        }

        // Tampilkan dialog hasil
        JOptionPane.showMessageDialog(frame, result, "Game Over", JOptionPane.INFORMATION_MESSAGE);

        // Reset poin
        player1Points = 0;
        player2Points = 0;
        totalGames = 0;

        // Reset papan permainan
        resetGame();
    }

    private void resetBoard() {
        board.reset(); // Reset logika permainan
        // Reset tombol pada UI tanpa mengubah skor
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setIcon(null); // Hapus ikon dari tombol
            }
        }
        // Update tampilan skor
        pointsLabel.setText(getPointsText());
        SoundEffect.BACKGROUND.loop();
    }
}
