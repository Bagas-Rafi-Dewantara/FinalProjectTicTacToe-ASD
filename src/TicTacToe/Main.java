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

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TTTGraphics gameUI = new TTTGraphics();
            gameUI.showMenu();
        });
    }
}
