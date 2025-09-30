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
    /* Tham chiếu đến PowerUp mà hiện tại paddle đang áp dụng */
    private PowerUp currentPowerUp;

    /**
     * Phương thức khởi tạo Paddle
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
     double dx, double dy, double speed, PowerUp currPowerUp) {
        super(x, y, width, height, dx, dy);
        this.speed = speed;
        this.currentPowerUp = currPowerUp;
     }

    /** Phương thức getter và setter của speed (tốc độ)*/
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    /** Phương thức getter và setter của currentPowerUp (trạng thái PowerUp hiện tại của Paddle)*/
    public void setCurrentPower(PowerUp currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    public PowerUp getCurrentPower() {
        return this.currentPowerUp;
    }

    
}
