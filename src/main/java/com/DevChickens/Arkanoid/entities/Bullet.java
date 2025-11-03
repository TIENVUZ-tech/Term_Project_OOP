package com.DevChickens.Arkanoid.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.DevChickens.Arkanoid.graphics.AssetLoader;

/**
 * Bullet kế thừa từ MovebleObject. Viên đạn mà Paddle sẽ bắn ra khi ăn GunPaddlePowerUp.
 * Thuộc tính: speed(tốc độ của viên dạn), image(ảnh của viên dạn).
 * Phương thức: move, update, checkCollision.
 */
public class Bullet extends MovableObject {

    /** Hướng di chuyển của đạn theo phương ngang. */
    private static final int DIRECTION_X = 0;
    /** Hướng di chuyển của đạn theo phương dọc. */
    private static final int DIRECTION_Y = -1;

    /** Tốc độ di chuyển của đạn. */
    private double speed;
    /** Biến chứa ảnh của viên đạn. */
    private BufferedImage image;
    /** Trạng thái của viên đạn (true nếu đã bị phá hủy) */
    private boolean isDestroyed = false;

    /**
     * Phương thức khởi tạo Bullet.
     * @param x (tọa độ x)
     * @param y (toạ độ y)
     * @param dx (tốc độ di chuyển theo trục x - được truyền lên lớp cha)
     * @param dy (tốc độ di chuyển theo chiều y - được truyền lên lớp cha)
     * @param speed (tốc độ di chuyển tổng - được dùng trong move())
     */
    public Bullet(double x, double y, double dx, double dy,
            double speed) {
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

    /**
     * Thiết lập tốc độ di chuyển của đạn.
     * @param speed Tốc độ mới
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Lấy tốc độ di chuyển hiện tại của đạn.
     * @return Tốc độ của đạn
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Lấy ảnh (BufferedImage) của viên đạn để vẽ.
     * @return Ảnh của viên đạn
     */
    public BufferedImage getImage() {
        return this.image;
    }

    /**
     * Kiểm tra xem viên đạn đã bị phá hủy chưa.
     * @return true nếu đã bị phá hủy, ngược lại false
     */
    public boolean isDestroyed() {
        return this.isDestroyed;
    }

    /**
     * Cập nhật vận tốc (dx, dy) và vị trí (x, y) của đạn.
     * Đạn luôn di chuyển thẳng đứng.
     */
    @Override
    public void move() {
        // Cách thức di chuyển thẳng đứng của các viên đạn.
        setDx(speed * DIRECTION_X);
        setDy(speed * DIRECTION_Y);
        setX(getX() + getDx());
        setY(getY() + getDy());
    }

    /**
     * Cập nhật logic của đạn mỗi khung hình.
     * Di chuyển đạn và kiểm tra va chạm với tường trên.
     */
    @Override
    public void update() {
        // Cập nhật lại vị trí sau khi di chuyển.
        move();
        // Xử lý va chạm với tường trên
        if (this.getY() < 0) {
            this.destroy();
        }
    }

    /**
     * Vẽ viên đạn lên màn hình.
     * @param g Đối tượng Graphics để vẽ
     */
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (this.getImage() != null) {
            g2d.drawImage(this.getImage(), (int) this.getX(), (int) this.getY(),
                    (int) this.getWidth(), (int) this.getHeight(), null);
        }
    }

    /**
     * Phương thức checkCollision để kiểm tra va chạm AABB giữa Bullet và các vật thể khác.
     * @param other Vật thể (GameObject) cần kiểm tra
     * @return true nếu có va chạm, ngược lại false
     */
    public boolean checkCollision(GameObject other) {
        return (this.getX() < other.getX() + other.getWidth()) &&
               (this.getX() + this.getWidth() > other.getX()) &&
               (this.getY() < other.getY() + other.getHeight()) &&
               (this.getY() + this.getHeight() > other.getY());
    }

    /**
     * Phương thức destroy (đánh dấu viên đạn là đã bị phá hủy).
     * (Dùng khi viên đạn chạm vào gạch hoặc khung hình trên).
     */
    public void destroy() {
        this.isDestroyed = true;
    }
}