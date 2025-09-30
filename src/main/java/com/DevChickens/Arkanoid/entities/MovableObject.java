package com.DevChickens.Arkanoid.entities;
/**
 * MovableObject (Abstract Class, kế thừa từ GameObject):
 *  Lớp cơ sở cho các đối tượng có thể di chuyển. - - - 
 * Thuộc tính: dx, dy (tốc độ di chuyển theo trục x, y). 
 */
public abstract class MovableObject extends GameObject {
    /*Tốc độ di chuyển theo trục x (trục hoành) */
    private double dx;
    /*Tốc độ di chuyển theo chiều y (trục tung) */
    private double dy;

    /**
     * Phương thức khởi tạo MovableOject
     * @param dx (tốc độ di chuyển theo trục x)
     * @param dy (tốc độ di chuyển theo chiều y)
     */

}
