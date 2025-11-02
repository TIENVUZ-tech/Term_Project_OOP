package com.DevChickens.Arkanoid.entities.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics; // Cần import để override render

/**
 * Lớp kiểm thử cho lớp trừu tượng Brick.
 * Tạo lớp con TestBrick dể test.
 */
class BrickTest {

    /**
     * Lớp TestBrick, tuy chỉnh hitpoint để test.
     */
    private class TestBrick extends Brick {
        public TestBrick(double x, double y, int hitPoints) {
            super(x, y, 10, 10, hitPoints, "TestBrick");
        }

        @Override
        public void render(Graphics g) {

        }
    }

    private TestBrick singleHitBrick;
    private TestBrick multiHitBrick;

    @BeforeEach
    void setUp() {
        // Gạch này vỡ sau 1 lần bắn
        singleHitBrick = new TestBrick(0, 0, 1);
        // Gạch này cần 3 lần bắn
        multiHitBrick = new TestBrick(10, 10, 3);
    }

    @Test
    void testTakeHit_SingleHitBrick() {
        assertFalse(singleHitBrick.isDestroyed(), "Gạch không bị phá hủy lúc đầu");

        // Lần bắn đầu tiên
        singleHitBrick.takeHit();

        assertTrue(singleHitBrick.isDestroyed(), "Gạch 1 HP bị phá hủy sau 1 lần takeHit()");
    }

    @Test
    void testTakeHit_MultiHitBrick() {
        assertFalse(multiHitBrick.isDestroyed(), "Gạch không bị phá hủy lúc đầu");

        // Lần bắn 1
        multiHitBrick.takeHit();
        assertFalse(multiHitBrick.isDestroyed(), "Gạch 3 HP không bị phá hủy sau 1 lần takeHit()");

        // Lần bắn 2
        multiHitBrick.takeHit();
        assertFalse(multiHitBrick.isDestroyed(), "Gạch 3 HP không bị phá hủy sau 2 lần takeHit()");

        // Lần bắn 3
        multiHitBrick.takeHit();
        assertTrue(multiHitBrick.isDestroyed(), "Gạch 3 HP bị phá hủy sau 3 lần takeHit()");
    }

    @Test
    void testTakeHit_AfterDestroyed() {
        // Bắn gạch 1 HP
        singleHitBrick.takeHit();
        assertTrue(singleHitBrick.isDestroyed(), "Gạch đã bị phá hủy");

        // Thử bắn thêm lần nữa
        // Nếu hitPoints bị giảm xuống < 0
        singleHitBrick.takeHit();
        assertTrue(singleHitBrick.isDestroyed(), "Gạch vẫn bị phá hủy");
    }

    @Test
    void testBreakBrick() {
        assertFalse(multiHitBrick.isDestroyed(), "Gạch 3 HP không bị phá hủy lúc đầu");

        // Dùng Super Ball (hoặc hiệu ứng vỡ ngay)
        multiHitBrick.breakBrick();

        assertTrue(multiHitBrick.isDestroyed(), "Gạch bị phá hủy ngay lập tức bởi breakBrick()");
    }
}