package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.entities.*;
import com.DevChickens.Arkanoid.entities.bricks.*;
import com.DevChickens.Arkanoid.entities.powerups.*;
import com.DevChickens.Arkanoid.enums.GameState;
import com.DevChickens.Arkanoid.graphics.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;

    private int score;
    private int lives;
    private GameState gameState;
    private int currentRound;
    private final int maxRounds = 5;
    private long nextRoundStartTime; // Thời điểm bắt đầu hiển thị "ROUND X"
    private final long ROUND_DISPLAY_DURATION = 2000; // 2 giây

    private List<PowerUp> powerUps; // Power-up đang rơi
    private List<PowerUp> activePowerUps; // Power-up đang có hiệu lực
    private Renderer renderer;

    // Kích thước khu vực chơi (Tường)
    public static final int GAME_WIDTH = 920;
    public static final int GAME_HEIGHT = 690;

    public GameManager() {
        renderer = new Renderer();
        initGame();
    }

    public void addBall(Ball b) {
        if (this.balls != null) {
            this.balls.add(b);
        }
    }

    /**
     * Khởi tạo game mới
     */
    public void initGame() {
        score = 0;
        lives = 3;
        currentRound = 1; // Bắt đầu từ round 1
        gameState = GameState.MENU;
        initRound(currentRound); // Khởi tạo round đầu tiên
        activePowerUps = new ArrayList<>();
    }

    private void initRound(int round) {
        paddle = new Paddle(GAME_WIDTH / 2.0 - 50, GAME_HEIGHT - 50, 0, 0, 35, null);
        balls = new ArrayList<>(); // Khởi tạo danh sách
        balls.add(new Ball(GAME_WIDTH / 2.0, GAME_HEIGHT - 70, 3, -3, 5 + round, 1, -1));
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        activePowerUps = new ArrayList<>();
        Random random = new Random();

        // Số hàng và cột tăng dần theo round
        int rows = 3 + round;       // round 1: 4 hàng, round 5: 8 hàng
        int cols = 8 + (round / 2); // tăng dần

        // chiều rộng cố định mong muốn cho viên gạch
        final double BRICK_WIDTH = 85;
        final double PADDING = 2; // Khoảng cách cho hàng và cột
        final double TOP_OFFSET = 70; // khoảng cách từ lề trên xuống

        // Chiều cao tạm thời, giá trị thật sẽ được tính trong mỗi class gạch
        final double BRICK_HEIGHT = BRICK_WIDTH * (85.0 / 256.0);

        // Tính toán tổng chiều rộng của toàn bộ lưới gạch
        // Tổng chiều rộng = (số cột * chiều rộng gạch) + (số khoảng trống ở giữa * padding)
        double totalGridWidth = (cols * BRICK_WIDTH) + ((cols - 1) * PADDING);

        // Căn giữa toàn bộ lưới gạch bằng cách tính vị trí X bắt đầu
        double startX = (GAME_WIDTH - totalGridWidth) / 2;

        // Độ khó: càng cao càng có nhiều gạch “khó”
        double strongChance = 0.1 * round;
        double explosiveChance = 0.05 * round;
        double quiteChance = 0.15 * round;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String type;
                double r = random.nextDouble();
                if (r < explosiveChance) type = "explosive";
                else if (r < explosiveChance + strongChance) type = "strong";
                else if (r < explosiveChance + strongChance + quiteChance) type = "quite";
                else type = "normal";

                int hitPoints = switch (type) {
                    case "strong" -> 3;
                    case "explosive" -> 1;
                    case "quite" -> 2;
                    default -> 1;
                };
                // Sử dụng các biến PADDING đã tính toán ở trên để đặt vị trí
                double brickX = startX + col * (BRICK_WIDTH + PADDING);
                double brickY = TOP_OFFSET + row * (BRICK_HEIGHT + PADDING);

                bricks.add(BrickFactory.createBrick(
                        brickX,
                        brickY,
                        BRICK_WIDTH,  // Truyền chiều rộng mới
                        BRICK_HEIGHT, // Truyền chiều cao mới
                        hitPoints,
                        type
                ));
            }
        }
    }

    /**
     * Cập nhật logic game
     */
    public void update() {
        // Nếu chưa chơi thì không update
        if (gameState == GameState.MENU || gameState == GameState.GAME_OVER || gameState == GameState.VICTORY) return;

        // Hiển thị "ROUND X" vài giây trước khi bắt đầu chơi
        if (gameState == GameState.NEXT_ROUND) {
            long now = System.currentTimeMillis();
            if (now - nextRoundStartTime >= ROUND_DISPLAY_DURATION) {
                gameState = GameState.PLAYING;
            }
            return; // Dừng update logic game trong lúc hiển thị round
        }

        if (gameState != GameState.PLAYING) return;

        for (Ball b : balls ) {
            b.move();
        }
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
        java.util.Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball b = ballIterator.next();
            if (b.getY() > GAME_HEIGHT) {
                ballIterator.remove(); // Xóa quả bóng bị rơi
            }
        }

        // Nếu KHÔNG CÒN bóng nào trên màn hình -> mất mạng
        if (balls.isEmpty()) {
            lives--;
            if (lives <= 0) {
                gameState = GameState.GAME_OVER;
            } else {
                resetBallAndPaddle(); // Reset lại màn (tạo 1 bóng mới)
            }
        }

        // Kiểm tra thắng
        boolean allBreakableDestroyed = true;
        for (Brick b : bricks) {
            // Bỏ qua gạch không thể phá (StrongBrick)
            if (b instanceof StrongBrick) {
                continue;
            }

            // Nếu còn gạch bình thường chưa vỡ → chưa thắng
            if (!b.isDestroyed()) {
                allBreakableDestroyed = false;
                break;
            }
        }

        if (allBreakableDestroyed) {
            if (currentRound < maxRounds) {
                currentRound++;
                initRound(currentRound);
                gameState = GameState.NEXT_ROUND;
                nextRoundStartTime = System.currentTimeMillis();
            } else {
                gameState = GameState.VICTORY;
            }
        }

        // Kiểm tra và gỡ bỏ Power-up hết hạn
        java.util.Iterator<PowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp p = iterator.next();
            // Giả sử bạn có phương thức isExpired() trong PowerUp để kiểm tra
            if (p.isExpired()) {
                for (Ball b : balls) {
                    p.removeEffect(paddle, b);
                }
                iterator.remove();
            }
        }
    }

    /**
     * Xử lý va chạm giữa bóng và các cạnh của khung trò chơi (Tường).
     */
    private void checkWallCollisions() {
        for (Ball ball : balls) {
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
    }

    /**
     * Kiểm tra va chạm giữa các đối tượng
     */
    private void checkCollisions() {
        for (Ball ball : new ArrayList<>(balls)) {
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
                    if (ball.getIsSuperBall()) {
                        b.breakBrick();
                    } else {
                        b.takeHit();
                    }

                    // Đẩy lùi bóng một bước để thoát ra khỏi gạch (Fix Kẹt Brick)
                    // Lùi về vị trí trước khi va chạm
                    ball.setX(ball.getX() - ball.getSpeed() * ball.getDirectionX());
                    ball.setY(ball.getY() - ball.getSpeed() * ball.getDirectionY());

                    // Va chạm và Bật ngược (sử dụng logic bounceOff trong Ball.java)
                    ball.bounceOff(b);

                    b.takeHit();

                    if (b.isDestroyed()) {
                        int points = switch (b.getType().toLowerCase()) {
                            case "strong" -> 300;
                            case "explosive" -> 200;
                            case "quite" -> 150;
                            default -> 100;
                        };
                        score += points;

                        // Có thể rơi PowerUp ngẫu nhiên
                        double rand = Math.random(); // Lấy 1 số ngẫu nhiên

                        if (rand < 0.1) { // 10% cơ hội
                            powerUps.add(new ExpandPaddlePowerUp(b.getX(), b.getY(), "EXPAND_PADDLE", 5000));
                        } else if (rand < 0.2) { // 10% cơ hội (tổng 20%)
                            powerUps.add(new SuperBallPowerUp(b.getX(), b.getY(), "SUPER_BALL", 5000));
                        } else if (rand < 0.3) { // 10% cơ hội (tổng 30%)
                            powerUps.add(new MultiBallPowerUp(b.getX(), b.getY(), "MULTI_BALL", 1000));
                        }
                    }

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

            // Khi Paddle va chạm PowerUp
            if (p.checkCollision(paddle)) {

                // Loại 1: Power-up chỉ ảnh hưởng đến Paddle (chạy 1 lần)
                if (p instanceof ExpandPaddlePowerUp) {
                    // Ta truyền 'this' (manager) và 'null' (ball)
                    p.applyEffect(this, paddle, null);
                }

                // Loại 2: Power-up ảnh hưởng đến TẤT CẢ bóng
                else if (p instanceof SuperBallPowerUp ||
                        p instanceof FastBallPowerUp ||
                        p instanceof MultiBallPowerUp) {

                    // Lặp qua bản sao của danh sách
                    for (Ball b : new ArrayList<>(balls)) {
                        p.applyEffect(this, paddle, b);
                    }
                }

                p.activate();
                activePowerUps.add(p);
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
                GAME_WIDTH / 2.0,
                GAME_HEIGHT - 50,
                0, 0, 35, null
        );

        // Thiết lập lại vị trí và hướng/tốc độ ban đầu cho bóng
        balls.clear();
        balls.add(new Ball(
                GAME_WIDTH / 2.0,
                GAME_HEIGHT - 70,
                3, -3, 5,
                1, -1
        ));
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
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

                // VẼ GAME OBJECTS
                renderer.drawPaddle(g, paddle);
                for (Ball b : balls) {
                    renderer.drawBall(g, b);
                }

                for (Brick b : bricks) renderer.drawBrick(g, b);
                for (PowerUp p : powerUps) renderer.drawPowerUp(g, p);

                // VẼ UI
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 10, 20);
                g.drawString("Lives: " + lives, 10, 40);
                g.drawString("Round: " + currentRound + "/" + maxRounds, 10, 60);


                // VẼ MÀN HÌNH PAUSED
                if (gameState == GameState.PAUSED) {
                    renderer.drawPause(g, GAME_WIDTH, GAME_HEIGHT);
                }
                break;
            case NEXT_ROUND:
                renderer.drawNextRound(g, GAME_WIDTH, GAME_HEIGHT, currentRound);
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