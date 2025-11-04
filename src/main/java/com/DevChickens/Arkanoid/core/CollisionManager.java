package com.DevChickens.Arkanoid.core;

// Import tất cả các thực thể (entities)
import com.DevChickens.Arkanoid.entities.*;
import com.DevChickens.Arkanoid.entities.bricks.*;
import com.DevChickens.Arkanoid.entities.effects.Explosion;
import com.DevChickens.Arkanoid.entities.powerups.*;

// Import các thư viện Java
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator; // Quan trọng
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Lớp Quản lý Va chạm.
 * Chịu trách nhiệm xử lý TẤT CẢ logic vật lý,
 * phát hiện va chạm, và xử lý hậu quả (tính điểm, nổ, v.v.)
 *
 */
public class CollisionManager {


    /**
     * Định nghĩa các hướng va chạm có thể xảy ra.
     */
    public enum CollisionSide {
        HORIZONTAL,
        VERTICAL,
        CORNER
    }

    /**
     * Hàm kiểm tra va chạm AABB (Axis-Aligned Bounding Box) cơ bản.
     * @param a Vật thể 1
     * @param b Vật thể 2
     * @return true nếu 2 vật va chạm, false nếu không.
     */
    public static boolean checkCollision(GameObject a, GameObject b) {
        return (a.getX() < b.getX() + b.getWidth()) &&
                (a.getX() + a.getWidth() > b.getX()) &&
                (a.getY() < b.getY() + b.getHeight()) &&
                (a.getY() + a.getHeight() > b.getY());
    }

    /**
     * Tính toán hướng va chạm (Ngang, Dọc, Góc)
     */
    public static CollisionSide getCollisionDirection(GameObject a, GameObject b) {
        double centreA_X = a.getX() + a.getWidth() / 2.0;
        double centreA_Y = a.getY() + a.getHeight() / 2.0;
        double centreB_X = b.getX() + b.getWidth() / 2.0;
        double centreB_Y = b.getY() + b.getHeight() / 2.0;
        double dx = centreA_X - centreB_X;
        double dy = centreA_Y - centreB_Y;
        double combinedHalfWidth = a.getWidth() / 2.0 + b.getWidth() / 2.0;
        double combinedHalfHeight = a.getHeight() / 2.0 + b.getHeight() / 2.0;
        double overlapX = combinedHalfWidth - Math.abs(dx);
        double overlapY = combinedHalfHeight - Math.abs(dy);

        if (overlapX < overlapY) {
            return CollisionSide.HORIZONTAL;
        } else if (overlapY < overlapX) {
            return CollisionSide.VERTICAL;
        } else {
            return CollisionSide.CORNER;
        }
    }

    // Hệ thống quản lý.

    // Tham chiếu ngược lại GameManager để gọi các API.
    private GameManager gm;

    /**
     * Hàm khởi tạo mới, nhận vào GameManager.
     */
    public CollisionManager(GameManager gameManager) {
        this.gm = gameManager;
    }

    /**
     * Hàm kiểm tra và xử lý tất cả va chạm trong game.
     * Sẽ được gọi 60 lần/giây từ GameManager.update().
     */
    public void checkAndResolveCollisions() {
        List<Ball> balls = gm.getBalls();
        Paddle paddle = gm.getPaddle();
        List<Brick> bricks = gm.getBricks();
        List<Bullet> bullets = gm.getBullets();
        List<PowerUp> powerUps = gm.getPowerUps();

        // 1. Ball vs Paddle
        for (Ball ball : new ArrayList<>(balls)) {
            // Dùng hàm static helper
            if (CollisionManager.checkCollision(ball, paddle)) {
                if (ball.getDirectionY() > 0) {
                    ball.bounceOff(paddle, null);
                    ball.setY(paddle.getY() - ball.getHeight());
                    // Gọi API của GameManager
                    gm.getSoundManager().playSound("paddle_hit", gm.getVolumePaddle());
                }
            }

            // 2. Ball vs Bricks
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
                        gm.addScore(points);

                        if (b instanceof ExplosiveBrick) {
                            double explosionX = b.getX() + b.getWidth() / 2.0;
                            double explosionY = b.getY() + b.getHeight() / 2.0;
                            gm.addExplosion(new Explosion(explosionX, explosionY, b.getWidth() * 2, b.getHeight() * 2)); // <-- Gọi API
                            this.processExplosion(b); // <-- Gọi hàm nội bộ
                            gm.getSoundManager().playSound("brick_explode", gm.getVolumeExplosion());
                        } else {
                            gm.getSoundManager().playSound("brick_hit", gm.getVolumeBrick());
                        }

                        PowerUp newPowerUp = PowerUpFactory.createRandomPowerUp(b.getX(), b.getY());
                        if (newPowerUp != null) {
                            gm.addPowerUp(newPowerUp);
                        }
                    } else {
                        gm.getSoundManager().playSound("brick_crack", gm.getVolumeBrick());
                    }
                    break;
                }
            }
        }

        // 3. Bullet vs Bricks
        Iterator<Bullet> bulletIterator = bullets.iterator();
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
                        gm.addScore(points);

                        if (b instanceof ExplosiveBrick) {
                            double explosionX = b.getX() + b.getWidth() / 2.0;
                            double explosionY = b.getY() + b.getHeight() / 2.0;
                            gm.addExplosion(new Explosion(explosionX, explosionY, b.getWidth() * 2, b.getHeight() * 2)); // <-- Gọi API
                            this.processExplosion(b);
                        } else {
                            gm.getSoundManager().playSound("brick_hit", gm.getVolumeBrick());
                        }
                        PowerUp newPowerUp = PowerUpFactory.createRandomPowerUp(b.getX(), b.getY());
                        if (newPowerUp != null) {
                            gm.addPowerUp(newPowerUp);
                        }
                    } else {
                        gm.getSoundManager().playSound("brick_crack", gm.getVolumeBrick());
                    }
                    bulletIterator.remove();
                    break;
                }
            }
        }

        // 4. Paddle vs PowerUps
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp p = powerUpIterator.next();
            if (p.getY() > GameManager.GAME_HEIGHT) {
                powerUpIterator.remove();
                continue;
            }

            if (p.checkCollision(paddle)) {
                gm.getSoundManager().playSound("powerup_collect", gm.getVolumePowerUp());
                gm.getSoundManager().playSound("powerup_apply", gm.getVolumePowerUp());

                if (p instanceof ExpandPaddlePowerUp) {
                    p.applyEffect(gm, paddle, null);
                } else if (p instanceof GunPaddlePowerUp) {
                    p.applyEffect(null, paddle, null);
                    gm.setFiring(true); // <-- Gọi API
                } else if (p instanceof SuperBallPowerUp ||
                        p instanceof FastBallPowerUp ||
                        p instanceof MultiBallPowerUp) {
                    for (Ball b : new ArrayList<>(balls)) {
                        p.applyEffect(gm, paddle, b);
                    }
                }

                p.activate();
                gm.activatePowerUp(p);
                powerUpIterator.remove(); // Xóa khỏi danh sách đang rơi
            }
        }
    }

    /**
     * Hàm xử lý một vụ nổ lan (BFS).
     * @param initialBrick Viên gạch nổ ban đầu.
     */
    private void processExplosion(Brick initialBrick) {
        List<Brick> bricks = gm.getBricks(); // Lấy danh sách gạch

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
                        gm.addScore(points);

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
}