package com.DevChickens.Arkanoid.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.graphics.AssetLoader;

/**
 * Bullet kế thừa từ MovebleObject. Viên đạn mà Paddle sẽ bắn ra khi ăn GunPaddlePowerUp.
 * Thuộc tính: speed(tốc độ của viên dạn), image(ảnh của viên dạn).
 * Phương thức: move, update, checkCollision.
 * @author Vũ Văn Tiến.
 */

public class Bullet extends MovableObject {

    private static final int DIRECTION_X = 0; // Hướng di chuyển của đạn theo phương ngang.
    private static final int DIRECTION_Y = -1; // Hướng di chuyển của đạn theo phương dọc.

    private double speed; // Tốc độ di chuyển của đạn.
    private BufferedImage image; // Biến chứa ảnh của viên đạn.

    /**
     * Phương thức khởi tạo Bullet, giữ nguyên 9 tham số đầu vào.
     * @param x (tọa độ x)
     * @param y (toạ độ y)
     * @param dx (tốc độ di chuyển theo trục x - được truyền lên lớp cha)
     * @param dy (tốc độ di chuyển theo chiều y - được truyền lên lớp cha)
     * @param speed (tốc độ di chuyển tổng - được dùng trong move())
     */
    public Bullet(double x, double y, double dx, double dy,
            double speed) { // LỖI: Sửa thụt lề ngắt dòng (Mục 4.2)
        super(x, y, 0, 0, dx, dy);
        this.speed = speed;
        try {
            // Đọc ảnh từ thư mục images
            this.image = AssetLoader.loadImage("/images/Bullet.png");

            // Thiết lập các thông số kích thước cho viên đạn.
            if (this.image != null) {
                final double TARGET_WIDTH = 10.0;

                // Tính toán chiều cao để giữ nguyên tỉ lệ ảnh gốc.
                double aspectRatio = (double) this.image.getHeight() / this.image.getWidth();
                double targetHeight = aspectRatio * TARGET_WIDTH;

                // Đặt kích thước cho viên đạn
                this.setWidth(TARGET_WIDTH);
                this.setHeight(targetHeight);
            } else {
                throw new IOException("Không thể tải ảnh của Bullet tại: /images/Bulle.png");
            }
        } catch (Exception e) {
            // In ra lỗi gốc.
            e.printStackTrace();
            // Ném ra ngoại lệ và dừng chương trình.
            throw new RuntimeException("Lỗi không thể tải ảnh cho Bullet", e);
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    @Override
    public void move() {
        // Cách thức di chuyển thẳng đứng của các viên đạn.
        setDx(speed * DIRECTION_X);
        setDy(speed * DIRECTION_Y);
        setX(getX() + getDx());
        setY(getY() + getDy());
    }

    @Override
    public void update() {
        // Cập nhật lại vị trí sau khi di chuyển.
        move();
    }

    @Override
    public void render(Graphics g) {
         Graphics2D g2d = (Graphics2D) g;
        if (this.getImage() != null) {
            g2d.drawImage(this.getImage(), (int) this.getX(), (int) this.getY(), 
            (int) this.getWidth(), (int) this.getHeight(), null);
        }
    }

    /**
     * Phương thức checkCollision để kiểm tra va chạm giữa Bullet và các vật thể khác.
     * @param other
     * @return
     */
    public boolean checkCollision(GameObject other) {
        return (this.getX() < other.getX() + other.getWidth()) &&
               (this.getX() + this.getWidth() > other.getX()) &&
               (this.getY() < other.getY() + other.getHeight()) &&
               (this.getY() + this.getHeight() > other.getY());
    }
}