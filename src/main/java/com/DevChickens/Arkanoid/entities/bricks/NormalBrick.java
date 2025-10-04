package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.image.BufferedImage;

/**
 * Viên gạch bình thường trong game
 * Bị phá hủy sau 1 va chạm.
 */
public class NormalBrick extends Brick {
    /** Lượng máu của gạch, chỉ cần 1 lần va chạm. */
    private static final int HEALTH = 1;
    /** Điểm số người chơi nhận được. */
    private static final int SCORE = 100;

    /** Biến lưu trữ hình ảnh của viên gạch. */
    private BufferedImage image;

    /**
     * Phương thức khởi tạo một viên gạch bình thường tại một vị trí cụ thể.
     * @param x Tọa độ X (hoành độ) của viên gạch.
     * @param y Tọa độ Y (tung độ) của viên gạch.
     */
    public NormalBrick(double x, double y) {
        super(x, y, 64, 20, HEALTH, SCORE);
        this.image = AssetLoader.loadImage("/images/NormalBrick.png");
    }

    /**
     * Cung cấp hình ảnh của viên gạch.
     * Được gọi bởi lớp Renderer để vẽ gạch lên màn hình.
     * @return Đối tượng BufferedImage chứa hình ảnh của gạch.
     */
    @Override
    public BufferedImage getImage() {
        return this.image;
    }
}
