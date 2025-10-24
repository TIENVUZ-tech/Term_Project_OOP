package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.entities.bricks.Brick;
import com.DevChickens.Arkanoid.entities.bricks.BrickFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    /**
     * Tạo một danh sách các viên gạch bằng cách đọc file .txt
     */
    public static List<Brick> createLevel(int round, int gameWidth) {
        List<Brick> bricks = new ArrayList<>();

        String levelFile = "/levels/level" + round + ".txt";
        List<String> levelRows = new ArrayList<>();

        try (var in = LevelLoader.class.getResourceAsStream(levelFile)) {
            if (in == null) {
                System.err.println("Lỗi: Không thể tìm thấy file level: " + levelFile);
                return bricks;
            }

            var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                levelRows.add(line);
            }
        } catch (Exception e) {
            System.err.println("Không thể tải level: " + levelFile);
            e.printStackTrace();
            return bricks;
        }

        if (levelRows.isEmpty()) {
            System.err.println("File level rỗng: " + levelFile);
            return bricks;
        }

        int rows = levelRows.size();
        int cols = levelRows.get(0).length();

        final double BRICK_WIDTH = 85;
        final double PADDING = 2;
        final double TOP_OFFSET = 70;
        final double BRICK_HEIGHT = BRICK_WIDTH * (85.0 / 256.0);
        double totalGridWidth = (cols * BRICK_WIDTH) + ((cols - 1) * PADDING);
        double startX = (gameWidth - totalGridWidth) / 2;

        for (int row = 0; row < rows; row++) {
            String line = levelRows.get(row);

            int currentRowCols = line.length();

            for (int col = 0; col < cols; col++) {

                if (col >= currentRowCols) {
                    continue;
                }

                char typeChar = line.charAt(col);

                if (typeChar == '_' || typeChar == ' ') {
                    continue;
                }

                String type = switch (typeChar) {
                    case 'S' -> "strong";
                    case 'E' -> "explosive";
                    case 'Q' -> "quite";
                    case 'N' -> "normal";
                    default -> "normal";
                };

                int hitPoints = switch (type) {
                    case "quite" -> 2;
                    case "explosive" -> 2;
                    default -> 1;
                };

                double brickX = startX + col * (BRICK_WIDTH + PADDING);
                double brickY = TOP_OFFSET + row * (BRICK_HEIGHT + PADDING);

                bricks.add(BrickFactory.createBrick(
                        brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT, hitPoints, type
                ));
            }
        }
        return bricks;
    }
}