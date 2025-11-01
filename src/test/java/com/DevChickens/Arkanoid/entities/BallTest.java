package com.DevChickens.Arkanoid.entities;

import com.DevChickens.Arkanoid.core.CollisionManager.CollisionSide;
import com.DevChickens.Arkanoid.entities.bricks.Brick;
import com.DevChickens.Arkanoid.graphics.AssetLoader;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Lớp BallTest dùng để kiểm thử (Unit Test) các logic nghiệp vụ của lớp Ball.
 * Giống như Paddle, Ball cũng phụ thuộc vào AssetLoader để tải ảnh.
 * Chúng ta sẽ sử dụng Mockito để "giả lập" (mock) lớp AssetLoader,
 * cho phép chúng ta test Ball một cách độc lập mà không cần load file ảnh thật.
 * @author Vũ Văn Tiến
 */
public class BallTest {

    private Ball ball;
    private MockedStatic<AssetLoader> assetLoaderMock;
    private BufferedImage mockNormalBall;
    private BufferedImage mockSuperBall;

    // Hằng số để test
    private static final double BALL_SPEED = 10.0;
    private static final double BALL_START_X = 100.0;
    private static final double BALL_START_Y = 100.0;
    private static final double BALL_START_DIR_X = 0.5;
    private static final double BALL_START_DIR_Y = 0.5;
    private static final int MOCK_IMAGE_SIZE = 30; // Kích thước ảnh giả

    @BeforeEach
    void setUpOuter() {
        // Tạo các đối tượng ảnh giả (mock)
        mockNormalBall = new BufferedImage(MOCK_IMAGE_SIZE, MOCK_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        mockSuperBall = new BufferedImage(MOCK_IMAGE_SIZE, MOCK_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        
        // Bắt đầu mock các phương thức static của AssetLoader
        assetLoaderMock = mockStatic(AssetLoader.class);
    }

    @AfterEach
    void tearDownOuter() {
        // Dừng mock sau khi mỗi test hoàn tất để tránh ảnh hưởng test khác
        assetLoaderMock.close();
    }

    // --- CÁC TEST CẦN CONSTRUCTOR CHẠY THÀNH CÔNG ---
    @Nested
    @DisplayName("Khi constructor chạy thành công")
    class WhenConstructorSucceeds {

        // Giả lập Paddle và Brick để test va chạm
        private Paddle mockPaddle;
        private Brick mockBrick;

        @BeforeEach
        void setUpHappyPath() {
            // Dạy AssetLoader trả về ảnh giả khi được gọi
            assetLoaderMock.when(() -> AssetLoader.loadImage("/images/Ball.png"))
                    .thenReturn(mockNormalBall);
            assetLoaderMock.when(() -> AssetLoader.loadImage("/images/SuperBall.png"))
                    .thenReturn(mockSuperBall);

            // Khởi tạo các đối tượng mock
            mockPaddle = mock(Paddle.class);
            mockBrick = mock(Brick.class);

            // Tạo đối tượng Ball thật
            ball = new Ball(BALL_START_X, BALL_START_Y, 0, 0, 
                            BALL_SPEED, BALL_START_DIR_X, BALL_START_DIR_Y);
        }

        @Test
        @DisplayName("Constructor: Gán đúng các giá trị ban đầu")
        void testConstructorCalculations() {
            // Lấy các giá trị hằng số private từ trong lớp Ball (nếu có)
            final double EXPECTED_BALL_SIZE = 30.0; // Dựa trên code Ball.java

            assertAll(
                    () -> assertEquals(BALL_SPEED, ball.getSpeed(), "Tốc độ chưa được gán"),
                    () -> assertEquals(BALL_START_DIR_X, ball.getDirectionX(), "Hướng X chưa được gán"),
                    () -> assertEquals(BALL_START_DIR_Y, ball.getDirectionY(), "Hướng Y chưa được gán"),
                    () -> assertEquals(BALL_START_X, ball.getX(), "Vị trí X ban đầu chưa đúng"),
                    () -> assertEquals(BALL_START_Y, ball.getY(), "Vị trí Y ban đầu chưa đúng"),
                    () -> assertEquals(EXPECTED_BALL_SIZE, ball.getWidth(), "Chiều rộng chưa được gán"),
                    () -> assertEquals(EXPECTED_BALL_SIZE, ball.getHeight(), "Chiều cao chưa được gán"),
                    () -> assertFalse(ball.isSuperBall(), "Bóng ban đầu không phải là SuperBall"),
                    () -> assertEquals(mockNormalBall, ball.getImage(), "Bóng ban đầu phải dùng ảnh NormalBall")
            );
        }

        @Test
        @DisplayName("Logic: move() cập nhật vị trí chính xác")
        void testMoveUpdatesPositionCorrectly() {
            
            // Di chuyển
            ball.move(); // update() sẽ gọi move()

            // 3. Khẳng định (Assert)
            double expectedDx = BALL_SPEED * BALL_START_DIR_X; // 10 * 0.5 = 5
            double expectedDy = BALL_SPEED * BALL_START_DIR_Y; // 10 * 0.5 = 5
            
            assertEquals(BALL_START_X + expectedDx, ball.getX(), "Vị trí X mới chưa đúng");
            assertEquals(BALL_START_Y + expectedDy, ball.getY(), "Vị trí Y mới chưa đúng");
        }

        @Test
        @DisplayName("Va chạm Tường/Gạch: Đảo ngược Y khi va chạm dọc (Vertical)")
        void testBounceOff_WallOrBrick_Vertical() {
            ball.setDirectionY(0.5); // Đang bay xuống
            ball.bounceOff(mockBrick, CollisionSide.VERTICAL);
            assertEquals(-0.5, ball.getDirectionY(), "Hướng Y phải bị đảo ngược");
        }

        @Test
        @DisplayName("Va chạm Tường/Gạch: Đảo ngược X khi va chạm ngang (Horizontal)")
        void testBounceOff_WallOrBrick_Horizontal() {
            ball.setDirectionX(0.5); // Đang bay sang phải
            ball.bounceOff(mockBrick, CollisionSide.HORIZONTAL);
            assertEquals(-0.5, ball.getDirectionX(), "Hướng X phải bị đảo ngược");
        }

        @Test
        @DisplayName("Va chạm Tường/Gạch: Đảo ngược cả X và Y khi va chạm góc (Corner)")
        void testBounceOff_WallOrBrick_Corner() {
            ball.setDirectionX(0.5);
            ball.setDirectionY(0.5);
            ball.bounceOff(mockBrick, CollisionSide.CORNER);
            assertAll(
                    () -> assertEquals(-0.5, ball.getDirectionX(), "Hướng X phải bị đảo ngược"),
                    () -> assertEquals(-0.5, ball.getDirectionY(), "Hướng Y phải bị đảo ngược")
            );
        }

        @Test
        @DisplayName("Va chạm Paddle: Nảy thẳng lên khi đập vào giữa Paddle")
        void testBounceOff_Paddle_Center() {
            // Dạy mockPaddle trả về các thông số của Paddle
            when(mockPaddle.getWidth()).thenReturn(100.0);
            when(mockPaddle.getX()).thenReturn(100.0); // Tâm Paddle = 100 + (100/2) = 150
            
            // Tâm Ball = x(135) + width/2(15) = 150
            ball.setX(135); 
            
            // Xử lý va chạm
            ball.bounceOff(mockPaddle, null);

            // Kiểm tra
            // Góc nảy = 0 -> sin(0) = 0
            // Góc nảy = 0 -> -cos(0) = -1
            assertEquals(0.0, ball.getDirectionX(), 0.001, "Hướng X phải là 0 (thẳng)");
            assertEquals(-1.0, ball.getDirectionY(), 0.001, "Hướng Y phải là -1 (lên)");
        }
        
        @Test
        @DisplayName("Va chạm Paddle: Nảy sang phải khi đập vào bên phải")
        void testBounceOff_Paddle_RightEdge() {
            // Dạy mockPaddle trả về các thông số của Paddle
            when(mockPaddle.getWidth()).thenReturn(100.0);
            when(mockPaddle.getX()).thenReturn(100.0); 
            
            // (Mép paddle: 100 + 100 = 200. Mép bóng: 170 + 30 = 200)
            ball.setX(170);

            // Xử lý va chạm
            ball.bounceOff(mockPaddle, null);
            
            // Kiểm tra
            assertTrue(ball.getDirectionX() > 0, "Hướng X phải dương (sang phải)");
            assertTrue(ball.getDirectionY() < 0, "Hướng Y phải âm (đi lên)");
        }

        @Test
        @DisplayName("Logic: ensureMinimumVerticalSpeed() ép giá trị khi quá thấp")
        void testEnsureMinimumVerticalSpeed_CorrectsLowSpeed() {
            // Lấy hằng số MIN_VERTICAL_DIRECTION từ code
            final double MIN_DIR = 0.3;

            // Trường hợp dương
            ball.setDirectionY(0.1); // Thấp hơn 0.3
            ball.ensureMinimumVerticalSpeed();
            assertEquals(MIN_DIR, ball.getDirectionY(), "Phải ép lên giá trị dương tối thiểu");

            // Trường hợp âm
            ball.setDirectionY(-0.1); // Thấp hơn 0.3
            ball.ensureMinimumVerticalSpeed();
            assertEquals(-MIN_DIR, ball.getDirectionY(), "Phải ép xuống giá trị âm tối thiểu");
        }
        
        @Test
        @DisplayName("Logic: ensureMinimumVerticalSpeed() bỏ qua khi tốc độ đủ")
        void testEnsureMinimumVerticalSpeed_IgnoresSufficientSpeed() {
            ball.setDirectionY(0.5); // Lớn hơn 0.3
            ball.ensureMinimumVerticalSpeed();
            assertEquals(0.5, ball.getDirectionY(), "Phải giữ nguyên tốc độ");
            
            ball.setDirectionY(-0.5); // Lớn hơn 0.3
            ball.ensureMinimumVerticalSpeed();
            assertEquals(-0.5, ball.getDirectionY(), "Phải giữ nguyên tốc độ");
        }
        
        @Test
        @DisplayName("State: Kích hoạt và hủy kích hoạt SuperBall")
        void testActivateAndDeactivateSuperBall() {
            // Trạng thái ban đầu
            assertFalse(ball.isSuperBall(), "Ban đầu không phải SuperBall");
            assertEquals(mockNormalBall, ball.getImage(), "Ban đầu dùng ảnh thường");

            // Kích hoạt
            ball.activateSuperBall();
            assertTrue(ball.isSuperBall(), "Phải là SuperBall sau khi kích hoạt");
            assertEquals(mockSuperBall, ball.getImage(), "Phải dùng ảnh SuperBall");

            // Hủy kích hoạt
            ball.deactivateSuperBall();
            assertFalse(ball.isSuperBall(), "Không phải SuperBall sau khi hủy");
            assertEquals(mockNormalBall, ball.getImage(), "Phải quay lại ảnh thường");
        }
    }

    // --- TEST KHI CONSTRUCTOR THẤT BẠI ---
    @Nested
    @DisplayName("Khi constructor chạy thất bại (Sad Path)")
    class WhenConstructorFails {

        @Test
        @DisplayName("Ném RuntimeException nếu không tải được ảnh NormalBall")
        void testConstructorThrowsExceptionOnLoadFailure() {
            // Dạy mock thất bại (trả về null)
            assetLoaderMock.when(() -> AssetLoader.loadImage("/images/Ball.png"))
                    .thenReturn(null);

            // Kiểm tra xem Exception có được ném ra không
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                // Hành động gây lỗi
                new Ball(0, 0, 0, 0, BALL_SPEED, 0, 1);
            }, "Constructor phải ném RuntimeException nếu tải ảnh lỗi");

            // Kiểm tra thông báo lỗi
            assertTrue(exception.getMessage().contains("Lỗi không thể tải ảnh cho Ball"));
        }
    }
}