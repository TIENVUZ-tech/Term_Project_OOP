package com.DevChickens.Arkanoid.entities;

import com.DevChickens.Arkanoid.core.GameManager;
import com.DevChickens.Arkanoid.graphics.AssetLoader;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Lớp Paddle có một vài phụ thuộc vào bên ngoài(GAME_WIDTH, GAME_HEIGHT...).
 * Khiến cho việc viết unit test trở nên phức tạp.
 * => Để test Paddle một cách độc lập, ta dùng "giả lập" các phụ thuộc này.
 * @author Vũ Văn Tiến.
 */

public class PaddleTest {
    
    private Paddle paddle;
    private MockedStatic<AssetLoader> assetLoaderMock;
    private BufferedImage mockNormalPaddle;
    private BufferedImage mockGunPaddle;

    // Hằng số để test
    private static final double PADDLE_SPEED = 5.0;
    private static int MOCK_IMAGE_WIDTH = 120;
    private static int MOCK_IMAGE_HEIGHT = 30;

    @BeforeEach
    void setUpOuter() {
        // Tạo ảnh thật
        mockNormalPaddle = new BufferedImage(MOCK_IMAGE_WIDTH, MOCK_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        mockGunPaddle = new BufferedImage(MOCK_IMAGE_WIDTH, MOCK_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        // Bắt đầu mock static
        assetLoaderMock = mockStatic(AssetLoader.class);
    }

    @AfterEach
    void tearDownOuter() {
        assetLoaderMock.close();
    }

    // --- CÁC TEST CẦN CONSTRUCTOR CHẠY THÀNH CÔNG ---
    @Nested
    @DisplayName("Khi constructor chạy thành công")
    class WhenConstructorSucceeds {

        @BeforeEach
        void setUpHappyPath() {
            // Dạy mock "thành công"
            assetLoaderMock.when(() -> AssetLoader.loadImage("/images/NormalPaddle.png"))
                    .thenReturn(mockNormalPaddle);
            assetLoaderMock.when(() -> AssetLoader.loadImage("/images/GunPaddle.png"))
                    .thenReturn(mockGunPaddle);
            
            // Tạo paddle
            paddle = new Paddle(0, 0, 0, 0, PADDLE_SPEED, null);
        }

        // CÁC TEST CASE
        @Test
        @DisplayName("Constructor: Vị trí và kích thước được tính toán đúng")
        void testConstructorCalculations() {
            // Kích thước mong muốn của Paddle = 1/6 kích thước của GAME_WIDTH
            double expectedWidth = GameManager.GAME_WIDTH / 6.0;
            // Tính toán kích thước chiều cao mong muốn
            double aspectRatio = (double) MOCK_IMAGE_HEIGHT / MOCK_IMAGE_WIDTH;
            double expectedHeight = expectedWidth * aspectRatio;
            // Tính toán đọa độ tâm của Paddle (giữa khung hình bên dưới)
            double expectedX = (GameManager.GAME_WIDTH / 2) - (expectedWidth / 2);
            double expectedY = GameManager.GAME_HEIGHT - expectedHeight - 10;

            // Dùng assertAll để chương trình kiểm tra hết testcase và báo lỗi một thể.
            assertAll(
                    () -> assertEquals(expectedWidth, paddle.getWidth(), "Chiều rộng Paddle chưa tính đúng"),
                    () -> assertEquals(expectedHeight, paddle.getHeight(), "Chiều cao Paddle chưa tính đúng"),
                    () -> assertEquals(expectedX, paddle.getX(), "Vị trí X ban đầu phải ở giữa"),
                    () -> assertEquals(expectedY, paddle.getY(), "Vị trí của Y ban phải ở gần đáy"),
                    () -> assertEquals(expectedWidth, paddle.getBaseWidth(), "baseWidth chưa được gán chính xác"),
                    () -> assertEquals(PADDLE_SPEED, paddle.getSpeed(), "Tốc độc chưa được gán")
            );
        }

        @Test
        @DisplayName("Điều khiển: moveLeft(), moveRight() đặt vận tốc Dx chính xác")
        void testMoveLeftAndRight() {
            // Kiểm tra moveLeft
            paddle.moveLeft();
            assertEquals(-PADDLE_SPEED, paddle.getDx(), "moveLeft() phải đặt DX âm");
            
            // Kiểm tra moveRight
            paddle.moveRight();
            assertEquals(PADDLE_SPEED, paddle.getDx(), "moveRight() phải đặt DX dương");
        }

        @Test
        @DisplayName("Logic: update() gọi move() và reset Dx về 0")
        void testUpdateCallsMoveAndResetDx() {
            // Vị trí ban đầu của Paddle
            double initialX = paddle.getX();

            // Gán vận tốc cho Paddle
            paddle.moveRight();
            assertEquals(PADDLE_SPEED, paddle.getDx());

            // Cập nhật lại vị trí
            paddle.update(); // update sẽ gọi phương thức move()
            
            // Kiểm tra sự thay đổi của Dx và reset
            double expectedX = initialX + PADDLE_SPEED;
            assertEquals(expectedX, paddle.getX(), "Paddle phải được di chuyển sang phải");
            assertEquals(0, paddle.getDx(), "Dx phải được reset về 0 sau khi Paddle di chuyển");
        }

        @Test
        @DisplayName("Va chạm: Paddle dừng ở tường bên phải")
        void testMoveStopAtRightWall() {
            // Đặt paddle ngay sát tường phải
            double paddleWidth = paddle.getWidth();
            paddle.setX(GameManager.GAME_WIDTH - paddleWidth - 1.0); // cách tường 1 pixel

            // Di chuyển sang phải
            paddle.moveRight();
            paddle.update();

            // Vị trí của Paddle chạm vào tường và dừng lại
            assertEquals(GameManager.GAME_WIDTH - paddleWidth, paddle.getX(), "Paddle phải dừng ở tường phải");
            assertEquals(0, paddle.getDx(), "Dx phải được reset");
        }

        @Test
        @DisplayName("Va chạm: Paddle dừng ở tượng bên trái")
        void testMoveStopAtLeftWall() {
            // Đặt paddle ngay sát tường trái
            paddle.setX(1.0); // cách tường 1 pixel

            // Di chuyển sang trái
            paddle.moveLeft();
            paddle.update();

            // Paddle sẽ chạm vào tường và dừng lại
            assertEquals(0, paddle.getX(), "Paddle phải dừng ở tường trái");
            assertEquals(0, paddle.getDx(), "Dx phải được reset");
        }

        @Test
        @DisplayName("PowerUp: Kích hoạt và hủy kích hoạt GunPaddle")
        void testActivateAndDeactivateGunPaddle() {
            // Trạng thái ban đầu
            assertFalse(paddle.isGunPaddle(), "Ban đầu không phải là GunPaddle");
            assertEquals(mockNormalPaddle, paddle.getImage(), "Ban đầu phải dùng ảnh NormalPaddle");

            // Kích hoạt
            paddle.activateGunPaddle();
            assertTrue(paddle.isGunPaddle(), "Phải là GunPaddle sau khi kích hoạt");
            assertEquals(mockGunPaddle, paddle.getImage(), "Phải dùng ảnh GunPaddle sau khi kích hoạt");

            // Hủy kích hoạt
            paddle.deactivateGunPaddle();
            assertFalse(paddle.isGunPaddle(), "Không phải là GunPaddle sau khi hủy");
            assertEquals(mockNormalPaddle, paddle.getImage(), "Phải dùng ảnh thường sau khi hủy");
        }

        @Test
        @DisplayName("Setter: setExpandEffectCount() chỉ nhận giá trị dương")
        void testSetExpandEffectCount() {
            // Không cần setup!
            paddle.setExpandEffectCount(3);
            assertEquals(3, paddle.getExpandEffectCount());

            paddle.setExpandEffectCount(-5);
            assertEquals(3, paddle.getExpandEffectCount(), "Không được nhận giá trị âm");
        }
    }

    // --- TEST KHI CONSTRUCTOR THẤT BẠI ---
    @Nested
    @DisplayName("Khi Constructor chạy thất bại")
    class WhenConstructorFails {

        @Test
        @DisplayName("Ném RuntimeException nếu không tải được ảnh")
        void testConstructorThrowsExceptionOnLoadFailure() {
            // Dạy mock "thất bại"
            assetLoaderMock.when(() -> AssetLoader.loadImage("/images/NormalPaddle.png"))
                    .thenReturn(null);

            // Kiểm tra lỗi
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                new Paddle(0, 0, 0, 0, PADDLE_SPEED, null);
            }, "Constructor phải ném RuntimeException nếu tải ảnh lỗi");

            // Kiểm tra thông báo
            assertTrue(exception.getMessage().contains("Không thể tải ảnh cho Paddle"));
        }
    }
}