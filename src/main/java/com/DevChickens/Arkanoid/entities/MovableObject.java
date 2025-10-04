package com.DevChickens.Arkanoid.entities;
/**
 * MovableObject (Abstract Class, kế thừa từ GameObject):
 *  Lớp cơ sở cho các đối tượng có thể di chuyển. - - - 
 * Thuộc tính: dx, dy (tốc độ di chuyển theo trục x, y). 
 */
public abstract class MovableObject extends GameObject {
    /*Tốc độ di chuyển theo trục x (trục hoành). */
    private double dx;
    /*Tốc độ di chuyển theo chiều y (trục tung). */
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
    
    /** Phương thức getter và setter cho dx (tốc độ theo phương ngang). */
    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDx() {
        return this.dx;
    }

    /** Phương thức getter và setter cho dy (tốc độ theo phương dọc). */
    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDy() {
        return this.dy;
    }

    /**
     * Phương thức move(). Di chuyển đối tượng dựa trên tốc độ di chuyển dx, dy.
     * Phương thức move được để là abstract.
     * Nghĩa là mọi lớp con kế thừa từ MovableObject bắt buộc phải cung cấp cách di chuyển riêng.
     */
    public abstract void move();
}
