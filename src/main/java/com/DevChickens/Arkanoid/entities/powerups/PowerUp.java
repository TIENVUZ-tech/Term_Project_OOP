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

    /**
     * Cập nhật trạng thái của Power-up (ví dụ: di chuyển rơi xuống).
     */
    @Override
    public void update() {

    }

    /**
     * Áp dụng Powerup.
     * @param paddle Thanh trượt có thể bị ảnh hưởng.
     * @param ball Quả bóng có thể bị ảnh hưởng.
     */
    public abstract void applyEffect(Paddle paddle, Ball ball);

    /**
     * Gỡ bỏ Powerup khi hết hạn.
     * @param paddle Thanh trượt có thể bị ảnh hưởng.
     * @param ball Quả bóng có thể bị ảnh hưởng.
     */
    public abstract void removeEffect(Paddle paddle, Ball ball);


    @Override
    public void render(Graphics g) {
    }

    @Override
    public String getTerminalString() {
        return "[ P ]";
    }
}