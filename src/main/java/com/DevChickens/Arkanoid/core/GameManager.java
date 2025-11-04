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
import com.DevChickens.Arkanoid.input.InputHandler;
import com.DevChickens.Arkanoid.utils.SoundManager;

public class GameManager {
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<Bullet> bullets;
    private List<Explosion> explosions;
    private static GameManager instance;
    private InputHandler inputHandler;
    private UIManager uiManager;

    private SoundManager soundManager;
    private int score;
    private int lives;
    private GameState gameState;
    private int currentRound;
    private final int maxRounds = 5;
    private long nextRoundStartTime;
    private boolean isGameInProgress = false;

    private long roundClearStartTime;
    private final long ROUND_CLEAR_DISPLAY_DURATION = 1000;

    private List<PowerUp> powerUps;
    private List<PowerUp> activePowerUps;
    private Renderer renderer;
    private boolean isBallLaunched;

    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;

    private boolean isFiring = false;
    private long lastFireTime = 0;
    private final long FIRE_RATE_DELAY = 400;

    // BIẾN LƯU ÂM LƯỢNG (Vẫn do GameManager quản lý)
    private float volumeBGM = 0.2f;
    private float volumePaddle = 0.5f;
    private float volumeBrick = 0.5f;
    private float volumeWall = 0.5f;
    private float volumeExplosion = 0.5f;
    private float volumeGun = 0.5f;
    private float volumeClick = 0.5f;
    private float volumePowerUp = 0.5f;
    private float volumePause = 0.5f;

    private List<Integer> highScores;
    private static final String SCORE_FILE = "highscores.txt";

    public static final int GAME_WIDTH = 920;
    public static final int GAME_HEIGHT = 690;

    private GameManager() {
        renderer = new Renderer();
        soundManager = new SoundManager();
        try {
            // Tải âm thanh
            soundManager.loadSound("paddle_hit", "sounds/paddle_hit.wav");
            soundManager.loadSound("brick_explode", "sounds/brick_explode.wav");
            soundManager.loadSound("wall_hit", "sounds/paddle_hit.wav");
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

    // Hàm đăng ký để gamePanel gọi.
    public void registerInputHandler(InputHandler handler) {
        this.inputHandler = handler;
    }

    public void registerUIManager(UIManager manager) {
        this.uiManager = manager;
    }


    public SoundManager getSoundManager() {
        return this.soundManager;
    }

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
        initRound(currentRound);
        activePowerUps = new ArrayList<>();
        isMovingLeft = false;
        isMovingRight = false;
        isGameInProgress = false;
    }

    private void initRound(int round) {
        paddle = new Paddle(GAME_WIDTH / 2.0 - 50, GAME_HEIGHT - 50, 0, 0, 10, null);
        balls = new ArrayList<>();
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
        isFiring = false;

        //Nhạc chờ menu
        if (soundManager != null) {
            soundManager.loopSound("bgm_menu", volumeBGM);
        }
    }

    public void update() {
        if (inputHandler == null || uiManager == null) {
            return;
        }
        // Xử lý Input Gameplay (phím bấm)
        processGameplayInput();
        // Xử lý Input UI (chuột)
        uiManager.processUIInput();

        // (Phần logic game giữ nguyên)
        if (gameState == GameState.MENU || gameState == GameState.GAME_OVER ||
                gameState == GameState.VICTORY || gameState == GameState.HIGH_SCORES) {
            return;
        }
        if (gameState == GameState.NEXT_ROUND) {
            long now = System.currentTimeMillis();
            long ROUND_DISPLAY_DURATION = 2000;
            if (now - nextRoundStartTime >= ROUND_DISPLAY_DURATION) {
                gameState = GameState.PLAYING;
            }
            return;
        }
        if (gameState == GameState.ROUND_CLEAR) {
            long now = System.currentTimeMillis();
            if (now - roundClearStartTime >= ROUND_CLEAR_DISPLAY_DURATION) {
                if (currentRound < maxRounds) {
                    currentRound++;
                    initRound(currentRound);
                    gameState = GameState.NEXT_ROUND;
                    nextRoundStartTime = System.currentTimeMillis();
                } else {
                    addScore(this.score);
                    gameState = GameState.VICTORY;
                    isGameInProgress = false;
                }
            }
            return;
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
            if (now - lastFireTime >= FIRE_RATE_DELAY) {
                final double GUN_OFFSET_X = paddle.getWidth() * 0.29;
                final double BULLET_SPEED = 15;
                final double BULLET_WIDTH = paddle.getWidth() * 0.05;
                double startY = paddle.getY();
                double leftX = paddle.getX() + GUN_OFFSET_X;
                double rightX = paddle.getX() + paddle.getWidth() - GUN_OFFSET_X - BULLET_WIDTH;

                Bullet b1 = new Bullet(leftX, startY, 0, 0, BULLET_SPEED);
                Bullet b2 = new Bullet(rightX, startY, 0, 0, BULLET_SPEED);

                bullets.add(b1);
                bullets.add(b2);
                soundManager.playSound("gun_fire", volumeGun);
                lastFireTime = now;
            }
        }

        // Cập nhật powerup đang kích hoạt
        java.util.Iterator<PowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp p = iterator.next();
            if (p.isExpired()) {
                for (Ball b : balls) {
                    p.removeEffect(paddle, b);
                }
                if (p instanceof GunPaddlePowerUp) {
                    isFiring = false; // SỬA 2 LỖI (offFire())
                }
                iterator.remove();
            }
        }

        // Cập nhật vụ nổ
        java.util.Iterator<Explosion> expIterator = explosions.iterator();
        while (expIterator.hasNext()) {
            Explosion exp = expIterator.next();
            exp.update();
            if (exp.isFinished()) {
                expIterator.remove();
            }
        }

        if (isBallLaunched) {
            for (Ball b : balls) {
                double oldDirX = b.getDirectionX();
                double oldDirY = b.getDirectionY();
                b.update();
                double newDirX = b.getDirectionX();
                double newDirY = b.getDirectionY();
                if (oldDirX != newDirX || oldDirY != newDirY) {
                    soundManager.playSound("wall_hit", volumeWall);
                }
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
                if (b.isDestroyed()) {
                    ballIterator.remove();
                }
            }

            if (balls.isEmpty()) {
                lives--;
                if (lives <= 0) {
                    addScore(this.score);
                    gameState = GameState.GAME_OVER;
                    isGameInProgress = false;
                } else {
                    resetBallAndPaddle();
                }
            }

            // Kiểm tra thắng
            boolean allBreakableDestroyed = true;
            for (Brick b : bricks) {
                if (b instanceof StrongBrick) {
                    continue;
                }
                if (!b.isDestroyed()) {
                    allBreakableDestroyed = false;
                    break;
                }
            }
            if (allBreakableDestroyed) {
                gameState = GameState.ROUND_CLEAR;
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

    private void checkCollisions() {
        for (Ball ball : new ArrayList<>(balls)) {
            // Ball vs Paddle
            if (CollisionManager.checkCollision(ball, paddle)) {
                if (ball.getDirectionY() > 0) {
                    ball.bounceOff(paddle, null);
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

                    ball.setX(ball.getX() - ball.getSpeed() * ball.getDirectionX());
                    ball.setY(ball.getY() - ball.getSpeed() * ball.getDirectionY());

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

                        if (b instanceof ExplosiveBrick) {
                            double explosionX = b.getX() + b.getWidth() / 2.0;
                            double explosionY = b.getY() + b.getHeight() / 2.0;
                            explosions.add(new Explosion(explosionX, explosionY, b.getWidth() * 2, b.getHeight() * 2));
                            processExplosion(b);
                            soundManager.playSound("brick_explode", volumeExplosion);
                        } else {
                            soundManager.playSound("brick_hit", volumeBrick);
                        }

                        PowerUp newPowerUp = PowerUpFactory.createRandomPowerUp(b.getX(), b.getY());
                        if (newPowerUp != null) {
                            powerUps.add(newPowerUp);
                        }
                    } else {
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
            if (currentBullet.isDestroyed()) {
                bulletIterator.remove();
                continue;
            }
            for (int i = 0; i < bricks.size(); i++) {
                Brick b = bricks.get(i);
                if (!b.isDestroyed() && currentBullet.checkCollision(b)) {
                    b.takeHit();
                    if (b.isDestroyed()) {
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
                            soundManager.playSound("brick_hit", volumeBrick);
                        }
                        PowerUp newPowerUp = PowerUpFactory.createRandomPowerUp(b.getX(), b.getY());
                        if (newPowerUp != null) {
                            powerUps.add(newPowerUp);
                        }
                    } else {
                        soundManager.playSound("brick_crack", volumeBrick);
                    }
                    bulletIterator.remove();
                    break;
                }
            }
        }

        // Paddle vs PowerUps
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            if (p.getY() > GAME_HEIGHT) {
                powerUps.remove(i);
                i--;
                continue;
            }

            if (p.checkCollision(paddle)) {
                soundManager.playSound("powerup_collect", volumePowerUp);
                soundManager.playSound("powerup_apply", volumePowerUp);

                if (p instanceof ExpandPaddlePowerUp) {
                    p.applyEffect(this, paddle, null);
                } else if (p instanceof GunPaddlePowerUp) {
                    p.applyEffect(null, paddle, null);
                    isFiring = true;
                }
                else if (p instanceof SuperBallPowerUp ||
                        p instanceof FastBallPowerUp ||
                        p instanceof MultiBallPowerUp) {
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

    private void processExplosion(Brick initialBrick) {
        Queue<Brick> explosionQueue = new LinkedList<>();
        Set<Brick> alreadyExploded = new HashSet<>();
        explosionQueue.add(initialBrick);
        alreadyExploded.add(initialBrick);

        while (!explosionQueue.isEmpty()) {
            Brick currentExplosion = explosionQueue.poll();
            double brickWidth = currentExplosion.getWidth();
            double brickHeight = currentExplosion.getHeight();
            double centerX = currentExplosion.getX() + brickWidth / 2.0;
            double centerY = currentExplosion.getY() + brickHeight / 2.0;
            double radiusX = brickWidth * 1.5;
            double radiusY = brickHeight * 1.5;
            double explosionLeft = centerX - radiusX;
            double explosionRight = centerX + radiusX;
            double explosionTop = centerY - radiusY;
            double explosionBottom = centerY + radiusY;

            for (Brick neighbor : bricks) {
                if (neighbor.isDestroyed() || neighbor == currentExplosion) {
                    continue;
                }
                double otherLeft = neighbor.getX();
                double otherRight = neighbor.getX() + neighbor.getWidth();
                double otherTop = neighbor.getY();
                double otherBottom = neighbor.getY() + neighbor.getHeight();

                boolean overlaps = (otherLeft < explosionRight &&
                        otherRight > explosionLeft &&
                        otherTop < explosionBottom &&
                        otherBottom > explosionTop);

                if (overlaps) {
                    neighbor.takeHit();
                    if (neighbor.isDestroyed()) {
                        int points = switch (neighbor.getType().toLowerCase()) {
                            case "strong" -> 300;
                            case "explosive" -> 200;
                            case "quite" -> 150;
                            default -> 100;
                        };
                        score += points;

                        if (neighbor instanceof ExplosiveBrick &&
                                !alreadyExploded.contains(neighbor)) {
                            explosionQueue.add(neighbor);
                            alreadyExploded.add(neighbor);
                        }
                    }
                }
            }
        }
    }

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
        isFiring = false;
    }

    // --- CÁC HÀM API SẠCH (ĐÃ CÓ SẴN) ---
    public void pauseGame() {
        if (gameState == GameState.PLAYING) {
            soundManager.playSound("pause_in", volumePause);
            setGameState(GameState.PAUSED);
            soundManager.stopSound("bgm_menu");
        }
    }
    public void resumeGame() {
        if (gameState == GameState.PAUSED) {
            soundManager.playSound("pause_out", volumePause);
            setGameState(GameState.PLAYING);
            soundManager.loopSound("bgm_menu", volumeBGM);
        }
    }
    public void restartRound() {
        playSound("ui_click");
        initRound(currentRound);
        setGameState(GameState.NEXT_ROUND);
        nextRoundStartTime = System.currentTimeMillis();
        isGameInProgress = true;
    }
    public void exitToMenu() {
        playSound("ui_click");
        setGameState(GameState.MENU);
        soundManager.loopSound("bgm_menu", volumeBGM);
    }
    public void startNewGame() {
        playSound("ui_click");
        initGame();
        setGameState(GameState.NEXT_ROUND);
        nextRoundStartTime = System.currentTimeMillis();
        isGameInProgress = true;
    }
    public void navigateToHighScores() {
        playSound("ui_click");
        soundManager.stopSound("bgm_menu");
        this.highScores = loadScores();
        setGameState(GameState.HIGH_SCORES);
    }
    public void navigateToSettings() {
        playSound("ui_click");
        soundManager.stopSound("bgm_menu");
        setGameState(GameState.SETTINGS);
    }

    // Các hàm tiện ích
    public void playSound(String sound) {
        soundManager.playSound(sound, volumeClick);
    }
    public void stopSound(String sound) {
        soundManager.stopSound(sound);
    }
    public void loopSound(String sound) {
        soundManager.loopSound(sound, volumeBGM);
    }

    // Các hàm Setter cho âm lượng
    public void setVolumeBGM(float vol) {
        this.volumeBGM = vol;
        soundManager.updateVolume("bgm_menu", this.volumeBGM);
    }
    public void setVolumePaddle(float vol) { this.volumePaddle = vol; }
    public void setVolumeBrick(float vol) { this.volumeBrick = vol; }
    public void setVolumeWall(float vol) { this.volumeWall = vol; }
    public void setVolumeExplosion(float vol) { this.volumeExplosion = vol; }


    private void processGameplayInput() {
        if (inputHandler.consumePausePress()) {
            if (gameState == GameState.PLAYING) pauseGame();
            else if (gameState == GameState.PAUSED) resumeGame();
        }
        if (inputHandler.consumeLaunchPress()) {
            if (gameState == GameState.PLAYING && !isBallLaunched) {
                isBallLaunched = true;
            }
        }
        if (inputHandler.consumeConfirmPress()) {
            switch (gameState) {
                case GAME_OVER:
                case VICTORY:
                    initGame();
                    break;
            }
        }

        if (gameState == GameState.PLAYING) {
            isMovingLeft = inputHandler.isLeftPressed();
            isMovingRight = inputHandler.isRightPressed();
        } else {
            isMovingLeft = false;
            isMovingRight = false;
        }
    }

    /**
     * Vẽ các đối tượng lên màn hình
     */
    public void draw(Graphics g) {
        if (uiManager == null) return;

        switch (gameState) {
            case MENU:
                renderer.drawMenu(g, GAME_WIDTH, GAME_HEIGHT,
                        uiManager.getMouseX(), uiManager.getMouseY(),
                        uiManager.getPlayButtonRect(),
                        uiManager.getHighScoresButtonRect(),
                        uiManager.getMenuSettingsButtonRect(),
                        uiManager.getExitButtonRect(),
                        isGameInProgress, // Biến này vẫn ở GameManager
                        uiManager.getContinueButtonRect());
                break;
            case PLAYING:
            case PAUSED:
                renderer.drawGameBackground(g, GAME_WIDTH, GAME_HEIGHT, currentRound);

                // VẼ GAME OBJECTS.
                renderer.drawPaddle(g, paddle);
                for (Ball b : new ArrayList<>(balls)) renderer.drawBall(g, b);
                for (Bullet b : new ArrayList<>(bullets)) renderer.drawBullet(g, b);
                for (Brick b : new ArrayList<>(bricks)) renderer.drawBrick(g, b);
                for (PowerUp p : new ArrayList<>(powerUps)) renderer.drawPowerUp(g, p);
                for (Explosion exp : new ArrayList<>(explosions)) renderer.drawExplosion(g, exp);

                // VẼ UI.
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 10, 20);
                g.drawString("Lives: " + lives, 10, 40);
                g.drawString("Round: " + currentRound + "/" + maxRounds, 10, 60);

                renderer.drawPauseIcon(g, gameState,
                        uiManager.getMouseX(), uiManager.getMouseY(),
                        uiManager.getPauseButtonRect());

                if (gameState == GameState.PAUSED) {
                    renderer.drawPause(g, GAME_WIDTH, GAME_HEIGHT,
                            uiManager.getMouseX(), uiManager.getMouseY(),
                            uiManager.getPauseContinueButton(),
                            uiManager.getPauseRestartButton(),
                            uiManager.getPauseSettingsButtonRect(),
                            uiManager.getPauseExitButton());
                }
                break;
            case ROUND_CLEAR:
                renderer.drawGameBackground(g, GAME_WIDTH, GAME_HEIGHT, currentRound);
                renderer.drawPaddle(g, paddle);
                for (Ball b : new ArrayList<>(balls)) renderer.drawBall(g, b);
                for (Bullet b : new ArrayList<>(bullets)) renderer.drawBullet(g, b);
                for (Brick b : new ArrayList<>(bricks)) renderer.drawBrick(g, b);
                for (PowerUp p : new ArrayList<>(powerUps)) renderer.drawPowerUp(g, p);
                for (Explosion exp : new ArrayList<>(explosions)) renderer.drawExplosion(g, exp);
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 10, 20);
                g.drawString("Lives: " + lives, 10, 40);
                g.drawString("Round: " + currentRound + "/" + maxRounds, 10, 60);

                renderer.drawPauseIcon(g, gameState,
                        uiManager.getMouseX(), uiManager.getMouseY(),
                        uiManager.getPauseButtonRect());
                break;
            case HIGH_SCORES:
                renderer.drawHighScores(g, GAME_WIDTH, GAME_HEIGHT,
                        uiManager.getMouseX(), uiManager.getMouseY(),
                        uiManager.getBackButtonRect(),
                        highScores);
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
                switch (uiManager.getCurrentSettingsPage()) {
                    case MAIN:
                        renderer.drawSettingsMain(g, GAME_WIDTH, GAME_HEIGHT,
                                uiManager.getMouseX(), uiManager.getMouseY(),
                                uiManager.getSettingsSoundButtonRect(),
                                uiManager.getSettingsBackRect());
                        break;
                    case SOUND:
                        renderer.drawSettingsSound(g, GAME_WIDTH, GAME_HEIGHT,
                                uiManager.getMouseX(), uiManager.getMouseY(),
                                uiManager.getSliderBgmRect(), volumeBGM,
                                uiManager.getSliderPaddleRect(), volumePaddle,
                                uiManager.getSliderBrickRect(), volumeBrick,
                                uiManager.getSliderWallRect(), volumeWall,
                                uiManager.getSliderExplosionRect(), volumeExplosion,
                                uiManager.getSoundBackRect());
                        break;
                    // (case CONTROLS: chưa có)
                }
                break;
        }
    }

    private List<Integer> loadScores() {
        try {
            return Files.lines(Paths.get(SCORE_FILE))
                    .map(s -> s.replaceAll("\\s", ""))
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .sorted(Collections.reverseOrder())
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Chưa có file điểm. Đang tạo mới...");
            return new ArrayList<>();
        }
    }

    private void addScore(int newScore) {
        if (newScore <= 0) return;
        try {
            Files.write(Paths.get(SCORE_FILE),
                    (newScore + "\n").getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            this.highScores = loadScores();
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu điểm: " + e.getMessage());
        }
    }

    // Getter/setter cho game logic.
    public boolean isGameInProgress() { return isGameInProgress; }
    public GameState getGameState() { return this.gameState; }
    public void setGameState(GameState gameState) { this.gameState = gameState; }
    public int getLives() { return this.lives; }
    public void setLives(int lives) { this.lives = lives; }
    public int getScore() { return this.score; }
    public int getCurrentRound() { return this.currentRound; }
    public List<Ball> getBalls() { return this.balls; }
    public List<Brick> getBricks() { return this.bricks; }
    public boolean isBallLaunched() { return this.isBallLaunched; }
    public void setIsBallLaunched(boolean isBallLaunched) { this.isBallLaunched = isBallLaunched; }
    public Paddle getPaddle() { return this.paddle; }
}