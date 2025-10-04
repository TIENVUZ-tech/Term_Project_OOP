package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.entities.*;
import com.DevChickens.Arkanoid.entities.bricks.*;
import com.DevChickens.Arkanoid.entities.powerups.*;

import java.awt.*;

/**
 * Renderer vẽ toàn bộ đối tượng và màn hình của Arkanoid
 */
public class Renderer {

    // =========================
    // Vẽ đối tượng gameplay
    // =========================
    public void drawPaddle(Graphics g, Paddle paddle) {
        g.setColor(Color.CYAN);
        g.fillRect((int) paddle.getX(), (int) paddle.getY(),
                (int) paddle.getWidth(), (int) paddle.getHeight());
    }

    public void drawBall(Graphics g, Ball ball) {
        g.setColor(Color.YELLOW);
        g.fillOval((int) ball.getX(), (int) ball.getY(),
                (int) ball.getWidth(), (int) ball.getHeight());
    }

    public void drawBrick(Graphics g, Brick brick) {
        if (brick.isDestroyed()) return;

        // Màu sắc dựa trên loại hoặc hitPoints
        switch (brick.getType().toLowerCase()) {
            case "normal":
                g.setColor(Color.LIGHT_GRAY);
                break;
            case "strong":
                g.setColor(Color.RED);
                break;
            case "explosive":
                g.setColor(Color.ORANGE);
                break;
            case "quite":
                g.setColor(Color.MAGENTA);
                break;
            default:
                g.setColor(Color.WHITE);
        }

        // Tô màu chính
        g.fillRect((int) brick.getX(), (int) brick.getY(),
                (int) brick.getWidth(), (int) brick.getHeight());

        // Viền
        g.setColor(Color.BLACK);
        g.drawRect((int) brick.getX(), (int) brick.getY(),
                (int) brick.getWidth(), (int) brick.getHeight());

    }

    public void drawPowerUp(Graphics g, PowerUp p) {
        g.setColor(Color.GREEN);
        g.fillRect((int) p.getX(), (int) p.getY(),
                (int) p.getWidth(), (int) p.getHeight());

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(p.getType(), (int) p.getX() + 2, (int) p.getY() + 15);
    }

    public void drawMenu(Graphics g, int w, int h) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("ARKANOID", w / 2 - 100, h / 2 - 50);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press ENTER to Start", w / 2 - 100, h / 2);
    }

    public void drawPause(Graphics g, int w, int h) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("PAUSED", w / 2 - 80, h / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press P to Resume", w / 2 - 100, h / 2 + 40);
    }

    public void drawGameOver(Graphics g, int w, int h, int score) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("GAME OVER", w / 2 - 120, h / 2 - 50);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Final Score: " + score, w / 2 - 80, h / 2);

        g.drawString("Press ENTER to Restart", w / 2 - 120, h / 2 + 40);
    }

    public void drawVictory(Graphics g, int w, int h, int score) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("YOU WIN!", w / 2 - 100, h / 2 - 50);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Final Score: " + score, w / 2 - 80, h / 2);

        g.drawString("Press ENTER to Play Again", w / 2 - 140, h / 2 + 40);
    }
}
