package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.Paddle;
import java.awt.Graphics;

public class MultiBallPowerUp extends PowerUp {

    /* Tham số chiều cao và chiều rộng của vật thể. */
    private static final int MULTIBALLPOWERUP_WIDTH = 60;
    private static final int MULTIBALLPOWERUP_HEIGHT = 40;
    /* Đường dẫn tới file ảnh. */
    private static final String FILEPATH = "/images/MultiBallPowerUp.png";

    public MultiBallPowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration, FILEPATH, 
        MULTIBALLPOWERUP_WIDTH, MULTIBALLPOWERUP_HEIGHT);
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
        int originalDirY = originalBall.getDirectionY();

        // bóng 1 chéo bên trái 45 độ so với bóng gốc
        int rawX1 = originalDirX + originalDirY;
        int rawY1 = -originalDirX + originalDirY;

        // bóng 2 chéo bên phải 45 độ so với bóng gốc
        int rawX2 = originalDirX - originalDirY;
        int rawY2 = originalDirX + originalDirY;

        // Chuẩn hóa kết quả về (-1, 0, 1)
        // tránh các trường hợp ra 2 hoặc -2
        int newDirX1 = (int) Math.signum(rawX1);
        int newDirY1 = (int) Math.signum(rawY1);
        int newDirX2 = (int) Math.signum(rawX2);
        int newDirY2 = (int) Math.signum(rawY2);

        // Tạo 2 bóng mới
        Ball newBall1 = new Ball(startX, startY, 0, 0, baseSpeed, newDirX1, newDirY1);
        Ball newBall2 = new Ball(startX, startY, 0, 0, baseSpeed, newDirX2, newDirY2);

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