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

    private Image backgroundImage;
    private ImageIcon xIcon; // Gambar untuk X
    private ImageIcon oIcon; // Gambar untuk O

    private void loadBackgroundImage(String imagePath) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (NullPointerException e) {
            System.err.println("Background image not found: " + imagePath);
        }
    }

    private void loadIcons(String xImagePath, String oImagePath) {
        try {
            xIcon = new ImageIcon(getClass().getResource(xImagePath));
            oIcon = new ImageIcon(getClass().getResource(oImagePath));
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

    public void start(String backgroundPath) {
        // Muat gambar latar belakang
        loadBackgroundImage(backgroundPath);

        // Muat ikon X dan O
        loadIcons("/TicTacToe/image/cross.png", "/TicTacToe/image/nough.png");

        // Hapus konten sebelumnya
        frame.getContentPane().removeAll();

        // Buat panel dengan latar belakang
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        // Tambahkan tombol ke panel
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));

                // Atur transparansi tombol
                buttons[i][j].setOpaque(false);
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

        Token currentPlayerToken = isPlayer1Turn ? Token.X : Token.O;
        board.makeMove(row, col, currentPlayerToken);

        // Gunakan ikon gambar sebagai representasi X atau O
        if (currentPlayerToken == Token.X) {
            buttons[row][col].setIcon(xIcon);
            SoundEffect.CROSS_SOUND.play();
        } else {
            buttons[row][col].setIcon(oIcon);
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
        start("/TicTacToe/image/bc_malam.jpg");
    }
}
