package com.DevChickens.Arkanoid.graphics;
import com.DevChickens.Arkanoid.entities.effects.Explosion;

import com.DevChickens.Arkanoid.enums.GameState;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;
import java.awt.BasicStroke;

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
            titleFont = new Font("Georgia", Font.BOLD, 72);
            instructionFont = new Font("Georgia", Font.PLAIN, 30);
            smallFont = new Font("Georgia", Font.PLAIN, 20);
        } catch (Exception e) {
            titleFont = new Font("SansSerif", Font.BOLD, 72);
            instructionFont = new Font("SansSerif", Font.PLAIN, 28);
            smallFont = new Font("SansSerif", Font.PLAIN, 20);
        }
    }

    public void drawPaddle(Graphics g, com.DevChickens.Arkanoid.entities.Paddle paddle) {
        if (paddle != null) {
            paddle.render(g);
        }
    }

    public void drawBall(Graphics g, com.DevChickens.Arkanoid.entities.Ball ball) {
        if (ball != null) {
            ball.render(g);
        }
    }

    public void drawBullet(Graphics g, com.DevChickens.Arkanoid.entities.Bullet bullet) {
        if (bullet != null) {
            bullet.render(g);
        }
    }

    public void drawBrick(Graphics g, com.DevChickens.Arkanoid.entities.bricks.Brick brick) {
        if (brick != null && !brick.isDestroyed()) {
            brick.render(g);
        }
    }

    public void drawPowerUp(Graphics g, com.DevChickens.Arkanoid.entities.powerups.PowerUp powerUp) {
        if (powerUp != null) {
            powerUp.render(g);
        }
    }

    public void drawMenu(Graphics g, int w, int h, int mouseX, int mouseY,
                         Rectangle playRect, Rectangle highScoresRect,
                         Rectangle settingsRect, Rectangle exitRect,
                         boolean isGameInProgress, Rectangle continueRect) {

        // Vẽ nền
        if (AssetLoader.MENU_BACKGROUND != null) {
            g.drawImage(AssetLoader.MENU_BACKGROUND, 0, 0, w, h, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, w, h);
        }

        // Dùng Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // VẼ CÁC NÚT BẤM
        g2d.setFont(instructionFont);
        FontMetrics fm = g2d.getFontMetrics();

        int startY = h / 2 + 20;
        int spacing = 50;

        if (isGameInProgress) {
            // TH1: Có game đang chơi dở.
            // Nút 1: "CONTINUE"
            String textContinue = "CONTINUE";
            int continueX = (w - fm.stringWidth(textContinue)) / 2;
            int continueY = startY;
            continueRect.setBounds(continueX, continueY - fm.getAscent(), fm.stringWidth(textContinue), fm.getHeight());
            if (continueRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textContinue, continueX, continueY);

            // Nút 2: "NEW GAME"
            String textPlay = "NEW GAME";
            int playX = (w - fm.stringWidth(textPlay)) / 2;
            int playY = continueY + spacing;
            playRect.setBounds(playX, playY - fm.getAscent(), fm.stringWidth(textPlay), fm.getHeight());
            if (playRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textPlay, playX, playY);

            // Nút 3: "HIGH SCORES"
            String textScores = "HIGH SCORES";
            int scoresX = (w - fm.stringWidth(textScores)) / 2;
            int scoresY = playY + spacing;
            highScoresRect.setBounds(scoresX, scoresY - fm.getAscent(), fm.stringWidth(textScores), fm.getHeight());
            if (highScoresRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textScores, scoresX, scoresY);

            // Nút 4: "SETTINGS"
            String textSettings = "SETTINGS";
            int settingsX = (w - fm.stringWidth(textSettings)) / 2;
            int settingsY = scoresY + spacing;
            settingsRect.setBounds(settingsX, settingsY - fm.getAscent(), fm.stringWidth(textSettings), fm.getHeight());
            if (settingsRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textSettings, settingsX, settingsY);

            // Nút 5: "EXIT"
            String textExit = "EXIT";
            int exitX = (w - fm.stringWidth(textExit)) / 2;
            int exitY = settingsY + spacing;
            exitRect.setBounds(exitX, exitY - fm.getAscent(), fm.stringWidth(textExit), fm.getHeight());
            if (exitRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textExit, exitX, exitY);

        } else {
            // TH2: Game mới.

            // Nút 1: "PLAY"
            String textPlay = "PLAY";
            int playX = (w - fm.stringWidth(textPlay)) / 2;
            int playY = startY;
            playRect.setBounds(playX, playY - fm.getAscent(), fm.stringWidth(textPlay), fm.getHeight());
            if (playRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textPlay, playX, playY);

            // Nút 2: "HIGH SCORES"
            String textScores = "HIGH SCORES";
            int scoresX = (w - fm.stringWidth(textScores)) / 2;
            int scoresY = playY + spacing;
            highScoresRect.setBounds(scoresX, scoresY - fm.getAscent(), fm.stringWidth(textScores), fm.getHeight());
            if (highScoresRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textScores, scoresX, scoresY);

            // Nút 3: "SETTINGS"
            String textSettings = "SETTINGS";
            int settingsX = (w - fm.stringWidth(textSettings)) / 2;
            int settingsY = scoresY + spacing;
            settingsRect.setBounds(settingsX, settingsY - fm.getAscent(), fm.stringWidth(textSettings), fm.getHeight());
            if (settingsRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textSettings, settingsX, settingsY);

            // Nút 4: "EXIT"
            String textExit = "EXIT";
            int exitX = (w - fm.stringWidth(textExit)) / 2;
            int exitY = settingsY + spacing;
            exitRect.setBounds(exitX, exitY - fm.getAscent(), fm.stringWidth(textExit), fm.getHeight());
            if (exitRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textExit, exitX, exitY);
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

    /**
     * Vẽ màn hình PAUSE với các nút (Continue, Restart, Exit)
     */
    /**
     * Vẽ màn hình PAUSE với các nút (Continue, Restart, Exit)
     */
    public void drawPause(Graphics g, int w, int h, int mouseX, int mouseY,
                          Rectangle continueBtn, Rectangle restartBtn,
                          Rectangle settingsBtn, Rectangle exitBtn) {

        // Vẽ lớp phủ mờ
        g.setColor(new Color(0, 0, 0, 150)); // Màu đen mờ (Alpha = 150).
        g.fillRect(0, 0, w, h);

        // Dùng Graphics2D và bật khử răng cưa
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Chuẩn bị Font (dùng chung font instruction)
        g2d.setFont(instructionFont);
        FontMetrics fm = g2d.getFontMetrics();

        // Vẽ Nút Continue
        String textContinue = "Continue";
        // Căn giữa text bên trong Rectangle
        int continueX = continueBtn.x + (continueBtn.width - fm.stringWidth(textContinue)) / 2;
        int continueY = continueBtn.y + (continueBtn.height - fm.getHeight()) / 2 + fm.getAscent();

        // Vẽ highlight
        if (continueBtn.contains(mouseX, mouseY)) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        g2d.drawRect(continueBtn.x, continueBtn.y, continueBtn.width, continueBtn.height);
        g2d.drawString(textContinue, continueX, continueY);


        // Vẽ Nút Restart Level
        String textRestart = "Restart Level";
        int restartX = restartBtn.x + (restartBtn.width - fm.stringWidth(textRestart)) / 2;
        int restartY = restartBtn.y + (restartBtn.height - fm.getHeight()) / 2 + fm.getAscent();

        if (restartBtn.contains(mouseX, mouseY)) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        g2d.drawRect(restartBtn.x, restartBtn.y, restartBtn.width, restartBtn.height);
        g2d.drawString(textRestart, restartX, restartY);

        // Vẽ nút setting
        String textSettings = "Settings";
        int settingsX = settingsBtn.x + (settingsBtn.width - fm.stringWidth(textSettings)) / 2;
        int settingsY = settingsBtn.y + (settingsBtn.height - fm.getHeight()) / 2 + fm.getAscent();
        if (settingsBtn.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.WHITE);
        g2d.drawRect(settingsBtn.x, settingsBtn.y, settingsBtn.width, settingsBtn.height);
        g2d.drawString(textSettings, settingsX, settingsY);

        // --- Vẽ Nút "Exit to Menu" ---
        String textExit = "Exit to Menu";
        int exitX = exitBtn.x + (exitBtn.width - fm.stringWidth(textExit)) / 2;
        int exitY = exitBtn.y + (exitBtn.height - fm.getHeight()) / 2 + fm.getAscent();

        if (exitBtn.contains(mouseX, mouseY)) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.WHITE);
        }
        g2d.drawRect(exitBtn.x, exitBtn.y, exitBtn.width, exitBtn.height);
        g2d.drawString(textExit, exitX, exitY);
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

    /**
     * Vẽ icon Pause/Play
     */
    public void drawPauseIcon(Graphics g, GameState state, int mouseX, int mouseY, Rectangle pauseRect) {
        BufferedImage icon = null;

        // Chọn icon dựa trên trạng thái game
        if (state == GameState.PLAYING) {
            icon = AssetLoader.PAUSE_ICON;
        } else { // (state == GameState.PAUSED)
            icon = AssetLoader.PLAY_ICON;
        }

        if (icon == null) {
            // Vẽ dự phòng nếu ảnh lỗi
            g.setColor(Color.WHITE);
            g.drawRect(pauseRect.x, pauseRect.y, pauseRect.width, pauseRect.height);
            if (state == GameState.PLAYING) g.drawString("II", pauseRect.x + 10, pauseRect.y + 30);
            else g.drawString(">", pauseRect.x + 10, pauseRect.y + 30);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        // Thêm hiệu ứng highlight khi di chuột qua
        if (pauseRect.contains(mouseX, mouseY)) {
            // Vẽ một lớp mờ màu trắng (tăng độ sáng)
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.fillRect(pauseRect.x, pauseRect.y, pauseRect.width, pauseRect.height);
        }

        // Vẽ icon
        g2d.drawImage(icon, pauseRect.x, pauseRect.y, pauseRect.width, pauseRect.height, null);
    }

    /**
     * Vẽ màn hình Bảng Xếp Hạng
     */
    public void drawHighScores(Graphics g, int w, int h, int mouseX, int mouseY,
                               Rectangle backRect, List<Integer> scores) {

        //  Vẽ nền
        drawGameBackground(g, w, h, 5);

        //  Dùng Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //  Vẽ Tiêu đề
        g2d.setFont(titleFont);
        g2d.setColor(Color.YELLOW);
        String title = "HIGH SCORES";
        FontMetrics fmTitle = g2d.getFontMetrics();
        int titleX = (w - fmTitle.stringWidth(title)) / 2;
        int titleY = 100;
        g2d.drawString(title, titleX, titleY);

        //  Vẽ danh sách điểm
        g2d.setFont(instructionFont);
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();

        int startY = titleY + 100; // Vị trí dòng điểm đầu tiên
        if (scores == null || scores.isEmpty()) {
            String noScores = "No scores yet!";
            int noScoresX = (w - fm.stringWidth(noScores)) / 2;
            g2d.drawString(noScores, noScoresX, startY);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String rank = (i + 1) + ".";
                String scoreText = scores.get(i).toString();

                // Căn lề: Rank bên trái, Score bên phải
                g2d.drawString(rank, w / 2 - 150, startY + i * 40);
                g2d.drawString(scoreText, w / 2 - 50, startY + i * 40);
            }
        }

        // Vẽ nút BACK
        String textBack = "BACK";
        int backX = (w - fm.stringWidth(textBack)) / 2;
        int backY = h - 70; // Đặt ở gần đáy

        // Cập nhật nút
        backRect.setBounds(backX, backY - fm.getAscent(), fm.stringWidth(textBack), fm.getHeight());

        // Vẽ highlight
        if (backRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.WHITE);
        g2d.drawString(textBack, backX, backY);
    }
    public void drawExplosion(Graphics g, Explosion explosion) {
        explosion.render(g);
    }

    /**
     * Hàm xử lý trong Setting, vẽ một nút bấm có style giống hệt màn hình Pause
     */
    private void drawStyledButton(Graphics2D g2d, Rectangle rect, String text, boolean isHovered) {
        FontMetrics fm = g2d.getFontMetrics();

        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();

        if (isHovered) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.WHITE);

        g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
        g2d.drawString(text, textX, textY);
    }

    /**
     * Vẽ một thanh trượt (slider)
     */
    private void drawSlider(Graphics2D g2d, Rectangle rect, float value, String label, boolean isHovered) {
        // Vẽ nhãn
        g2d.setFont(smallFont); // Dùng font nhỏ
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, rect.x, rect.y - 5);

        // Vẽ %
        g2d.drawString((int)(value * 100) + "%", rect.x + rect.width + 10, rect.y + rect.height / 2 + 5);

        // Vẽ nền thanh trượt
        g2d.setColor(new Color(50, 50, 50)); // Xám tối
        g2d.fill(rect);

        // Vẽ giá trị
        if (isHovered) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.CYAN);
        g2d.fillRect(rect.x, rect.y, (int)(rect.width * value), rect.height);

        // Vẽ viền
        if (isHovered) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.WHITE);
        g2d.draw(rect);
    }

    /**
     * Vẽ trang Settings chính
     */
    public void drawSettingsMain(Graphics g, int w, int h, int mx, int my,
                                 Rectangle soundRect, Rectangle backRect) {

        // Vẽ nền mờ (giống drawPause)
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, w, h);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Vẽ tiêu đề
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "SETTINGS";
        FontMetrics fmTitle = g2d.getFontMetrics(titleFont);
        int titleX = (w - fmTitle.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 150);

        // Vẽ các nút
        g2d.setFont(instructionFont); // Đặt font cho hàm drawStyledButton
        drawStyledButton(g2d, soundRect, "Sound", soundRect.contains(mx, my));
        drawStyledButton(g2d, backRect, "Back", backRect.contains(mx, my));

    }

    /**
     * HÀM MỚI: Vẽ trang Settings âm thanh
     */
    public void drawSettingsSound(Graphics g, int w, int h, int mx, int my,
                                  Rectangle bgmRect, float bgmVol,
                                  Rectangle paddleRect, float paddleVol,
                                  Rectangle brickRect, float brickVol,
                                  Rectangle wallRect, float wallVol,
                                  Rectangle explosionRect, float explosionVol,
                                  Rectangle backRect) {

        // Vẽ nền mờ
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, w, h);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Vẽ tiêu đề
        g2d.setFont(titleFont.deriveFont(60f)); // Font nhỏ hơn 1 chút
        g2d.setColor(Color.WHITE);
        String title = "SOUND SETTINGS";
        FontMetrics fmTitle = g2d.getFontMetrics();
        int titleX = (w - fmTitle.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 100);

        // Vẽ các thanh trượt
        Point mousePoint = new Point(mx, my); // Dùng Point để check hover

        drawSlider(g2d, bgmRect, bgmVol, "Music Volume (WIP)", bgmRect.contains(mousePoint));
        drawSlider(g2d, paddleRect, paddleVol, "Paddle Hit", paddleRect.contains(mousePoint));
        drawSlider(g2d, brickRect, brickVol, "Brick Hit (WIP)", brickRect.contains(mousePoint));
        drawSlider(g2d, wallRect, wallVol, "Wall Hit (WIP)", wallRect.contains(mousePoint));
        drawSlider(g2d, explosionRect, explosionVol, "Explosion", explosionRect.contains(mousePoint));

        // Vẽ nút Back
        g2d.setFont(instructionFont); // Đặt font cho nút Back
        drawStyledButton(g2d, backRect, "Back", backRect.contains(mx, my));
    }
}
