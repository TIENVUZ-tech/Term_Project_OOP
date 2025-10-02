package com.DevChickens.Arkanoid.entities;
/**
 * Paddle (Kế thừa từ MovableObject): Thanh đỡ mà người chơi điều khiển.
 * Thuộc tính: speed, currentPowerUp.
 * Phương thức: moveLeft(), moveRight(), applyPowerUp(). 
 */

import com.DevChickens.Arkanoid.entities.powerups.PowerUp;

public class Paddle extends MovableObject {
    /* Tốc độ di chuyển của Paddle theo phương ngang.
     * với mỗi lần cập nhật thì vị trí của paddle thay đổi speed (pixel).
     */
    private double speed;
    /* Tham chiếu đến PowerUp mà hiện tại paddle đang áp dụng. */
    private PowerUp currentPowerUp;
    /*Biến lưu ảnh của Paddle. */

    /**
     * Phương thức khởi tạo Paddle.
     * @param x (tọa độ x)
     * @param y (toạ độ y)
     * @param width (độ rộng)
     * @param height (độ cao)
     * @param dx (tốc độ di chuyển theo trục x)
     * @param dy (tốc độ di chuyển theo chiều y)
     * @param speed (tốc độ di chuyển)
     * @param currentPowerUp (PowerUp hiện tại mà paddle đang dùng)
     */
    public Paddle(double x, double y, double width, double height,
     double dx, double dy, double speed, PowerUp currentPowerUp) {
        super(x, y, width, height, dx, dy);
        this.speed = speed;
        this.currentPowerUp = currentPowerUp;
    }
    /** Phương thức getter và setter của speed (tốc độ).
     * @param speed
    */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    /** Phương thức getter và setter của currentPowerUp (trạng thái PowerUp hiện tại của Paddle).
     * @param currentPowerUp
    */
    public void setCurrentPowerUp(PowerUp currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    public PowerUp getCurrentPowerUp() {
        return this.currentPowerUp;
    }

    @Override
    public void update() {}

    @Override
    public void render() {}

    @Override
    public void move() {}
    
    /**
     * Phương thức moveLeft() (di chuyển sang trái speed đơn vị).
     * @param none
     */
    public void moveLeft() {
        setX(getX() - speed); // cập nhật lại toạ độ trục x của paddle.
    }

    /**
     * Phương thức moveRight() (di chuyển sang phải speed đơn vị).
     * @param none
     */
    public void moveRight() {
        setX(getX() + speed); // cập nhật lại toạn độ trục x của paddle.
    }
    /**
     * Phương thức applyPowerUp. Cập nhật trạng thái PowerUp hiện tại.
     * @param p
     */
    public void applyPowerUp(PowerUp p) {
        this.currentPowerUp = p;
    }
}
