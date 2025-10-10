package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Đại diện cho một viên gạch nổ. Bị phá hủy sau một lần va chạm.
 * Lưu ý: Logic gây nổ lan truyền được xử lý ở lớp cấp cao hơn (GameManager).
 */
public class ExplosiveBrick extends Brick {
    /**
     * Khởi tạo một viên gạch nổ tại một vị trí cụ thể.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     */
    public ExplosiveBrick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height, hitPoints, type);
        this.image = AssetLoader.loadImage("/images/ExplosiveBrick.png");
        if (this.image != null) {
            double originalWidth = this.image.getWidth();
            double originalHeight = this.image.getHeight();
            double aspectRatio = originalHeight / originalWidth;

            this.setWidth(width);
            this.setHeight(width * aspectRatio);
        }
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