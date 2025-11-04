package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.Bullet;
import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.bricks.Brick;
import com.DevChickens.Arkanoid.entities.effects.Explosion;
import com.DevChickens.Arkanoid.entities.powerups.PowerUp;
import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.core.UIManager;
import com.DevChickens.Arkanoid.enums.GameState;
// Import các thư viện Java AWT
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Renderer - Trình kết xuất: chịu trách nhiệm cho toàn bộ logic vẽ của game.
 */
public class Renderer {

    private  Font titleFont;
    private  Font instructionFont;
    private  Font smallFont;

    /**
     * Hàm khởi tạo Renderer.
     */
    public Renderer() {
        // Khởi tạo các font này một lần duy nhất để tối ưu hiệu suất
        try {
            titleFont = new Font("Georgia", Font.BOLD, 72);
            instructionFont = new Font("Georgia", Font.PLAIN, 30);
            smallFont = new Font("Georgia", Font.PLAIN, 20);
        } catch (Exception e) {
            // Fallback an toàn nếu font không tồn tại
            titleFont = new Font("SansSerif", Font.BOLD, 72);
            instructionFont = new Font("SansSerif", Font.PLAIN, 28);
            smallFont = new Font("SansSerif", Font.PLAIN, 20);
        }
    }

    // Các hàm tiện ích vẽ đối tượng game.

    /**
     * Cho paddle tự vẽ.
     */
    public void drawPaddle(Graphics g, Paddle paddle) {
        if (paddle != null) {
            paddle.render(g);
        }
    }

    /**
     * Cho ball tự vẽ.
     */
    public void drawBall(Graphics g, Ball ball) {
        if (ball != null) {
            ball.render(g);
        }
    }

    /**
     * Cho bullet tự vẽ.
     */
    public void drawBullet(Graphics g, Bullet bullet) {
        if (bullet != null) {
            bullet.render(g);
        }
    }

    /**
     * Cho brick tự vẽ.
     */
    public void drawBrick(Graphics g, Brick brick) {
        if (brick != null && !brick.isDestroyed()) {
            brick.render(g);
        }
    }

    /**
     * Cho powerUp tự vẽ
     */
    public void drawPowerUp(Graphics g, PowerUp powerUp) {
        if (powerUp != null) {
            powerUp.render(g);
        }
    }

    /**
     * Cho Explosion tự vẽ.
     */
    public void drawExplosion(Graphics g, Explosion explosion) {
        if (explosion != null) {
            explosion.render(g);
        }
    }

    // Các hàm vẽ toàn màn hình.

    /**
     * Vẽ màn hình Menu chính.
     */
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

        // Bật khử răng cưa để chữ đẹp hơn
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setFont(instructionFont);
        FontMetrics fm = g2d.getFontMetrics();

        int startY = h / 2 + 20;
        int spacing = 50;

        if (isGameInProgress) {
            // TH1: Đã có game đang chơi dở
            String textContinue = "CONTINUE";
            int continueX = (w - fm.stringWidth(textContinue)) / 2;
            int continueY = startY;
            // Tác dụng phụ: Cập nhật hitbox cho UIManager
            continueRect.setBounds(continueX, continueY - fm.getAscent(), fm.stringWidth(textContinue), fm.getHeight());
            if (continueRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textContinue, continueX, continueY);

            String textPlay = "NEW GAME";
            int playX = (w - fm.stringWidth(textPlay)) / 2;
            int playY = continueY + spacing;
            playRect.setBounds(playX, playY - fm.getAscent(), fm.stringWidth(textPlay), fm.getHeight());
            if (playRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textPlay, playX, playY);

            String textScores = "HIGH SCORES";
            int scoresX = (w - fm.stringWidth(textScores)) / 2;
            int scoresY = playY + spacing;
            highScoresRect.setBounds(scoresX, scoresY - fm.getAscent(), fm.stringWidth(textScores), fm.getHeight());
            if (highScoresRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textScores, scoresX, scoresY);

            String textSettings = "SETTINGS";
            int settingsX = (w - fm.stringWidth(textSettings)) / 2;
            int settingsY = scoresY + spacing;
            settingsRect.setBounds(settingsX, settingsY - fm.getAscent(), fm.stringWidth(textSettings), fm.getHeight());
            if (settingsRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textSettings, settingsX, settingsY);

            String textExit = "EXIT";
            int exitX = (w - fm.stringWidth(textExit)) / 2;
            int exitY = settingsY + spacing;
            exitRect.setBounds(exitX, exitY - fm.getAscent(), fm.stringWidth(textExit), fm.getHeight());
            if (exitRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textExit, exitX, exitY);

        } else {
            //  TH2: Game mới
            String textPlay = "PLAY";
            int playX = (w - fm.stringWidth(textPlay)) / 2;
            int playY = startY;
            // Tác dụng phụ: Cập nhật hitbox cho UIManager
            playRect.setBounds(playX, playY - fm.getAscent(), fm.stringWidth(textPlay), fm.getHeight());
            if (playRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textPlay, playX, playY);

            String textScores = "HIGH SCORES";
            int scoresX = (w - fm.stringWidth(textScores)) / 2;
            int scoresY = playY + spacing;
            highScoresRect.setBounds(scoresX, scoresY - fm.getAscent(), fm.stringWidth(textScores), fm.getHeight());
            if (highScoresRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textScores, scoresX, scoresY);

            String textSettings = "SETTINGS";
            int settingsX = (w - fm.stringWidth(textSettings)) / 2;
            int settingsY = scoresY + spacing;
            settingsRect.setBounds(settingsX, settingsY - fm.getAscent(), fm.stringWidth(textSettings), fm.getHeight());
            if (settingsRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textSettings, settingsX, settingsY);

            String textExit = "EXIT";
            int exitX = (w - fm.stringWidth(textExit)) / 2;
            int exitY = settingsY + spacing;
            exitRect.setBounds(exitX, exitY - fm.getAscent(), fm.stringWidth(textExit), fm.getHeight());
            if (exitRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
            else g2d.setColor(Color.WHITE);
            g2d.drawString(textExit, exitX, exitY);
        }
    }

    /**
     * Vẽ nền của màn chơi, dựa trên màn (round) hiện tại.
     */
    public void drawGameBackground(Graphics g, int w, int h, int round) {
        BufferedImage bg = AssetLoader.getRoundBackground(round);

        if (bg != null) {
            g.drawImage(bg, 0, 0, w, h, null);
        } else {
            // Fallback: Vẽ nền đen nếu không tải được ảnh
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, w, h);
        }
    }

    /**
     * Vẽ màn hình PAUSE, chỉ dựa trên các Rectangle được tính sẵn.
     */
    public void drawPause(Graphics g, int w, int h, int mouseX, int mouseY,
                          Rectangle continueBtn, Rectangle restartBtn,
                          Rectangle settingsBtn, Rectangle exitBtn) {

        // Vẽ lớp phủ mờ (Alpha = 150)
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, w, h);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(instructionFont);

        // Dùng hàm tiện ích drawStyledButton
        Point mousePoint = new Point(mouseX, mouseY);
        drawStyledButton(g2d, continueBtn, "Continue", continueBtn.contains(mousePoint));
        drawStyledButton(g2d, restartBtn, "Restart Level", restartBtn.contains(mousePoint));
        drawStyledButton(g2d, settingsBtn, "Settings", settingsBtn.contains(mousePoint));
        drawStyledButton(g2d, exitBtn, "Exit to Menu", exitBtn.contains(mousePoint));
    }

    /**
     * Vẽ màn hình chuyển màn (ví dụ: "ROUND 2").
     */
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
        int x = (width - fm.stringWidth(text)) / 2; // Căn giữa
        int y = height / 2;
        g2d.drawString(text, x, y);

        // Vẽ "Get Ready..."
        g2d.setFont(instructionFont);
        text = "Get Ready...";
        fm = g2d.getFontMetrics();
        x = (width - fm.stringWidth(text)) / 2; // Căn giữa
        y = height / 2 + 50; // Dịch xuống dưới
        g2d.drawString(text, x, y);
    }

    /**
     * Vẽ màn hình "Game Over".
     */
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

    /**
     * Vẽ màn hình "Victory".
     */
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
     * Vẽ icon Pause/Play ở góc màn hình khi đang chơi.
     */
    public void drawPauseIcon(Graphics g, GameState state, int mouseX, int mouseY, Rectangle pauseRect) {
        BufferedImage icon = null;

        // Chọn icon dựa trên trạng thái game
        if (state == GameState.PLAYING) {
            icon = AssetLoader.PAUSE_ICON;
        } else {
            icon = AssetLoader.PLAY_ICON;
        }

        if (icon == null) {
            // Vẽ dự phòng (fallback) nếu ảnh bị lỗi
            g.setColor(Color.WHITE);
            g.drawRect(pauseRect.x, pauseRect.y, pauseRect.width, pauseRect.height);
            if (state == GameState.PLAYING) g.drawString("II", pauseRect.x + 10, pauseRect.y + 30);
            else g.drawString(">", pauseRect.x + 10, pauseRect.y + 30);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        // Thêm hiệu ứng highlight (phản hồi) khi di chuột qua
        if (pauseRect.contains(mouseX, mouseY)) {
            // Vẽ một lớp mờ màu trắng (tăng độ sáng)
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.fillRect(pauseRect.x, pauseRect.y, pauseRect.width, pauseRect.height);
        }

        // Vẽ icon
        g2d.drawImage(icon, pauseRect.x, pauseRect.y, pauseRect.width, pauseRect.height, null);
    }

    /**
     * Vẽ màn hình Bảng Xếp Hạng. Màn hình này sẽ tự tính toán và cập nhật số điểm rồi thay đổi.
     */
    public void drawHighScores(Graphics g, int w, int h, int mouseX, int mouseY,
                               Rectangle backRect, List<Integer> scores) {

        //  Vẽ nền (dùng nền của round 5 cho đẹp)
        drawGameBackground(g, w, h, 5);

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
            // Chỉ vẽ 10 điểm cao nhất
            int numScoresToShow = Math.min(scores.size(), 10);
            for (int i = 0; i < numScoresToShow; i++) {
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

        // Tác dụng phụ: Cập nhật hitbox cho UIManager
        backRect.setBounds(backX, backY - fm.getAscent(), fm.stringWidth(textBack), fm.getHeight());

        // Vẽ highlight
        if (backRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.WHITE);
        g2d.drawString(textBack, backX, backY);
    }

    // Các hàm tiện tích vẽ.

    /**
     * Hàm tiện ích (private) để vẽ một nút bấm có viền và chữ
     * (thường dùng trong các menu Pop-up như Pause, Settings).
     *
     * @param g2d       Đối tượng Graphics2D (đã set Font).
     * @param rect      Hitbox (vị trí) của nút.
     * @param text      Nội dung chữ.
     * @param isHovered Cờ cho biết chuột có đang di lên nút không (để highlight).
     */
    private void drawStyledButton(Graphics2D g2d, Rectangle rect, String text, boolean isHovered) {
        FontMetrics fm = g2d.getFontMetrics();

        // Tính toán để căn giữa chữ bên trong hình chữ nhật
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();

        if (isHovered) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.WHITE);

        g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
        g2d.drawString(text, textX, textY);
    }

    /**
     * Hàm tiện ích (private) để vẽ một thanh trượt (slider) âm lượng.
     *
     * @param g2d       Đối tượng Graphics2D.
     * @param rect      Hitbox của thanh trượt.
     * @param value     Giá trị hiện tại (0.0f - 1.0f).
     * @param label     Tên của thanh trượt.
     * @param isHovered Cờ cho biết chuột có đang di lên không.
     */
    private void drawSlider(Graphics2D g2d, Rectangle rect, float value, String label, boolean isHovered) {
        // Vẽ nhãn (ví dụ: "Music Volume")
        g2d.setFont(smallFont); // Dùng font nhỏ
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, rect.x, rect.y - 5);

        // Vẽ giá trị % (ví dụ: "50%")
        g2d.drawString((int)(value * 100) + "%", rect.x + rect.width + 10, rect.y + rect.height / 2 + 5);

        // Vẽ nền thanh trượt (phần màu xám)
        g2d.setColor(new Color(50, 50, 50)); // Xám tối
        g2d.fill(rect);

        // Vẽ giá trị hiện tại (phần màu)
        if (isHovered) g2d.setColor(Color.YELLOW); // Highlight
        else g2d.setColor(Color.CYAN); // Màu mặc định
        g2d.fillRect(rect.x, rect.y, (int)(rect.width * value), rect.height);

        // Vẽ viền
        if (isHovered) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.WHITE);
        g2d.draw(rect);
    }

    /**
     * Vẽ trang chính của màn hình Settings.
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
        Point mousePoint = new Point(mx, my);
        drawStyledButton(g2d, soundRect, "Sound", soundRect.contains(mousePoint));
        drawStyledButton(g2d, backRect, "Back", backRect.contains(mousePoint));
    }

    /**
     * Vẽ trang Settings Âm thanh (Sound).
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

        drawSlider(g2d, bgmRect, bgmVol, "Music Volume", bgmRect.contains(mousePoint));
        drawSlider(g2d, paddleRect, paddleVol, "Paddle Hit", paddleRect.contains(mousePoint));
        drawSlider(g2d, brickRect, brickVol, "Brick Hit", brickRect.contains(mousePoint));
        drawSlider(g2d, wallRect, wallVol, "Wall Hit", wallRect.contains(mousePoint));
        drawSlider(g2d, explosionRect, explosionVol, "Explosion", explosionRect.contains(mousePoint));

        // Vẽ nút Back
        g2d.setFont(instructionFont); // Đặt font cho nút Back
        drawStyledButton(g2d, backRect, "Back", backRect.contains(mousePoint));
    }


    /**
     * Hàm vẽ chính (entry point) - được gọi bởi GameManager.
     * Nó đọc GameState hiện tại để vẽ.
     *
     * @param g  Đối tượng Graphics để vẽ.
     * @param gm Tham chiếu đến {@link GameManager} (để lấy dữ liệu game).
     * @param ui Tham chiếu đến {@link UIManager} (để lấy dữ liệu UI).
     */
    public void drawCurrentState(Graphics g, GameManager gm, UIManager ui) {

        // Lấy trạng thái game hiện tại
        GameState gameState = gm.getGameState();

        // Máy trạng thái Vẽ:
        // Quyết định màn hình nào sẽ được vẽ
        switch (gameState) {
            case MENU:
                drawMenu(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT,
                        ui.getMouseX(), ui.getMouseY(),
                        ui.getPlayButtonRect(),
                        ui.getHighScoresButtonRect(),
                        ui.getMenuSettingsButtonRect(),
                        ui.getExitButtonRect(),
                        gm.isGameInProgress(),
                        ui.getContinueButtonRect());
                break;
            case PLAYING:
            case PAUSED:
                drawGameBackground(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT, gm.getCurrentRound());

                // Vẽ các đối tượng game
                drawPaddle(g, gm.getPaddle());

                // --- CHÚ THÍCH QUAN TRỌNG ---
                // Phải lặp qua BẢN SAO (new ArrayList<>) của danh sách
                // để tránh lỗi ConcurrentModificationException.
                // Lỗi này xảy ra khi luồng Update (game loop) cố gắng
                // xóa một đối tượng (ví dụ: đạn nổ) cùng lúc
                // luồng Draw (UI) đang lặp qua nó để vẽ.
                for (Ball b : new ArrayList<>(gm.getBalls())) drawBall(g, b);
                for (Bullet b : new ArrayList<>(gm.getBullets())) drawBullet(g, b);
                for (Brick b : new ArrayList<>(gm.getBricks())) drawBrick(g, b);
                for (PowerUp p : new ArrayList<>(gm.getPowerUps())) drawPowerUp(g, p);
                for (Explosion exp : new ArrayList<>(gm.getExplosions())) drawExplosion(g, exp);

                // VẼ UI TRONG GAME (Điểm, Mạng, Màn)
                g.setColor(Color.WHITE);
                g.drawString("Score: " + gm.getScore(), 10, 20);
                g.drawString("Lives: " + gm.getLives(), 10, 40);
                g.drawString("Round: " + gm.getCurrentRound() + "/" + gm.getMaxRounds(), 10, 60);

                // Vẽ icon Pause (lấy vị trí từ UIManager)
                drawPauseIcon(g, gameState,
                        ui.getMouseX(), ui.getMouseY(),
                        ui.getPauseButtonRect());

                // VẼ MÀN HÌNH PAUSED (nếu đang pause)
                if (gameState == GameState.PAUSED) {
                    drawPause(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT,
                            ui.getMouseX(), ui.getMouseY(),
                            ui.getPauseContinueButton(),
                            ui.getPauseRestartButton(),
                            ui.getPauseSettingsButtonRect(),
                            ui.getPauseExitButton());
                }
                break;
            case ROUND_CLEAR:
                // Giống Playing.
                drawGameBackground(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT, gm.getCurrentRound());
                drawPaddle(g, gm.getPaddle());

                for (Ball b : new ArrayList<>(gm.getBalls())) drawBall(g, b);
                for (Bullet b : new ArrayList<>(gm.getBullets())) drawBullet(g, b);
                for (Brick b : new ArrayList<>(gm.getBricks())) drawBrick(g, b);
                for (PowerUp p : new ArrayList<>(gm.getPowerUps())) drawPowerUp(g, p);
                for (Explosion exp : new ArrayList<>(gm.getExplosions())) drawExplosion(g, exp);

                g.setColor(Color.WHITE);
                g.drawString("Score: " + gm.getScore(), 10, 20);
                g.drawString("Lives: " + gm.getLives(), 10, 40);
                g.drawString("Round: " + gm.getCurrentRound() + "/" + gm.getMaxRounds(), 10, 60);

                drawPauseIcon(g, gameState,
                        ui.getMouseX(), ui.getMouseY(),
                        ui.getPauseButtonRect());
                break;
            case HIGH_SCORES:
                drawHighScores(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT,
                        ui.getMouseX(), ui.getMouseY(),
                        ui.getBackButtonRect(),
                        gm.getHighScores());
                break;
            case NEXT_ROUND:
                drawNextRound(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT, gm.getCurrentRound());
                break;
            case GAME_OVER:
                drawGameOver(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT, gm.getScore());
                break;
            case VICTORY:
                drawVictory(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT, gm.getScore());
                break;
            case SETTINGS:
                // Lấy trang settings con từ UIManager
                switch (ui.getCurrentSettingsPage()) {
                    case MAIN:
                        drawSettingsMain(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT,
                                ui.getMouseX(), ui.getMouseY(),
                                ui.getSettingsSoundButtonRect(),
                                ui.getSettingsBackRect());
                        break;
                    case SOUND:
                        // Lấy giá trị âm lượng từ GameManager để vẽ con trượt
                        drawSettingsSound(g, GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT,
                                ui.getMouseX(), ui.getMouseY(),
                                ui.getSliderBgmRect(), gm.getVolumeBGM(),
                                ui.getSliderPaddleRect(), gm.getVolumePaddle(),
                                ui.getSliderBrickRect(), gm.getVolumeBrick(),
                                ui.getSliderWallRect(), gm.getVolumeWall(),
                                ui.getSliderExplosionRect(), gm.getVolumeExplosion(),
                                ui.getSoundBackRect());
                        break;
                }
                break;
        }
    }
}