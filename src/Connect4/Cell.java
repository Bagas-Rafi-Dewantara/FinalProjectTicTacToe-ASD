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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private ImageIcon xIcon; // Icon for X
    private ImageIcon oIcon; // Icon for O
    private int player1Points = 0; // Tambahkan variabel untuk poin
    private int player2Points = 0; // Tambahkan variabel untuk poin
    private int totalGames = 0; // Total jumlah permainan

    private JLabel pointsLabel; // Label untuk menampilkan poin

    public Cell(Board board, boolean isPlayerVsComputer, JFrame frame, String player1Name, String player2Name, Token player1Token, Token player2Token, ComputerAI computerAI) {
        this.board = board;
        this.frame = frame;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Token = player1Token;
        this.player2Token = player2Token;
        this.computerAI = computerAI; // Assign AI

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
            Image xImage = new ImageIcon(getClass().getResource(xImagePath)).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            Image oImage = new ImageIcon(getClass().getResource(oImagePath)).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            xIcon = new ImageIcon(xImage);
            oIcon = new ImageIcon(oImage);
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
        // Load background image
        loadBackgroundImage(backgroundPath);

        // Load X and O icons
        loadIcons("/Connect4/image/crossc4.png", "/Connect4/image/noughc4.png");

        // Clear all previous components
        frame.getContentPane().removeAll();

        // Create panel for Connect Four grid
        BackgroundPanel panels = new BackgroundPanel();
        panels.setLayout(new BorderLayout()); // 6x7 grid with spacing

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout()); // Judul berada di tengah
        titlePanel.setOpaque(true);
        titlePanel.setBackground(new Color(33, 53, 85)); // Warna latar belakang judul

        JLabel titleLabel = new JLabel("Connect Four Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false); // Transparan untuk mengikuti warna panel
        titlePanel.add(titleLabel, BorderLayout.CENTER); // Tambahkan judul ke panel

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Horizontal dengan jarak antar elemen
        scorePanel.setOpaque(true);
        scorePanel.setBackground(new Color(33, 53, 85)); // Warna latar belakang sama dengan judul

        pointsLabel = new JLabel(getPointsText());
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setOpaque(false); // Transparan untuk mengikuti warna panel
        scorePanel.add(pointsLabel); // Tambahkan label skor ke panel skor

        JPanel titleAndScorePanel = new JPanel();
        titleAndScorePanel.setLayout(new BorderLayout());
        titleAndScorePanel.add(titlePanel, BorderLayout.NORTH); // Tambahkan judul ke atas
        titleAndScorePanel.add(scorePanel, BorderLayout.CENTER); // Tambahkan skor di bawahnya

        panels.add(titleAndScorePanel, BorderLayout.NORTH);

        JPanel gridC4 = new JPanel(new GridLayout(Board.ROWS, Board.COLS, 5, 5));
        gridC4.setOpaque(false);
        buttons = new JButton[Board.ROWS][Board.COLS];

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                buttons[row][col].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                buttons[row][col].setEnabled(false); // Disable all buttons initially

                // Empty border for buttons
                buttons[row][col].setOpaque(true);
                buttons[row][col].setBackground(new Color(255, 255, 255, 50)); // Button color

                int finalCol = col;
                buttons[row][col].addActionListener(e -> handleMove(finalCol));

                buttons[row][col].setContentAreaFilled(false);
                buttons[row][col].setBorderPainted(true);

                gridC4.add(buttons[row][col]);
            }
        }

        panels.add(gridC4, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 20));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(new Color(33, 53, 85));
        resetButton.setOpaque(true);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        resetButton.addActionListener(e -> resetGameWithResult());
        panels.add(resetButton, BorderLayout.SOUTH);

        frame.add(panels);

        // Enable top row buttons for input
        for (int col = 0; col < Board.COLS; col++) {
            buttons[0][col].setEnabled(true);
        }

        // Refresh frame
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);

        // If computer starts first, make its move
        if (!isPlayer1Turn && computerAI != null) {
            makeAIMove();
        }
    }

    private String getPointsText() { // Format teks horizontal untuk skor
        return String.format(
                "%s: %d    |    %s: %d    |    Games Played: %d",
                player1Name, player1Points,
                player2Name, player2Points,
                totalGames
        );
    }

    private void handleMove(int col) {
        if (!board.isValidMove(col)) return;

        Token currentPlayerToken = isPlayer1Turn ? player1Token : player2Token;
        int row = board.makeMove(col, currentPlayerToken);

        // Update UI
        buttons[row][col].setIcon(currentPlayerToken == Token.X ? xIcon : oIcon);
        buttons[row][col].setEnabled(false); // Disable filled button

        // Enable button above the current one if it exists
        if (row > 0) {
            buttons[row - 1][col].setEnabled(true);
        }

        if (currentPlayerToken == Token.X) {
            SoundEffect.CROSS_SOUND.play(); // Sound for X
        } else {
            SoundEffect.NOUGH_SOUND.play(); // Sound for O
        }

        if (board.isWinningMove(currentPlayerToken, row, col)) {
            String winner = isPlayer1Turn ? player1Name : player2Name;
            if (currentPlayerToken == Token.X) {
                player1Points++;
                SoundEffect.CROSSWIN_SOUND.play();
                SoundEffect.BACKGROUND.stop();
            } else {
                player2Points++;
                SoundEffect.NOUGHWIN_SOUND.play();
                SoundEffect.BACKGROUND.stop();
            }
            totalGames++; // Tambahkan increment games played di sini
            pointsLabel.setText(getPointsText()); // Update skor
            JOptionPane.showMessageDialog(frame, winner + " wins!");

            resetGame();
            return;
        }

        if (board.isGameOver()) {
            SoundEffect.DRAW_SOUND.play();
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            totalGames++; // Tambahkan increment games played di sini
            pointsLabel.setText(getPointsText()); // Update skor
            resetGame();
            return;
        }

        // Switch turns
        isPlayer1Turn = !isPlayer1Turn;

        // If it's the AI's turn, make its move
        if (!isPlayer1Turn && computerAI != null) {
            makeAIMove();
        }
    }

    private void makeAIMove() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Thread.sleep(1000); // Jeda 1 detik (1000 ms) sebelum komputer membuat langkah
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                // Setelah jeda, komputer membuat langkah
                Point aiMove = computerAI.turn(board, player2Token);
                if (aiMove != null) {
                    handleMove(aiMove.y); // Langkah komputer
                }
            }
        }.execute();
    }

    private void resetGame() {
        board.reset();
        isPlayer1Turn = true;

        for (int i = 0; i < board.ROWS; i++) {
            for (int j = 0; j < board.COLS; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setIcon(null); // Clear button icons
            }
        }

        // Enable top row buttons for input
        for (int col = 0; col < Board.COLS; col++) {
            buttons[0][col].setEnabled(true);
        }

        JOptionPane.showMessageDialog(frame, "Game has been reset. Let's play again!");

        SoundEffect.BACKGROUND.loop();
        pointsLabel.setText(getPointsText());
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

        resetGame();
    }
}
