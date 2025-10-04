package com.DevChickens.Arkanoid.entities.bricks;

/**
 * Factory tạo ra các loại Brick cụ thể dựa vào tham số hitPoints hoặc type.
 */
public class BrickFactory {

    /**
     * Tạo Brick dựa trên hitPoints hoặc type.
     *
     * @param x         tọa độ X
     * @param y         tọa độ y
     * @param type      loại brick ("normal", "strong", "explosive", "quite")
     * @return          một đối tượng Brick cụ thể
     */
    public static Brick createBrick(double x, double y, String type) {
        switch (type.toLowerCase()) {
            case "normal":
                return new NormalBrick(x, y);
            case "strong":
                return new StrongBrick(x, y);
            case "explosive":
                return new ExplosiveBrick(x, y);
            case "quite":
                return new QuiteBrick(x, y);
            default:
                // fallback mặc định = NormalBrick
                return new NormalBrick(x, y);
        }
    }
}
