package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.Paddle;

/**
 * Loại PowerUp này sẽ làm cho bóng mạnh hơn.
 * Bất kỳ loạt gạch nào chạm vào bóng sẽ bị vỡ.
 */
public class SuperBallPowerUp extends PowerUp {

    // Tham số chiều cao và chiều rộng của vật thể.
    private static final int SUPERBALLPOWERUP_WIDTH = 50;
    private static final int SUPERBALLPOWERUP_HEIGHT = 60;
    // Đường dẫn tới file ảnh.
    private static final String FILEPATH = "/images/SuperBallPowerUp.png";

    /**
     * Constructor.
     * @param x tọa độ x.
     * @param y tọa độ y.
     * @param type kiểu powerUp.
     * @param duration thời gian tồn tại.
     */
    public SuperBallPowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration, FILEPATH,
                SUPERBALLPOWERUP_WIDTH, SUPERBALLPOWERUP_HEIGHT);
    }

    @Override
    public void applyEffect(GameManager manager, Paddle paddle, Ball ball) {
        ball.activateSuperBall();
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.deactivateSuperBall();
    }
}