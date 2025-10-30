package com.DevChickens.Arkanoid.entities;

import com.DevChickens.Arkanoid.graphics.AssetLoader;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException; // Thêm import này

/**
 * Lớp Ball, giữ nguyên chữ ký hàm khởi tạo 7 tham số theo yêu cầu.
 * Logic di chuyển chỉ sử dụng 'speed', 'directionX', 'directionY'.
 * Các tham số 'dx' và 'dy' được truyền lên lớp cha MovableObject.
 */
public class Ball extends MovableObject {

    private double speed;           // Tốc độ của bóng. Đơn vị là pixel/frame.
    private int directionX;         // Hướng di chuyển của bóng theo phương ngang (1 hoặc -1).
    private int directionY;         // Hướng di chuyển của bóng theo phương dọc (1 hoặc -1).
    private BufferedImage normalBall;  // biến chứa ảnh của bóng.
    private BufferedImage superBall;   // biến chứa ảnh của siêu bóng.
    private BufferedImage image;        // biến chứa hình ảnh hiện tại của bóng.
    private boolean isSuperBall = false; // biến thể hiện tình trạng bóng.

    /**
     * Phương thức khởi tạo Ball, giữ nguyên 9 tham số đầu vào.
     * @param x (tọa độ x)
     * @param y (toạ độ y)
     * @param dx (tốc độ di chuyển theo trục x - được truyền lên lớp cha)
     * @param dy (tốc độ di chuyển theo chiều y - được truyền lên lớp cha)
     * @param speed (tốc độ di chuyển tổng - được dùng trong move())
     * @param directionX (hướng di chuyển theo phương ngang)
     * @param directionY (hướng di chuyển theo phương dọc)
     */
    public Ball(double x, double y, double dx, double dy,
            double speed, int directionX, int directionY) {
        // Gọi hàm khởi tạo của MovableObject, sử dụng dx và dy đầu vào
        super(x, y, 0, 0, dx, dy);

        this.speed = speed;
        // Đảm bảo hướng di chuyển luôn là 1 hoặc -1
        this.directionX = directionX == 0 ? 1 : directionX;
        this.directionY = directionY == 0 ? 1 : directionY;

        try {
            // Đọc ảnh từ thư mục images.
            this.normalBall = AssetLoader.loadImage("/images/Ball.png");
            this.superBall = AssetLoader.loadImage("/images/SuperBall.png");

            // Lỗi khi không tải được ảnh mặc định.
            if (this.normalBall == null) {
                throw new IOException("Không thể tải tệp ảnh mặc định của bóng: /images/Ball.png");
            }

            // Đặt kích thước 1 LẦN DUY NHẤT
            final double TARGET_SIZE = 30;
            this.setWidth(TARGET_SIZE);
            this.setHeight(TARGET_SIZE);
            this.image = this.normalBall; // Đặt ảnh hiện tại

        } catch (Exception e) {
            // In ra lối gốc.
            e.printStackTrace();
            // Ném ra ngoại lệ và dừng chương trình.
            throw new RuntimeException("Lỗi không thể tải ảnh cho Ball", e);
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setDirectionX(int directionX) {
        // Đảm bảo luôn có hướng (1 hoặc -1)
        this.directionX = directionX == 0 ? 1 : directionX;
    }

    public int getDirectionX() {
        return this.directionX;
    }

    public void setDirectionY(int directionY) {
        // Đảm bảo luôn có hướng (1 hoặc -1)
        this.directionY = directionY == 0 ? 1 : directionY;
    }

    public int getDirectionY() {
        return this.directionY;
    }

    public boolean getIsSuperBall() {
        return this.isSuperBall;
    }

    /**
     * Phương thức trả về biến hình ảnh dùng để vẽ vật thể trong Renderer.
     */
    public BufferedImage getImage() {
        return this.image;
    }

    @Override
    public void move() {
        /* Di chuyển bóng dựa vào speed và hướng (directionX/Y).
         * Chúng ta không dùng getDx() và getDy() ở đây. */
        setDx(speed * directionX);
        setDy(speed * directionY);
        setX(getX() + getDx());
        setY(getY() + getDy());
    }

    @Override
    public void update() {
        // cập nhật vị trí của bóng.
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

    /**
     * Phương thức checkCollistion().
     * Kiểm tra va chạm AABB (Axis-Aligned Bounding Box).
     * @param other (Đối tượng GameObject cần kiểm tra va chạm)
     * @return true (nếu bóng va chạm với vật thể).
     */
    public boolean checkCollision(GameObject other) {
        // SỬA LỖI CONVENTION (Mục 6.2 và 10.5.2)
        return (this.getX() < other.getX() + other.getWidth()) &&
               (this.getX() + this.getWidth() > other.getX()) &&
               (this.getY() < other.getY() + other.getHeight()) &&
               (this.getY() + this.getHeight() > other.getY());
    }

    /**
     * Phương thức bounceOff().
     * Xử lý logic bật ngược (đổi hướng) khi bóng va chạm.
     * Sử dụng phương pháp kiểm tra vùng giao nhau (overlap) để xác định hướng bật.
     * @param other (đối tượng GameObject)
     */
    public void bounceOff(GameObject other) {
        // --- 1. KIỂM TRA NẾU VA CHẠM VỚI PADDLE ---
        if (other instanceof Paddle) {
            Paddle paddle = (Paddle) other;
            
            // Tính toán vị trí tâm bóng và tâm paddle
            double ballCenterX = this.getX() + this.getWidth() / 2;
            double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
            
            // Tính vị trí va chạm tương đối
            // `relativeIntersect` là khoảng cách từ tâm paddle đến tâm bóng
            double relativeIntersect = ballCenterX - paddleCenterX;
            
            // Chuẩn hóa vị trí va chạm về khoảng [-1.0, 1.0]
            // -1.0 là mép trái, 0.0 là tâm, 1.0 là mép phải
            double normalizedIntersect = (relativeIntersect / (paddle.getWidth() / 2));
            
            // Giới hạn giá trị để tránh lỗi (ví dụ: bóng đập vào cạnh của paddle)
            if (normalizedIntersect > 1.0) normalizedIntersect = 1.0;
            if (normalizedIntersect < -1.0) normalizedIntersect = -1.0;
            
            // --- 2. TÍNH TOÁN VECTOR VẬN TỐC MỚI ---
            // Đây là phần cốt lõi của vật lý "tự nhiên"
            
            // Dùng lượng giác để tính góc nảy
            // `normalizedIntersect` sẽ điều khiển góc
            // Góc 0 độ (ở giữa) sẽ là thẳng đứng
            // Góc -75 độ (mép trái) và +75 độ (mép phải)
            double maxBounceAngle = Math.toRadians(75); // 75 độ
            double bounceAngle = normalizedIntersect * maxBounceAngle;
            
            // Lấy TỐC ĐỘ (SPEED) hiện tại của bóng
            // Tốc độ chính là độ dài của vector (dx, dy)
            double currentSpeed = Math.sqrt(getDx() * getDx() + getDy() * getDy());
            
            // Tính toán dx, dy mới dựa trên góc nảy và giữ nguyên tốc độ
            // Math.sin(bounceAngle) sẽ là dx mới
            // -Math.cos(bounceAngle) sẽ là dy mới (dấu - vì nảy LÊN)
            setDx(currentSpeed * Math.sin(bounceAngle));
            setDy(currentSpeed * -Math.cos(bounceAngle));
            
        } else {
            // --- 3. VA CHẠM VỚI GẠCH HOẶC TƯỜNG (Logic cũ) ---
            // Logic va chạm AABB đơn giản
           // SỬA LỖI CONVENTION (Mục 6.2): Khai báo biến ở đầu khối.

        double ballCentreX, ballCentreY, otherCentreX, otherCentreY;

        double dx, dy, combinedHalfWidth, combinedHalfHeight;

        double overlapX, overlapY;



        /* 1. Tính toán lại tâm bóng và tâm vật thể. */

        ballCentreX = this.getX() + this.getWidth() / 2;

        ballCentreY = this.getY() + this.getHeight() / 2;

        otherCentreX = other.getX() + other.getWidth() / 2;

        otherCentreY = other.getY() + other.getHeight() / 2;



        /* 2. Tính khoảng cách giữa hai tâm. */

        dx = ballCentreX - otherCentreX;

        dy = ballCentreY - otherCentreY;



        /* 3. Tính độ dài nửa chiều rộng/cao kết hợp. */

        combinedHalfWidth = this.getWidth() / 2 + other.getWidth() / 2;

        combinedHalfHeight = this.getHeight() / 2 + other.getHeight() / 2;



        /* 4. Tính độ lớn vùng giao nhau (overlap) trên mỗi trục. */

        overlapX = combinedHalfWidth - Math.abs(dx);

        overlapY = combinedHalfHeight - Math.abs(dy);



        // Đảo hướng dựa trên vùng giao nhau nhỏ nhất (overlap)

        if (overlapX < overlapY) {

            // Va chạm theo phương ngang (giao nhau theo X ít hơn)

            directionX = -directionX;

        } else if (overlapY < overlapX) {

            // Va chạm theo phương dọc (giao nhau theo Y ít hơn)

            directionY = -directionY;

        } else {

            // Va chạm góc (overlapX == overlapY)

            directionX = -directionX;

            directionY = -directionY;

        }
        }
    }

    /**
     * Phương thức thay đổi tốc độ của quả bóng.
     * Chỉ thay đổi biến speed, vì nó là biến được dùng trong move().
     * @param factor hệ số thay đổi tốc độ.
     */
    public void multiplySpeed(double factor) {
        this.speed *= factor;
    }

    /**
     * Phương thức activateSuperBall() để thay đổi trạng thái của isSuperBall.
     */
    public void activateSuperBall(long duration) {
        this.isSuperBall = true;
        this.image = this.superBall;
    }

    /**
     * Phương thức deactivateSuperBall() để thay đổi trạng thái của isSuperBall.
     */
    public void deactivateSuperBall() {
        this.isSuperBall = false;
        this.image = this.normalBall;
    }
}