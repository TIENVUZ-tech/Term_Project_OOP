package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.core.GameManager;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.DevChickens.Arkanoid.entities.Ball;

/**
 * Power-up làm tăng tốc độ của quả bóng.
 */
public class FastBallPowerUp extends PowerUp {

    private final double speedFactor = 1.5; // Hệ số tăng tốc
    /* Biến lưu ảnh. */
    private BufferedImage image;
    /* Tham số chiều cao và chiều rộng của vật thể. */
    private static final int FASTBALLPOWERUP_WIDTH = 20;
    private static final int FASTBALLPOWERUP_HEIGHT = 30;
    /* Đường dẫn tới file ảnh. */
    private static final String FILEPATH = "/images/FastBallPowerUp.png";

    /**
     * Khởi tạo Power-up làm bóng nhanh hơn.
     * @param x Tọa độ X ban đầu.
     * @param y Tọa độ Y ban đầu.
     */
    public FastBallPowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration, FILEPATH, 
        FASTBALLPOWERUP_WIDTH, FASTBALLPOWERUP_HEIGHT);
        
    }

    /* Getter cho image. */
    public BufferedImage getImage() {
        return this.image;
    }

    /**
     * Áp dụng hiệu ứng: Tăng tốc độ của bóng.
     * @param paddle ko sử dụng.
     * @param ball Quả bóng được tăng tốc.
     */
    @Override
    public void applyEffect(GameManager manager, Paddle paddle, Ball ball) {
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

    @Override
    public void render(Graphics g) {}
}