package com.DevChickens.Arkanoid.graphics;
import com.DevChickens.Arkanoid.entities.effects.Explosion;

import com.DevChickens.Arkanoid.enums.GameState;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.awt.Rectangle;

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

    public void drawMenu(Graphics g, int w, int h, int mouseX, int mouseY,
                         Rectangle playRect, Rectangle highScoresRect, Rectangle exitRect) {

        // 1. Vẽ nền
        if (AssetLoader.MENU_BACKGROUND != null) {
            g.drawImage(AssetLoader.MENU_BACKGROUND, 0, 0, w, h, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, w, h);
        }

        // 2. Dùng Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 3. Vẽ Tiêu đề (đẩy lên cao một chút)
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "ARKANOID";
        FontMetrics fmTitle = g2d.getFontMetrics();
        int titleX = (w - fmTitle.stringWidth(title)) / 2;
        int titleY = h / 2 - 70;
        g2d.drawString(title, titleX, titleY);

        // 4. --- VẼ CÁC NÚT BẤM ---
        g2d.setFont(instructionFont);
        FontMetrics fm = g2d.getFontMetrics();

        // Tọa độ "PLAY" (Nút 1)
        String textPlay = "PLAY";
        int playX = (w - fm.stringWidth(textPlay)) / 2;
        int playY = h / 2 + 20;

        // Tọa độ "HIGH SCORES" (Nút 2)
        String textScores = "HIGH SCORES";
        int scoresX = (w - fm.stringWidth(textScores)) / 2;
        int scoresY = playY + 50;

        // Tọa độ "EXIT" (Nút 3)
        String textExit = "EXIT";
        int exitX = (w - fm.stringWidth(textExit)) / 2;
        int exitY = scoresY + 50;

        // --- Cập nhật "nút" (Rect) và vẽ ---
        // (Tính toán vị trí chính xác của chữ để bắt click)

        // Vẽ PLAY
        playRect.setBounds(playX, playY - fm.getAscent(), fm.stringWidth(textPlay), fm.getHeight());
        if (playRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
        else g2d.setColor(Color.WHITE);
        g2d.drawString(textPlay, playX, playY);

        // Vẽ HIGH SCORES
        highScoresRect.setBounds(scoresX, scoresY - fm.getAscent(), fm.stringWidth(textScores), fm.getHeight());
        if (highScoresRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
        else g2d.setColor(Color.WHITE);
        g2d.drawString(textScores, scoresX, scoresY);

        // Vẽ EXIT
        exitRect.setBounds(exitX, exitY - fm.getAscent(), fm.stringWidth(textExit), fm.getHeight());
        if (exitRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW); // Highlight
        else g2d.setColor(Color.WHITE);
        g2d.drawString(textExit, exitX, exitY);
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
                          Rectangle continueBtn, Rectangle restartBtn, Rectangle exitBtn) {

        // Vẽ lớp phủ mờ
        g.setColor(new Color(0, 0, 0, 150)); // Màu đen mờ (Alpha = 150).
        g.fillRect(0, 0, w, h);

        // Dùng Graphics2D và bật khử răng cưa
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Chuẩn bị Font (dùng chung font instruction)
        g2d.setFont(instructionFont);
        FontMetrics fm = g2d.getFontMetrics();

        // --- Vẽ Nút "Continue" ---
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

        // 1. Vẽ nền (dùng chung nền round 1)
        drawGameBackground(g, w, h, 1);

        // 2. Dùng Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 3. Vẽ Tiêu đề
        g2d.setFont(titleFont);
        g2d.setColor(Color.YELLOW);
        String title = "HIGH SCORES";
        FontMetrics fmTitle = g2d.getFontMetrics();
        int titleX = (w - fmTitle.stringWidth(title)) / 2;
        int titleY = 100;
        g2d.drawString(title, titleX, titleY);

        // 4. Vẽ danh sách điểm
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

        // 5. --- Vẽ nút "BACK" (dùng chuột) ---
        String textBack = "BACK";
        int backX = (w - fm.stringWidth(textBack)) / 2;
        int backY = h - 70; // Đặt ở gần đáy

        // Cập nhật "nút"
        backRect.setBounds(backX, backY - fm.getAscent(), fm.stringWidth(textBack), fm.getHeight());

        // Vẽ highlight
        if (backRect.contains(mouseX, mouseY)) g2d.setColor(Color.YELLOW);
        else g2d.setColor(Color.WHITE);
        g2d.drawString(textBack, backX, backY);
    }
    public void drawExplosion(Graphics g, Explosion explosion) {
        explosion.render(g);
    }
}
