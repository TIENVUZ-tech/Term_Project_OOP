package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Đại diện cho một viên gạch bình thường, bị phá hủy sau một lần va chạm.
 */
public class NormalBrick extends Brick {
    private static final int HIT_POINTS = 1;
    private final BufferedImage image;

    /**
     * Khởi tạo một viên gạch bình thường tại một vị trí cụ thể.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     */
    public NormalBrick(double x, double y) {
        super(x, y, 64, 20, HIT_POINTS, "NORMAL");
        this.image = AssetLoader.loadImage("/images/NormalBrick.png");
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