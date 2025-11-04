package com.DevChickens.Arkanoid.input;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

/**
 * Lớp InputHandler là một "dumb listener" (bộ lắng nghe "ngốc").
 * <p>
 * Vai trò duy nhất của nó là bắt các sự kiện từ bàn phím và chuột,
 * sau đó lưu lại trạng thái của chúng.
 * <p>
 * Nó không chứa bất kỳ logic game nào. Các lớp khác sẽ hỏi lớp này về trạng thái input
 * và tự quyết định phải làm gì.
 *
 * @author Tuấn (DKCTuan)
 */
public class InputHandler extends KeyAdapter implements MouseListener, MouseMotionListener {

    // Trạng thái Phím (Nhấn giữ)
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // Trạng thái Phím (Nhấn 1 lần)
    private boolean pausePressed = false;
    private boolean launchPressed = false;
    private boolean confirmPressed = false;

    // Trạng thái Chuột
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean mouseClicked = false;
    private boolean isDragging = false;

    // Các hàm getter.
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }
    public boolean isDragging() { return isDragging; }

    public boolean consumePausePress() {
        boolean pressed = pausePressed;
        pausePressed = false;
        return pressed;
    }

    public boolean consumeLaunchPress() {
        boolean pressed = launchPressed;
        launchPressed = false;
        return pressed;
    }

    public boolean consumeConfirmPress() {
        boolean pressed = confirmPressed;
        confirmPressed = false;
        return pressed;
    }

    public Point consumeClick() {
        if (mouseClicked) {
            mouseClicked = false;
            return new Point(mouseX, mouseY);
        }
        return null;
    }


    // Các hàm listener được ghi đè từ AWT - Abstract Window Toolkit.
    /**
     * Được gọi khi một phím được nhấn.
     * Cập nhật trạng thái cho các cờ tương ứng.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;

            case KeyEvent.VK_P:
                pausePressed = true;
                break;
            case KeyEvent.VK_SPACE:
                launchPressed = true;
                break;
            case KeyEvent.VK_ENTER:
                confirmPressed = true;
                break;
        }
    }

    /**
     * Được gọi khi một phím được thả ra.
     * Chỉ cập nhật trạng thái cho các phím nhấn giữ.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
        }
    }

    /**
     * Được gọi khi chuột di chuyển (nhưng không nhấn).
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * Được gọi khi chuột được nhấn VÀ kéo.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        // Nếu chuột đang bị kéo, đây không phải là một cú "click".
        mouseClicked = false;
    }

    /**
     * Được gọi khi người dùng nhấn chuột.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        isDragging = true;
    }

    /**
     * Được gọi khi người dùng thả chuột.
     * Đây là nơi quyết định đó là "click" hay "drag".
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        if (isDragging) {
            mouseClicked = true;
            mouseX = e.getX(); // Cập nhật vị trí lần cuối
            mouseY = e.getY();
        }
        isDragging = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // Các hàm không sử dụng
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}