package com.DevChickens.Arkanoid.entities.bricks;

import com.DevChickens.Arkanoid.entities.GameObject;
import java.awt.image.BufferedImage;

/**
 * Lớp trừu tượng cơ sở (Model) cho tất cả các loại gạch trong game.
 * Chứa dữ liệu và logic chung như máu, điểm, và trạng thái.
 */
public abstract class Brick extends GameObject {

    /** Máu của viên gạch. Khi về 0, gạch sẽ bị phá hủy. */
    protected int health;
    /** Điểm số người chơi nhận được khi phá hủy gạch. */
    protected int score;
    /** Cờ đánh dấu viên gạch đã bị phá hủy hay chưa. */
    protected boolean isDestroyed;
    /** Cờ xác định xem gạch có khả năng sinh ra Power-up hay không. */
    protected boolean canSpawnPowerUp;

    /**
     * Phương thức khởi tạo chung cho mọi viên gạch.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param health Lượng máu ban đầu.
     * @param score Điểm số khi bị phá hủy.
     */
    public Brick(double x, double y, double width, double height, int health, int score) {
        super(x, y, width, height);
        this.health = health;
        this.score = score;
        this.isDestroyed = false;

        // Quyết định ngẫu nhiên khả năng sinh Power-up
        if (Math.random() < 0.2) {
            this.canSpawnPowerUp = true;
        } else {
            this.canSpawnPowerUp = false;
        }
    }

    /**
     * Xử lý logic khi bóng va chạm vào gạch.
     * Giảm máu và kiểm tra xem gạch có bị phá hủy không.
     */
    public void hit() {
        if (this.health > 0) {
            this.health--; // SỬA LỖI: health
        }
        if (this.health == 0) {
            this.isDestroyed = true;
        }
    }

    /**
     * Kiểm tra xem gạch đã bị phá hủy chưa.
     * @return true nếu gạch đã bị phá hủy, ngược lại false.
     */
    public boolean isDestroyed() {
        return this.isDestroyed;
    }

    /**
     * Kiểm tra xem gạch có nên sinh ra một Power-up hay không.
     * @return true nếu gạch đã bị phá hủy và có khả năng sinh Power-up.
     */
    public boolean shouldSpawnPowerUp() { // SỬA LỖI: Thêm ()
        return isDestroyed() && canSpawnPowerUp;
    }

    /**
     * Lấy điểm số của viên gạch.
     * @return giá trị điểm số.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Lấy lượng máu hiện tại của viên gạch.
     * @return số máu còn lại.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Phương thức trừu tượng buộc các lớp con phải cung cấp một hình ảnh
     * phù hợp với trạng thái hiện tại của chúng.
     * @return Đối tượng BufferedImage để cho Renderer vẽ.
     */
    public abstract BufferedImage getImage();

    @Override
    public void update() {
        // Gạch tĩnh không cần update.
    }
}
