package com.DevChickens.Arkanoid.entities;
import java.awt.Graphics;
/**
 * GameObject là lớp cơ sở (base class) trừu tượng cho mọi vật thể xuất hiện trên màn hình game:
 * Paddle, Ball, Brick, PowerUp… đều kế thừa từ GameObject.
 * Lớp này gom những thuộc tính vị trí – kích thước – hành vi chung mà mọi đối tượng đều cần.
 */
public abstract class  GameObject {
    /*Tọa độ trục hoành (trục X). Dùng để quy định vị trí của vật theo phương ngang.*/
    private double x;
    /*Tọa độ trục tung (trục Y). Dùng để quy định vị trí của vật theo phương thẳng đứng.*/
    private double y;
    /*Chiều rộng của vật thể(pixel). Đùng để vẽ và tính va chạm.*/
    private double width; 
    /*Chiều cao của vật thể, cũng dùng cho hiển thị và tính ca chạm.*/
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

    /** Phương thức getter và setter cho thuộc tính x (hoành độ). */
    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    /** Phương thức getter và setter cho thuộc tính y (tung độ). */
    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }

    /** Phương thức getter và setter cho thuộc tính width (chiều rộng). */
    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return this.width;
    }

    /** Phương thức getter và setter cho thuộc tính height (chiều cao). */
    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return this.height;
    }

    /**
     * Cập nhật trạng thái logic của vật thể mỗi khung hình (frame) của game.
     *  Ví dụ: di chuyển, thay đổi kích thước...
     * Được định nghĩa chi tiết ở lớp con.
     */
    public abstract void update();

    /**
     * Được gọi sau update() để hiển thị hình ảnh mới nhất.
     * Dùng đối tượng đồ hoạ (Graphics) để vẽ hình chữ nhật, ảnh sprite… tại vị trí đã cập nhật.
     */
    public abstract void render(Graphics g);
}
