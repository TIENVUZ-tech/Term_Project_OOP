package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.entities.GameObject;
import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.graphics.AssetLoader;
import com.DevChickens.Arkanoid.entities.Ball;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


/**
 * Lớp trừu tượng cơ sở cho các vật phẩm tăng sức mạnh rơi ra khi phá gạch.
 */
public abstract class PowerUp extends GameObject {

    /** Loại của Power-up. */
    protected String type;
    /** Thời gian hiệu lực của Power-up (tính bằng số lần update hoặc mili giây). */
    protected long duration;
    /** Thời điểm Power-up được kích hoạt. */
    private long activationTime;
    /** Trạng thái kích hoạt của Power-up. */
    private boolean isActive = false;
    /* Biến lưu ảnh. */
    protected BufferedImage image;

    /**
     * Phương thức khởi tạo chung cho mọi Power-up.
     * @param x Vị trí X ban đầu.
     * @param y Vị trí Y ban đầu.
     * @param type Chuỗi ký tự định danh loại Power-up.
     * @param duration Thời gian hiệu lực.
     * @param filePath Đường dẫn tới ảnh cần load.
     */
    public PowerUp(double x, double y, String type, long duration, 
    String filePath, int width, int height) {
        super(x, y, width, height); 
        this.type = type;
        this.duration = duration;
        try {
            this.image = AssetLoader.loadImage(filePath);
        } catch (Exception e) {
            // In ra lỗi gốc
            e.printStackTrace();
            // Ném ra ngoại lệ và dừng chương trình.
            throw new RuntimeException("Lỗi tải ảnh tại đường dẫn: " + filePath);
        }   
    }

    /* Getter cho image. */
    public Image getImage() {
        return this.image;
    }

    /**
     * Kích hoạt Power-up và bắt đầu đếm ngược thời gian.
     */
    public void activate() {
        this.isActive = true;
        this.activationTime = System.currentTimeMillis();
    }

    /**
     * Kiểm tra xem Power-up đã hết hạn hay chưa.
     * @return true nếu đã hết hạn, ngược lại là false.
     */
    public boolean isExpired() {
        if (!isActive) {
            return false;
        }
        return System.currentTimeMillis() > activationTime + duration;
    }

    /** Getter cho type. */
    public String getType() {
        return type;
    }

    /** Getter cho duration (nếu cần dùng ngoài). */
    public long getDuration() {
        return duration;
    }

    /**
     *
     * Dùng AABB collision giống Ball.
     */
    public boolean checkCollision(GameObject other) {
        boolean conditionOne   = this.getX() < other.getX() + other.getWidth();
        boolean conditionTwo   = this.getX() + this.getWidth() > other.getX();
        boolean conditionThree = this.getY() < other.getY() + other.getHeight();
        boolean conditionFour  = this.getY() + this.getHeight() > other.getY();
        return conditionOne && conditionTwo && conditionThree && conditionFour;
    }

    /**
     * Cập nhật trạng thái của Power-up (chỉ rơi khi chưa kích hoạt).
     */
    @Override
    public void update() {
        if (!isActive) {
            this.setY(this.getY() + 2);
        }
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
     * Áp dụng Power-up.
     * @param paddle Thanh trượt có thể bị ảnh hưởng.
     * @param ball Quả bóng có thể bị ảnh hưởng.
     */
    public abstract void applyEffect(GameManager manager, Paddle paddle, Ball ball);

    /**
     * Gỡ bỏ Power-up khi hết hạn.
     * @param paddle Thanh trượt có thể bị ảnh hưởng.
     * @param ball Quả bóng có thể bị ảnh hưởng.
     */
    public abstract void removeEffect(Paddle paddle, Ball ball);
}
