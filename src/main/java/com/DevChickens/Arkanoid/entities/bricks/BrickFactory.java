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
    public static Brick createBrick(double x, double y, double width, double height, int hitPoints, String type) {
        switch (type.toLowerCase()) {
            case "normal":
                return new NormalBrick(x, y, width, height, hitPoints, type);
            case "strong":
                return new StrongBrick(x, y, width, height, hitPoints, type);
            case "explosive":
                return new ExplosiveBrick(x, y, width, height, hitPoints, type);
            case "quite":
                return new QuiteBrick(x, y, width, height, hitPoints, type);
            default:
                // fallback mặc định = NormalBrick
                return new NormalBrick(x, y, width, height, hitPoints, type);
        }
    }
}
