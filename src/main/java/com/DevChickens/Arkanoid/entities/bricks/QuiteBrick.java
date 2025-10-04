package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.image.BufferedImage;

/**
 * Viên gạch cần 2 lần va chạm mới bị phá hủy
 * Viên gạch sẽ thay đổi hình dạng (bị nứt) sau lần va chạm đầu tiên.
 */
public class QuiteBrick extends Brick {
    /** Lượng máu của gạch, cần 2 lần va chạm. */
    private static final int HEALTH = 2;
    /** Điểm số người chơi nhận được. */
    private static final int SCORE = 250;

    /** Hình ảnh khi gạch còn nguyên vẹn. */
    private BufferedImage imageNormal;
    /** Hình ảnh khi gạch đã bị nứt sau 1 lần va chạm. */
    private BufferedImage imageCracked;

    /**
     * Phương thức khởi tạo một viên gạch "dai" tại một vị trí cụ thể.
     * @param x Tọa độ X của viên gạch.
     * @param y Tọa độ Y của viên gạch.
     */
    public QuiteBrick(double x, double y) {
        super(x, y, 64, 20, HEALTH, SCORE);
        this.imageNormal = AssetLoader.loadImage("/images/QuiteBrick.png");
        this.imageCracked = AssetLoader.loadImage("/images/QuiteBrick_cracked.png");
    }

    /**
     * viên gạch có 2 trạng thái, mỗi hình ảnh tương ứng với 1 trạng thái.
     * @return Hình ảnh bị nứt nếu còn 1 máu, ngược lại trả về hình ảnh bình thường.
     */
    @Override
    public BufferedImage getImage() {
        if (this.getHealth() == 1) {
            return this.imageCracked;
        } else {
            return this.imageNormal;
        }
    }

}
