package com.DevChickens.Arkanoid.entities;

import com.DevChickens.Arkanoid.graphics.AssetLoader;

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

    private BufferedImage normalBall;
    private BufferedImage superBall;
    private BufferedImage image;
    private boolean isSuperBall = false;

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
        move();
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
    public void bounceOff(GameObject other) {
        // VA CHẠM VỚI PADDLE
        if (other instanceof Paddle) {
            Paddle paddle = (Paddle) other;

            // Tính toán vị trí va chạm
            double relativeIntersectX = (this.getX() + (this.getWidth() / 2.0)) - (paddle.getX() + (paddle.getWidth() / 2.0));

            // Chuẩn hóa vị trí va chạm [-1.0, 1.0]
            double normalizedIntersectX = relativeIntersectX / (paddle.getWidth() / 2.0);

            // Kẹp giá trị
            normalizedIntersectX = Math.max(-1.0, Math.min(1.0, normalizedIntersectX));

            // Tính góc bật (tối đa 60 độ)
            double maxBounceAngle = Math.toRadians(69); // 0.358
            double bounceAngle = normalizedIntersectX * maxBounceAngle;

            // Set hướng cho quả bóng.
            this.setDirectionX(Math.sin(bounceAngle));
            this.setDirectionY(-Math.cos(bounceAngle));

        } else {
            // VA CHẠM VỚI GẠCH HOẶC TƯỜNG

            // Logic AABB để tìm hướng va chạm (ngang hay dọc). Lấy tọa độ tâm của quả bóng và viên gạch.
            double ballCentreX = this.getX() + this.getWidth() / 2;
            double ballCentreY = this.getY() + this.getHeight() / 2;
            double otherCentreX = other.getX() + other.getWidth() / 2;
            double otherCentreY = other.getY() + other.getHeight() / 2;

            // Tính khoảng cách giữa 2 tâm.
            double dx = ballCentreX - otherCentreX;
            double dy = ballCentreY - otherCentreY;

            // Khoảng cách tối thiểu mà 2 tâm cách nhau trên trục để chúng vừa chạm nhau.
            double combinedHalfWidth = this.getWidth() / 2 + other.getWidth() / 2;
            double combinedHalfHeight = this.getHeight() / 2 + other.getHeight() / 2;

            // Tính xem chúng đang lấn vào nhau bao nhiêu trên mỗi trục.
            double overlapX = combinedHalfWidth - Math.abs(dx);
            double overlapY = combinedHalfHeight - Math.abs(dy);

            // Đảo ngược hướng.
            if (overlapX < overlapY) {
                // Va chạm theo phương ngang -> lật hướng X
                this.directionX = -this.directionX;
            } else if (overlapY < overlapX) {
                // Va chạm theo phương dọc -> lật hướng Y
                this.directionY = -this.directionY;
            } else {
                // Va chạm góc -> lật cả hai
                this.directionX = -this.directionX;
                this.directionY = -this.directionY;
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

    public void multiplySpeed(double factor) {
        this.speed *= factor;
    }

    public void activateSuperBall() {
        this.isSuperBall = true;
        this.image = this.superBall;
    }

    public void deactivateSuperBall() {
        this.isSuperBall = false;
        this.image = this.normalBall;
    }
}