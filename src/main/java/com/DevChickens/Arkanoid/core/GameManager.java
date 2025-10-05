package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.entities.*;
import com.DevChickens.Arkanoid.entities.bricks.*;
import com.DevChickens.Arkanoid.entities.powerups.*;
import com.DevChickens.Arkanoid.enums.GameState;
import com.DevChickens.Arkanoid.graphics.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;

    private int score;
    private int lives;
    private GameState gameState;

    private Renderer renderer;

    // Kích thước khu vực chơi (Tường)
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    public GameManager() {
        renderer = new Renderer();
        initGame();
    }

    /**
     * Khởi tạo game mới
     */
    public void initGame() {
        score = 0;
        lives = 3;
        gameState = GameState.MENU;

        // Paddle: (x, y, width, height, dx, dy, speed, currentPowerUp)
        paddle = new Paddle(
                GAME_WIDTH / 2.0 - 50,
                GAME_HEIGHT - 50,
                100,
                15,
                0,
                0,
                20,
                null
        );

        // Ball: (x, y, width, height, dx, dy, speed, directionX, directionY)
        ball = new Ball(
                GAME_WIDTH / 2.0,
                GAME_HEIGHT - 70,
                15,
                15,
                3,
                -3,
                5,
                1,
                -1
        );

        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();

        // Tạo lưới gạch bằng BrickFactory
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                int hitPoints = (row < 2) ? 1 : 2;
                String type = (hitPoints == 1) ? "normal" : "strong";

                bricks.add(BrickFactory.createBrick(
                        60 + col * 70,
                        50 + row * 30,
                        64, 20, hitPoints,
                        type
                ));
            }
        }
    }

    /**
     * Cập nhật logic game
     */
    public void update() {
        if (gameState != GameState.PLAYING) return;

        ball.move();
        paddle.update();

        // PowerUps rơi xuống
        for (PowerUp p : powerUps) {
            p.update();
        }

        // Kiểm tra va chạm TƯỜNG
        checkWallCollisions();

        // Kiểm tra va chạm giữa các vật thể (Paddle, Bricks, PowerUps)
        checkCollisions();

        // Nếu bóng rơi ra ngoài
        if (ball.getY() > GAME_HEIGHT) {
            lives--;
            if (lives <= 0) {
                gameState = GameState.GAME_OVER;
            } else {
                resetBallAndPaddle();
            }
        }

        // Kiểm tra thắng (mọi brick bị phá)
        boolean allDestroyed = true;
        for (Brick b : bricks) {
            if (!b.isDestroyed()) {
                allDestroyed = false;
                break;
            }
        }
        if (allDestroyed) {
            gameState = GameState.VICTORY;
        }
    }

    /**
     * Xử lý va chạm giữa bóng và các cạnh của khung trò chơi (Tường).
     */
    private void checkWallCollisions() {
        // Va chạm Cạnh Trái
        if (ball.getX() < 0) {
            ball.setX(0); // Đẩy bóng về sát lề
            ball.setDirectionX(1); // Luôn đảo hướng sang phải
        }

        // Va chạm Cạnh Phải
        if (ball.getX() + ball.getWidth() > GAME_WIDTH) {
            ball.setX(GAME_WIDTH - ball.getWidth()); // Đẩy bóng về sát lề
            ball.setDirectionX(-1); // Luôn đảo hướng sang trái
        }

        // Va chạm Cạnh Trên
        if (ball.getY() < 0) {
            ball.setY(0); // Đẩy bóng về sát lề
            ball.setDirectionY(1); // Luôn đảo hướng xuống
        }
    }

    /**
     * Kiểm tra va chạm giữa các đối tượng
     */
    private void checkCollisions() {
        // --- Ball vs Paddle (Logic Chống Kẹt) ---
        if (ball.checkCollision(paddle)) {
            // Đảm bảo bóng luôn bật lên khi va chạm với Paddle
            if (ball.getDirectionY() > 0) { // Nếu bóng đang đi xuống
                ball.setDirectionY(-1);      // Đảo hướng lên
            }
            
            // Đẩy bóng ra khỏi Paddle ngay lập tức để ngăn bị kẹt
            ball.setY(paddle.getY() - ball.getHeight());
            
            // Tính góc bật thông minh dựa trên vị trí va chạm
            double relativeIntersectX = (ball.getX() + (ball.getWidth() / 2.0)) - (paddle.getX() + (paddle.getWidth() / 2.0));
            // normalizedIntersectX: Giá trị từ -1.0 (cực trái) đến 1.0 (cực phải)
            double normalizedIntersectX = relativeIntersectX / (paddle.getWidth() / 2.0); 

            // Thiết lập directionX để bóng đi theo góc, tránh đi thẳng lên
            if (normalizedIntersectX < -0.1) {
                ball.setDirectionX(-1); // Bật sang trái
            } else if (normalizedIntersectX > 0.1) {
                ball.setDirectionX(1); // Bật sang phải
            } 
            // Nếu va chạm gần tâm (-0.1 đến 0.1), directionX giữ nguyên
        }

        // --- Ball vs Bricks (Logic Chống Kẹt) ---
        for (int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            if (!b.isDestroyed() && ball.checkCollision(b)) {
                
                // Đẩy lùi bóng một bước để thoát ra khỏi gạch (Fix Kẹt Brick)
                // Lùi về vị trí trước khi va chạm
                ball.setX(ball.getX() - ball.getSpeed() * ball.getDirectionX());
                ball.setY(ball.getY() - ball.getSpeed() * ball.getDirectionY());

                // Va chạm và Bật ngược (sử dụng logic bounceOff trong Ball.java)
                ball.bounceOff(b); 
                
                b.takeHit();
                score += 100;

                // Nếu Brick vỡ thì có thể rơi PowerUp
                if (b.isDestroyed() && Math.random() < 0.2) {
                    powerUps.add(new ExpandPaddlePowerUp(b.getX(), b.getY()));
                }
            }
        }

        // --- Paddle vs PowerUps ---
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            // Loại bỏ PowerUp nếu nó rơi ra ngoài màn hình
            if (p.getY() > GAME_HEIGHT) {
                powerUps.remove(i);
                i--;
                continue;
            }
            
            if (p.checkCollision(paddle)) {
                p.applyEffect(paddle, ball);
                powerUps.remove(i);
                i--;
            }
        }
    }


    /**
     * Reset bóng và paddle khi mất mạng
     */
    private void resetBallAndPaddle() {
        paddle = new Paddle(
                GAME_WIDTH / 2.0 - 50,
                GAME_HEIGHT - 50,
                100,
                15,
                0, 0, 5, null
        );

        // Thiết lập lại vị trí và hướng/tốc độ ban đầu cho bóng
        ball = new Ball(
                GAME_WIDTH / 2.0,
                GAME_HEIGHT - 70,
                15,
                15,
                3,
                -3,
                5,
                1,
                -1
        );
        gameState = GameState.PAUSED;
    }

    /**
     * Nhận input từ bàn phím
     */
    public void handleInput(int keyCode) {
        switch (gameState) {
            case MENU:
                if (keyCode == java.awt.event.KeyEvent.VK_ENTER)
                    gameState = GameState.PLAYING;
                break;

            case PLAYING:
                if (keyCode == java.awt.event.KeyEvent.VK_LEFT)
                    paddle.moveLeft();
                if (keyCode == java.awt.event.KeyEvent.VK_RIGHT)
                    paddle.moveRight();
                if (keyCode == java.awt.event.KeyEvent.VK_P)
                    gameState = GameState.PAUSED;
                break;

            case PAUSED:
                if (keyCode == java.awt.event.KeyEvent.VK_P)
                    gameState = GameState.PLAYING;
                break;

            case GAME_OVER:
            case VICTORY:
                if (keyCode == java.awt.event.KeyEvent.VK_ENTER)
                    initGame();
                break;
        }
    }

    /**
     * Vẽ các đối tượng lên màn hình
     */
    public void draw(Graphics g) {
        switch (gameState) {
            case MENU:
                renderer.drawMenu(g, GAME_WIDTH, GAME_HEIGHT);
                break;
            case PLAYING:
            case PAUSED:
                // VẼ GAME OBJECTS
                renderer.drawPaddle(g, paddle);
                renderer.drawBall(g, ball);

                for (Brick b : bricks) renderer.drawBrick(g, b);
                for (PowerUp p : powerUps) renderer.drawPowerUp(g, p);

                // VẼ UI
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 10, 20);
                g.drawString("Lives: " + lives, 10, 40);
                
                // VẼ MÀN HÌNH PAUSED
                if (gameState == GameState.PAUSED) {
                    renderer.drawPause(g, GAME_WIDTH, GAME_HEIGHT);
                }
                break;
            case GAME_OVER:
                renderer.drawGameOver(g, GAME_WIDTH, GAME_HEIGHT, score);
                break;
            case VICTORY:
                renderer.drawVictory(g, GAME_WIDTH, GAME_HEIGHT, score);
                break;
        }
    }
}