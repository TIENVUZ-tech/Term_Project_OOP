package com.DevChickens.Arkanoid.entities;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.powerups.PowerUp;
import com.DevChickens.Arkanoid.graphics.AssetLoader;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Paddle (Kế thừa từ MovableObject): Thanh đỡ mà người chơi điều khiển.
 * Thuộc tính: speed, currentPowerUp.
 * Phương thức: moveLeft(), moveRight(), applyPowerUp().
 * @author Vũ Văn Tiến.
 */
public class Paddle extends MovableObject {

    // Tốc độ di chuyển của Paddle theo phương ngang.
    // với mỗi lần cập nhật thì vị trí của paddle thay đổi speed (pixel).
    private double speed;
    private PowerUp currentPowerUp;      // Tham chiếu đến PowerUp mà hiện tại paddle đang áp dụng.
    private BufferedImage image;         // Biến lưu ảnh của Paddle.
    private BufferedImage normalPaddle;  // Biến lưu ảnh của paddle bình thường.
    private BufferedImage gunPaddle;     // Biến lưu ảnh của LaserPadlee.
    private final double baseWidth;      // Biến để lưu kích thước gốc của paddle.
    private boolean isGunPaddle = false; // Biến lưu tình trạng của paddle (laserPaddle hoặc thường).
    private int expandEffectCount;       // Biến để đếm số lượng hiệu ứng "Expand" đang có hiệu lực.

    /**
     * Phương thức khởi tạo Paddle.
     * @param x (tọa độ x)
     * @param y (toạ độ y)
     * @param dx (tốc độ di chuyển theo trục x)
     * @param dy (tốc độ di chuyển theo chiều y)
     * @param speed (tốc độ di chuyển)
     * @param currentPowerUp (PowerUp hiện tại mà paddle đang dùng)
     */
    public Paddle(double x, double y,
            double dx, double dy, double speed, PowerUp currentPowerUp) {
        super(x, y, 0, 0, dx, dy);
        this.speed = speed;
        this.currentPowerUp = currentPowerUp;
        double targetWidth = 0;
        try {
            this.normalPaddle = AssetLoader.loadImage("/images/NormalPaddle.png");
            this.gunPaddle = AssetLoader.loadImage("/images/GunPaddle.png");

            // Lỗi khi không tải được ảnh NormalPaddle.
            if (this.normalPaddle == null) {
                throw new IOException("Không thể tải tệp ảnh mặc định của Paddle: /images/normalPaddle.png");
            }
            this.image = this.normalPaddle;

            if (this.image != null) {
                /* Định nghĩa chiều rộng mong muốn tương đối so với chiều rộng màn hình
                 * (1/6 chiều rộng màn hình) */
                final double DESIRED_SCREEN_WIDTH_RATIO = 6.0;
                targetWidth = GameManager.GAME_WIDTH / DESIRED_SCREEN_WIDTH_RATIO; // Chiều rộng paddle mong muốn

                // Tính toán chiều cao để giữ nguyên tỷ lệ ảnh gốc
                double aspectRatio = (double) this.image.getHeight() / this.image.getWidth();
                double targetHeight = targetWidth * aspectRatio;

                // Đặt kích thước cho Paddle
                this.setWidth(targetWidth);
                this.setHeight(targetHeight);

                // Đặt vị trí ban đầu của Paddle
                // X: giữa màn hình theo chiều ngang
                this.setX((GameManager.GAME_WIDTH / 2) - (this.getWidth() / 2));
                // Y: cách đáy màn hình một khoảng nhất định.
                final double MARGIN_FROM_BOTTOM = 10;
                this.setY(GameManager.GAME_HEIGHT - this.getHeight() - MARGIN_FROM_BOTTOM);
            }
        } catch (Exception e) {
            // In ra lỗi gốc.
            e.printStackTrace();
            // Ném ra lỗi để dừng game.
            throw new RuntimeException("Không thể tải ảnh cho Padlle", e);
        }
        this.baseWidth = targetWidth; // lưu lại kích thước gốc.
        this.expandEffectCount = 0; 
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public boolean isGunPaddle() {
        return this.isGunPaddle;
    }

    public void setCurrentPowerUp(PowerUp currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    public PowerUp getCurrentPowerUp() {
        return this.currentPowerUp;
    }

    public double getBaseWidth() {
        return this.baseWidth;
    }

    public int getExpandEffectCount() {
        return this.expandEffectCount;
    }

    public void setExpandEffectCount(int count) {
        if (count >= 0) {
            this.expandEffectCount = count;
        }
    }

    public BufferedImage getImage() {
        if (isGunPaddle) {
            return this.gunPaddle;
        } else {
            return this.normalPaddle;
        }
    }

    @Override
    public void update() {
        // cập nhật lại vị trí.
        move();
    }

    @Override
    public void move() {
        double newX = super.getX() + super.getDx();

        // xử lý va chạm tường trái
        if (newX < 0) {
            super.setX(0);
        } else if (newX + this.getWidth() > GameManager.GAME_WIDTH) {
            // xử lý va chạm tường phải.
            super.setX(GameManager.GAME_WIDTH - super.getWidth());
        } else {
            super.setX(newX);
        }
        // đặt tốc độ về 0 sau khi di chuyển.
        super.setDx(0);
    }
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (this.getImage() != null) {
            g2d.drawImage(this.getImage(),
                    (int) this.getX(),
                    (int) this.getY(),
                    (int) this.getWidth(),
                    (int) this.getHeight(), null);
        }
    }

    public void moveLeft() {
        // cập nhật tốc độ.
        super.setDx(-this.getSpeed());
    }

    /**
     * Phương thức moveRight() (di chuyển sang phải speed đơn vị).
     */
    public void moveRight() {
        // cập nhật tốc độ.
        super.setDx(this.getSpeed());
    }

    /**
     * Phương thức applyPowerUp. Cập nhật trạng thái PowerUp hiện tại.
     * @param p
     */
    public void applyPowerUp(PowerUp p) {
        this.currentPowerUp = p;
    }

    /**
     * Phương thức activateGunPaddle.
     */
    public void activateGunPaddle() {
        this.isGunPaddle = true;
        this.image = gunPaddle;
    }

    /**
     * Phương thức deactivateLaserPaddle.
     */
    public void deactivateGunPaddle() {
        this.isGunPaddle = false;
        this.image = normalPaddle;
    }
}