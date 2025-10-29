package com.DevChickens.Arkanoid.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.graphics.AssetLoader;;

public class Bullet extends MovableObject {
    /*Tốc độ di chuyển của đạn. */
    private double speed;
    /*Hướng di chuyển của đạn theo phương ngang.
     */
    private static final int directionX = 0;
    /*Hướng di chuyển của đạn theo phương dọc.*/
    private  static final int directionY = -1;
    /*Biến chứa ảnh của viên đạn.*/
    private BufferedImage image;

    /**
     * Phương thức khởi tạo Bullet, giữ nguyên 9 tham số đầu vào.
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
        
        // Đọc ảnh từ thư mục images
        this.image = AssetLoader.loadImage("/images/Bullet.png");

        // Thiết lập các thông số kích thước cho viên đạn.
        if (this.image != null) {
            final double PADDLE_WIDTH = 6.0;
            final double BULLET_WIDTH = 0.05; // 5% của paddle.

            double targetWidth = GameManager.GAME_WIDTH / PADDLE_WIDTH * BULLET_WIDTH;

            // tính toán chiều cao để giữ nguyên tỷ lệ ảnh gốc.
            double aspectRatio = (double) this.image.getHeight() / this.image.getWidth();
            double targetHeight = targetWidth * aspectRatio;

            // đặt kích thước cho viên đạn
            this.setWidth(targetWidth);
            this.setHeight(targetHeight);
        }
    }

    /*getter và setter cho speed. */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    /*getter cho ảnh. */
    public BufferedImage getImage() {
        return this.image;
    }

    @Override
    public void move() {
        // Cách thức di chuyển thẳng đứng của các viên đạn.
        setDx(speed * directionX);
        setDy(speed * directionY);
        setX(getX() + getDx());
        setY(getY() + getDy());
    }

    @Override
    public void render(Graphics g) {}

    @Override
    public void update() {}

    public boolean checkCollision(GameObject other) {
        boolean conditionOne   = this.getX() < other.getX() + other.getWidth();
        boolean conditionTwo   = this.getX() + this.getWidth() > other.getX();
        boolean conditionThree = this.getY() < other.getY() + other.getHeight();
        boolean conditionFour  = this.getY() + this.getHeight() > other.getY();
        return conditionOne && conditionTwo && conditionThree && conditionFour;
    }
}
