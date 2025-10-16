package com.DevChickens.Arkanoid.entities;
/**
 * Paddle (Kế thừa từ MovableObject): Thanh đỡ mà người chơi điều khiển.
 * Thuộc tính: speed, currentPowerUp.
 * Phương thức: moveLeft(), moveRight(), applyPowerUp(). 
 */

import java.awt.Graphics;
import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.powerups.PowerUp;
import com.DevChickens.Arkanoid.graphics.AssetLoader; // Thêm import
import java.awt.image.BufferedImage;

public class Paddle extends MovableObject {
    /** Tốc độ di chuyển của Paddle theo phương ngang.
     * với mỗi lần cập nhật thì vị trí của paddle thay đổi speed (pixel).
     */
    private double speed;
    /** Tham chiếu đến PowerUp mà hiện tại paddle đang áp dụng. */
    private PowerUp currentPowerUp;
    /** Biến lưu ảnh của Paddle. */
    private BufferedImage image;
    /** Biến để lưu kích thước GỐC của paddle. */
    private final double baseWidth;

    /** Biến để đếm số lượng hiệu ứng "Expand" đang có hiệu lực. */
    private int expandEffectCount;

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
        this.image = AssetLoader.loadImage("/images/Paddle.png");
        double targetWidth = 0;
        if (this.image != null) {
            // Định nghĩa chiều rộng mong muốn tương đối so với chiều rộng màn hình
            // Ví dụ: 1/6 chiều rộng màn hình
            final double DESIRED_SCREEN_WIDTH_RATIO = 6.0;
            targetWidth = GameManager.GAME_WIDTH / DESIRED_SCREEN_WIDTH_RATIO; // Chiều rộng paddle mong muốn

            // Tính toán chiều cao để giữ nguyên tỷ lệ ảnh gốc
            double aspectRatio = (double)this.image.getHeight() / this.image.getWidth();
            double targetHeight = targetWidth * aspectRatio;

            // Đặt kích thước cho Paddle
            this.setWidth(targetWidth);
            this.setHeight(targetHeight);

            // Đặt vị trí ban đầu của Paddle
            // X: giữa màn hình theo chiều ngang
            this.setX((GameManager.GAME_WIDTH / 2) - (this.getWidth() / 2));
            // Y: cách đáy màn hình một khoảng nhất định (ví dụ 50px)
            final double MARGIN_FROM_BOTTOM = 10;
            this.setY(GameManager.GAME_HEIGHT - this.getHeight() - MARGIN_FROM_BOTTOM);

        }
        this.baseWidth = targetWidth;         // MỚI: Lưu lại kích thước gốc
        this.expandEffectCount = 0;
    }

    /** Phương thức getter và setter của speed (tốc độ).
     * @param speed
    */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    /** Phương thức getter và setter của currentPowerUp (trạng thái PowerUp hiện tại của Paddle).
     * @param currentPowerUp
    */
    public void setCurrentPowerUp(PowerUp currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    public PowerUp getCurrentPowerUp() {
        return this.currentPowerUp;
    }

    /** Lấy kích thước gốc. */
    public double getBaseWidth() {
        return this.baseWidth;
    }

    /** Lấy số lượng hiệu ứng "Expand" đang có. */
    public int getExpandEffectCount() {
        return this.expandEffectCount;
    }

    /** Thiết lập số lượng hiệu ứng "Expand". */
    public void setExpandEffectCount(int count) {
        if (count >= 0) {
            this.expandEffectCount = count;
        }
    }

    @Override
    public void update() {}

    @Override
    public void move() {}

    @Override
    public void render(Graphics g) {

    }
    /**
     * Phương thức moveLeft() (di chuyển sang trái speed đơn vị).
     */
    public void moveLeft() {
        if (this.getX() > 0) {
            setX(getX() - speed); // cập nhật lại toạ độ trục x của paddle.
        }
    }

    /**
     * Phương thức moveRight() (di chuyển sang phải speed đơn vị).
     */
    public void moveRight() {
        if(this.getX() + this.getWidth() < 920) {
            setX(getX() + speed); // cập nhật lại toạn độ trục x của paddle.
        }
    }

    /**
     * Phương thức applyPowerUp. Cập nhật trạng thái PowerUp hiện tại.
     * @param p
     */
    public void applyPowerUp(PowerUp p) {
        this.currentPowerUp = p;
    }

    public BufferedImage getImage() {
        return this.image;
    }
}
