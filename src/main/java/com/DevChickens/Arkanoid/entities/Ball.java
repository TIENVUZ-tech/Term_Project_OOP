package com.DevChickens.Arkanoid.entities;

import java.awt.Graphics;
import com.DevChickens.Arkanoid.graphics.AssetLoader;
import java.awt.image.BufferedImage;
/**
 * Lớp Ball, giữ nguyên chữ ký hàm khởi tạo 9 tham số theo yêu cầu.
 * Logic di chuyển chỉ sử dụng 'speed', 'directionX', 'directionY'.
 * Các tham số 'dx' và 'dy' được truyền lên lớp cha MovableObject.
 */
public class Ball extends MovableObject {
    /* Tốc độ của bóng. Đơn vị là pixel/frame. */
    private double speed;
    /* Hướng di chuyển của bóng theo phương ngang (1 hoặc -1). */
    private int directionX;
    /* Hướng di chuyển của bóng theo phương dọc (1 hoặc -1). */
    private int directionY;
    /* biến chứa ảnh của bóng. */
    private BufferedImage normalImage;
    /* biến chứa ảnh của siêu bóng. */
    private BufferedImage superImage;
    /* biến chứa hình ảnh hiện tại của bóng. */
    private BufferedImage image;
    /* biến thể hiện tình trạng bóng. */
    private boolean isSuperBall = false;
    /* biến thể hiện thời gian cho đến khi superBallPowerUp hết tác dụng. */
    private long superBallEndTime = 0;

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
        // Đọc ảnh từ thư mục images.
        this.normalImage = AssetLoader.loadImage("/images/Ball.png");
        if (this.normalImage != null) {
            final double TARGET_SIZE = 30;
            this.setWidth(TARGET_SIZE);
            this.setHeight(TARGET_SIZE);
        }
        // Đọc ảnh superBall từ thư mục images.
        this.superImage = AssetLoader.loadImage("/images/SuperBall.png");
        if (this.superImage != null) {
            final double TARGET_SIZE = 30;
            this.setWidth(TARGET_SIZE);
            this.setHeight(TARGET_SIZE);
        }
        this.image = this.normalImage;
    }

    /**Phương thức setter và getter của speed. */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    /**Phương thức setter và getter của directionX. */
    public void setDirectionX(int directionX) {
        // Đảm bảo luôn có hướng (1 hoặc -1)
        this.directionX = directionX == 0 ? 1 : directionX;
    }

    public int getDirectionX() {
        return this.directionX;
    }

    /**Phương thức setter và getter của directionY. */
    public void setDirectionY(int directionY) {
        // Đảm bảo luôn có hướng (1 hoặc -1)
        this.directionY = directionY == 0 ? 1 : directionY;
    }

    public int getDirectionY() {
        return this.directionY;
    }

    /* Phương thức  */
    
    // --- Logic Game ---

    @Override
    public void move() {
        /* Di chuyển bóng dựa vào speed và hướng (directionX/Y). 
           Chúng ta không dùng getDx() và getDy() ở đây. */
        setX(getX() + speed * directionX); 
        setY(getY() + speed * directionY); 
    }

    @Override
    public void update() {
        // gọi phương thức di chuyển.
        move();
        // kiểm tra và kết thúc SuperBallPowerUp
        if (isSuperBall && System.currentTimeMillis() > superBallEndTime) {
            deactivateSuperBall();
        }
    }

    @Override
    public void render(Graphics g) {
        
    }

    /**
     * Phương thức checkCollistion().
     * Kiểm tra va chạm AABB (Axis-Aligned Bounding Box).
     * @param other (Đối tượng GameObject cần kiểm tra va chạm)
     * @return true (nếu bóng va chạm với vật thể).
     */
    public boolean checkCollision(GameObject other) {
        boolean conditionOne   = this.getX() < other.getX() + other.getWidth();
        boolean conditionTwo   = this.getX() + this.getWidth() > other.getX();
        boolean conditionThree = this.getY() < other.getY() + other.getHeight();
        boolean conditionFour  = this.getY() + this.getHeight() > other.getY();
        return conditionOne && conditionTwo && conditionThree && conditionFour;
    }

    /**
     * Phương thức bounceOff().
     * Xử lý logic bật ngược (đổi hướng) khi bóng va chạm.
     * Sử dụng phương pháp kiểm tra vùng giao nhau (overlap) để xác định hướng bật.
     * @param other (đối tượng GameObject)
     */
    public void bounceOff(GameObject other) {
        /* 1. Tính toán lại tâm bóng và tâm vật thể. */
        double ballCentreX = this.getX() + this.getWidth() / 2;
        double ballCentreY = this.getY() + this.getHeight() / 2;
        double otherCentreX = other.getX() + other.getWidth() / 2;
        double otherCentreY = other.getY() + other.getHeight() / 2;

        /* 2. Tính khoảng cách giữa hai tâm. */
        double dx = ballCentreX - otherCentreX;
        double dy = ballCentreY - otherCentreY;

        /* 3. Tính độ dài nửa chiều rộng/cao kết hợp. */
        double combinedHalfWidth = this.getWidth() / 2 + other.getWidth() / 2;
        double combinedHalfHeight = this.getHeight() / 2 + other.getHeight() / 2;
        
        /* 4. Tính độ lớn vùng giao nhau (overlap) trên mỗi trục. */
        double overlapX = combinedHalfWidth - Math.abs(dx);
        double overlapY = combinedHalfHeight - Math.abs(dy);

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
        this.image = this.superImage;
        long now = System.currentTimeMillis();
        // Tính thời gian còn lại của hiệu ứng cũ (nếu có)
        long remainingTime = (this.superBallEndTime > now) ? (this.superBallEndTime - now) : 0;
        // Thời gian kết thúc mới = Hiện tại + Thời gian còn lại + Thời gian mới
        this.superBallEndTime = System.currentTimeMillis() + remainingTime + duration;
    }

    /**
     * Phương thức deactivateSuperBall() để thay đổi trạng thái của isSuperBall.
     */
    public void deactivateSuperBall() {
        this.isSuperBall = false;
        this.image = this.normalImage;
    }

    /**
     * Phương thức trả về biến hình ảnh dùng để vẽ vật thể trong Renderer.
     */
    public BufferedImage getImage() {
        return this.image;
    }
}