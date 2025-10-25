package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.entities.*;
import com.DevChickens.Arkanoid.entities.bricks.*;
import com.DevChickens.Arkanoid.entities.powerups.*;
import com.DevChickens.Arkanoid.enums.GameState;
import com.DevChickens.Arkanoid.graphics.Renderer;
import com.DevChickens.Arkanoid.entities.effects.Explosion;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.stream.Collectors;

public class GameManager {
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<Bullet> bullets;
    private List<Explosion> explosions;

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
    private boolean isBallLaunched;

    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;

    // các thông số cho GunPaddle bắn đạn.
    private boolean isFiring = false; // flag
    private long lastFireTime = 0; // bộ đếm thời gian
    private final long FIRE_RATE_DELAY = 400; // 400ms giữa các lần bắn.

    private int mouseX, mouseY; // Tọa độ chuột
    private Rectangle playButtonRect;
    private Rectangle highScoresButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle backButtonRect;
    private Rectangle pauseButtonRect;

    private List<Integer> highScores;
    private static final String SCORE_FILE = "highscores.txt";

    // Kích thước khu vực chơi (Tường)
    public static final int GAME_WIDTH = 920;
    public static final int GAME_HEIGHT = 690;

    public GameManager() {
        renderer = new Renderer();
        playButtonRect = new Rectangle();
        highScoresButtonRect = new Rectangle();
        exitButtonRect = new Rectangle();
        backButtonRect = new Rectangle();

        int iconSize = 40;
        int padding = 10;
        pauseButtonRect = new Rectangle(GAME_WIDTH - iconSize - padding, padding, iconSize, iconSize);

        initGame();
    }

    public void addBall(Ball b) {
        if (this.balls != null) {
            this.balls.add(b);
        }
    }

    public void initGame() {
        score = 0;
        lives = 3;
        currentRound = 1;
        gameState = GameState.MENU;
        initRound(currentRound); // Khởi tạo round đầu tiên
        activePowerUps = new ArrayList<>();
        isMovingLeft = false;
        isMovingRight = false;
    }

    private void initRound(int round) {
        paddle = new Paddle(GAME_WIDTH / 2.0 - 50, GAME_HEIGHT - 50, 0, 0, 10, null);
        balls = new ArrayList<>(); // Khởi tạo danh sách
        balls.add(new Ball(GAME_WIDTH / 2.0, GAME_HEIGHT - 70, 3, -3, 3 + round, 1, -1));
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        activePowerUps = new ArrayList<>();
        bullets = new ArrayList<>();
        explosions = new ArrayList<>();
        isBallLaunched = false;
        bricks = LevelLoader.createLevel(round, GAME_WIDTH);
        isMovingLeft = false;
        isMovingRight = false;
    }

    public void update() {
        // Nếu chưa chơi thì không update
        if (gameState == GameState.MENU || gameState == GameState.GAME_OVER ||
                gameState == GameState.VICTORY || gameState == GameState.HIGH_SCORES) {
            return;
        }
        // Hiển thị "ROUND X" vài giây trước khi bắt đầu chơi
        if (gameState == GameState.NEXT_ROUND) {
            long now = System.currentTimeMillis();
            if (now - nextRoundStartTime >= ROUND_DISPLAY_DURATION) {
                gameState = GameState.PLAYING;
            }
            return; // Dừng update logic game trong lúc hiển thị round
        }

        if (gameState == GameState.PAUSED) return;
        if (gameState != GameState.PLAYING) return;

        if (isMovingLeft) {
            paddle.moveLeft();
        } else if (isMovingRight) {
            paddle.moveRight();
        }

        paddle.update();

        // Xử lý bắn đạn
        if (paddle.getIsGunPaddle() && isFiring) {
            long now = System.currentTimeMillis();

            // Kiểm tra thời gian và bắn đạn
            if (now - lastFireTime >= FIRE_RATE_DELAY) {
                // vị trí của súng.
                final double GUN_OFFSET_X = paddle.getWidth() * 0.1;
                final double BULLET_SPEED = 15;
                final double BULLET_WIDTH = paddle.getWidth() * 0.1;
                // vị trí bắt đầu bắn.
                double startY = paddle.getY();
                // ví trí súng trái
                double leftX = paddle.getX() + GUN_OFFSET_X;
                // vị trí súng phải
                double rightX = paddle.getX() + paddle.getWidth() - GUN_OFFSET_X - BULLET_WIDTH;

                // Khởi tạo các viên đạn
                Bullet b1 = new Bullet(leftX, startY, 0, 0, BULLET_SPEED);
                Bullet b2 = new Bullet(rightX, startY, 0, 0, BULLET_SPEED);

                bullets.add(b1);
                bullets.add(b2);

                // cập nhật lại thời gian bắn lần cuối.
                lastFireTime = now;
            }
        }

        java.util.Iterator<PowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp p = iterator.next();
            if (p.isExpired()) {
                for (Ball b : balls) {
                    p.removeEffect(paddle, b);
                    offFire();
                }
                iterator.remove();
            }
        }

        // UPDATE vụ nổ
        java.util.Iterator<Explosion> expIterator = explosions.iterator();
        while (expIterator.hasNext()) {
            Explosion exp = expIterator.next();
            exp.update();
            if (exp.isFinished()) {
                expIterator.remove(); // Xóa vụ nổ nếu đã chạy xong
            }
        }

        if (isBallLaunched) {
            for (Ball b : balls) {
                b.move();
            }

            for (PowerUp p : powerUps) {
                p.update();
            }

            for (Bullet bullet : bullets) {
                bullet.move();
            }

            checkWallCollisions();

            checkCollisions();

            java.util.Iterator<Ball> ballIterator = balls.iterator();
            while (ballIterator.hasNext()) {
                Ball b = ballIterator.next();
                if (b.getY() > GAME_HEIGHT) {
                    ballIterator.remove(); // Xóa quả bóng bị rơi
                }
            }

            // Logic loại bỏ đạn ra khỏi màn hình.
            java.util.Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet b = bulletIterator.next();
                if (b.getY() + b.getHeight() < 0) {
                    // xóa đạn nếu đạn đi khỏi cạnh trên.
                    bulletIterator.remove();
                }
            }


            if (balls.isEmpty()) {
                lives--;
                if (lives <= 0) {
                    addScore(this.score);
                    gameState = GameState.GAME_OVER;
                } else {
                    resetBallAndPaddle(); // Reset lại màn (tạo 1 bóng mới)
                }
            }

            // Kiểm tra thắng
            boolean allBreakableDestroyed = true;
            for (Brick b : bricks) {
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
                    addScore(this.score);
                    gameState = GameState.VICTORY;
                }
            }
        } else {
            if (!balls.isEmpty()) {
                Ball mainBall = balls.get(0);

                double ballNewX = paddle.getX() + (paddle.getWidth() / 2) - (mainBall.getWidth() / 2);

                double ballNewY = paddle.getY() - mainBall.getHeight();

                mainBall.setX(ballNewX);
                mainBall.setY(ballNewY);
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

                    if (b.isDestroyed()) {
                        int points = switch (b.getType().toLowerCase()) {
                            case "strong" -> 300;
                            case "explosive" -> 200;
                            case "quite" -> 150;
                            default -> 100;
                        };
                        score += points;

                        // Nếu viên gạch bị phá hủy là gạch nổ, kích hoạt vụ nổ
                        if (b instanceof ExplosiveBrick) {
                            double explosionX = b.getX() + b.getWidth() / 2.0;
                            double explosionY = b.getY() + b.getHeight() / 2.0;
                            explosions.add(new Explosion(explosionX, explosionY, b.getWidth() * 2, b.getHeight() * 2));
                            processExplosion(b);
                        }

                        // Có thể rơi PowerUp ngẫu nhiên
                        double rand = Math.random(); // Lấy 1 số ngẫu nhiên

                        if (rand < 0.1) { // 10% cơ hội
                            powerUps.add(new ExpandPaddlePowerUp(b.getX(), b.getY(), "EXPAND_PADDLE", 5000));
                        } else if (rand < 0.2) { // 10% cơ hội (tổng 20%)
                            powerUps.add(new SuperBallPowerUp(b.getX(), b.getY(), "SUPER_BALL", 5000));
                        } else if (rand < 0.3) { // 10% cơ hội (tổng 30%)
                            powerUps.add(new MultiBallPowerUp(b.getX(), b.getY(), "MULTI_BALL", 1000));
                        } else if (rand < 0.4) { // 10% cơ hội (tổng 40%)
                            powerUps.add(new GunPaddlePowerUp(b.getX(), b.getY(), "GUN_PADDLE", 5000));
                        }
                    }
                    break;
                }
            }
        }

        // Bullet vs Bricks (logic va chạm)
        java.util.Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet currentBullet = bulletIterator.next();

            for (int i = 0; i < bricks.size(); i++) {
                Brick b = bricks.get(i);

                if (!b.isDestroyed() && currentBullet.checkCollision(b)) {
                    b.takeHit(); // gây sát thương cho gạch

                    if (b.isDestroyed()) {
                        // cộng điểm
                        int points = switch (b.getType().toLowerCase()) {
                            case "strong" -> 300;
                            case "explosive" -> 200;
                            case "quite" -> 150;
                            default -> 100;
                        };
                        score += points;

                        if (b instanceof ExplosiveBrick) {
                            double explosionX = b.getX() + b.getWidth() / 2.0;
                            double explosionY = b.getY() + b.getHeight() / 2.0;
                            explosions.add(new Explosion(explosionX, explosionY, b.getWidth() * 2, b.getHeight() * 2));
                            processExplosion(b);
                        }
                        // Có thể rơi PowerUp ngẫu nhiên
                        double rand = Math.random(); // Lấy 1 số ngẫu nhiên

                        if (rand < 0.1) { // 10% cơ hội
                            powerUps.add(new ExpandPaddlePowerUp(b.getX(), b.getY(), "EXPAND_PADDLE", 5000));
                        } else if (rand < 0.2) { // 10% cơ hội (tổng 20%)
                            powerUps.add(new SuperBallPowerUp(b.getX(), b.getY(), "SUPER_BALL", 5000));
                        } else if (rand < 0.3) { // 10% cơ hội (tổng 30%)
                            powerUps.add(new MultiBallPowerUp(b.getX(), b.getY(), "MULTI_BALL", 1000));
                        } else if (rand < 0.4) { // 10% cơ hội (tổng 40%)
                            powerUps.add(new GunPaddlePowerUp(b.getX(), b.getY(), "GUN_PADDLE", 5000));
                        }
                    }
                    // loại bỏ đạn khi va chạm với gạch
                    bulletIterator.remove();
                    break;
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
                } else if (p instanceof GunPaddlePowerUp) {
                    // Loại 2: Gunpaddle.
                    p.applyEffect(null, paddle, null);
                    onFire();
                }
                // Loại 3: Power-up ảnh hưởng đến TẤT CẢ bóng
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
     * Xử lý một vụ nổ, bắt đầu từ một viên gạch.
     * @param initialBrick Viên gạch nổ ban đầu (vừa bị bóng phá hủy).
     */
    private void processExplosion(Brick initialBrick) {
        // Hàng đợi để xử lý nổ dây chuyền (BFS)
        Queue<Brick> explosionQueue = new LinkedList<>();
        // Set để đảm bảo mỗi viên gạch nổ chỉ nổ 1 lần
        Set<Brick> alreadyExploded = new HashSet<>();

        // bắt đầu với viên gạch gốc
        explosionQueue.add(initialBrick);
        alreadyExploded.add(initialBrick);

        while (!explosionQueue.isEmpty()) {
            Brick currentExplosion = explosionQueue.poll();

            // Lấy thông số của viên gạch đang nổ
            double brickWidth = currentExplosion.getWidth();
            double brickHeight = currentExplosion.getHeight();
            double centerX = currentExplosion.getX() + brickWidth / 2.0;
            double centerY = currentExplosion.getY() + brickHeight / 2.0;

            // Bán kính nổ 1 ô xung quanh mỗi bên
            // phạm vi là ô vuông cạch 3 viên, viên nổ là trung tâm
            double radiusX = brickWidth * 1.5;
            double radiusY = brickHeight * 1.5;

            // Tính toán vùng ảnh hưởng
            double explosionLeft = centerX - radiusX;
            double explosionRight = centerX + radiusX;
            double explosionTop = centerY - radiusY;
            double explosionBottom = centerY + radiusY;

            // Duyệt qua tất cả các viên gạch trong màn chơi
            for (Brick neighbor : bricks) {
                // Bỏ qua nếu gạch đã bị phá hủy
                if (neighbor.isDestroyed()) {
                    continue;
                }
                // Bỏ qua nếu là chính viên gạch đang nổ
                if (neighbor == currentExplosion) {
                    continue;
                }
                // Lấy phạm vi của gạch lân cận
                double otherLeft = neighbor.getX();
                double otherRight = neighbor.getX() + neighbor.getWidth();
                double otherTop = neighbor.getY();
                double otherBottom = neighbor.getY() + neighbor.getHeight();

                // Kiểm tra xem gạch lân cận có nằm trong vùng nổ không (AABB collision)
                boolean overlaps = (otherLeft < explosionRight &&
                        otherRight > explosionLeft &&
                        otherTop < explosionBottom &&
                        otherBottom > explosionTop);

                if (overlaps) {
                    // Gây sát thương (trừ 1 máu)
                    neighbor.takeHit();

                    // Nếu gạch lân cận này bị phá hủy bởi vụ nổ
                    if (neighbor.isDestroyed()) {

                        // Cộng điểm cho gạch bị phá hủy bởi vụ nổ
                        int points = switch (neighbor.getType().toLowerCase()) {
                            case "strong" -> 300;
                            case "explosive" -> 200;
                            case "quite" -> 150;
                            default -> 100;
                        };
                        score += points;

                        // nêu là gạch nổ và ch có trong hàng đợi
                        if (neighbor instanceof ExplosiveBrick &&
                                !alreadyExploded.contains(neighbor))
                        {
                            // Thêm nó vào hàng đợi để xử lý nổ tiếp
                            explosionQueue.add(neighbor);
                            alreadyExploded.add(neighbor);
                        }
                    }
                }
            }
        }
    }

    /**
     * Reset bóng và paddle khi mất mạng.
     */
    private void resetBallAndPaddle() {
        paddle = new Paddle(
                GAME_WIDTH / 2.0 - 50,
                GAME_HEIGHT - 50,
                0, 0, 10, null
        );

        balls.clear();
        balls.add(new Ball(
                GAME_WIDTH / 2.0,
                GAME_HEIGHT - 70,
                3, -3,
                5 + currentRound,
                1, -1
        ));

        isBallLaunched = false;
        isMovingLeft = false;
        isMovingRight = false;

        offFire();
    }

    public void onConfirmPressed() {
        switch (gameState) {
            case GAME_OVER:
            case VICTORY:
                initGame();
                break;
        }
    }

    public void onPausePressed() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
        } else if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING;
        }
    }

    public void onLaunchPressed() {
        if (gameState == GameState.PLAYING && !isBallLaunched) {
            isBallLaunched = true;
        }
    }

    public void onMoveLeftPressed() {
        if (gameState == GameState.PLAYING) {
            isMovingLeft = true;

        }
    }

    public void onMoveRightPressed() {
        if (gameState == GameState.PLAYING) {
            isMovingRight = true;
        }
    }

    public void onMoveLeftReleased() {
        isMovingLeft = false;
    }

    public void onMoveRightReleased() {
        isMovingRight = false;
    }

    // Cho phép bắn đạn
    public void onFire() {
        if (gameState == GameState.PLAYING && paddle.getIsGunPaddle()) {
            isFiring = true;
        }
    }

    // Không cho phép bắn đạn
    public void offFire() {
        isFiring = false;
    }

    public void onMouseMove(int x, int y) {
        // Luôn cập nhật tọa độ chuột
        this.mouseX = x;
        this.mouseY = y;
    }

    public void onMouseClick(int x, int y) {
        // Lọc sự kiện click dựa trên TRẠNG THÁI GAME

        if (gameState == GameState.PLAYING || gameState == GameState.PAUSED) {
            if (pauseButtonRect.contains(x, y)) {
                onPausePressed(); // Gọi hàm pause/resume có sẵn
            }
        }
        else if (gameState == GameState.MENU) {
            if (playButtonRect.contains(x, y)) {
                gameState = GameState.PLAYING;
            } else if (highScoresButtonRect.contains(x, y)) {
                gameState = GameState.HIGH_SCORES;
            } else if (exitButtonRect.contains(x, y)) {
                System.exit(0);
            }
        }
        else if (gameState == GameState.HIGH_SCORES) {
            if (backButtonRect.contains(x, y)) {
                gameState = GameState.MENU;
            }
        }
    }

    /**
     * Vẽ các đối tượng lên màn hình
     */
    public void draw(Graphics g) {
        switch (gameState) {
            case MENU:
                renderer.drawMenu(g, GAME_WIDTH, GAME_HEIGHT, mouseX, mouseY,
                        playButtonRect, highScoresButtonRect, exitButtonRect);
                break;
            case PLAYING:
            case PAUSED:
                renderer.drawGameBackground(g, GAME_WIDTH, GAME_HEIGHT, currentRound);

                // VẼ GAME OBJECTS
                renderer.drawPaddle(g, paddle);
                for (Ball b : balls) {
                    renderer.drawBall(g, b);
                }

                for (Bullet b : bullets) {
                    renderer.drawBullet(g, b);
                }

                for (Brick b : bricks) renderer.drawBrick(g, b);
                for (PowerUp p : powerUps) renderer.drawPowerUp(g, p);
                for (Explosion exp : explosions) {
                    renderer.drawExplosion(g, exp);
                }

                // VẼ UI
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 10, 20);
                g.drawString("Lives: " + lives, 10, 40);
                g.drawString("Round: " + currentRound + "/" + maxRounds, 10, 60);

                renderer.drawPauseIcon(g, gameState, mouseX, mouseY, pauseButtonRect);

                // VẼ MÀN HÌNH PAUSED
                if (gameState == GameState.PAUSED) {
                    renderer.drawPause(g, GAME_WIDTH, GAME_HEIGHT);
                }
                break;
            case HIGH_SCORES:
                renderer.drawHighScores(g, GAME_WIDTH, GAME_HEIGHT, mouseX, mouseY,
                        backButtonRect, highScores);
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

    private List<Integer> loadScores() {
        try {
            return Files.lines(Paths.get(SCORE_FILE))
                    .map(s -> s.replaceAll("\\s", "")) // Xóa khoảng trắng
                    .filter(s -> !s.isEmpty()) // Bỏ qua dòng trống
                    .map(Integer::parseInt)
                    .sorted(Collections.reverseOrder())
                    .limit(10) // Chỉ lấy Top 10
                    .collect(Collectors.toList());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Chưa có file điểm. Đang tạo mới...");
            return new ArrayList<>();
        }
    }

    private void addScore(int newScore) {
        if (newScore <= 0) return; // Không lưu điểm 0

        try {
            // Ghi điểm mới vào cuối file, mỗi điểm một dòng
            Files.write(Paths.get(SCORE_FILE),
                    (newScore + "\n").getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            // Sau khi thêm, tải lại danh sách để nó được cập nhật
            this.highScores = loadScores();

        } catch (IOException e) {
            System.err.println("Lỗi khi lưu điểm: " + e.getMessage());
        }
    }
}