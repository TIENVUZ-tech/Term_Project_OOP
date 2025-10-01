package com.DevChickens.Arkanoid.graphics;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.core.GameState;

/**
 * Lớp chịu trách nhiệm hiển thị thông tin trò chơi Arkanoid.
 *
 */
public class Renderer {

    /**
     * Vẽ toàn bộ trạng thái game ra màn hình console.
     *
     * @param gm đối tượng GameManager chứa dữ liệu game.
     */
    public void render(GameManager gm) {
        System.out.println("===== GAME SCREEN =====");
        System.out.println("Score : " + gm.getScore());
        System.out.println("Lives : " + gm.getLives());
        System.out.println("Level : " + gm.getLevel());
        System.out.println("State : " + gm.getState());
        System.out.println("========================");
    }

    /**
     * Hiển thị một thông điệp đơn giản.
     *
     * @param msg nội dung cần in ra.
     */
    public void showMessage(String msg) {
        System.out.println("[INFO] " + msg);
    }

    /**
     * Hiển thị thông điệp đặc biệt khi game over.
     */
    public void showGameOver() {
        System.out.println("===== GAME OVER =====");
        System.out.println("Cảm ơn bạn đã chơi Arkanoid!");
    }
}
