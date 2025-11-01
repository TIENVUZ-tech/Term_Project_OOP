package com.DevChickens.Arkanoid;

import javax.swing.*;
import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.graphics.AssetLoader;
import com.DevChickens.Arkanoid.graphics.GameWindow; // <-- Import GameWindow

/**
 * Điểm khởi chạy chính của trò chơi Arkanoid.
 */
public class Main {

    public static void main(String[] args) {

        AssetLoader.loadAllAssets();

        SwingUtilities.invokeLater(() -> {

            GameManager gameManager = GameManager.getInstance();

            //  Tạo GameWindow đã là JFrame và truyền manager vào
            // GameWindow sẽ tự động tạo GamePanel bên trong nó
            new GameWindow(gameManager);

        });
    }
}