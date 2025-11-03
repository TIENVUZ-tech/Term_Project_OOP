package com.DevChickens.Arkanoid.entities;

import java.awt.Graphics;

/**
 * MovableObject (Abstract Class, kế thừa từ GameObject):
 * Lớp cơ sở cho các đối tượng có thể di chuyển.
 * Thuộc tính: dx, dy (tốc độ di chuyển theo trục x, y).
 */
public abstract class MovableObject extends GameObject {

    /** Tốc độ di chuyển theo trục x (trục hoành). */
    private double dx;
    /** Tốc độ di chuyển theo chiều y (trục tung). */
    private double dy;

    /**
     * Phương thức khởi tạo MovableOject.
     * @param x (tọa độ x)
     * @param y (toạ độ y)
     * @param width (độ rộng)
     * @param height (độ cao)
     * @param dx (tốc độ di chuyển theo trục x)
     * @param dy (tốc độ di chuyển theo chiều y)
     */
    public MovableObject(double x, double y, double width, double height,
            double dx, double dy) {
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Thiết lập vận tốc di chuyển theo trục X.
     * @param dx Vận tốc X mới
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Lấy vận tốc di chuyển hiện tại theo trục X.
     * @return Vận tốc X
     */
    public double getDx() {
        return this.dx;
    }

    /**
     * Thiết lập vận tốc di chuyển theo trục Y.
     * @param dy Vận tốc Y mới
     */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * Lấy vận tốc di chuyển hiện tại theo trục Y.
     * @return Vận tốc Y
     */
    public double getDy() {
        return this.dy;
    }

    /**
     * Phương thức move(). Di chuyển đối tượng dựa trên tốc độ di chuyển dx, dy.
     * Phương thức move được để là abstract.
     * Nghĩa là mọi lớp con kế thừa từ MovableObject bắt buộc phải cung cấp cách di chuyển riêng.
     */
    public abstract void move();

    /**
     * Phương thức render dùng để vẽ hình ảnh của vật thể.
     * @param g Đối tượng Graphics dùng để vẽ
     */
    public abstract void render(Graphics g);
}