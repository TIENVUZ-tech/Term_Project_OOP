package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.input.InputHandler; // <-- 1. THÊM IMPORT

import javax.swing.*;
import java.awt.*;

/**
 * GamePanel là nơi hiển thị game và chứa vòng lặp game (game loop).
 */
public class GamePanel extends JPanel implements Runnable {

    private GameManager manager;
    private Thread gameThread;
    private boolean running;

    public GamePanel(GameManager manager) {
        this.manager = manager;

        setPreferredSize(new Dimension(GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT));
        setBackground(Color.BLACK);
        setOpaque(false);
        setFocusable(true);
        requestFocus();

        InputHandler inputHandler = new InputHandler(this.manager);
        addKeyListener(inputHandler);

        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);

        startGameLoop();
    }
    private void startGameLoop() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        //  Thiết lập timeline cố định cho logic game
        final double TicksPerSecond = 60.0;

        // Tính toán xem mỗi tick logic mất bao nhiêu nano giây.
        final double nsPerTick = 1000000000.0 / TicksPerSecond;

        long lastTime = System.nanoTime();
        double delta = 0;

        // Các biến để đếm FPS (khung hình/giây) và TPS (tick/giây) để debug
        long timer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;

        while (running) {
            long now = System.nanoTime();
            // delta: Biến đếm xem đã tích lũy đủ thời gian để chạy 1 tick logic chưa
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            // CẬP NHẬT LOGIC (Fixed-Step)
            // Vòng lặp này đảm bảo logic luôn chạy 60 lần/giây.
            // Nếu máy lag nặng (delta > 2), nó sẽ chạy update() 2 lần
            // để "bắt kịp" thời gian, đảm bảo vật lý không bị chậm lại.
            while (delta >= 1) {
                manager.update();
                updates++;
                delta--;
            }

            // VẼ (Variable-Step)
            // Yêu cầu Swing vẽ lại (sẽ gọi hàm paintComponent)
            // Hàm này chạy nhanh nhất có thể để không bị sleep
            repaint();
            frames++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        manager.draw(g);
    }

}