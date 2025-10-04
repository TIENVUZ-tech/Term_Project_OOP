package com.DevChickens.Arkanoid;

import javax.swing.*;
import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.graphics.GamePanel;

/**
 * Điểm khởi chạy chính của trò chơi Arkanoid.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Tạo cửa sổ chính
            JFrame frame = new JFrame("Arkanoid - DevChickens");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            // Tạo GameManager và gắn vào GamePanel
            GameManager manager = new GameManager();
            GamePanel panel = new GamePanel(manager);

            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null); // căn giữa màn hình
            frame.setVisible(true);
        });
    }
}
