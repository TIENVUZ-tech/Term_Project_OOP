package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Viên gạch cần 2 lần va chạm để phá hủy.
 * Viên gạch sẽ thay đổi hình dạng bị nứt sau lần va chạm đầu tiên.
 */
public class QuiteBrick extends Brick {
    private final BufferedImage imageNormal;
    private final BufferedImage imageCracked;

    /**
     * Khởi tạo một viên gạch cứng tại một vị trí cụ thể.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     */
    public QuiteBrick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height, hitPoints, type);
        this.imageNormal = AssetLoader.loadImage("/images/QuiteBrick.png");
        this.imageCracked = AssetLoader.loadImage("/images/QuiteBrick_cracked.png");
        this.image = this.imageNormal;
        if (this.imageNormal != null) {
            double originalWidth = this.imageNormal.getWidth();
            double originalHeight = this.imageNormal.getHeight();
            double aspectRatio = originalHeight / originalWidth;

            this.setWidth(width);
            this.setHeight(width * aspectRatio);
        }

    }

        /**
         * override takeHit() để cập nhật ảnh
         */
        @Override
        public void takeHit() {
            super.takeHit(); // Gọi phương thức của lớp cha để trừ máu (hitPoints--)

            // Sau khi trừ máu, kiểm tra xem có cần đổi ảnh không
            if (this.hitPoints == 1) {
                this.image = this.imageCracked; // Cập nhật sang ảnh bị nứt
            }
        }

        /**
         * Logic chọn ảnh nào đã được chuyển vào takeHit().
         */
        @Override
        public void render(Graphics g) {
            if (!isDestroyed() && this.image != null) {
                g.drawImage(this.image, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
            }
        }
}