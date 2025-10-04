package com.DevChickens.Arkanoid.graphics;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Lớp tiện ích (utility class) dùng để tải các tài nguyên cho game, ví dụ như hình ảnh.
 * Tất cả các phương thức trong lớp này đều là 'static' để có thể gọi trực tiếp
 * mà không cần tạo đối tượng của lớp: AssetLoader.loadImage(...)
 */
public class AssetLoader {

    public static BufferedImage loadImage(String path) {
        try {
            // ImageIO.read() đọc file ảnh.
            // AssetLoader.class.getResource(path) tìm và lấy file từ trong thư mục resources.
            return ImageIO.read(AssetLoader.class.getResource(path));
        } catch (IOException e) {
            // Nếu có lỗi (ví dụ: file không tồn tại, sai đường dẫn), in ra lỗi và thoát.
            System.err.println("Lỗi: không thể tải ảnh tại đường dẫn: " + path);
            e.printStackTrace();
            System.exit(1); // Thoát game vì không thể tải tài nguyên cần thiết.
        }
        return null;
    }
}
