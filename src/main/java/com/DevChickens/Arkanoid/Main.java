package com.DevChickens.Arkanoid;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.enums.GameState;
import com.DevChickens.Arkanoid.graphics.Renderer;

/**
 * Điểm khởi chạy chính của trò chơi Arkanoid.
 * <p>
 * Trong phiên bản demo, luồng chạy đơn giản:
 * START -> RUNNING -> PAUSED -> RUNNING -> GAME_OVER.
 * </p>
 *
 */
public class Main {

    public static void main(String[] args) {
        GameManager gm = new GameManager();
        Renderer renderer = new Renderer();

        // Hiển thị trạng thái ban đầu (START)
        renderer.render(gm);

        // Bắt đầu game
        gm.startGame();
        renderer.showMessage("Game started!");
        renderer.render(gm);

        // Giả lập update vài lần
        for (int i = 0; i < 2; i++) {
            gm.update();
            renderer.render(gm);
        }

        // Tạm dừng game
        gm.pauseGame();
        renderer.showMessage("Game paused!");
        renderer.render(gm);

        // Tiếp tục game
        gm.resumeGame();
        renderer.showMessage("Game resumed!");
        renderer.render(gm);

        // Giả lập update cho tới khi GAME_OVER
        while (gm.getState() != GameState.GAME_OVER) {
            gm.update();
            renderer.render(gm);
        }

        // Hiện Game Over
        renderer.showGameOver();
    }
}
