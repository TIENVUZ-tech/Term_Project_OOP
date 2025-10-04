package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Đại diện cho một viên gạch nổ. Bị phá hủy sau một lần va chạm.
 * Lưu ý: Logic gây nổ lan truyền được xử lý ở lớp cấp cao hơn (GameManager).
 */
public class ExplosiveBrick extends Brick {
    private static final int HIT_POINTS = 1;
    private final BufferedImage image;

    /**
     * Khởi tạo một viên gạch nổ tại một vị trí cụ thể.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     */
    public ExplosiveBrick(double x, double y) {
        super(x, y, 64, 20, HIT_POINTS, "EXPLOSIVE");
        this.image = AssetLoader.loadImage("/images/ExplosiveBrick.png");
    }

    /**
     * Vẽ viên gạch lên màn hình nếu nó chưa bị phá hủy.
     * @param g Đối tượng Graphics dùng để vẽ.
     */
    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            g.drawImage(this.image, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
        }
    }
}