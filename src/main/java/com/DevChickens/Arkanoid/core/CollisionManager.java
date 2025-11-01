package com.DevChickens.Arkanoid.core;
import com.DevChickens.Arkanoid.entities.GameObject;

/**
 * Lớp tiện ích static để xử lý tất cả logic va chạm hình học.
 */
public final class CollisionManager {

    /**
     * Một lớp không thể được khởi tạo (utility class).
     */
    private CollisionManager() {}

    /**
     * Định nghĩa các hướng va chạm có thể xảy ra.
     * HORIZONTAL: Va chạm xảy ra ở cạnh TRÁI hoặc PHẢI.
     * VERTICAL: Va chạm xảy ra ở cạnh TRÊN hoặc DƯỚI.
     * CORNER: Va chạm ở góc (overlapX == overlapY).
     */
    public enum CollisionSide {
        HORIZONTAL,
        VERTICAL,
        CORNER
    }

    /**
     * Hàm kiểm tra va chạm AABB (Axis-Aligned Bounding Box) cơ bản.
     * @param a Vật thể 1
     * @param b Vật thể 2
     * @return true nếu 2 vật va chạm, false nếu không.
     */
    public static boolean checkCollision(GameObject a, GameObject b) {
        return (a.getX() < b.getX() + b.getWidth()) &&
                (a.getX() + a.getWidth() > b.getX()) &&
                (a.getY() < b.getY() + b.getHeight()) &&
                (a.getY() + a.getHeight() > b.getY());
    }

    /**
     * Tính toán hướng va chạm (Ngang, Dọc, Góc)
     * Đây là logic AABB nâng cao mà bạn đã có trong Ball.java.
     * Nó tính toán độ "chồng lấn" (overlap) trên mỗi trục.
     * * @param a Vật thể 1 (ví dụ: Ball)
     * @param b Vật thể 2 (ví dụ: Brick)
     * @return Hướng va chạm (HORIZONTAL, VERTICAL, or CORNER)
     */
    public static CollisionSide getCollisionDirection(GameObject a, GameObject b) {
        // Lấy tọa độ tâm
        double centreA_X = a.getX() + a.getWidth() / 2.0;
        double centreA_Y = a.getY() + a.getHeight() / 2.0;
        double centreB_X = b.getX() + b.getWidth() / 2.0;
        double centreB_Y = b.getY() + b.getHeight() / 2.0;

        // Tính khoảng cách giữa 2 tâm
        double dx = centreA_X - centreB_X;
        double dy = centreA_Y - centreB_Y;

        // Khoảng cách tối thiểu để 2 tâm vừa chạm nhau
        double combinedHalfWidth = a.getWidth() / 2.0 + b.getWidth() / 2.0;
        double combinedHalfHeight = a.getHeight() / 2.0 + b.getHeight() / 2.0;

        // Tính xem chúng đang lấn vào nhau bao nhiêu trên mỗi trục
        double overlapX = combinedHalfWidth - Math.abs(dx);
        double overlapY = combinedHalfHeight - Math.abs(dy);

        // So sánh độ lấn (overlap)
        // Trục nào có độ lấn ÍT HƠN là trục va chạm chính
        if (overlapX < overlapY) {
            return CollisionSide.HORIZONTAL;
        } else if (overlapY < overlapX) {
            return CollisionSide.VERTICAL;
        } else {
            return CollisionSide.CORNER;
        }
    }
}