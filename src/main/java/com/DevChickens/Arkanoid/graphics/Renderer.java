package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.entities.*;
import com.DevChickens.Arkanoid.entities.bricks.*;
import com.DevChickens.Arkanoid.entities.powerups.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Renderer vẽ toàn bộ đối tượng và màn hình của Arkanoid.
 */
public class Renderer {

    private Font titleFont;
    private Font instructionFont;
    private Font smallFont;

    public Renderer() {
        // Khởi tạo các font này một lần duy nhất.
        try {
            titleFont = new Font("Arial", Font.BOLD, 72);
            instructionFont = new Font("Arial", Font.PLAIN, 28);
            smallFont = new Font("Arial", Font.PLAIN, 20);
        } catch (Exception e) {
            titleFont = new Font("SansSerif", Font.BOLD, 72);
            instructionFont = new Font("SansSerif", Font.PLAIN, 28);
            smallFont = new Font("SansSerif", Font.PLAIN, 20);
        }
    }

    public void drawPaddle(Graphics g, Paddle paddle) {
        Graphics2D g2d = (Graphics2D) g;
        if (paddle.getImage() != null) {
            g2d.drawImage(paddle.getImage(),
                    (int) paddle.getX(),
                    (int) paddle.getY(),
                    (int) paddle.getWidth(),
                    (int) paddle.getHeight(), null);
        }
    }

    public void drawBall(Graphics g, Ball ball) {
        Graphics2D g2d = (Graphics2D) g;
        if (ball.getImage() != null) {
            g2d.drawImage(ball.getImage(), (int) ball.getX(), (int) ball.getY(),
                    (int) ball.getWidth(), (int) ball.getHeight(), null);
        }
    }

    public void drawBrick(Graphics g, Brick brick) {
        if (brick.isDestroyed() || brick.getImage() == null) {
            return;
        }
        // Ép kiểu g sang Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        // Dùng g2d để vẽ
        g2d.drawImage(brick.getImage(), (int) brick.getX(), (int) brick.getY(),
                (int) brick.getWidth(), (int) brick.getHeight(), null);
    }

    public void drawPowerUp(Graphics g, PowerUp p) {
        g.setColor(Color.GREEN);
        g.fillRect((int) p.getX(), (int) p.getY(),
                (int) p.getWidth(), (int) p.getHeight());

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(p.getType(), (int) p.getX() + 2, (int) p.getY() + 15);
    }

    public void drawBullet(Graphics g, Bullet bullet) {
        Graphics2D g2d = (Graphics2D) g;
        if (bullet.getImage() != null) {
            g2d.drawImage(bullet.getImage(), (int) bullet.getX(), (int) bullet.getY(), 
            (int) bullet.getWidth(), (int) bullet.getHeight(), null);
        }
    }

    public void drawMenu(Graphics g, int w, int h) {
        if (AssetLoader.MENU_BACKGROUND != null) {
            g.drawImage(AssetLoader.MENU_BACKGROUND, 0, 0, w, h, null);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "ARKANOID";
        // Căn giữa text tự động.
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (w - fm.stringWidth(title)) / 2;
        int titleY = h / 2 - 50; // Đặt ở nửa trên màn hình
        g2d.drawString(title, titleX, titleY);

        g2d.setFont(instructionFont);
        String instruction = "Press ENTER to Start";
        fm = g2d.getFontMetrics();
        int instructionX = (w - fm.stringWidth(instruction)) / 2;
        int instructionY = h / 2 + 40; // Đặt ở nửa dưới

        if (System.currentTimeMillis() / 500 % 2 == 0) {
            g2d.drawString(instruction, instructionX, instructionY);
        }
    }

    public void drawGameBackground(Graphics g, int w, int h, int round) {
        BufferedImage bg = AssetLoader.getRoundBackground(round);

        if (bg != null) {
            g.drawImage(bg, 0, 0, w, h, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, w, h);
        }
    }

    public void drawPause(Graphics g, int w, int h) {
        g.setColor(new Color(0, 0, 0, 150)); // Màu đen mờ (Alpha = 150).
        g.fillRect(0, 0, w, h);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.YELLOW);

        // Vẽ chữ PAUSED
        g2d.setFont(titleFont); // Dùng font lớn
        String text = "PAUSED";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(text)) / 2;
        int y = h / 2;
        g2d.drawString(text, x, y);

        // Vẽ hướng dẫn
        g2d.setFont(instructionFont);
        text = "Press P to Resume";
        fm = g2d.getFontMetrics();
        x = (w - fm.stringWidth(text)) / 2;
        y = h / 2 + 50;
        g2d.drawString(text, x, y);
    }

    public void drawNextRound(Graphics g, int width, int height, int round) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);

        // Vẽ "ROUND X"
        g2d.setFont(titleFont);
        String text = "ROUND " + round;
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = height / 2;
        g2d.drawString(text, x, y);

        // Vẽ "Get Ready..."
        g2d.setFont(instructionFont);
        text = "Get Ready...";
        fm = g2d.getFontMetrics();
        x = (width - fm.stringWidth(text)) / 2;
        y = height / 2 + 50;
        g2d.drawString(text, x, y);
    }

    public void drawGameOver(Graphics g, int w, int h, int score) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Vẽ "GAME OVER"
        g2d.setFont(titleFont);
        g2d.setColor(Color.RED);
        String text = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(text)) / 2;
        int y = h / 2 - 50;
        g2d.drawString(text, x, y);

        // Vẽ Điểm
        g2d.setFont(instructionFont);
        g2d.setColor(Color.WHITE);
        text = "Final Score: " + score;
        fm = g2d.getFontMetrics();
        x = (w - fm.stringWidth(text)) / 2;
        y = h / 2 + 20;
        g2d.drawString(text, x, y);

        // Vẽ Hướng dẫn
        g2d.setFont(smallFont); // Dùng font nhỏ hơn
        text = "Press ENTER to Restart";
        fm = g2d.getFontMetrics();
        x = (w - fm.stringWidth(text)) / 2;
        y = h / 2 + 60;
        g2d.drawString(text, x, y);
    }

    public void drawVictory(Graphics g, int w, int h, int score) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Vẽ "YOU WIN!"
        g2d.setFont(titleFont);
        g2d.setColor(Color.GREEN);
        String text = "YOU WIN!";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(text)) / 2;
        int y = h / 2 - 50;
        g2d.drawString(text, x, y);

        // Vẽ Điểm
        g2d.setFont(instructionFont);
        g2d.setColor(Color.WHITE);
        text = "Final Score: " + score;
        fm = g2d.getFontMetrics();
        x = (w - fm.stringWidth(text)) / 2;
        y = h / 2 + 20;
        g2d.drawString(text, x, y);

        // Vẽ Hướng dẫn
        g2d.setFont(smallFont);
        text = "Press ENTER to Play Again";
        fm = g2d.getFontMetrics();
        x = (w - fm.stringWidth(text)) / 2;
        y = h / 2 + 60;
        g2d.drawString(text, x, y);
    }
}
