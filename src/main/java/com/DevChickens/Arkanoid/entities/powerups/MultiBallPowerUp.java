package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.Paddle;

import java.awt.Graphics;

public class MultiBallPowerUp extends PowerUp {

    public MultiBallPowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration);
    }

    /**
     * Áp dụng hiệu ứng: Tạo 2 bóng mới VÀ thay đổi bóng gốc.
     * @param manager GameManager chính (để gọi addBall).
     * @param originalBall Quả bóng gốc ăn item.
     */
    @Override
    public void applyEffect(GameManager manager, Paddle paddle, Ball originalBall) {

        // 1. Lấy thuộc tính của bóng gốc
        double startX = originalBall.getX();
        double startY = originalBall.getY();
        double baseSpeed = originalBall.getSpeed();
        boolean isSuper = originalBall.getIsSuperBall();
        int originalDirX = originalBall.getDirectionX(); // Lấy hướng X cũ

        // Tạo 2 bóng mới
        // Bóng mới 1: Bay hướng lên trái
        Ball newBall1 = new Ball(startX, startY, 0, 0, baseSpeed, -1, -1);

        // Bóng mới 2: Bay hướng Llên phải
        Ball newBall2 = new Ball(startX, startY, 0, 0, baseSpeed, 1, -1);

        // XỬ LÝ BÓNG GỐC
        // Đổi hướng X của bóng gốc (nếu đang sang phải thì đổi sang trái)
        originalBall.setDirectionX(originalDirX * -1);
        // BẮT BUỘC nó bay LÊN
        originalBall.setDirectionY(-1);
        // ---------------------------------------------------

        if (isSuper) {
            newBall1.activateSuperBall(0);
            newBall2.activateSuperBall(0);
        }

        if (manager != null) {
            manager.addBall(newBall1);
            manager.addBall(newBall2);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {

    }

    @Override
    public void render(Graphics g) {

    }
}