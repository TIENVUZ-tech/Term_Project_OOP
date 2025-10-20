package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.Paddle;

import java.awt.Graphics;

/**
 * Power-up này sẽ nhân đôi quả bóng.
 * Nó gọi trực tiếp GameManager để thêm bóng mới vào game.
 */
public class MultiBallPowerUp extends PowerUp {

    public MultiBallPowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration);
    }

    /**
     * Áp dụng hiệu ứng: Tạo 2 quả bóng mới và thêm chúng vào GameManager.
     * @param manager GameManager chính.
     * @param originalBall Quả bóng gốc để lấy thông tin.
     */
    @Override
    public void applyEffect(GameManager manager, Paddle paddle, Ball originalBall) {

        // Lấy thuộc tính của bóng gốc
        double startX = originalBall.getX();
        double startY = originalBall.getY();
        double baseSpeed = originalBall.getSpeed();

        // Tạo bóng 1: Bay lên-trái
        Ball ball1 = new Ball(startX, startY, 0, 0, baseSpeed, -1, -1);

        // Tạo bóng 2: Bay lên-phải
        Ball ball2 = new Ball(startX, startY, 0, 0, baseSpeed, 1, -1);

        // Đảm bảo bóng gốc cũng bay lên
        originalBall.setDirectionY(-1);

        // Sao chép trạng thái (ví dụ: nếu bóng gốc là SuperBall)
        if (originalBall.getIsSuperBall()) {
            ball1.activateSuperBall(0);
            ball2.activateSuperBall(0);
        }

        // Dùng GameManager để thêm bóng mới vào danh sách
        manager.addBall(ball1);
        manager.addBall(ball2);
    }

    /**
     * Gỡ bỏ hiệu ứng: Không làm gì cả.
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {

    }

    @Override
    public void render(Graphics g) {

    }
}