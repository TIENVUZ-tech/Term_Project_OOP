package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.entities.GameObject;
import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.Ball;
import java.awt.Graphics;

/**
 * Lớp trừu tượng cơ sở cho các vật phẩm tăng sức mạnh rơi ra khi phá gạch.
 */
public abstract class PowerUp extends GameObject {

    /** Loại của Power-up. */
    protected String type;
    /** Thời gian hiệu lực của Power-up (tính bằng số lần update hoặc mili giây). */
    protected int duration;

    /**
     * Phương thức khởi tạo chung cho mọi Power-up.
     * @param x Vị trí X ban đầu.
     * @param y Vị trí Y ban đầu.
     * @param type Chuỗi ký tự định danh loại Power-up.
     * @param duration Thời gian hiệu lực.
     */
    public PowerUp(double x, double y, String type, int duration) {
        super(x, y, 30, 30); // Giả sử kích thước Power-up là 30x30
        this.type = type;
        this.duration = duration;
    }

    /** Getter cho type. */
    public String getType() {
        return type;
    }

    /** Getter cho duration (nếu cần dùng ngoài). */
    public int getDuration() {
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
     * Cập nhật trạng thái của Power-up (ví dụ: di chuyển rơi xuống).
     */
    @Override
    public void update() {
        this.setY(this.getY() + 2);
    }


    /**
     * Áp dụng Power-up.
     * @param paddle Thanh trượt có thể bị ảnh hưởng.
     * @param ball Quả bóng có thể bị ảnh hưởng.
     */
    public abstract void applyEffect(Paddle paddle, Ball ball);

    /**
     * Gỡ bỏ Power-up khi hết hạn.
     * @param paddle Thanh trượt có thể bị ảnh hưởng.
     * @param ball Quả bóng có thể bị ảnh hưởng.
     */
    public abstract void removeEffect(Paddle paddle, Ball ball);

    @Override
    public void render(Graphics g) {
        // Có thể vẽ biểu tượng power-up ở đây
    }
}
