package com.DevChickens.Arkanoid.entities;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.DevChickens.Arkanoid.graphics.AssetLoader;

/**
 * Dùng giả lập để test các tính năng và logic của Bullet.
 * @author Vũ Văn Tiến
 */
public class BulletTest {
    
    private Bullet bullet;
    private MockedStatic<AssetLoader> assetLoaderMock;
    private BufferedImage mockBulletImage;

    // Hằng số để test
    private static final double MOCK_IMG_WIDTH = 10.0;
    private static final double MOCK_IMG_HEIGHT = 40.0;
    private static final double BULLET_SPEED = 7.0;

    @BeforeEach
    void setUpOuter() {
        // Tạo ảnh thật cho vật thể
        mockBulletImage = new BufferedImage((int) MOCK_IMG_WIDTH, (int) MOCK_IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        // Bắt đầu "mock" AssetLoader
        assetLoaderMock = mockStatic(AssetLoader.class);
    }

    @AfterEach
    void tearDownOuter() {
        // Dọn dẹp mock static sau mỗi lần test
        assetLoaderMock.close();
    }

    // --- CÁC TEST CẦN CONSTRUCTOR CHẠY THÀNH CÔNG ---

    @Nested
    @DisplayName("Khi Constructor chạy thành công")
    class WhenConstructorSucceeds {

        @BeforeEach
        void setUpHappyPath() {
            // Thiết lập trả về ảnh giả
            assetLoaderMock.when(() -> AssetLoader.loadImage("/images/Bullet.png"))
                           .thenReturn(mockBulletImage);
            
            // Tạo Bullet
            bullet = new Bullet(100, 200, 0, 0, BULLET_SPEED);
        }

        @Test
        @DisplayName("Constructor: Tính toán kích thước và gán thuộc tính chính xác")
        void testConstructorCalculations() {
            // Chiều rộng mong muốn
            final double TARGET_WIDTH = 10.0;

            // Tính toán chiều cao mong muốn thông qua tỉ lệ khung hình
            double aspectRatio = MOCK_IMG_HEIGHT / MOCK_IMG_WIDTH;
            double targetHeight = TARGET_WIDTH * aspectRatio;

            // Kiểm tra
            assertAll(
                () -> assertEquals(BULLET_SPEED, bullet.getSpeed(), "Tốc đổ phải được gán"),
                () -> assertEquals(TARGET_WIDTH, bullet.getWidth(), "Chiều rộng Bullet phải được tính đúng"),
                () -> assertEquals(targetHeight, bullet.getHeight(), "Chiều cao Bullet phải được tính đúng"),
                () -> assertEquals(mockBulletImage, bullet.getImage(), "Ảnh phải được gán")
            );
        }

        @Test
        @DisplayName("Logic: move() và update() Bullet di chuyển thẳng đứng")
        void testMoveAndUpdate() {
            // Tọa độ ban đầu
            double initialX = bullet.getX();
            double initialY = bullet.getY();

            // Gọi update() (sẽ gọi move())
            bullet.update();

            // Kiểm tra vị trí mới
            double expectedX = initialX;
            double expectedY = initialY - BULLET_SPEED;

            assertAll(
                () -> assertEquals(expectedX, bullet.getX(), "Vị trí X không đươc thay đổi"),
                () -> assertEquals(expectedY, bullet.getY(), "Vị trí Y phải giảm đi (di chuyển lên)")
            );
        }

        @Test
        @DisplayName("Logic: update() tự hủy khi chạm tường trên (Y < 0)")
        void testUpdate_Destroys_When_HittingTopWall() {
            
            // Đặt bullet ở vị trí Y=5. Sau khi move(), Y sẽ là 5 - 7 = -2 (tức < 0)
            bullet.setY(5.0);
            assertFalse(bullet.isDestroyed(), "Đạn chưa bị hủy ban đầu");

            // Di chuyển
            bullet.update();

            // Kiểm tra
            assertTrue(bullet.isDestroyed(), "Đạn phải tự hủy khi Y < 0");
        }

        @Test
        @DisplayName("Logic: checkCollision() phát hiện va chạm AABB")
        void testCheckCollision() {
            // Đặt kích thước cho Bullet
            bullet.setWidth(10);
            bullet.setHeight(40);
            // Như vậy bullet ở vị trí (100, 200) rộng 10, cao 40 => (x: 100-110, y: 200-240)

            // Tạo một viên gạch giả (Mock Brick)
            GameObject mockBrick = mock(GameObject.class);

            // Trường hợp 1: Va chạm (Brick ở 105, 210)
            when(mockBrick.getX()).thenReturn(105.0);
            when(mockBrick.getY()).thenReturn(210.0);
            when(mockBrick.getWidth()).thenReturn(40.0);
            when(mockBrick.getHeight()).thenReturn(20.0);
            assertTrue(bullet.checkCollision(mockBrick), "Phải phát hiện va chạm khi chồng lấp");

            // Trường hợp 2: Không va chạm (Brick ở 500, 500)
            when(mockBrick.getX()).thenReturn(500.0);
            when(mockBrick.getY()).thenReturn(500.0);
            when(mockBrick.getWidth()).thenReturn(40.0);
            when(mockBrick.getHeight()).thenReturn(20.0);
            assertFalse(bullet.checkCollision(mockBrick), "Không được va chạm khi hai vật thể ở xa nhau");

            // Trường hợp 3: Không va chạm (Chỉ chạm ở cạnh, không chòng lấp)
            when(mockBrick.getX()).thenReturn(110.0);
            when(mockBrick.getY()).thenReturn(200.0);
            assertFalse(bullet.checkCollision(mockBrick), "Không được va chạm khi chỉ chạm cạnh");

        }
    }

    // --- TEST KHI CONSTRUCTOR THẤT BẠI
    @Nested
    @DisplayName("Khi Constructor thất bại")
    class WhenConstructorFails {

        @Test
        @DisplayName("Constructor: Ném RuntimeException nếu không tải được ảnh")
        void testConstructorThrowExceptionFailure() {
            // Dạy mock thất bại
            assetLoaderMock.when(() -> AssetLoader.loadImage("/images/Bullet.png"))
                           .thenReturn(null);
            
            // Kiểm tra lỗi
            RuntimeException exception =  assertThrows(RuntimeException.class, () -> {
                new Bullet(100, 200, 0, 0, BULLET_SPEED);
            }, "Constructor phải ném RuntimeException nếu tải ảnh lỗi");

            // Kiểm tra thông báo
            assertTrue(exception.getMessage().contains("Lỗi không thể tải ảnh cho Bullet"));
        }
    }
}
