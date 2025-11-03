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
 */
public class Paddle extends MovableObject {

    /** Tốc độ di chuyển của Paddle theo phương ngang (pixel/frame). */
    private double speed;
    /** Tham chiếu đến PowerUp mà hiện tại paddle đang áp dụng. */
    private PowerUp currentPowerUp;
    /** Biến lưu ảnh của Paddle. */
    private BufferedImage image;
    /** Biến lưu ảnh của paddle bình thường. */
    private BufferedImage normalPaddle;
    /** Biến lưu ảnh của LaserPaddle. */
    private BufferedImage gunPaddle;
    /** Biến để lưu kích thước gốc của paddle. */
    private final double baseWidth;
    /** Biến lưu tình trạng của paddle (true nếu là laserPaddle). */
    private boolean isGunPaddle = false;
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
            throw new RuntimeException("Không thể tải ảnh cho Paddle", e);
        }
        this.baseWidth = targetWidth; // lưu lại kích thước gốc.
        this.expandEffectCount = 0;
    }

    /**
     * Thiết lập tốc độ di chuyển của Paddle.
     * @param speed Tốc độ mới
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Lấy tốc độ di chuyển hiện tại của Paddle.
     * @return Tốc độ (pixel/frame)
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Kiểm tra xem Paddle có đang ở trạng thái GunPaddle không.
     * @return true nếu là GunPaddle, ngược lại false
     */
    public boolean isGunPaddle() {
        return this.isGunPaddle;
    }

    /**
     * Thiết lập PowerUp hiện tại đang có hiệu lực trên Paddle.
     * @param currentPowerUp PowerUp đang áp dụng
     */
    public void setCurrentPowerUp(PowerUp currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    /**
     * Lấy PowerUp hiện tại đang có hiệu lực trên Paddle.
     * @return PowerUp đang áp dụng (có thể là null)
     */
    public PowerUp getCurrentPowerUp() {
        return this.currentPowerUp;
    }

    /**
     * Lấy chiều rộng cơ sở (gốc) của Paddle.
     * @return Chiều rộng gốc
     */
    public double getBaseWidth() {
        return this.baseWidth;
    }

    /**
     * Lấy số lượng hiệu ứng "Expand" đang được xếp chồng.
     * @return Số lượng hiệu ứng "Expand"
     */
    public int getExpandEffectCount() {
        return this.expandEffectCount;
    }

    /**
     * Thiết lập số lượng hiệu ứng "Expand" đang có hiệu lực.
     * @param count Số lượng mới (phải >= 0)
     */
    public void setExpandEffectCount(int count) {
        if (count >= 0) {
            this.expandEffectCount = count;
        }
    }

    /**
     * Lấy ảnh (BufferedImage) hiện tại của Paddle để vẽ.
     * @return Ảnh (thường hoặc GunPaddle)
     */
    public BufferedImage getImage() {
        if (isGunPaddle) {
            return this.gunPaddle;
        } else {
            return this.normalPaddle;
        }
    }

    /**
     * Cập nhật logic của Paddle mỗi khung hình (gọi phương thức move).
     */
    @Override
    public void update() {
        // cập nhật lại vị trí.
        move();
    }

    /**
     * Di chuyển Paddle dựa trên vận tốc dx và kiểm tra va chạm tường.
     * Tự động reset vận tốc dx về 0 sau khi di chuyển.
     */
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

    /**
     * Vẽ Paddle lên màn hình.
     * @param g Đối tượng Graphics để vẽ
     */
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

    /**
     * Thiết lập vận tốc (dx) cho Paddle để di chuyển sang trái.
     */
    public void moveLeft() {
        // cập nhật tốc độ.
        super.setDx(-this.getSpeed());
    }

    /**
     * Phương thức moveRight() (di chuyển sang phải speed đơn vị).
     * Thiết lập vận tốc (dx) cho Paddle.
     */
    public void moveRight() {
        // cập nhật tốc độ.
        super.setDx(this.getSpeed());
    }

    /**
     * Phương thức applyPowerUp. Cập nhật trạng thái PowerUp hiện tại.
     * @param p PowerUp mới
     */
    public void applyPowerUp(PowerUp p) {
        this.currentPowerUp = p;
    }

    /**
     * Phương thức activateGunPaddle.
     * Kích hoạt trạng thái GunPaddle và thay đổi ảnh.
     */
    public void activateGunPaddle() {
        this.isGunPaddle = true;
        this.image = gunPaddle;
    }

    /**
     * Phương thức deactivateGunPaddle.
     * Hủy kích hoạt trạng thái GunPaddle và trả về ảnh bình thường.
     */
    public void deactivateGunPaddle() {
        this.isGunPaddle = false;
        this.image = normalPaddle;
    }
}