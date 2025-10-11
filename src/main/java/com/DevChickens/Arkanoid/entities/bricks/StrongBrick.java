package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.entities.GameObject;
import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Đại diện cho một viên gạch bất tử, không thể bị phá hủy.
 */
public class StrongBrick extends Brick {

    /**
     * Khởi tạo một viên gạch bất tử tại một vị trí cụ thể.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     */
    public StrongBrick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height, hitPoints, type);
        this.image = AssetLoader.loadImage("/images/StrongBrick.png");
    }

    /**
     * Ghi đè phương thức takeHit() để không làm gì cả, khiến gạch trở nên bất tử.
     */
    @Override
    public void takeHit() {
        // Gạch này không nhận sát thương.
    }

    /**
     * Vẽ viên gạch lên màn hình.
     * @param g Đối tượng Graphics dùng để vẽ.
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(this.image, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
    }
}