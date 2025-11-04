package com.DevChickens.Arkanoid;

import javax.swing.*;
import com.DevChickens.Arkanoid.graphics.AssetLoader;
import com.DevChickens.Arkanoid.graphics.GameWindow;

/**
 * Lớp Main chứa điểm khởi chạy (entry point) chính của trò chơi Arkanoid.
 * <p>
 * Trách nhiệm duy nhất của lớp này là tải các tài nguyên game ban đầu
 * và khởi tạo cửa sổ chính của game trên luồng Swing Event Dispatch Thread (EDT).
 *
 * @author Tuấn (DKCTuan)
 */
public class Main {

    /**
     * Phương thức main, điểm khởi đầu của ứng dụng.
     *
     * @param args Các tham số dòng lệnh (không được sử dụng trong game này).
     */
    public static void main(String[] args) {

        // 1. Tải tài nguyên (hình ảnh, âm thanh)
        // Phải thực hiện việc này ĐẦU TIÊN, trước khi bất kỳ
        // đối tượng game nào được tạo, để đảm bảo không bị lỗi null.
        AssetLoader.loadAllAssets();

        // 2. Khởi tạo và hiển thị cửa sổ game (GameWindow)
        // Luôn phải khởi tạo các thành phần UI của Swing bên trong
        // luồng Event Dispatch Thread (EDT) để đảm bảo an toàn luồng (thread-safety).
        SwingUtilities.invokeLater(GameWindow::new);

        /*
         * Ghi chú về "GameWindow::new":
         * Đây là một "Method Reference" trong Java 8.
         * Nó báo cho Swing: "Hãy lấy hàm khởi tạo (new) của GameWindow
         * và chạy nó trên luồng UI."
         */
    }
}