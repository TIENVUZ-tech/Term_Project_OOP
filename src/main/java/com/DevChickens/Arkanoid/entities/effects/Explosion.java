package com.DevChickens.Arkanoid.entities.effects;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.List;

/**
 * Lớp quản lý hiệu ứng nổ (animation).
 * Tự cập nhật frame và tự đánh dấu là đã kết thúc khi chạy xong.
 */
public class Explosion {
    private double x, y; // Tọa độ tâm của vụ nổ
    private double width, height; // kích thước của vụ nổ

    private List<BufferedImage> frames; // Danh sách các ảnh (lấy từ AssetLoader)
    private int currentFrame; // Frame hiện tại (index)
    private long lastFrameTime; // Thời điểm vẽ frame cuối
    private final long frameDuration = 70; // Thời gian cho mỗi frame
    private boolean finished; // biến đánh dấu chạy xong animation chưa.

    public Explosion(double x, double y, double width, double height) {
        this.x = x; // tâm vụ nổ là tâm viên gạch
        this.y = y;
        this.width = width; // Kích thước mong muốn của vụ nổ
        this.height = height;

        // Lấy các frame đã được tải sẵn từ AssetLoader
        this.frames = AssetLoader.EXPLOSION_FRAMES;

        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
        this.finished = false;

        // Nếu không tải được ảnh nào, coi như kết thúc luôn
        if (frames == null || frames.isEmpty()) {
            finished = true;
        }
    }

    /**
     * Cập nhật logic animation, chuyển frame nếu đủ thời gian.
     */
    public void update() {
        if (finished) return;

        long now = System.currentTimeMillis();
        // nếu đến lúc chuyển sang frame tiếp
        if (now - lastFrameTime >= frameDuration) {
            currentFrame++; // Chuyển sang frame tiếp theo
            lastFrameTime = now; // Đặt lại thời gian

            // Nếu đã chạy hết số frame
            if (currentFrame >= frames.size()) {
                finished = true; // Đánh dấu là đã kết thúc
            }
        }
    }

    /**
     * Vẽ frame hiện tại lên màn hình.
     */
    public void render(Graphics g) {
        if (finished || frames == null || frames.isEmpty()) return;

        // Lấy ảnh của frame hiện tại
        BufferedImage frame = frames.get(currentFrame);

        // Vẽ ảnh, điều chỉnh tọa độ
        int drawX = (int) (x - width / 2);
        int drawY = (int) (y - height / 2);

        g.drawImage(frame, drawX, drawY, (int) width, (int) height, null);
    }

    /**
     * Trả về true nếu animation đã chạy xong.
     */
    public boolean isFinished() {
        return finished;
    }
}