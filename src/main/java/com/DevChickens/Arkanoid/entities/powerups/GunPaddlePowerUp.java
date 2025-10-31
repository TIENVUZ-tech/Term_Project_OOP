package com.DevChickens.Arkanoid.entities.powerups;

import java.awt.Graphics;
import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.Ball;

/*
 * PowerUp này giúp cho paddle đổi hình dạng, trở thành hình dạng mới
 * kèm theo hai chiếc súng, khi đạn gặp gạch thì sẽ làm cho gạch bị vỡ.
 */
public class GunPaddlePowerUp extends PowerUp {

    /* Tham số chiều cao và chiều rộng của vật thể. */
    private static final int GUNPADDLEPOWERUP_WIDTH = 60;
    private static final int GUNPADDLEPOWERUP_HEIGHT = 40;
    /* Đường dẫn tới file ảnh. */
    private static final String FILEPATH = "/images/GunPaddlePowerUp.png";

    /**
     * Constructor.
     * @param x (tọa độ x).
     * @param y (tọa độ y).
     * @param type (kiểu powerUp)
     * @param duration (thời gian tồn tại).
     */
    public GunPaddlePowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration, FILEPATH, 
        GUNPADDLEPOWERUP_WIDTH, GUNPADDLEPOWERUP_HEIGHT);
    }

    @Override
    public void applyEffect(GameManager manager, Paddle paddle, Ball ball) {
        paddle.activateGunPaddle();
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.deactivateGunPaddle();
    }
}
