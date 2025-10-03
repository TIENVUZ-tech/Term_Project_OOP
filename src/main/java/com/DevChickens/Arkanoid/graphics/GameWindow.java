package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.core.GameManager;
import javax.swing.*;

/**
 * GameWindow tạo JFrame để chứa GamePanel.
 */
public class GameWindow {
    public static void main(String[] args) {
        GameManager manager = new GameManager();
        manager.startGame(); // khởi động game

        JFrame window = new JFrame("Arkanoid - DevChickens");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel(manager);
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameLoop(); // chạy vòng lặp game
    }
}
