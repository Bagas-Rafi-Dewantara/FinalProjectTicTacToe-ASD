import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.sound.sampled.*;
import TicTacToe.TTTGraphics;
import Connect4.TTTGraphics4;

public class GameSelector {
    private JFrame frame;
    private BufferedImage backgroundImage;
    private Clip clip;

    public GameSelector() {
        // Membuat JFrame
        frame = new JFrame("Game Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);  // Memperbesar ukuran frame
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        try {
            // Muat gambar background dari folder src atau path lain
            backgroundImage = ImageIO.read(getClass().getResource("lollziee.jpg"));  // Pastikan path sudah benar
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Muat dan putar lagu
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/intro.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);  // Putar lagu secara terus menerus
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kelas untuk menggambar background
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Gambar background
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void showMenu() {
        // Membuat panel dengan background
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());  // Menggunakan GridBagLayout untuk kontrol yang lebih baik

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20);  // Menambahkan jarak antara elemen dan panel

        // Membuat label dengan teks dan pengaturan font
        JLabel label = new JLabel("                  ", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 40));  // Memperbesar ukuran font untuk label
        label.setForeground(Color.WHITE);  // Menetapkan warna teks menjadi putih

        // Membuat tombol untuk game
        JButton ticTacToeButton = new JButton("Tic Tac Toe");
        JButton connectFourButton = new JButton("Connect Four");

        // Menetapkan font dan warna tombol
        ticTacToeButton.setFont(new Font("Times New Roman", Font.BOLD, 30));
        connectFourButton.setFont(new Font("Times New Roman", Font.BOLD, 30));
        ticTacToeButton.setForeground(Color.white);
        connectFourButton.setForeground(Color.white);
        ticTacToeButton.setBackground(new Color(62, 88, 121));  // Ubah warna background sesuai keinginan
        connectFourButton.setBackground(new Color(33, 53, 85));  // Ubah warna background sesuai keinginan

        // Menambahkan listener untuk tombol
        ticTacToeButton.addActionListener(e -> startTicTacToe());
        connectFourButton.addActionListener(e -> startConnectFour());

        // Menambahkan efek hover pada tombol
        ticTacToeButton.setRolloverEnabled(true);
        ticTacToeButton.setContentAreaFilled(true);
        ticTacToeButton.setBorderPainted(true);
        connectFourButton.setRolloverEnabled(true);
        connectFourButton.setContentAreaFilled(true);
        connectFourButton.setBorderPainted(true);

        // Menambahkan elemen ke background panel
        backgroundPanel.add(label, gbc);

        // Mengatur tombol "Tic Tac Toe"
        gbc.gridy = 1;  // Pindahkan tombol ke baris berikutnya
        backgroundPanel.add(ticTacToeButton, gbc);

        // Mengatur tombol "Connect Four"
        gbc.gridy = 2;  // Pindahkan tombol ke baris berikutnya
        backgroundPanel.add(connectFourButton, gbc);

        // Mengganti konten frame dengan panel yang sudah diatur
        frame.getContentPane().removeAll();
        frame.add(backgroundPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void startTicTacToe() {
        TTTGraphics gameUI = new TTTGraphics();
        gameUI.showMenu(); // This will open TicTacToe options
        frame.dispose(); // Close the GameSelector window
        stopMusic(); // Hentikan musik
    }

    private void startConnectFour() {
        TTTGraphics4 game4 = new TTTGraphics4();
        game4.showMenu();
        frame.dispose(); // Close the GameSelector window
        stopMusic(); // Hentikan musik
    }

    private void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameSelector gameSelector = new GameSelector();
            gameSelector.showMenu();
        });
    }
}
