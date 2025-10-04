package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.entities.GameObject;
import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.bricks.Brick;
import com.DevChickens.Arkanoid.entities.powerups.PowerUp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Renderer chịu trách nhiệm vẽ tất cả đối tượng game lên màn hình.
 * Sử dụng Graphics2D (Java2D) để vẽ Paddle, Ball, Bricks, PowerUps.
 */
public class Renderer extends JPanel {

    /**
     * Vẽ paddle
     */
    public void drawPaddle(Graphics2D g, Paddle paddle) {
        if (paddle != null) {
            g.setColor(Color.BLUE);
            g.fillRect(
                    (int) paddle.getX(),
                    (int) paddle.getY(),
                    paddle.getWidth(),
                    paddle.getHeight()
            );
        }
    }

    /**
     * Vẽ ball
     */
    public void drawBall(Graphics2D g, Ball ball) {
        if (ball != null) {
            g.setColor(Color.RED);
            g.fillOval(
                    (int) ball.getX(),
                    (int) ball.getY(),
                    ball.getWidth(),
                    ball.getHeight()
            );
        }
    }

    /**
     * Vẽ bricks
     */
    public void drawBricks(Graphics2D g, List<Brick> bricks) {
        g.setColor(Color.ORANGE);
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                g.fillRect(
                        (int) brick.getX(),
                        (int) brick.getY(),
                        brick.getWidth(),
                        brick.getHeight()
                );
            }
        }
    }

    /**
     * Vẽ power-ups
     */
    public void drawPowerUps(Graphics2D g, List<PowerUp> powerUps) {
        g.setColor(Color.GREEN);
        for (PowerUp p : powerUps) {
            g.fillRect(
                    (int) p.getX(),
                    (int) p.getY(),
                    p.getWidth(),
                    p.getHeight()
            );
        }
    }

    /**
     * Hàm tổng hợp để vẽ tất cả đối tượng
     */
    public void renderAll(Graphics2D g, Paddle paddle, Ball ball,
                          List<Brick> bricks, List<PowerUp> powerUps) {
        drawPaddle(g, paddle);
        drawBall(g, ball);
        drawBricks(g, bricks);
        drawPowerUps(g, powerUps);
    }
}
