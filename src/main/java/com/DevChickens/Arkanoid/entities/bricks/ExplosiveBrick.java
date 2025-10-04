package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.image.BufferedImage;

/**
 * Đại diện cho một viên gạch nổ.
 * Bị phá hủy sau 1 va chạm và đóng vai trò là ngòi nổ.
 * Logic gây nổ lan truyền được xử lý ở lớp GameManager.
 */
public class ExplosiveBrick extends Brick {
    /** Lượng máu của gạch, chỉ cần 1 lần va chạm để kích nổ. */
    private static final int HEALTH = 1;
    /** Điểm số người chơi nhận được. */
    private static final int SCORE = 150;

    /** Biến lưu trữ hình ảnh của viên gạch. */
    private BufferedImage image;

    /**
     * Phương thức khởi tạo một viên gạch nổ tại một vị trí cụ thể.
     * @param x Tọa độ X của viên gạch.
     * @param y Tọa độ Y của viên gạch.
     */
    public ExplosiveBrick(double x, double y) {
        super(x, y, 64, 20, HEALTH, SCORE);
        this.image = AssetLoader.loadImage("/images/ExplosiveBrick.png");
    }

    /**
     * Cung cấp hình ảnh trực quan của viên gạch.
     * @return Đối tượng BufferedImage chứa hình ảnh của gạch.
     */
    @Override
    public BufferedImage getImage() {
        return this.image;
    }
}