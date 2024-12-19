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
        panel.setLayout(new GridLayout(3, 3, 5, 5)); // Tambahkan jarak antar grid
        buttons = new JButton[3][3];

        // Tambahkan tombol ke panel
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

                // Tambahkan tombol ke panel
                panel.add(buttons[i][j]);
            }
        }

        // Tambahkan panel ke frame
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        SoundEffect.BACKGROUND.loop();
    }

    private void handleMove(int row, int col) {
        if (!board.isValidMove(row, col)) return;

        Token currentPlayerToken = isPlayer1Turn ? player1Token : player2Token;
        board.makeMove(row, col, currentPlayerToken);

        // Gunakan ikon gambar sebagai representasi token
        if (currentPlayerToken == player1Token) {
            buttons[row][col].setIcon(xIcon);
            SoundEffect.CROSS_SOUND.play();
        } else {
            buttons[row][col].setIcon(oIcon);
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

        JOptionPane.showMessageDialog(frame, "Game has been reset. Let's play again!");
    }
}
