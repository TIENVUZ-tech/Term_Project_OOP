package com.DevChickens.Arkanoid.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssetLoader {

    // Thêm các biến để lưu ảnh.
    public static BufferedImage MENU_BACKGROUND;
    public static List<BufferedImage> ROUND_BACKGROUNDS;
    public static final int MAX_ROUNDS = 5;

    public static BufferedImage PAUSE_ICON;
    public static BufferedImage PLAY_ICON;

    public static void loadAllAssets() {
        System.out.println("Bắt đầu tải tài nguyên (assets)...");

        MENU_BACKGROUND = loadImage("/images/background_menu_galaxy.png");

        System.out.println("Tải ảnh nền các round...");
        ROUND_BACKGROUNDS = new ArrayList<>();
        for (int i = 1; i <= MAX_ROUNDS; i++) {
            BufferedImage bg = loadImage("/images/round" + i + ".jpg");
            ROUND_BACKGROUNDS.add(bg);
        }

        PAUSE_ICON = loadImage("/images/pause_icon.png");
        PLAY_ICON = loadImage("/images/play_icon.png");

        System.out.println("Tải tài nguyên thành công!");
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(AssetLoader.class.getResource(path));
        } catch (Exception e) {
            System.err.println("Lỗi: không thể tải ảnh tại đường dẫn: " + path);
            return null;
        }
    }

    public static BufferedImage getRoundBackground(int round) {
        // Trừ 1 vì round 1 nằm ở index 0 của List
        int index = round - 1;

        if (index < 0 || index >= ROUND_BACKGROUNDS.size()) {
            return ROUND_BACKGROUNDS.get(0); // Trả về ảnh nền round 1
        }

        BufferedImage bg = ROUND_BACKGROUNDS.get(index);

        if (bg == null) {
            return ROUND_BACKGROUNDS.get(0); // Trả về ảnh round 1 dự phòng
        }

        return bg;
    }
}