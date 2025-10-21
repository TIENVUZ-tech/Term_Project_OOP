package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.input.InputHandler; // <-- 1. THÊM IMPORT

import javax.swing.*;
import java.awt.*;
// import java.awt.event.KeyEvent;     // <-- Không cần nữa
// import java.awt.event.KeyListener; // <-- 2. XÓA IMPORT NÀY

/**
 * GamePanel là nơi hiển thị game và chứa vòng lặp game (game loop).
 * - Gọi GameManager.update() để cập nhật logic
 * - repaint() để vẽ lại game
 * - (ĐÃ SỬA) Ủy quyền việc lắng nghe phím cho InputHandler
 */
// 3. BỎ "implements KeyListener"
public class GamePanel extends JPanel implements Runnable {

    private GameManager manager;
    private Thread gameThread;
    private boolean running;

    public GamePanel(GameManager manager) {
        this.manager = manager;

        setPreferredSize(new Dimension(GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT));
        setBackground(Color.BLACK);
        setOpaque(false);
        setFocusable(true); // <-- Tốt, bạn đã có dòng này
        requestFocus();     // <-- Tốt, bạn đã có dòng này

        // 4. SỬA CHỖ NÀY
        // addKeyListener(this); // Xóa dòng cũ

        // Thay bằng InputHandler mới
        InputHandler inputHandler = new InputHandler(this.manager);
        addKeyListener(inputHandler);
        // --- KẾT THÚC SỬA ---

        startGameLoop();
    }
    private void startGameLoop() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // Game loop với ~60 FPS
        final int FPS = 60;
        final long frameTime = 1000 / FPS;

        while (running) {
            long start = System.currentTimeMillis();

            manager.update();
            repaint();

            long elapsed = System.currentTimeMillis() - start;
            long sleepTime = frameTime - elapsed;
            if (sleepTime < 0) sleepTime = 2;

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        manager.draw(g);
    }

}