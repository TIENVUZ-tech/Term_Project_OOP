package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.core.UIManager;
import com.DevChickens.Arkanoid.input.InputHandler;

import javax.swing.*;
import java.awt.*;

/**
 * GamePanel là "trái tim" của toàn bộ trò chơi.
 * <p>
 * Kế thừa từ {@link JPanel}, nó chịu trách nhiệm:
 * 1. Khởi tạo và "kết nối" các hệ thống cốt lõi (GameManager, UIManager, InputHandler).
 * 2. Chạy vòng lặp game (game loop) trên một luồng (Thread) riêng biệt.
 * 3. Nhận lệnh vẽ (draw) từ GameManager và hiển thị chúng lên màn hình.
 *
 * @author Tuấn (DKCTuan)
 */
public class GamePanel extends JPanel implements Runnable {

    private final GameManager gameManager;
    private final UIManager uiManager;
    private final InputHandler inputHandler;
    private volatile boolean running;

    /**
     * Hàm khởi tạo của GamePanel.
     */
    public GamePanel() {
        // Khởi tạo các hệ thống.
        this.gameManager = GameManager.getInstance();
        this.inputHandler = new InputHandler();
        this.uiManager = new UIManager(this.gameManager, this.inputHandler);

        // Đăng ký hander với gameManager.
        this.gameManager.registerInputHandler(this.inputHandler);
        this.gameManager.registerUIManager(this.uiManager);

        // Cài đặt Panel lập kích thước mong muốn, GameWindow sẽ dùng
        // hàm pack() để tự động điều chỉnh cửa sổ theo kích thước này.
        setPreferredSize(new Dimension(GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT));
        setBackground(Color.BLACK);

        // Đánh dấu panel này có thể nhận sự chú ý, là bắt buộc.
        setFocusable(true);

        // Thêm listener vào Panel để nói với nó là InputHandler sẽ chịu trách nhiệm
        // xử lý tất cả các sự kiện đầu vào của nó.
        addKeyListener(inputHandler);
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);

        startGameLoop();
    }

    /**
     * Khởi tạo và bắt đầu luồng (Thread) chính của game.
     */
    private void startGameLoop() {
        running = true;
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Phương thức {@link Runnable#run()}, đây là "trái tim" của vòng lặp game (Game Loop).
     * <p>
     * Vòng lặp này sử dụng kiến trúc Fixed-Step Update (Cập nhật logic theo bước cố định)
     * và Variable-Step Draw (Vẽ theo tốc độ thay đổi).
     * <p>
     */
    @Override
    public void run() {
        // Thiết lập mục tiêu là 60 Tick (cập nhật logic) mỗi giây.
        final double TicksPerSecond = 60.0;

        // Tính toán xem mỗi tick logic cần bao nhiêu nano giây.
        final double nsPerTick = 1000000000.0 / TicksPerSecond;

        long lastTime = System.nanoTime();

        // delta là một bộ đếm, tích lũy thời gian trôi qua.
        // Khi delta >= 1.0, nghĩa là đã đủ thời gian để chạy 1 tick logic.
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            // Cộng dồn thời gian đã trôi qua vào delta
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            // Cập nhật logic Fixed-Step
            // Vòng lặp này đảm bảo logic update luôn chạy 60 lần / giây.
            // Nếu bị chậm có thể gọi 2 lần update liên tục.
            while (delta >= 1) {
                gameManager.update();
                delta--;
            }

            // Vẽ Variable-Step
            // Yêu cầu Swing vẽ lại = cách gọi hàm paintComponent.
            repaint();
        }
    }

    /**
     * Ghi đè phương thức vẽ cốt lõi của {@link JPanel}.
     * Được gọi bởi hệ thống Swing (thông qua `repaint()`).
     *
     * @param g Đối tượng Graphics do Swing cung cấp để vẽ lên.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Luôn gọi đầu tiên để Jpanel ưu tiên dọn dẹp background.
        super.paintComponent(g);
        // Anh Manager sẽ lo phần vẽ.
        gameManager.draw(g);
    }
}