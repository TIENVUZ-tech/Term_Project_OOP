package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.Ball;

/**
 * Power-up làm tăng tốc độ của quả bóng.
 */
public class FastBallPowerUp extends PowerUp {

    private final double speedFactor = 1.5; // Hệ số tăng tốc

    /**
     * Khởi tạo Power-up làm bóng nhanh hơn.
     * @param x Tọa độ X ban đầu.
     * @param y Tọa độ Y ban đầu.
     */
    public FastBallPowerUp(double x, double y) {
        super(x, y, "FAST_BALL", 500);
    }

    /**
     * Áp dụng hiệu ứng: Tăng tốc độ của bóng.
     * @param paddle ko sử dụng.
     * @param ball Quả bóng được tăng tốc.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.multiplySpeed(speedFactor);
    }

    /**
     * Gỡ bỏ hiệu ứng: Giảm tốc độ của bóng về lại như cũ.
     * @param paddle ko sử dụng.
     * @param ball Quả bóng sẽ được trả về tốc độ cũ.
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.multiplySpeed(1 / speedFactor);
    }
}