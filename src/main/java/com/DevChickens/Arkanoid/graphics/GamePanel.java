package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.core.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * GamePanel là nơi hiển thị game và chứa vòng lặp game (game loop).
 *  - Gọi GameManager.update() để cập nhật logic
 *  - repaint() để vẽ lại game
 *  - Lắng nghe sự kiện bàn phím và chuyển cho GameManager xử lý
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {

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
        addKeyListener(this);

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

    @Override
    public void keyPressed(KeyEvent e) {
        manager.handleInput(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // không cần
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // không cần
    }
}
