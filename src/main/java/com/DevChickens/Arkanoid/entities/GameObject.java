package com.DevChickens.Arkanoid.entities;

import java.awt.Graphics;

/**
 * GameObject là lớp cơ sở (base class) trừu tượng cho mọi vật thể xuất hiện trên màn hình game:
 * Paddle, Ball, Brick, PowerUp… đều kế thừa từ GameObject.
 * Lớp này gom những thuộc tính vị trí – kích thước – hành vi chung mà mọi đối tượng đều cần.
 * @author Vũ Văn Tiến.
 */
public abstract class GameObject {

    private double x;      // Tọa độ trục hoành (trục X). Dùng để quy định vị trí của vật theo phương ngang.
    private double y;      // Tọa độ trục tung (trục Y). Dùng để quy định vị trí của vật theo phương thẳng đứng.
    private double width;  // Chiều rộng của vật thể (pixel). Dùng để vẽ và tính va chạm.
    private double height; // Chiều cao của vật thể, cũng dùng cho hiển thị và tính va chạm.

    /**
     * Phương thức khởi tạo GameObject.
     * @param x (tọa độ x)
     * @param y (toạ độ y)
     * @param width (độ rộng)
     * @param height (độ cao)
     */
    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return this.width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return this.height;
    }

    /**
     * Cập nhật trạng thái logic của vật thể mỗi khung hình (frame) của game.
     * Ví dụ: di chuyển, thay đổi kích thước...
     * Được định nghĩa chi tiết ở lớp con.
     */
    public abstract void update();

    /**
     * Phương thức render dùng để vẽ hình ảnh của vật thể.
     * @param g (biến để mẽ).
     */
    public abstract void render(Graphics g);
}