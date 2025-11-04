package com.DevChickens.Arkanoid.entities;

import com.DevChickens.Arkanoid.graphics.AssetLoader;
import com.DevChickens.Arkanoid.core.CollisionManager;
import com.DevChickens.Arkanoid.core.GameManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Lớp Ball kế thừa từ MovableObject.
 * Là quả bóng nảy trong giao diện của game.
 */
public class Ball extends MovableObject {

    /** Tốc độ của bóng (pixel/frame) */
    private double speed;
    /** Hướng di chuyển X (vector đã chuẩn hóa) */
    private double directionX;
    /** Hướng di chuyển Y (vector đã chuẩn hóa) */
    private double directionY;
    /** Trạng thái SuperBall (true nếu đang là SuperBall) */
    private boolean isSuperBall = false;
    /** Trạng thái bị phá hủy (true nếu bóng rơi xuống đáy) */
    private boolean isDestroyed = false;

    /** Ảnh của bóng thường */
    private BufferedImage normalBall;
    /** Ảnh của bóng SuperBall */
    private BufferedImage superBall;
    /** Ảnh hiện tại đang được sử dụng để vẽ */
    private BufferedImage image;

    /**
     * Phương thức khởi tạo Ball.
     * @param x Vị trí X ban đầu
     * @param y Vị trí Y ban đầu
     * @param dx Vận tốc X ban đầu (thường là 0, được tính bởi move())
     * @param dy Vận tốc Y ban đầu (thường là 0, được tính bởi move())
     * @param speed Tốc độ di chuyển tổng (pixel/frame)
     * @param directionX Hướng di chuyển X ban đầu (kiểu double)
     * @param directionY Hướng di chuyển Y ban đầu (kiểu double)
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

    /**
     * Thiết lập tốc độ di chuyển của bóng.
     * @param speed Tốc độ mới
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Lấy tốc độ di chuyển hiện tại của bóng.
     * @return Tốc độ (pixel/frame)
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Thiết lập hướng di chuyển X (đã chuẩn hóa).
     * @param directionX Hướng X mới
     */
    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    /**
     * Lấy hướng di chuyển X hiện tại.
     * @return Hướng X (đã chuẩn hóa)
     */
    public double getDirectionX() {
        return this.directionX;
    }

    /**
     * Thiết lập hướng di chuyển Y (đã chuẩn hóa).
     * @param directionY Hướng Y mới
     */
    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    /**
     * Lấy hướng di chuyển Y hiện tại.
     * @return Hướng Y (đã chuẩn hóa)
     */
    public double getDirectionY() {
        return this.directionY;
    }

    /**
     * Kiểm tra xem bóng có phải là SuperBall không.
     * @return true nếu là SuperBall, ngược lại false
     */
    public boolean isSuperBall() {
        return this.isSuperBall;
    }

    /**
     * Kiểm tra xem bóng đã bị phá hủy (rơi xuống đáy) chưa.
     * @return true nếu đã bị phá hủy, ngược lại false
     */
    public boolean isDestroyed() {
        return this.isDestroyed;
    }

    /**
     * Lấy ảnh hiện tại của bóng để vẽ.
     * @return Ảnh (thường hoặc SuperBall)
     */
    public BufferedImage getImage() {
        return this.image;
    }

    /**
     * Cập nhật vận tốc (dx, dy) dựa trên tốc độ (speed) và hướng (direction).
     * Sau đó cập nhật vị trí (x, y) dựa trên vận tốc.
     */
    @Override
    public void move() {
        setDx(speed * directionX);
        setDy(speed * directionY);
        setX(getX() + getDx());
        setY(getY() + getDy());
    }

    /**
     * Cập nhật logic của bóng mỗi khung hình.
     * Bao gồm di chuyển, kiểm tra va chạm tường, và đảm bảo góc nảy.
     */
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

    /**
     * Phương thức render để vẽ hình ảnh của vật thể.
     * @param g Đối tượng Graphics để vẽ
     */
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (this.getImage() != null) {
            g2d.drawImage(this.getImage(), (int) this.getX(), (int) this.getY(),
                    (int) this.getWidth(), (int) this.getHeight(), null);
        }
    }

    /**
     * Kiểm tra va chạm AABB (Axis-Aligned Bounding Box) với một vật thể khác.
     * @param other Vật thể (GameObject) cần kiểm tra
     * @return true nếu có va chạm, ngược lại false
     */
    public boolean checkCollision(GameObject other) {
        return (this.getX() < other.getX() + other.getWidth()) &&
               (this.getX() + this.getWidth() > other.getX()) &&
               (this.getY() < other.getY() + other.getHeight()) &&
               (this.getY() + this.getHeight() > other.getY());
    }

    /**
     * Xử lý logic nảy (thay đổi hướng) khi va chạm.
     * Xử lý riêng cho va chạm với Paddle (nảy tự nhiên) và Gạch/Tường (nảy cơ bản).
     * @param other Vật thể đã va chạm (có thể là null nếu va chạm tường)
     * @param side Hướng va chạm (HORIZONTAL, VERTICAL, CORNER)
     */
    public void bounceOff(GameObject other, CollisionManager.CollisionSide side) {
        // VA CHẠM VỚI PADDLE
        if (other instanceof Paddle) {
            Paddle paddle = (Paddle) other;

            // Tính toán vị trí va chạm
            double relativeIntersectX = (this.getX() + (this.getWidth() / 2.0)) - (paddle.getX() + (paddle.getWidth() / 2.0));

            // Chuẩn hóa vị trí va chạm [-1.0, 1.0]
            // Tuy nhiên có thể giá trị vẫn vượt quá [-1.0, 1.0]
            double normalizedIntersectX = relativeIntersectX / (paddle.getWidth() / 2.0);

            // Kẹp giá trị
            normalizedIntersectX = Math.max(-1.0, Math.min(1.0, normalizedIntersectX));

            // Tính góc bật (tối đa 69 độ)
            double maxBounceAngle = Math.toRadians(69); //1.204
            double bounceAngle = normalizedIntersectX * maxBounceAngle;

            // Set hướng cho quả bóng.
            this.setDirectionX(Math.sin(bounceAngle));
            this.setDirectionY(- Math.cos(bounceAngle));
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
     * Hàm để ngăn directionY bị kẹt ở 0 (di chuyển ngang tuyệt đối).
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
     * Phương thức destroy để phá hủy bóng (đánh dấu là đã bị phá hủy).
     */
    public void destroy() {
        this.isDestroyed = true;
    }

    /**
     * Phương thức multiplySpeed để tăng (hoặc giảm) tốc độ của bóng.
     * @param factor Hệ số nhân tốc độ
     */
    public void multiplySpeed(double factor) {
        this.speed *= factor;
    }

    /**
     * Phương thức kích hoạt SuperBall.
     * Thay đổi trạng thái và hình ảnh của bóng.
     */
    public void activateSuperBall() {
        this.isSuperBall = true;
        this.image = this.superBall;
    }

    /**
     * Phương thức hủy kích hoạt SuperBall.
     * Đưa bóng về trạng thái và hình ảnh bình thường.
     */
    public void deactivateSuperBall() {
        this.isSuperBall = false;
        this.image = this.normalBall;
    }
}