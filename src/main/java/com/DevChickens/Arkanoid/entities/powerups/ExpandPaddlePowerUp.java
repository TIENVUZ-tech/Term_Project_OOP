package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.core.GameManager;
import java.awt.Graphics;

import com.DevChickens.Arkanoid.entities.Ball;

/**
 * Power-up làm tăng kích thước của thanh trượt.
 */
public class ExpandPaddlePowerUp extends PowerUp {

    /* Các tham số cho chiều rộng và chiều cao của PowerUp. */
    private static final int EXPANDPADDLEPOWERUP_WIDTH = 40;
    private static final int EXPANDPADDLEPOWERUP_HEIGHT = 30;
    /* Đường dẫn tới ảnh. */
    private static final String FILEPATH = "/images/ExpandPaddlePowerUp.png";

    /**
     * Khởi tạo Power-up làm dài thanh trượt.
     * @param x Tọa độ X ban đầu.
     * @param y Tọa độ Y ban đầu.
     */
    public ExpandPaddlePowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration, FILEPATH, 
        EXPANDPADDLEPOWERUP_WIDTH, EXPANDPADDLEPOWERUP_HEIGHT);
    }

    /**
     * Áp dụng hiệu ứng: Lấy thông tin từ Paddle, tính toán và cập nhật lại Paddle.
     */
    @Override
    public void applyEffect(GameManager manager, Paddle paddle, Ball ball) {
        // Tăng bộ đếm hiệu ứng trong paddle
        int currentCount = paddle.getExpandEffectCount();
        paddle.setExpandEffectCount(currentCount + 1);

        // phuong thuc thực hiện toàn bộ logic tính toán
        this.updatePaddle(paddle);
    }

    /**
     * Gỡ bỏ hiệu ứng: Lấy thông tin từ Paddle, tính toán và cập nhật lại Paddle.
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Giảm bộ đếm hiệu ứng trong paddle
        int currentCount = paddle.getExpandEffectCount();
        paddle.setExpandEffectCount(currentCount - 1);

        // phuong thuc thực hiện toàn bộ logic tính toán
        this.updatePaddle(paddle);
    }

    /**
     * Phương thức riêng để tính toán và cập nhật paddle.
     */
    private void updatePaddle(Paddle paddle) {
        double oldWidth = paddle.getWidth();

        // Đọc dữ liệu từ paddle
        double baseWidth = paddle.getBaseWidth();
        int effectCount = paddle.getExpandEffectCount();

        // Tính toán kích thước mới
        double newWidth = baseWidth * (1 + (0.5 * effectCount));

        // Ghi dữ liệu mới trở lại paddle
        paddle.setWidth(newWidth);

        // Tính toán và cập nhật lại vị trí để căn giữa
        double widthChange = newWidth - oldWidth;
        paddle.setX(paddle.getX() - widthChange / 2.0);
    }

    @Override
    public void render(Graphics g) {}
}