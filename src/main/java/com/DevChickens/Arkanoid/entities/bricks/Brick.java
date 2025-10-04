package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.entities.GameObject;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Lớp trừu tượng cơ sở cho tất cả các khối gạch cần phá hủy.
 * Kế thừa từ GameObject và định nghĩa các thuộc tính, phương thức chung cho gạch.
 */
public abstract class Brick extends GameObject {

    /** Số lần va chạm cần thiết để phá hủy viên gạch. */
    protected int hitPoints;
    /** Loại của viên gạch, lưu dưới dạng một chuỗi ký tự. */
    protected String type;
    /** Cờ đánh dấu viên gạch đã bị phá hủy hay chưa. */
    protected boolean destroyed;
    /** Cờ xác định xem gạch có khả năng sinh ra Power-up khi bị phá hủy hay không. */
    protected boolean canSpawnPowerUp;

    /**
     * Phương thức khởi tạo chung cho mọi viên gạch.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param hitPoints Số lần va chạm ban đầu.
     * @param type Chuỗi ký tự định danh loại gạch.
     */
    public Brick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
        this.destroyed = false;

        if (hitPoints > 0 && Math.random() < 0.2) {
            this.canSpawnPowerUp = true;
        }
    }

    /**
     * Xử lý logic khi viên gạch nhận một va chạm từ bóng.
     */
    public void takeHit() {
        if (this.hitPoints > 0) {
            this.hitPoints--;
        }
        if (this.hitPoints == 0) {
            this.destroyed = true;
        }
    }

    /**
     * Kiểm tra xem viên gạch đã bị phá hủy hoàn toàn hay chưa.
     * @return true nếu gạch đã bị phá hủy, ngược lại false.
     */
    public boolean isDestroyed() {
        return this.destroyed;
    }

    /**
     * Kiểm tra xem gạch có nên sinh ra một Power-up hay không.
     * @return true nếu gạch đã bị phá hủy và có khả năng sinh Power-up.
     */
    public boolean shouldSpawnPowerUp() {
        return isDestroyed() && canSpawnPowerUp;
    }

    /**
     * Lấy loại của viên gạch.
     * @return Chuỗi ký tự định danh loại gạch.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Cài đặt phương thức update() được kế thừa từ GameObject.
     * Vì gạch là vật thể tĩnh, phương thức này để trống.
     */
    @Override
    public void update() {
        // Gạch tĩnh không cần cập nhật logic.
    }

    /**
     * Khai báo lại phương thức render() là abstract.
     * Điều này buộc các lớp con như NormalBrick, QuiteBrick... phải tự định nghĩa
     * cách vẽ của riêng chúng.
     * @param g Đối tượng Graphics dùng để vẽ.
     */
    @Override
    public abstract void render(Graphics g);
}