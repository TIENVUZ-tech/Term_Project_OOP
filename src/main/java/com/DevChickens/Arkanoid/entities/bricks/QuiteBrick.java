package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Đại diện cho một viên gạch cứng, cần hai lần va chạm để phá hủy.
 * Viên gạch sẽ thay đổi hình dạng (bị nứt) sau lần va chạm đầu tiên.
 */
public class QuiteBrick extends Brick {
    private static final int HIT_POINTS = 2;
    private final BufferedImage imageNormal;
    private final BufferedImage imageCracked;

    /**
     * Khởi tạo một viên gạch cứng tại một vị trí cụ thể.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     */
    public QuiteBrick(double x, double y) {
        super(x, y, 64, 20, HIT_POINTS, "QUITE");
        this.imageNormal = AssetLoader.loadImage("/images/QuiteBrick.png");
        this.imageCracked = AssetLoader.loadImage("/images/QuiteBrick_cracked.png");
    }

    /**
     * Vẽ viên gạch lên màn hình tùy theo trạng thái hitPoints.
     * @param g Đối tượng Graphics dùng để vẽ.
     */
    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            if (this.hitPoints == 1) {
                g.drawImage(this.imageCracked, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
            } else {
                g.drawImage(this.imageNormal, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
            }
        }
    }
}