package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.bricks.Brick;
import com.DevChickens.Arkanoid.entities.powerups.PowerUp;
import com.DevChickens.Arkanoid.core.GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GamePanel là JPanel nơi mọi thứ được vẽ lên.
 * Renderer được gọi trong phương thức paintComponent().
 */
public class GamePanel extends JPanel implements Runnable {

    private GameManager gameManager;
    private Renderer renderer;

    private Thread gameThread;
    private final int FPS = 60;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.renderer = new Renderer();
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS; // nanoseconds per frame
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            // 1. Cập nhật game
            gameManager.updateGame();

            // 2. Vẽ lại màn hình
            repaint();

            // 3. Đợi đến khung hình tiếp theo
            try {
                double remaining = nextDrawTime - System.nanoTime();
                remaining = remaining / 1000000; // chuyển sang ms

                if (remaining < 0) remaining = 0;
                Thread.sleep((long) remaining);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        Paddle paddle = gameManager.getPaddle();
        Ball ball = gameManager.getBall();
        List<Brick> bricks = gameManager.getBricks();
        List<PowerUp> powerUps = gameManager.getPowerUps();

        renderer.renderAll(g2, paddle, ball, bricks, powerUps);
    }
}
