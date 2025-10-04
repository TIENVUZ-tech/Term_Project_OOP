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
                5,
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
     * Kiểm tra va chạm giữa các đối tượng
     */
    private void checkCollisions() {
        // Ball vs Paddle
        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        // Ball vs Bricks
        for (int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            if (!b.isDestroyed() && ball.checkCollision(b)) {
                ball.bounceOff(b);
                b.takeHit();
                score += 100;

                // Nếu Brick vỡ thì có thể rơi PowerUp
                if (b.isDestroyed() && Math.random() < 0.2) {
                    powerUps.add(new ExpandPaddlePowerUp(b.getX(), b.getY()));
                }
            }
        }

        // Paddle vs PowerUps
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
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
                renderer.drawPaddle(g, paddle);
                renderer.drawBall(g, ball);

                for (Brick b : bricks) renderer.drawBrick(g, b);
                for (PowerUp p : powerUps) renderer.drawPowerUp(g, p);

                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 10, 20);
                g.drawString("Lives: " + lives, 10, 40);
                break;
            case PAUSED:
                renderer.drawPause(g, GAME_WIDTH, GAME_HEIGHT);
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
