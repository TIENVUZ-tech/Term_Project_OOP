package com.DevChickens.Arkanoid.entities.powerups;

import java.awt.Graphics;

import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.Paddle;

/**
 * Loại PowerUp này sẽ làm cho bóng mạnh hơn.
 * Bất kỳ loạt gạch nào chạm vào bóng sẽ bị vỡ
 */
public class SuperBallPowerUp extends PowerUp {
    /**
     * Constructor.
     * @param x (tọa độ x).
     * @param y (tọa độ y).
     * @param type (kiểu powerUp)
     * @param duration (thời gian tồn tại).
     */
    public SuperBallPowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.activateSuperBall(this.getDuration());
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.deactivateSuperBall();
    }

    @Override
    public void render(Graphics g) {}
}
