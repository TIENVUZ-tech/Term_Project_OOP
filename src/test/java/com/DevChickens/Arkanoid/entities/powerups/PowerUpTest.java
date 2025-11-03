package com.DevChickens.Arkanoid.entities.powerups;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.entities.Ball;
import com.DevChickens.Arkanoid.entities.Paddle;
import com.DevChickens.Arkanoid.graphics.AssetLoader;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.IOException; // Dùng để ném lỗi giả

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // Import tất cả các hàm static của Mockito

/**
 * Lớp kiểm thử cho PowerUp.
 * Sử dụng Mockito để "giả" hàm static AssetLoader.loadImage().
 */
class PowerUpTest {

    /**
     * Lớp TestPowerUp để test.
     */
    private class TestPowerUp extends PowerUp {
        public TestPowerUp(double x, double y, long duration) {
            // Mockito sẽ can thiệp để ngăn nó ném RuntimeException.
            super(x, y, "Test", duration, "dummy/path/to/image.png", 10, 10);
        }

        @Override
        public void applyEffect(GameManager manager, Paddle paddle, Ball ball) {
        }

        @Override
        public void removeEffect(Paddle paddle, Ball ball) {
        }
    }

    // Biến lưu trữ đối tượng mock
    //private MockedStatic<AssetLoader> assetLoaderMock;
    private final long TEST_DURATION = 100;

    //@BeforeEach
    //void setUpOuter() {
    // Bắt đầu giả lập AssetLoader trước mỗi test
    //  assetLoaderMock = mockStatic(AssetLoader.class);
    //}

    //@AfterEach
    //void tearDownOuter() {
    // Dọn dẹp, trả AssetLoader về trạng thái thật
    //  assetLoaderMock.close();
    //}

    // Các test cần để contructor chạy thành công
    @Nested
    @DisplayName("Khi constructor chạy thành công")
    class WhenConstructorSucceeds {

        private TestPowerUp powerUp;
        private MockedStatic<AssetLoader> assetLoaderMock;

        @BeforeEach
        void setUpHappyPath() {
            assetLoaderMock = mockStatic(AssetLoader.class);
            // Dạy cho Mockito Khi ai đó gọi loadImage... thì trả về null
            // khối try chạy xong mà không ném lỗi
            assetLoaderMock.when(() -> AssetLoader.loadImage(anyString()))
                    .thenReturn(null); // Trả về null là đủ để test

            // Giờ nó sẽ không ném lỗi
            powerUp = new TestPowerUp(50, 50, TEST_DURATION);
        }

        @AfterEach
        void tearDownHappyPath() {
            // Đóng mock
            assetLoaderMock.close();
        }

        // Các test logic của PowerUp
        @Test
        @DisplayName("Trạng thái ban đầu: Vị trí Y và chưa hết hạn")
        void testInitialState() {
            assertNotNull(powerUp, "PowerUp phải được khởi tạo thành công");
            assertEquals(50, powerUp.getY(), "Vị trí Y ban đầu phải là 50");
            assertFalse(powerUp.isExpired(), "PowerUp không nên hết hạn khi mới tạo");
        }

        @Test
        @DisplayName("Logic: update() làm PowerUp rơi xuống khi chưa kích hoạt")
        void testUpdate_BeforeActivation() {
            powerUp.update();
            assertEquals(52, powerUp.getY(), "Y nên tăng thêm 2 sau khi update()");

            powerUp.update();
            assertEquals(54, powerUp.getY(), "Y nên tăng thêm 2 một lần nữa");
        }

        @Test
        @DisplayName("Logic: update() không làm PowerUp rơi khi đã kích hoạt")
        void testUpdate_AfterActivation() {
            powerUp.activate();
            double currentY = powerUp.getY(); // Y = 50

            powerUp.update();
            assertEquals(currentY, powerUp.getY(), "Y không nên thay đổi sau khi đã kích hoạt");
        }

        @Test
        @DisplayName("Logic: Kích hoạt và hết hạn (isExpired) hoạt động đúng")
        void testActivationAndExpiry() throws InterruptedException {
            assertFalse(powerUp.isExpired(), "Chưa kích hoạt, không thể hết hạn");

            powerUp.activate();
            assertFalse(powerUp.isExpired(), "Vừa kích hoạt, chưa thể hết hạn");

            // Chờ quá thời gian 100ms
            Thread.sleep(TEST_DURATION + 10);

            assertTrue(powerUp.isExpired(), "Phải hết hạn sau khi chờ quá thời gian duration");
        }
    }

    // Test khi contructor thất bại
    @Nested
    @DisplayName("Khi constructor chạy thất bại")
    class WhenConstructorFails {

        @Test
        @DisplayName("Ném RuntimeException nếu AssetLoader.loadImage ném lỗi")
        void testConstructorThrowsExceptionOnLoadFailure() {

            // Dùng try-with-resources để mock tự động đóng
            try (MockedStatic<AssetLoader> assetLoaderMock = mockStatic(AssetLoader.class)) {

                // Dạy cho Mockito
                assetLoaderMock.when(() -> AssetLoader.loadImage(anyString()))
                        .thenThrow(new RuntimeException("Test: Giả lập lỗi không tìm thấy file"));

                // Kiểm tra
                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                    new TestPowerUp(50, 50, TEST_DURATION);
                }, "Constructor phải ném RuntimeException nếu tải ảnh lỗi");
                assertTrue(exception.getMessage().contains("Lỗi tải ảnh tại đường dẫn"));
            }
        }
    }
}