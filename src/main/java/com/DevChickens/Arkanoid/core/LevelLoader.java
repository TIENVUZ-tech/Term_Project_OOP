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
     * @param round Round của game (ví dụ: 1, 2, 3...)
     * @param gameWidth Chiều rộng của khu vực chơi, dùng để căn giữa
     * @return Một List<Brick> đã được sắp xếp
     */
    public static List<Brick> createLevel(int round, int gameWidth) {
        List<Brick> bricks = new ArrayList<>();

        // 1. Tải file "level1.txt", "level2.txt",...
        String levelFile = "/levels/level" + round + ".txt";
        List<String> levelRows = new ArrayList<>();

        try (var in = LevelLoader.class.getResourceAsStream(levelFile)) {
            if (in == null) {
                // Nếu không tìm thấy file, in lỗi và trả về level rỗng
                System.err.println("Lỗi nghiêm trọng: Không thể tìm thấy file level: " + levelFile);
                return bricks;
            }

            // Đọc file với mã hóa UTF-8
            var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                levelRows.add(line);
            }
        } catch (Exception e) {
            System.err.println("Không thể tải level: " + levelFile);
            e.printStackTrace();
            return bricks; // Trả về level rỗng
        }

        if (levelRows.isEmpty()) {
            System.err.println("File level rỗng: " + levelFile);
            return bricks;
        }

        // 2. Tính toán kích thước (giống code cũ của bạn)
        int rows = levelRows.size();
        int cols = levelRows.get(0).length(); // Lấy độ dài hàng đầu tiên

        final double BRICK_WIDTH = 85;
        final double PADDING = 2;
        final double TOP_OFFSET = 70;
        final double BRICK_HEIGHT = BRICK_WIDTH * (85.0 / 256.0);
        double totalGridWidth = (cols * BRICK_WIDTH) + ((cols - 1) * PADDING);
        double startX = (gameWidth - totalGridWidth) / 2;

        // 3. Lặp qua bản đồ (map) thay vì lặp ngẫu nhiên
        for (int row = 0; row < rows; row++) {
            String line = levelRows.get(row);

            // Đảm bảo tất cả các hàng có cùng độ dài với hàng đầu tiên
            int currentRowCols = line.length();

            for (int col = 0; col < cols; col++) {

                // Nếu hàng này ngắn hơn hàng đầu tiên, coi như là ô trống
                if (col >= currentRowCols) {
                    continue;
                }

                char typeChar = line.charAt(col);

                // Nếu là '_' hoặc ' ' (ô trống), bỏ qua
                if (typeChar == '_' || typeChar == ' ') {
                    continue;
                }

                // Chuyển ký tự thành "type"
                String type = switch (typeChar) {
                    case 'S' -> "strong";
                    case 'E' -> "explosive";
                    case 'Q' -> "quite";
                    case 'N' -> "normal";
                    default -> "normal"; // Mặc định là gạch thường nếu ký tự lạ
                };

                // Lấy hitPoints dựa trên type (giống code cũ của bạn)
                int hitPoints = switch (type) {
                    case "quite" -> 2;
                    // (Bạn nên thêm 'case "strong" -> 3;' hoặc gì đó ở đây)
                    // Nếu không, 'strong' sẽ chỉ có 1 hitpoint
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