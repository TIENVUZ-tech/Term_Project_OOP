package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.image.BufferedImage;

/**
 * Viên gạch không thể bị phá hủy
 * Thường được dùng làm chướng ngại vật cố định trên màn chơi.
 */
public class StrongBrick extends Brick {
    /** Điểm số là 0 vì không thể phá hủy. */
    private static final int SCORE = 0;
    /** Gán health là -1 để biểu thị trạng thái không thể phá hủy. */
    private static final int HEALTH = -1;

    /** Biến lưu trữ hình ảnh của viên gạch. */
    private BufferedImage image;

    /**
     * Phương thức khởi tạo một viên gạch bất tử tại một vị trí cụ thể.
     * @param x Tọa độ X của viên gạch.
     * @param y Tọa độ Y của viên gạch.
     */
    public StrongBrick(double x, double y) {
        super(x, y, 64, 20, HEALTH, SCORE);
        this.image = AssetLoader.loadImage("/images/StrongBrick.png");
    }

    /**
     * Ghi đè phương thức hit() của lớp cha.
     * không có bất kỳ hành động nào xảy ra, khiến viên gạch trở nên bất tử.
     */
    @Override
    public void hit() {

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
