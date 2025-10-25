package com.DevChickens.Arkanoid.input;
import com.DevChickens.Arkanoid.core.GameManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class InputHandler extends KeyAdapter implements MouseListener, MouseMotionListener {

    private GameManager manager;

    public InputHandler(GameManager manager) {
        this.manager = manager;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                manager.onMoveLeftPressed();
                break;
            case KeyEvent.VK_RIGHT:
                manager.onMoveRightPressed();
                break;
            case KeyEvent.VK_P:
                manager.onPausePressed();
                break;
            case KeyEvent.VK_SPACE:
                manager.onLaunchPressed();
                break;
            case KeyEvent.VK_ENTER:
                manager.onConfirmPressed();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                manager.onMoveLeftReleased();
                break;
            case KeyEvent.VK_RIGHT:
                manager.onMoveRightReleased();
                break;
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        manager.onMouseMove(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        manager.onMouseClick(e.getX(), e.getY());
    }

    @Override public void mouseDragged(MouseEvent e) { mouseMoved(e); } // Coi kéo chuột như di chuột
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

}
