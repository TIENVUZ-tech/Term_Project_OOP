package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.entities.bricks.Brick;
import com.DevChickens.Arkanoid.entities.bricks.BrickFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelLoader {

    /**
     * Tạo một danh sách các viên gạch dựa trên round hiện tại.
     * @param round Round của game (ví dụ: 1, 2, 3...)
     * @param gameWidth Chiều rộng của khu vực chơi, dùng để căn giữa
     * @return Một List<Brick> đã được sắp xếp
     */
    public static List<Brick> createLevel(int round, int gameWidth) {
        List<Brick> bricks = new ArrayList<>();
        Random random = new Random();

        int rows = 3 + round;
        int cols = 8 + (round / 2);

        final double BRICK_WIDTH = 85;
        final double PADDING = 2;
        final double TOP_OFFSET = 70;

        final double BRICK_HEIGHT = BRICK_WIDTH * (85.0 / 256.0);

        double totalGridWidth = (cols * BRICK_WIDTH) + ((cols - 1) * PADDING);

        double startX = (gameWidth - totalGridWidth) / 2;

        double strongChance = 0.1 * round;
        double explosiveChance = 0.05 * round;
        double quiteChance = 0.15 * round;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String type;
                double r = random.nextDouble();
                if (r < explosiveChance) type = "explosive";
                else if (r < explosiveChance + strongChance) type = "strong";
                else if (r < explosiveChance + strongChance + quiteChance) type = "quite";
                else type = "normal";

                int hitPoints = switch (type) {
                    case "strong" -> 3;
                    case "explosive" -> 1;
                    case "quite" -> 2;
                    default -> 1;
                };
                // Sử dụng các biến PADDING đã tính toán ở trên để đặt vị trí
                double brickX = startX + col * (BRICK_WIDTH + PADDING);
                double brickY = TOP_OFFSET + row * (BRICK_HEIGHT + PADDING);

                bricks.add(BrickFactory.createBrick(
                        brickX,
                        brickY,
                        BRICK_WIDTH,  // Truyền chiều rộng mới
                        BRICK_HEIGHT, // Truyền chiều cao mới
                        hitPoints,
                        type
                ));
            }
        }

        return bricks;
    }
}
