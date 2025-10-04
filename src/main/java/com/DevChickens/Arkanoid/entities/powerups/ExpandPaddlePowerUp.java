package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.Ball;

/**
 * Power-up làm tăng kích thước của thanh trượt.
 */
public class ExpandPaddlePowerUp extends PowerUp {

    /**
     * biến lưu kích thước gốc của baddle
     */
    private double originalPaddleWidth;

    /**
     * Khởi tạo Power-up làm dài thanh trượt.
     * @param x Tọa độ X ban đầu.
     * @param y Tọa độ Y ban đầu.
     */
    public ExpandPaddlePowerUp(double x, double y) {
        super(x, y, "EXPAND_PADDLE", 500);
    }

    /**
     * Áp dụng hiệu ứng: Tăng chiều dài của Paddle.
     * @param paddle Thanh trượt sẽ được làm dài ra.
     * @param ball ko sử dụng.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        this.originalPaddleWidth = paddle.getWidth();
        paddle.setWidth(this.originalPaddleWidth * 1.5);
    }

    /**
     * Gỡ bỏ hiệu ứng: Đưa Paddle về lại chiều dài ban đầu.
     * @param paddle Thanh trượt sẽ được trả về kích thước cũ.
     * @param ball không sử dụng.
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(this.originalPaddleWidth);
    }

    /**
     * ký tự đại diện cho phiên bản Terminal.
     * @return "[ E ]" (Expand).
     */
    @Override
    public String getTerminalString() {
        return "[ E ]";
    }
}