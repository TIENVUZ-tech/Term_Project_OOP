package com.DevChickens.Arkanoid.entities;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import com.DevChickens.Arkanoid.core.CollisionManager;
import com.DevChickens.Arkanoid.core.GameManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Lớp Ball đã được nâng cấp để sử dụng 'double' cho directionX và directionY
 * Điều này cho phép bóng di chuyển theo bất kỳ góc nào, không chỉ 45 độ.
 */
public class Ball extends MovableObject {

    private double speed;           // Tốc độ của bóng (pixel/frame)
    private double directionX;
    private double directionY;
    private boolean isSuperBall = false;
    private boolean isDestroyed = false;

    private BufferedImage normalBall;
    private BufferedImage superBall;
    private BufferedImage image;

    /**
     * Phương thức khởi tạo Ball.
     * @param directionX (hướng di chuyển X ban đầu, kiểu double)
     * @param directionY (hướng di chuyển Y ban đầu, kiểu double)
     */
    public Ball(double x, double y, double dx, double dy,
                double speed, double directionX, double directionY) {

        // Gọi hàm khởi tạo của MovableObject
        super(x, y, 0, 0, dx, dy);

        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;

        try {
            this.normalBall = AssetLoader.loadImage("/images/Ball.png");
            this.superBall = AssetLoader.loadImage("/images/SuperBall.png");

            if (this.normalBall == null) {
                throw new IOException("Không thể tải tệp ảnh mặc định của bóng: /images/Ball.png");
            }

            final double TARGET_SIZE = 30;
            this.setWidth(TARGET_SIZE);
            this.setHeight(TARGET_SIZE);
            this.image = this.normalBall;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi không thể tải ảnh cho Ball", e);
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }


    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionX() {
        return this.directionX;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    public double getDirectionY() {
        return this.directionY;
    }

    public boolean isSuperBall() {
        return this.isSuperBall;
    }

    public boolean isDestroyed() {
        return this.isDestroyed;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    @Override
    public void move() {
        setDx(speed * directionX);
        setDy(speed * directionY);
        setX(getX() + getDx());
        setY(getY() + getDy());
    }

    @Override
    public void update() {
        // Cập nhật vị trí của Ball
        move();

        // Kiểm tra va chạm với tường dưới
        if (this.getY() + this.getHeight() > GameManager.GAME_HEIGHT) {
            this.destroy();
        }

        // Va chạm tường trên
        if (this.getY() < 0) {
            this.setY(0); // Đặt lại vị trí để bóng không bị kẹt
            this.bounceOff(null, CollisionManager.CollisionSide.VERTICAL);
            GameManager.getInstance().getSoundManager().playSound("paddle_hit", GameManager.getInstance().getVolumeWall());
        }

        // Va chạm tường trái
        if (this.getX() < 0) {
            this.setX(0); // Đặt lại vị trí
            this.bounceOff(null, CollisionManager.CollisionSide.HORIZONTAL);
            GameManager.getInstance().getSoundManager().playSound("paddle_hit", GameManager.getInstance().getVolumeWall());
        }
        
        // Va chạm tường phải
        if (this.getX() + this.getWidth() > GameManager.GAME_WIDTH) {
            this.setX(GameManager.GAME_WIDTH - this.getWidth()); // Đặt lại vị trí
            this.bounceOff(null, CollisionManager.CollisionSide.HORIZONTAL);
            GameManager.getInstance().getSoundManager().playSound("paddle_hit", GameManager.getInstance().getVolumeWall());
        }

        // Thiết lập góc nảy tối thiểu
        this.ensureMinimumVerticalSpeed();
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (this.getImage() != null) {
            g2d.drawImage(this.getImage(), (int) this.getX(), (int) this.getY(),
                    (int) this.getWidth(), (int) this.getHeight(), null);
        }
    }

    public boolean checkCollision(GameObject other) {
        return (this.getX() < other.getX() + other.getWidth()) &&
                (this.getX() + this.getWidth() > other.getX()) &&
                (this.getY() < other.getY() + other.getHeight()) &&
                (this.getY() + this.getHeight() > other.getY());
    }

    /**
     * Phương thức bounceOff() đã được cập nhật.
     * Giờ đây nó xử lý logic nảy cho cả Paddle và Gạch/Tường.
     */
    public void bounceOff(GameObject other, CollisionManager.CollisionSide side) {
        // VA CHẠM VỚI PADDLE
        if (other instanceof Paddle) {
            Paddle paddle = (Paddle) other;

            // Tính toán vị trí va chạm
            double relativeIntersectX = (this.getX() + (this.getWidth() / 2.0)) - (paddle.getX() + (paddle.getWidth() / 2.0));

            // Chuẩn hóa vị trí va chạm [-1.0, 1.0]
            double normalizedIntersectX = relativeIntersectX / (paddle.getWidth() / 2.0);

            // Kẹp giá trị
            normalizedIntersectX = Math.max(-1.0, Math.min(1.0, normalizedIntersectX));

            // Tính góc bật (tối đa 69 độ)
            double maxBounceAngle = Math.toRadians(69); //1.204
            double bounceAngle = normalizedIntersectX * maxBounceAngle;

            // Set hướng cho quả bóng.
            this.setDirectionX(Math.sin(bounceAngle));
            this.setDirectionY(-Math.cos(bounceAngle));

        } else {
            // VA CHẠM VỚI GẠCH HOẶC TƯỜNG

            // Chỉ cần xử lý hậu quả dựa trên side
            switch (side) {
                case HORIZONTAL:
                    // Va chạm theo phương ngang -> lật hướng X
                    this.directionX = -this.directionX;
                    break;
                case VERTICAL:
                    // Va chạm theo phương dọc -> lật hướng Y
                    this.directionY = -this.directionY;
                    break;
                case CORNER:
                    // Va chạm góc -> lật cả hai
                    this.directionX = -this.directionX;
                    this.directionY = -this.directionY;
                    break;
            }
            // Gọi hàm này để đảm bảo giá trị tối thiểu của dirY.
            ensureMinimumVerticalSpeed();
        }
    }

    /**
     * Hàm để ngăn directionY bị kẹt ở 0 .
     * Đảm bảo bóng luôn có một vận tốc dọc tối thiểu.
     */
    public void ensureMinimumVerticalSpeed() {

        final double MIN_VERTICAL_DIRECTION = 0.3; // Tương đương 17,5 độ

        if (Math.abs(this.getDirectionY()) < MIN_VERTICAL_DIRECTION) {
            // Nếu nó gần bằng 0, ép nó theo một giá trị tối thiểu
            if (this.getDirectionY() >= 0) {
                // Nếu đang đi xuống, ép nó đi xuống
                this.setDirectionY(MIN_VERTICAL_DIRECTION);
            } else {
                // Nếu đang đi lên, ép nó đi lên
                this.setDirectionY(-MIN_VERTICAL_DIRECTION);
            }
        }
    }

    /**
     * Phương thức destroy để phá hủy bóng.
     */
    public void destroy() {
        this.isDestroyed = true;
    }
    /**
     * Phương thức multiplySpeed để tăng tốc cho bóng.
     * @param factor (hệ số tăng tốc).
     */
    public void multiplySpeed(double factor) {
        this.speed *= factor;
    }

    /**
     * Phương thức kích hoạt SuperBall.
     */
    public void activateSuperBall() {
        this.isSuperBall = true;
        this.image = this.superBall;
    }

    /**
     * Phương thức hủy kích hoạt SuperBall.
     */
    public void deactivateSuperBall() {
        this.isSuperBall = false;
        this.image = this.normalBall;
    }
}