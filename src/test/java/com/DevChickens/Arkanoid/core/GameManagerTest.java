package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.bricks.Brick;
import com.DevChickens.Arkanoid.enums.GameState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Lớp này test logic chuyển đổi trạng thái của GameManager.
 */
class GameManagerTest {

    private GameManager gameManager;

    /**
     * Hàm này chạy TRƯỚC MỖI @Test
     * Nó tạo ra một GameManager hoàn toàn mới cho mỗi test,
     * đảm bảo các test không ảnh hưởng lẫn nhau.
     */
    @BeforeEach
    void setUp() {
        gameManager = GameManager.getInstance();
    }

    /**
     * Test 1: Kiểm tra xem game có khởi tạo đúng giá trị ban đầu không.
     */
    @Test
    void testGameInitialization() {
        // GameManager sẽ tự động gọi initGame() trong constructor của nó

        assertEquals(3, gameManager.getLives(), "Game nên bắt đầu với 3 mạng");
        assertEquals(0, gameManager.getScore(), "Game nên bắt đầu với 0 điểm");
        assertEquals(1, gameManager.getCurrentRound(), "Game nên bắt đầu ở Round 1");
        assertEquals(GameState.MENU, gameManager.getGameState(), "Game nên bắt đầu ở trạng thái MENU");
    }

    /**
     * Test 2: Kiểm tra xem bóng rơi có làm mất 1 mạng không.
     */
    @Test
    void testBallDropLosesLife() {
        // Setup: Đưa game vào trạng thái đang chơi
        gameManager.setGameState(GameState.PLAYING);
        gameManager.setLives(3);
        gameManager.setIsBallLaunched(true);

        // Lấy quả bóng và đặt nó ra ngoài màn hình (ở đáy)
        Ball ball = gameManager.getBalls().get(0);
        ball.setY(GameManager.GAME_HEIGHT + 100); // Đặt vị trí Y lớn hơn chiều cao màn hình

        //  Action: Chạy 1 frame update
        gameManager.update();

        //  Assert: Kiểm tra xem có bị trừ mạng không
        assertEquals(2, gameManager.getLives(), "Phải mất 1 mạng khi bóng rơi");

        // Kiểm tra xem bóng đã được reset về paddle chưa
        assertFalse(gameManager.isBallLaunched(), "Bóng phải được reset (chưa phóng)");
    }

    /**
     * Test 3: Kiểm tra xem mất mạng cuối cùng có dẫn đến GAME_OVER không.
     */
    @Test
    void testBallDropLeadsToGameOver() {
        //  Setup nhưng chỉ set 1 mạng
        gameManager.setGameState(GameState.PLAYING);
        gameManager.setLives(1); // Mạng cuối cùng
        gameManager.setIsBallLaunched(true);

        Ball ball = gameManager.getBalls().get(0);
        ball.setY(GameManager.GAME_HEIGHT + 100);

        // Action:
        gameManager.update();

        // Assert:
        assertEquals(0, gameManager.getLives(), "Mạng phải giảm về 0");
        assertEquals(GameState.GAME_OVER, gameManager.getGameState(), "Trạng thái game phải là GAME_OVER");
    }

    /**
     * Test 4: Kiểm tra xem phá hết gạch có chuyển sang ROUND_CLEAR không.
     */
    @Test
    void testWinRoundTriggersRoundClear() {
        // Setup
        gameManager.setGameState(GameState.PLAYING);
        gameManager.setIsBallLaunched(true);

        // Giả lập "phá" tất cả gạch
        // Set isDestroyed = true cho mọi viên gạch
        for (Brick brick : gameManager.getBricks()) {
            // Dùng hàm takeHit() nhiều lần
            while (!brick.isDestroyed()) {
                brick.takeHit();
            }
        }

        // Xác nhận rằng tất cả gạch đã vỡ.
        boolean allBricksBroken = gameManager.getBricks().stream().allMatch(Brick::isDestroyed);
        assertTrue(allBricksBroken, "Setup thất bại: Không phải tất cả gạch đã vỡ");

        // Action: Chạy 1 frame update để logic "Kiểm tra thắng" được kích hoạt
        gameManager.update();

        // Assert:
        assertEquals(GameState.ROUND_CLEAR, gameManager.getGameState(), "Phải chuyển sang ROUND_CLEAR sau khi thắng");
    }
}