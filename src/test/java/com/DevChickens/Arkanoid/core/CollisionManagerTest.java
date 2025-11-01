package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.entities.GameObject;
import com.DevChickens.Arkanoid.entities.MovableObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Graphics;

/**
 * Test cho lớp tiện ích CollisionManager.
 */
class CollisionManagerTest {

    /**
     * Lớp Mock để test.
     */
    class MockGameObject extends MovableObject {

        public MockGameObject(double x, double y, double width, double height) {
            super(x, y, width, height, 0, 0);
        }

        @Override
        public void move() {
        }

        @Override
        public void render(Graphics g) {
        }

        @Override
        public void update() {
        }
    }

    // Một vật thể cố định để làm mốc
    private GameObject stationaryObject;

    @BeforeEach
    void setUp() {
        stationaryObject = new MockGameObject(100, 100, 30, 30);
    }

    // Test checkCollision (AABB cơ bản)
    @Nested
    @DisplayName("Tests for checkCollision (AABB)")
    class CheckCollisionTests {

        @Test
        void testCollision_ObjectsOverlap() {
            GameObject overlappingObject = new MockGameObject(120, 120, 10, 10);
            assertTrue(CollisionManager.checkCollision(stationaryObject, overlappingObject), "Phải va chạm (B nằm trong A)");
        }

        @Test
        void testCollision_ObjectsDoNotOverlap_X_Axis() {
            GameObject rightObject = new MockGameObject(130, 100, 10, 10);
            assertFalse(CollisionManager.checkCollision(stationaryObject, rightObject), "Không va chạm (B ở ngay cạnh phải A)");
        }

        @Test
        void testCollision_ObjectsDoNotOverlap_Y_Axis() {
            GameObject bottomObject = new MockGameObject(100, 130, 10, 10);
            assertFalse(CollisionManager.checkCollision(stationaryObject, bottomObject), "Không va chạm (B ở ngay cạnh dưới A)");
        }

        @Test
        void testCollision_ObjectIsInside() {
            GameObject insideObject = new MockGameObject(110, 110, 5, 5);
            assertTrue(CollisionManager.checkCollision(stationaryObject, insideObject), "Phải va chạm (B nằm lọt trong A)");
        }
    }

    // Test hàm getCollisionDirection (Overlap)
    @Nested
    @DisplayName("Tests for getCollisionDirection (Overlap)")
    class GetCollisionDirectionTests {

        private GameObject mainObject;

        @BeforeEach
        void setupMainObject() {
            mainObject = new MockGameObject(100, 100, 40, 40);
        }

        @Test
        @DisplayName("Va chạm DỌC (Bóng đập từ trên xuống)")
        void testDirection_VerticalCollision() {
            // Vật thể test: (110, 130), Kích thước (20, 20). Tâm: (120, 140)
            // dx = 0, dy = 20
            // combinedHalfWidth = 20 + 10 = 30
            // combinedHalfHeight = 20 + 10 = 30
            // overlapX = 30 - 0 = 30
            // overlapY = 30 - 20 = 10
            // overlapY (10) < overlapX (30) -> VA CHẠM DỌC
            GameObject testObject = new MockGameObject(110, 130, 20, 20);

            assertEquals(CollisionManager.CollisionSide.VERTICAL,
                    CollisionManager.getCollisionDirection(mainObject, testObject));
        }

        @Test
        @DisplayName("Va chạm NGANG (Bóng đập từ bên trái)")
        void testDirection_HorizontalCollision() {
            // Vật thể test: (130, 110), Kích thước (20, 20). Tâm: (140, 120)
            // dx = 20, dy = 0
            // combinedHalfWidth = 20 + 10 = 30
            // combinedHalfHeight = 20 + 10 = 30
            // overlapX = 30 - 20 = 10
            // overlapY = 30 - 0 = 30
            // overlapX (10) < overlapY (30) -> VA CHẠM NGANG
            GameObject testObject = new MockGameObject(130, 110, 20, 20);

            assertEquals(CollisionManager.CollisionSide.HORIZONTAL,
                    CollisionManager.getCollisionDirection(mainObject, testObject));
        }

        @Test
        @DisplayName("Va chạm GÓC (Bóng đập chéo góc)")
        void testDirection_CornerCollision() {
            // Vật thể test: (130, 130), Kích thước (20, 20). Tâm: (140, 140)
            // dx = 20, dy = 20
            // combinedHalfWidth = 20 + 10 = 30
            // combinedHalfHeight = 20 + 10 = 30
            // overlapX = 30 - 20 = 10
            // overlapY = 30 - 20 = 10
            // overlapX == overlapY -> VA CHẠM GÓC
            GameObject testObject = new MockGameObject(130, 130, 20, 20);

            assertEquals(CollisionManager.CollisionSide.CORNER,
                    CollisionManager.getCollisionDirection(mainObject, testObject));
        }
    }
}