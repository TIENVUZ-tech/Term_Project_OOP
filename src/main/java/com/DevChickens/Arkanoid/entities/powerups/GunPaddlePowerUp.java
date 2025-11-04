package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.Ball;

/**
 * PowerUp này giúp cho paddle đổi hình dạng, trở thành hình dạng mới
 * kèm theo hai chiếc súng, khi đạn gặp gạch thì sẽ làm cho gạch bị vỡ.
 */
public class GunPaddlePowerUp extends PowerUp {

    /** Tham số chiều rộng của vật thể. */
    private static final int GUNPADDLEPOWERUP_WIDTH = 60;
    /** Tham số chiều cao của vật thể. */
    private static final int GUNPADDLEPOWERUP_HEIGHT = 40;
    /** Đường dẫn tới file ảnh. */
    private static final String FILEPATH = "/images/GunPaddlePowerUp.png";

    /**
     * Constructor.
     * @param x tọa độ x.
     * @param y tọa độ y.
     * @param type kiểu powerUp.
     * @param duration thời gian tồn tại.
     */
    public GunPaddlePowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration, FILEPATH,
                GUNPADDLEPOWERUP_WIDTH, GUNPADDLEPOWERUP_HEIGHT);
    }

    /**
     * Áp dụng hiệu ứng GunPaddle lên thanh đỡ.
     * @param manager GameManager, không dùng trong hiệu ứng này.
     * @param paddle Thanh đỡ để áp dụng hiệu ứng.
     * @param ball Bóng, không dùng trong hiệu ứng này.
     */
    @Override
    public void applyEffect(GameManager manager, Paddle paddle, Ball ball) {
        paddle.activateGunPaddle();
    }

    /**
     * Xóa hiệu ứng GunPaddle khỏi thanh đỡ.
     * @param paddle Thanh đỡ để xóa hiệu ứng.
     * @param ball Bóng, không dùng trong hiệu ứng này.
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.deactivateGunPaddle();
    }
}