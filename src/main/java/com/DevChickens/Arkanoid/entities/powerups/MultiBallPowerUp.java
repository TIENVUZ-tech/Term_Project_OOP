package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.Paddle;

public class MultiBallPowerUp extends PowerUp {


    private static final int MULTIBALLPOWERUP_WIDTH = 60;
    private static final int MULTIBALLPOWERUP_HEIGHT = 40;

    private static final String FILEPATH = "/images/MultiBallPowerUp.png";

    public MultiBallPowerUp(double x, double y, String type, long duration) {
        super(x, y, type, duration, FILEPATH,
                MULTIBALLPOWERUP_WIDTH, MULTIBALLPOWERUP_HEIGHT);
    }

    /**
     * Áp dụng hiệu ứng: Tạo 2 bóng mới tách ra 2 bên so với bóng gốc.
     * @param manager GameManager chính (để gọi addBall).
     * @param originalBall Quả bóng gốc ăn item.
     */
    @Override
    public void applyEffect(GameManager manager, Paddle paddle, Ball originalBall) {

        // Lấy thuộc tính của bóng gốc
        double startX = originalBall.getX();
        double startY = originalBall.getY();
        double baseSpeed = originalBall.getSpeed();
        boolean isSuper = originalBall.isSuperBall();

        // Lấy vector hướng (double) của bóng gốc
        double originalDirX = originalBall.getDirectionX();
        double originalDirY = originalBall.getDirectionY();

        // Chuyển đổi vector hướng (dirX, dirY) về lại GÓC (radian)
        // Dùng Math.atan2(y, x) để lấy góc hiện tại
        double originalAngle = Math.atan2(originalDirY, originalDirX);

        // Tính toán 2 góc mới (tách ra 2 bên, ví dụ: 30 độ mỗi bên)
        double angleOffset = Math.toRadians(30); // 30 độ

        double angle1 = originalAngle - angleOffset; // Góc mới 1 (lệch trái)
        double angle2 = originalAngle + angleOffset; // Góc mới 2 (lệch phải)

        // Chuyển đổi 2 góc mới trở lại thành 2 vector hướng (dirX, dirY)
        // Dùng cos cho X và sin cho Y
        double newDirX1 = Math.cos(angle1);
        double newDirY1 = Math.sin(angle1);

        double newDirX2 = Math.cos(angle2);
        double newDirY2 = Math.sin(angle2);

        // Tạo 2 bóng mới với vector hướng (double)
        Ball newBall1 = new Ball(startX, startY, 0, 0, baseSpeed, newDirX1, newDirY1);
        Ball newBall2 = new Ball(startX, startY, 0, 0, baseSpeed, newDirX2, newDirY2);

        if (isSuper) {
            newBall1.activateSuperBall();
            newBall2.activateSuperBall();
        }

        if (manager != null) {
            manager.addBall(newBall1);
            manager.addBall(newBall2);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Không cần làm gì khi hết hạn
    }
}