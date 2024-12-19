package Connect4;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TTTGraphics4 game4 = new TTTGraphics4();
            game4.showMenu();

        });
    }
}
