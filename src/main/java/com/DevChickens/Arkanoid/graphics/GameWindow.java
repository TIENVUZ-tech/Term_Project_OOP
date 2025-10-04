package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.core.GameManager;

import javax.swing.*;

/**
 * GameWindow tạo cửa sổ chính chứa GamePanel.
 */
public class GameWindow extends JFrame {

    public GameWindow(GameManager manager) {
        setTitle("Arkanoid - OOP Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel(manager);
        add(panel);

        pack(); // auto chỉnh kích thước theo panel
        setLocationRelativeTo(null); // đặt giữa màn hình
        setVisible(true);
    }

    /**
     * Hàm main để chạy game
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager manager = new GameManager();
            new GameWindow(manager);
        });
    }
}
