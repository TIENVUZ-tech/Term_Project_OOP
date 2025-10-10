package com.DevChickens.Arkanoid.entities.bricks;

public class BrickFactory {

    public static Brick createBrick(double x, double y, double width, double height, int hitPoints, String type) {
        return switch (type.toLowerCase()) {
            case "normal" -> new NormalBrick(x, y, width, height, hitPoints, "normal");
            case "strong" -> new StrongBrick(x, y, width, height, hitPoints, "strong");
            case "explosive" -> new ExplosiveBrick(x, y, width, height, hitPoints, "explosive");
            case "quite" -> new QuiteBrick(x, y, width, height, hitPoints, "quite");
            default -> new NormalBrick(x, y, width, height, hitPoints, "normal");
        };
    }
}