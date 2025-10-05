package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Đại diện cho một viên gạch bình thường, bị phá hủy sau một lần va chạm.
 */
public class NormalBrick extends Brick {

    // private final BufferedImage image;

    /**
     * Khởi tạo một viên gạch bình thường tại một vị trí cụ thể.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     */
    public NormalBrick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height, hitPoints, type);
        // this.image = AssetLoader.loadImage("/images/NormalBrick.png");
    }

    /**
     * Vẽ viên gạch lên màn hình nếu nó chưa bị phá hủy.
     * @param g Đối tượng Graphics dùng để vẽ.
     */
    @Override
    public void render(Graphics g) {
        // if (!isDestroyed()) {
        //     g.drawImage(this.image, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
        // }
    }
}