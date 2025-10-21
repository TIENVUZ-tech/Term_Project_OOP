package com.DevChickens.Arkanoid.input;

import com.DevChickens.Arkanoid.core.GameManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {

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
    }
}
