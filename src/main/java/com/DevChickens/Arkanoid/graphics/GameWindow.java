package com.DevChickens.Arkanoid.graphics;

import javax.swing.*;

/**
 * Lớp GameWindow là cửa sổ chính của ứng dụng, kế thừa từ {@link JFrame}.
 * <p>
 * Trách nhiệm duy nhất của lớp này là tạo và cấu hình cửa sổ (frame)
 * để chứa {@link GamePanel} - nơi toàn bộ trò chơi diễn ra.
 * <p>
 *
 * @author Tuấn (DKCTuan)
 */
public class GameWindow extends JFrame {

    /**
     * Hàm khởi tạo của GameWindow.
     */
    public GameWindow() {

        setTitle("Arkanoid - OOP Project");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Không cho phép người dùng thay đổi kích thước cửa sổ
        setResizable(false);

        // Khởi tạo GamePanel.
        // GamePanel sẽ tự khởi tạo GameManager, UIManager, và InputHandler
        // ở bên trong hàm khởi tạo của nó.
        GamePanel gamePanel = new GamePanel();

        // Thêm panel chứa game vào nội dung của JFrame
        add(gamePanel);

        // pack() là một hàm tự động điều chỉnh kích thước của JFrame
        // sao cho vừa khít với Kích thước Ưu tiên (Preferred Size)
        // mà GamePanel đã định nghĩa (GAME_WIDTH, GAME_HEIGHT).
        pack();

        // Đặt cửa sổ ra chính giữa màn hình
        setLocationRelativeTo(null);

        // Hiển thị cửa sổ
        setVisible(true);

        // Yêu cầu GamePanel nhận sự chú ý ngay lập tức.
        gamePanel.requestFocusInWindow();
    }
}