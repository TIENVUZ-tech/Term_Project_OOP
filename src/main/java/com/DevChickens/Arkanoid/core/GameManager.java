package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.graphics.Renderer;

/**
 * Quản lý trạng thái và tiến trình chính của trò chơi Arkanoid.
 * <p>
 * GameManager giữ các thông tin như điểm số, số mạng, màn chơi hiện tại và trạng thái trò chơi.
 * Nó cung cấp các phương thức để bắt đầu, cập nhật, tạm dừng, tiếp tục và reset game.
 * </p>
 *
 * Luồng trạng thái chính:
 * <pre>
 * START  -> RUNNING <-> PAUSED -> GAME_OVER
 * </pre>
 *
 */
public class GameManager {

    /** Điểm số hiện tại của người chơi. */
    private int score;

    /** Số mạng còn lại của người chơi. */
    private int lives;

    /** Màn chơi hiện tại. */
    private int level;

    /** Trạng thái hiện tại của trò chơi. */
    private GameState state;

    /**
     * Khởi tạo GameManager với giá trị mặc định:
     * <ul>
     *   <li>score = 0</li>
     *   <li>lives = 3</li>
     *   <li>level = 1</li>
     *   <li>state = START</li>
     * </ul>
     */
    public GameManager() {
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.state = GameState.START;
    }

    /**
     * Bắt đầu trò chơi từ trạng thái START.
     * Trạng thái sẽ chuyển sang RUNNING.
     */
    public void startGame() {
        this.state = GameState.RUNNING;
    }

    /**
     * Cập nhật logic chính của game.
     * <p>
     * Trong phiên bản demo: mỗi lần update giảm 1 mạng.
     * Nếu mạng = 0 thì game chuyển sang GAME_OVER.
     * </p>
     */
    public void update() {
        if (state == GameState.RUNNING) {
            lives--;
            if (lives <= 0) {
                state = GameState.GAME_OVER;
            }
        }
    }

    /**
     * Tạm dừng trò chơi nếu đang ở trạng thái RUNNING.
     */
    public void pauseGame() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        }
    }

    /**
     * Tiếp tục trò chơi nếu đang ở trạng thái PAUSED.
     */
    public void resumeGame() {
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /**
     * Reset lại trò chơi về trạng thái ban đầu.
     */
    public void reset() {
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.state = GameState.START;
    }

    /**
     * @return điểm số hiện tại.
     */
    public int getScore() {
        return score;
    }

    /**
     * Tăng điểm số hiện tại.
     *
     * @param points số điểm được cộng thêm.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * @return số mạng hiện tại.
     */
    public int getLives() {
        return lives;
    }

    /**
     * @return màn chơi hiện tại.
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return trạng thái hiện tại của trò chơi.
     */
    public GameState getState() {
        return state;
    }

    /**
     * Thiết lập trạng thái game thủ công (nếu cần).
     *
     * @param state trạng thái mới.
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * @return chuỗi mô tả trạng thái game để debug hoặc hiển thị.
     */
    @Override
    public String toString() {
        return String.format("GameManager{score=%d, lives=%d, level=%d, state=%s}",
                score, lives, level, state);
    }
}
