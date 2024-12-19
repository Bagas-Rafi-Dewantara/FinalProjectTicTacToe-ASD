package TicTacToe;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TTTGraphics gameUI = new TTTGraphics();
            gameUI.showMenu();
        });
    }
}
