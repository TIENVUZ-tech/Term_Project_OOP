package com.DevChickens.Arkanoid.entities;

import java.awt.Graphics;

/**
 * GameObject là lớp cơ sở (base class) trừu tượng cho mọi vật thể xuất hiện trên màn hình game:
 * Paddle, Ball, Brick, PowerUp… đều kế thừa từ GameObject.
 * Lớp này gom những thuộc tính vị trí – kích thước – hành vi chung mà mọi đối tượng đều cần.
 */
public abstract class GameObject {

    /** Tọa độ trục hoành (trục X). Dùng để quy định vị trí của vật theo phương ngang. */
    private double x;
    /** Tọa độ trục tung (trục Y). Dùng để quy định vị trí của vật theo phương thẳng đứng. */
    private double y;
    /** Chiều rộng của vật thể (pixel). Dùng để vẽ và tính va chạm. */
    private double width;
    /** Chiều cao của vật thể, cũng dùng cho hiển thị và tính va chạm. */
    private double height;

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

    /**
     * Thiết lập vị trí X (hoành độ) của vật thể.
     * @param x Vị trí X mới.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Lấy vị trí X (hoành độ) hiện tại của vật thể.
     * @return Vị trí X
     */
    public double getX() {
        return this.x;
    }

    /**
     * Thiết lập vị trí Y (tung độ) của vật thể.
     * @param y Vị trí Y mới
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Lấy vị trí Y (tung độ) hiện tại của vật thể.
     * @return Vị trí Y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Thiết lập chiều rộng của vật thể.
     * @param width Chiều rộng mới
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Lấy chiều rộng hiện tại của vật thể.
     * @return Chiều rộng
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Thiết lập chiều cao của vật thể.
     * @param height Chiều cao mới
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Lấy chiều cao hiện tại của vật thể.
     * @return Chiều cao
     */
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
     * @param g Đối tượng Graphics dùng để vẽ
     */
    public abstract void render(Graphics g);
}