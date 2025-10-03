package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.bricks.Brick;
import com.DevChickens.Arkanoid.entities.bricks.NormalBrick;
import com.DevChickens.Arkanoid.entities.powerups.PowerUp;
import com.DevChickens.Arkanoid.enums.GameState;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;

    private int score;
    private int lives;
    private int level;   // ✅ thêm biến level
    private GameState gameState;

    public GameManager() {
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.level = 1;  // ✅ mặc định bắt đầu từ level 1
        this.gameState = GameState.STARTING;
    }

    /**
     * Khởi động lại game mới
     */
    public void startGame() {
        System.out.println("Game started! Level " + level);

        // Paddle ở giữa màn hình
        this.paddle = new Paddle(350, 550);
        this.ball = new Ball(390, 530);


        // Thêm vài viên gạch mẫu (số lượng có thể phụ thuộc level)
        bricks.clear();
        for (int i = 0; i < 5 + (level - 1) * 2; i++) { // mỗi level cao hơn thì nhiều gạch hơn
            bricks.add(new NormalBrick(100 + (i % 5) * 120, 100 + (i / 5) * 50, 100, 30));
        }

        this.gameState = GameState.RUNNING;
    }

    public void nextLevel() {
        level++;
        startGame();
    }

    public void updateGame() {
        if (gameState != GameState.RUNNING) return;

        // TODO: cập nhật vị trí ball, paddle
        System.out.println("Game updating...");
    }

    public void handleInput(String input) {
        // TODO: paddle.moveLeft(), moveRight()
        System.out.println("Handling input: " + input);
    }

    public void checkCollisions() {
        // TODO: xử lý ball đập vào paddle/brick
        System.out.println("Checking collisions...");
    }

    public void gameOver() {
        this.gameState = GameState.GAME_OVER;
        System.out.println("Game Over! Final score = " + score);
    }

    // Getter
    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public List<Brick> getBricks() { return bricks; }
    public List<PowerUp> getPowerUps() { return powerUps; }
    public GameState getState() { return gameState; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }   // ✅ thêm getter cho level
}
