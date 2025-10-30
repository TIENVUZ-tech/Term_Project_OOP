package com.DevChickens.Arkanoid.entities.powerups;
/**
 * Lớp Factory chịu trách nhiệm tạo ra các đối tượng PowerUp.
 */
public class PowerUpFactory {

    // Định nghĩa hằng số cho thời gian hiệu lực
    private static final long DEFAULT_DURATION = 5000; // 5 giây
    private static final long MULTIBALL_DURATION = 1000; // 1 giây

    // Định nghĩa hằng số cho tỷ lệ rớt (tích lũy)
    private static final double EXPAND_CHANCE = 0.05;  // 5%
    private static final double SUPERBALL_CHANCE = 0.10; // 5% (tổng 10%)
    private static final double MULTIBALL_CHANCE = 0.15; // 5% (tổng 15%)
    private static final double GUN_CHANCE = 0.20;       // 5% (tổng 20%)
    private static final double FASTBALL_CHANCE = 0.25;    // 5% (tổng 25%)

    /**
     * Thử tạo một PowerUp ngẫu nhiên tại vị trí (x, y).
     */
    public static PowerUp createRandomPowerUp(double x, double y) {
        double rand = Math.random();

        if (rand < EXPAND_CHANCE) {
            return new ExpandPaddlePowerUp(x, y, "EXPAND_PADDLE", DEFAULT_DURATION);
        } else if (rand < SUPERBALL_CHANCE) {
            return new SuperBallPowerUp(x, y, "SUPER_BALL", DEFAULT_DURATION);
        } else if (rand < MULTIBALL_CHANCE) {
            return new MultiBallPowerUp(x, y, "MULTI_BALL", MULTIBALL_DURATION);
        } else if (rand < GUN_CHANCE) {
            return new GunPaddlePowerUp(x, y, "GUN_PADDLE", DEFAULT_DURATION);
        } else if (rand < FASTBALL_CHANCE) {
            return new FastBallPowerUp(x, y, "FAST_BALL", DEFAULT_DURATION);
        }

        return null;
    }
}