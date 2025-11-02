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
        import com.DevChickens.Arkanoid.utils.SoundManager;
        
        public class GameManager {
            private Paddle paddle;
            private List<Ball> balls;
            private List<Brick> bricks;
            private List<Bullet> bullets;
            private List<Explosion> explosions;
            private static GameManager instance;
        
            private SoundManager soundManager;
            private int score;
            private int lives;
            private GameState gameState;
            private int currentRound;
            private final int maxRounds = 5;
            private long nextRoundStartTime; // Thời điểm bắt đầu hiển thị "ROUND X"
            private final long ROUND_DISPLAY_DURATION = 2000; // 2 giây
            private boolean isGameInProgress = false;

            private long roundClearStartTime; // Thời điểm thắng màn
            private final long ROUND_CLEAR_DISPLAY_DURATION = 1000; // Chờ 1 giây

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
            private Rectangle continueButtonRect;
            private Rectangle highScoresButtonRect;
            private Rectangle exitButtonRect;
            private Rectangle backButtonRect;
            private Rectangle pauseButtonRect;

            private Rectangle pauseSettingsButtonRect;
            private Rectangle menuSettingsButtonRect;
        
            private Rectangle pauseContinueButton;
            private Rectangle pauseRestartButton;
            private Rectangle pauseExitButton;

            // BIẾN LƯU ÂM LƯỢNG
            private float volumeBGM = 0.2f;
            private float volumePaddle = 0.5f;
            private float volumeBrick = 0.5f;
            private float volumeWall = 0.5f;
            private float volumeExplosion = 0.5f;
            private float volumeGun = 0.5f;
            private float volumeClick = 0.5f;
            private float volumePowerUp = 0.5f;
            private float volumePause = 0.5f;

            // BIẾN TRẠNG THÁI UI SETTINGS
            private GameState previousGameState; // Để biết quay lại màn hình nào
            private enum SettingsPage { MAIN, SOUND, CONTROLS } // Phân cấp menu
            private SettingsPage currentSettingsPage = SettingsPage.MAIN;

            // BIẾN RECTANGLE CHO UI
            private Rectangle settingsSoundButtonRect;
            private Rectangle settingsBackRect;

            private Rectangle sliderBgmRect;
            private Rectangle sliderPaddleRect;
            private Rectangle sliderBrickRect;
            private Rectangle sliderWallRect;
            private Rectangle sliderExplosionRect;
            private Rectangle soundBackRect;

            private List<Integer> highScores;
            private static final String SCORE_FILE = "highscores.txt";
        
            // Kích thước khu vực chơi (Tường)
            public static final int GAME_WIDTH = 920;
            public static final int GAME_HEIGHT = 690;
        
            private GameManager() {
                renderer = new Renderer();
                playButtonRect = new Rectangle();
                continueButtonRect = new Rectangle();
                highScoresButtonRect = new Rectangle();
                exitButtonRect = new Rectangle();
                backButtonRect = new Rectangle();
                menuSettingsButtonRect = new Rectangle();
        
                int iconSize = 40;
                int padding = 10;
                pauseButtonRect = new Rectangle(GAME_WIDTH - iconSize - padding, padding, iconSize, iconSize);
        
                int buttonWidth = 250;
                int buttonHeight = 50;
                int centerX = GAME_WIDTH / 2 - (buttonWidth / 2);
                int gap = 20; // Khoảng cách giữa các nút
                int totalPauseButtonHeight = (buttonHeight * 4) + (gap * 3);
                int startY_Pause = GAME_HEIGHT / 2 - totalPauseButtonHeight / 2;

                pauseContinueButton = new Rectangle(centerX, startY_Pause, buttonWidth, buttonHeight);
                pauseRestartButton = new Rectangle(centerX, startY_Pause + (buttonHeight + gap), buttonWidth, buttonHeight);
                pauseSettingsButtonRect = new Rectangle(centerX, startY_Pause + (buttonHeight + gap) * 2, buttonWidth, buttonHeight);
                pauseExitButton = new Rectangle(centerX, startY_Pause + (buttonHeight + gap) * 3, buttonWidth, buttonHeight);

                // KHỞI TẠO RECT CHO SETTINGS
                int settingsBtnWidth = 300;
                int settingsBtnHeight = 60;
                int settingsCenterX = GAME_WIDTH / 2 - (settingsBtnWidth / 2);

                // Nút trong Main Settings
                settingsSoundButtonRect = new Rectangle(settingsCenterX, 250, settingsBtnWidth, settingsBtnHeight);
                settingsBackRect = new Rectangle(settingsCenterX, 300, settingsBtnWidth, settingsBtnHeight);

                // Nút trong Sound Settingsx`
                int sliderWidth = 400;
                int sliderHeight = 20;
                int sliderCenterX = GAME_WIDTH / 2 - (sliderWidth / 2);

                sliderBgmRect = new Rectangle(sliderCenterX, 150, sliderWidth, sliderHeight);
                sliderPaddleRect = new Rectangle(sliderCenterX, 200, sliderWidth, sliderHeight);
                sliderBrickRect = new Rectangle(sliderCenterX, 250, sliderWidth, sliderHeight);
                sliderWallRect = new Rectangle(sliderCenterX, 300, sliderWidth, sliderHeight);
                sliderExplosionRect = new Rectangle(sliderCenterX, 350, sliderWidth, sliderHeight);
                soundBackRect = new Rectangle(settingsCenterX, 450, settingsBtnWidth, settingsBtnHeight);

                soundManager = new SoundManager();
                try {
                    // Tải các âm thanh va chạm
                    soundManager.loadSound("paddle_hit", "sounds/paddle_hit.wav");
                    soundManager.loadSound("brick_explode", "sounds/brick_explode.wav");
                    soundManager.loadSound("wall_hit", "sounds/wall.wav");
                    soundManager.loadSound("bgm_menu", "sounds/ChillMenu.wav");
                    soundManager.loadSound("brick_hit", "sounds/brick.wav");
                    soundManager.loadSound("brick_crack", "sounds/brick_crack.wav");
                    soundManager.loadSound("gun_fire", "sounds/gun.wav");
                    soundManager.loadSound("ui_click", "sounds/click.wav");
                    soundManager.loadSound("powerup_apply", "sounds/powerup.wav");
                    soundManager.loadSound("powerup_collect", "sounds/powerup_collect.wav");
                    soundManager.loadSound("pause_in", "sounds/pause_in.wav");
                    soundManager.loadSound("pause_out", "sounds/pause_out.wav");
        
                } catch (Exception e) {
                    System.err.println("Không thể khởi tạo SoundManager!");
                    e.printStackTrace();
                }
        
                initGame();
            }

            public static GameManager getInstance() {
                if (instance == null) {
                    instance = new GameManager();
                }
                return instance;
            }

            public SoundManager getSoundManager() {
                return this.soundManager;
            }

            /**
             * Getter để các đối tượng khác như Ball có thể truy cập volumeWall
             */
            public float getVolumeWall() {
                return this.volumeWall;
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
                isGameInProgress = false;
            }
        
            private void initRound(int round) {
                paddle = new Paddle(GAME_WIDTH / 2.0 - 50, GAME_HEIGHT - 50, 0, 0, 10, null);
                balls = new ArrayList<>(); // Khởi tạo danh sách
                balls.add(new Ball(GAME_WIDTH / 2.0, GAME_HEIGHT - 70, 3, -3, 5 + round, 0.7071, -0.7071));
                bricks = new ArrayList<>();
                powerUps = new ArrayList<>();
                activePowerUps = new ArrayList<>();
                bullets = new ArrayList<>();
                explosions = new ArrayList<>();
                isBallLaunched = false;
                bricks = LevelLoader.createLevel(round, GAME_WIDTH);
                isMovingLeft = false;
                isMovingRight = false;

                //Nhạc chờ menu
                soundManager.loopSound("bgm_menu", volumeBGM);
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

                // Game sẽ đứng hình trong 1 giây
                if (gameState == GameState.ROUND_CLEAR) {
                    long now = System.currentTimeMillis();

                    if (now - roundClearStartTime >= ROUND_CLEAR_DISPLAY_DURATION) {

                        // Logic chuyển màn
                        if (currentRound < maxRounds) {
                            currentRound++;
                            initRound(currentRound); // Tải màn mới
                            gameState = GameState.NEXT_ROUND; // Chuyển sang "ROUND X"
                            nextRoundStartTime = System.currentTimeMillis();
                        } else {
                            addScore(this.score);
                            gameState = GameState.VICTORY; // Thắng game
                            isGameInProgress = false;
                        }
                    }
                    return; // Dừng update mọi thứ khác trong lúc chờ
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
                if (paddle.isGunPaddle() && isFiring) {
                    long now = System.currentTimeMillis();
        
                    // Kiểm tra thời gian và bắn đạn
                    if (now - lastFireTime >= FIRE_RATE_DELAY) {
                        // vị trí của súng.
                        final double GUN_OFFSET_X = paddle.getWidth() * 0.29;
                        final double BULLET_SPEED = 15;
                        final double BULLET_WIDTH = paddle.getWidth() * 0.05;
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
                        soundManager.playSound("gun_fire", volumeGun);
        
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
                        }
                        if (p instanceof GunPaddlePowerUp) {
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
                        b.update();
                    }
        
                    for (PowerUp p : powerUps) {
                        p.update();
                    }
        
                    for (Bullet bullet : bullets) {
                        bullet.update();
                    }

                    checkCollisions();

                    java.util.Iterator<Ball> ballIterator = balls.iterator();
                    while (ballIterator.hasNext()) {
                        Ball b = ballIterator.next();
                        // Kiếm tra update đã cho phép xóa bóng hay chưa.
                        if (b.isDestroyed()) { 
                            ballIterator.remove(); // Xóa bóng vì nó báo đã tự hủy
                        }
                    }
        
                    if (balls.isEmpty()) {
                        lives--;
                        if (lives <= 0) {
                            addScore(this.score);
                            gameState = GameState.GAME_OVER;
                            isGameInProgress = false;
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
                        gameState = GameState.ROUND_CLEAR; // Chuyển sang trạng thái đứng hình.
                        roundClearStartTime = System.currentTimeMillis();
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
             * Kiểm tra va chạm giữa các đối tượng
             */
            private void checkCollisions() {
                for (Ball ball : new ArrayList<>(balls)) {
                    // Ball vs Paddle
                    if (CollisionManager.checkCollision(ball, paddle)) {
                        if (ball.getDirectionY() > 0) {

                            ball.bounceOff(paddle, null); // Truyền null

                            ball.setY(paddle.getY() - ball.getHeight());
                            soundManager.playSound("paddle_hit", volumePaddle);
                        }
                    }
        
                    // Ball vs Bricks
                    for (int i = 0; i < bricks.size(); i++) {
                        Brick b = bricks.get(i);
                        if (!b.isDestroyed() && CollisionManager.checkCollision(ball, b)) {
                            if (ball.isSuperBall()) {
                                b.breakBrick();
                            } else {
                                b.takeHit();
                            }
        
                            // Đẩy lùi bóng một bước để thoát ra khỏi gạch (Fix Kẹt Brick)
                            // Lùi về vị trí trước khi va chạm
                            ball.setX(ball.getX() - ball.getSpeed() * ball.getDirectionX());
                            ball.setY(ball.getY() - ball.getSpeed() * ball.getDirectionY());

                            // Lấy hướng va chạm từ Manager và truyền cho bóng.
                            CollisionManager.CollisionSide side = CollisionManager.getCollisionDirection(ball, b);
                            ball.bounceOff(b, side);

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
                                    soundManager.playSound("brick_explode", volumeExplosion);
                                } else {
                                    // Gạch  thường
                                    soundManager.playSound("brick_hit", volumeBrick);
                                }
        
                                // Có thể rơi PowerUp ngẫu nhiên
                                double rand = Math.random(); // Lấy 1 số ngẫu nhiên
        
                                if (rand < 0.05) { // 5% cơ hội
                                    powerUps.add(new ExpandPaddlePowerUp(b.getX(), b.getY(), "EXPAND_PADDLE", 5000));
                                } else if (rand < 0.1) { // 5% cơ hội (tổng 10%)
                                    powerUps.add(new SuperBallPowerUp(b.getX(), b.getY(), "SUPER_BALL", 5000));
                                } else if (rand < 0.15) { // 5% cơ hội (tổng 15%)
                                    powerUps.add(new MultiBallPowerUp(b.getX(), b.getY(), "MULTI_BALL", 1000));
                                } else if (rand < 0.2) { // 5% cơ hội (tổng 20%)
                                    powerUps.add(new GunPaddlePowerUp(b.getX(), b.getY(), "GUN_PADDLE", 5000));
                                } else if (rand < 0.25) { // 5% cơ hội (tổng 25%)
                                    powerUps.add(new FastBallPowerUp(b.getX(), b.getY(), "FAST_BALL", 5000));
                                }
                            } else {
                                // Gạch crack
                                soundManager.playSound("brick_crack", volumeBrick);
                            }
                            break;
                        }
                    }
                }
        
                // Bullet vs Bricks
                java.util.Iterator<Bullet> bulletIterator = bullets.iterator();
                while (bulletIterator.hasNext()) {

                    Bullet currentBullet = bulletIterator.next();

                    // Kiểm tra xem update trước đó đã báo hủy đạn chưa
                    if (currentBullet.isDestroyed()) {
                        bulletIterator.remove();
                        continue; // Chuyển qua viên đạn tiếp theo
                    }
        
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
                                } else {
                                    // Gạch thường
                                    soundManager.playSound("brick_hit", volumeBrick);
                                }
                                // Có thể rơi PowerUp ngẫu nhiên
                                double rand = Math.random(); // Lấy 1 số ngẫu nhiên
        
                                if (rand < 0.05) { // 5% cơ hội
                                    powerUps.add(new ExpandPaddlePowerUp(b.getX(), b.getY(), "EXPAND_PADDLE", 5000));
                                } else if (rand < 0.1) { // 5% cơ hội (tổng 10%)
                                    powerUps.add(new SuperBallPowerUp(b.getX(), b.getY(), "SUPER_BALL", 5000));
                                } else if (rand < 0.15) { // 5% cơ hội (tổng 15%)
                                    powerUps.add(new MultiBallPowerUp(b.getX(), b.getY(), "MULTI_BALL", 1000));
                                } else if (rand < 0.2) { // 5% cơ hội (tổng 20%)
                                    powerUps.add(new GunPaddlePowerUp(b.getX(), b.getY(), "GUN_PADDLE", 5000));
                                } else if (rand < 0.25) { // 5% cơ hội (tổng 25%)
                                    powerUps.add(new FastBallPowerUp(b.getX(), b.getY(), "FAST_BALL", 5000));
                                }
                            } else {
                                // Gạch crack
                                soundManager.playSound("brick_crack", volumeBrick);
                            }
                            // loại bỏ đạn khi va chạm với gạch
                            bulletIterator.remove();
                            break;
                        }
                    }
                }
        
                // Paddle vs PowerUps
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
                        // Ăn powerup
                        soundManager.playSound("powerup_collect", volumePowerUp);
                        // Áp dụng powerup
                        soundManager.playSound("powerup_apply", volumePowerUp);

                        // Power-up chỉ ảnh hưởng đến Paddle (chạy 1 lần)
                        if (p instanceof ExpandPaddlePowerUp) {
                            // Ta truyền 'this' (manager) và 'null' (ball)
                            p.applyEffect(this, paddle, null);
                        } else if (p instanceof GunPaddlePowerUp) {
                            // Gunpaddle.
                            p.applyEffect(null, paddle, null);
                            onFire();
                        }
                        // Power-up ảnh hưởng đến tất cả bóng
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
                        0.7071, -0.7071
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
                    soundManager.playSound("pause_in", volumePause);
                    gameState = GameState.PAUSED;
                    soundManager.stopSound("bgm_menu"); // Dừng nhạc
                } else if (gameState == GameState.PAUSED) {
                    soundManager.playSound("pause_out", volumePause);
                    gameState = GameState.PLAYING;
                    soundManager.loopSound("bgm_menu", volumeBGM); // Phát lại nhạc
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
                if (gameState == GameState.PLAYING && paddle.isGunPaddle()) {
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

                if (gameState == GameState.PLAYING) {
                    if (pauseButtonRect.contains(x, y)) {
                        onPausePressed(); // Gọi hàm pause/resume có sẵn
                    }
                }
                else if (gameState == GameState.PAUSED) {
                    if (pauseButtonRect.contains(x, y)) {
                        onPausePressed();
                    } else if (pauseContinueButton.contains(x, y)) {
                        onPausePressed();
                    } else if (pauseRestartButton.contains(x, y)) {
                        soundManager.playSound("ui_click", volumeClick);
                        initRound(currentRound);
                        gameState = GameState.NEXT_ROUND;
                        nextRoundStartTime = System.currentTimeMillis();
                        isGameInProgress = true;
                    } else if (pauseSettingsButtonRect.contains(x, y)) {
                        soundManager.playSound("ui_click", volumeClick);
                        previousGameState = GameState.PAUSED;
                        currentSettingsPage = SettingsPage.MAIN;
                        gameState = GameState.SETTINGS;

                    } else if (pauseExitButton.contains(x, y)) {
                        soundManager.playSound("ui_click", volumeClick);
                        gameState = GameState.MENU;
                        soundManager.loopSound("bgm_menu", volumeBGM);
                    }
                }

                else if (gameState == GameState.MENU) {

                    if (isGameInProgress) {
                        // TH1: Nếu đang có game chơi dở thì hiện cả continue và new game.

                        if (continueButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            gameState = GameState.PAUSED;
                            soundManager.stopSound("bgm_menu");
                        }
                        else if (playButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            initGame();
                            gameState = GameState.NEXT_ROUND;
                            nextRoundStartTime = System.currentTimeMillis();
                            isGameInProgress = true; // Cần đặt lại cờ vì init game set về false.
                        }
                        else if (highScoresButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            soundManager.stopSound("bgm_menu");
                            this.highScores = loadScores();
                            gameState = GameState.HIGH_SCORES;
                        }
                        else if (menuSettingsButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            soundManager.stopSound("bgm_menu");
                            previousGameState = GameState.MENU;
                            currentSettingsPage = SettingsPage.MAIN;
                            gameState = GameState.SETTINGS;
                        }
                        else if (exitButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            System.exit(0);
                        }

                    } else {
                        // Trường hợp 2: Chơi mới nên chỉ hiện Play.

                        if (playButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            gameState = GameState.NEXT_ROUND;
                            nextRoundStartTime = System.currentTimeMillis();
                            isGameInProgress = true;
                        }
                        else if (highScoresButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            soundManager.stopSound("bgm_menu");
                            this.highScores = loadScores();
                            gameState = GameState.HIGH_SCORES;
                        }
                        else if (menuSettingsButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            soundManager.stopSound("bgm_menu");
                            previousGameState = GameState.MENU;
                            currentSettingsPage = SettingsPage.MAIN;
                            gameState = GameState.SETTINGS;
                        }
                        else if (exitButtonRect.contains(x, y)) {
                            soundManager.playSound("ui_click", volumeClick);
                            System.exit(0);
                        }
                    }
                }

                else if (gameState == GameState.HIGH_SCORES) {
                    if (backButtonRect.contains(x, y)) {
                        soundManager.playSound("ui_click", volumeClick);
                        gameState = GameState.MENU;
                        soundManager.loopSound("bgm_menu", volumeBGM); // phát lại nhạc
                    }
                }
                else if (gameState == GameState.GAME_OVER || gameState == GameState.VICTORY) {
                    soundManager.playSound("ui_click", volumeClick);
                    // Click bất kỳ để quay về Menu
                    initGame();
                }
                else if (gameState == GameState.SETTINGS) {
                    switch (currentSettingsPage) {
                        case MAIN:
                            if (settingsSoundButtonRect.contains(x, y)) {
                                soundManager.playSound("ui_click", volumeClick);
                                currentSettingsPage = SettingsPage.SOUND;
                            } else if (settingsBackRect.contains(x, y)) {
                                soundManager.playSound("ui_click", volumeClick);
                                gameState = previousGameState;

                                if (gameState == GameState.MENU) {
                                    soundManager.loopSound("bgm_menu", volumeBGM);
                                }
                            }
                            break;

                        case SOUND:
                            if (soundBackRect.contains(x, y)) {
                                soundManager.playSound("ui_click", volumeClick);
                                currentSettingsPage = SettingsPage.MAIN;
                            }
                            // Cho phép click để set volume
                            else if (sliderBgmRect.contains(x, y)) {
                                soundManager.playSound("ui_click", volumeClick);
                                volumeBGM = calculateVolumeFromSlider(x, sliderBgmRect);
                                soundManager.updateVolume("bgm_menu", volumeBGM);
                            } else if (sliderPaddleRect.contains(x, y)) {
                                soundManager.playSound("ui_click", volumeClick);
                                volumePaddle = calculateVolumeFromSlider(x, sliderPaddleRect);
                            } else if (sliderBrickRect.contains(x, y)) {
                                soundManager.playSound("ui_click", volumeClick);
                                volumeBrick = calculateVolumeFromSlider(x, sliderBrickRect);
                            } else if (sliderWallRect.contains(x, y)) {
                                soundManager.playSound("ui_click", volumeClick);
                                volumeWall = calculateVolumeFromSlider(x, sliderWallRect);
                            } else if (sliderExplosionRect.contains(x, y)) {
                                soundManager.playSound("ui_click", volumeClick);
                                volumeExplosion = calculateVolumeFromSlider(x, sliderExplosionRect);
                            }
                            break;

                        case CONTROLS:
                            // (Xử lý cho trang controls sau)
                            break;
                    }
                }
            }

            /**
             * Hàm được gọi từ MouseMotionListener của GamePanel.
             * Dùng để xử lý việc kéo thanh trượt âm lượng.
             * @param x Tọa độ x của chuột
             * @param y Tọa độ y của chuột
             */
            public void onMouseDrag(int x, int y) {
                // Chỉ xử lý kéo chuột khi ở đúng màn hình sound setting
                if (gameState != GameState.SETTINGS || currentSettingsPage != SettingsPage.SOUND) {
                    return;
                }

                // Cập nhật tọa độ chuột (giống onMouseMove)
                this.mouseX = x;
                this.mouseY = y;

                // Kiểm tra từng thanh trượt
                if (sliderBgmRect.contains(x, y)) {
                    volumeBGM = calculateVolumeFromSlider(x, sliderBgmRect);
                    soundManager.updateVolume("bgm_menu", volumeBGM);

                } else if (sliderPaddleRect.contains(x, y)) {
                    volumePaddle = calculateVolumeFromSlider(x, sliderPaddleRect);
                } else if (sliderBrickRect.contains(x, y)) {
                    volumeBrick = calculateVolumeFromSlider(x, sliderBrickRect);
                } else if (sliderWallRect.contains(x, y)) {
                    volumeWall = calculateVolumeFromSlider(x, sliderWallRect);
                } else if (sliderExplosionRect.contains(x, y)) {
                    volumeExplosion = calculateVolumeFromSlider(x, sliderExplosionRect);
                }
            }

            /**
             * Hàm hỗ trợ tính toán giá trị 0.0 - 1.0 từ vị trí chuột trên thanh trượt
             */
            private float calculateVolumeFromSlider(int mouseX, Rectangle sliderRect) {
                double relativeX = mouseX - sliderRect.getX();
                float volume = (float) (relativeX / sliderRect.getWidth());
                return Math.max(0.0f, Math.min(1.0f, volume)); // Kẹp giá trị trong [0.0, 1.0]
            }

            /**
             * Vẽ các đối tượng lên màn hình
             */
            public void draw(Graphics g) {
                switch (gameState) {
                    case MENU:
                        renderer.drawMenu(g, GAME_WIDTH, GAME_HEIGHT, mouseX, mouseY,
                                playButtonRect, highScoresButtonRect,
                                menuSettingsButtonRect, exitButtonRect,
                                isGameInProgress,
                                continueButtonRect);
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
                            renderer.drawPause(g, GAME_WIDTH, GAME_HEIGHT, mouseX, mouseY,
                                    pauseContinueButton,  pauseRestartButton, pauseSettingsButtonRect, pauseExitButton);
                        }
                        break;
                    // Copy y hệt nội dung của case PLAYING/PAUSED ở trên
                    case ROUND_CLEAR:
                        renderer.drawGameBackground(g, GAME_WIDTH, GAME_HEIGHT, currentRound);
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
                        g.setColor(Color.WHITE);
                        g.drawString("Score: " + score, 10, 20);
                        g.drawString("Lives: " + lives, 10, 40);
                        g.drawString("Round: " + currentRound + "/" + maxRounds, 10, 60);
                        renderer.drawPauseIcon(g, gameState, mouseX, mouseY, pauseButtonRect);
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
                    case SETTINGS:
                        // Vẽ UI dựa trên trang settings
                        switch (currentSettingsPage) {
                            case MAIN:
                                renderer.drawSettingsMain(g, GAME_WIDTH, GAME_HEIGHT, mouseX, mouseY,
                                        settingsSoundButtonRect, settingsBackRect);
                                break;
                            case SOUND:
                                renderer.drawSettingsSound(g, GAME_WIDTH, GAME_HEIGHT, mouseX, mouseY,
                                        sliderBgmRect, volumeBGM,
                                        sliderPaddleRect, volumePaddle,
                                        sliderBrickRect, volumeBrick,
                                        sliderWallRect, volumeWall,
                                        sliderExplosionRect, volumeExplosion,
                                        soundBackRect);
                                break;
                        }
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

            // Các hàm getter/setter để test.
            public GameState getGameState() {
                return this.gameState;
            }

            public void setGameState(GameState gameState) {
                this.gameState = gameState;
            }

            public int getLives() {
                return this.lives;
            }

            public void setLives(int lives) {
                this.lives = lives;
            }

            public int getScore() {
                return this.score;
            }

            public int getCurrentRound() {
                return this.currentRound;
            }

            public List<Ball> getBalls() {
                return this.balls;
            }

            public List<Brick> getBricks() {
                return this.bricks;
            }

            public boolean isBallLaunched() {
                return this.isBallLaunched;
            }

            public void setIsBallLaunched(boolean isBallLaunched) {
                this.isBallLaunched = isBallLaunched;
            }
        }