package com.DevChickens.Arkanoid.core;

import com.DevChickens.Arkanoid.enums.GameState;
import com.DevChickens.Arkanoid.input.InputHandler;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Lớp UIManager (Quản lý Giao diện Người dùng).
 * <p>
 * Lớp này thực hiện vai trò "Controller" khi đứng giữa InputHander và GameManager.
 * <p>
 * Trách nhiệm chính:
 * Lưu trữ trạng thái của Giao diện (vị trí nút, trang settings hiện tại).
 * Lấy input thô từ InputHander chuyển thành những hành động có ý nghĩa.
 * Gọi các hàm API trên GameManager để xử lý.
 *
 * @author Tuấn (DKCTuan)
 */
public class UIManager {

    // Tham chiếu đến các hệ thống cốt lõi
    private final GameManager gameManager;
    private final InputHandler inputHandler;

    // Các biến trạng thái nội bộ của UI.

    private int mouseX = 0;
    private int mouseY = 0;
    private GameState previousGameState; // Lưu lại trạng thái game trước khi về màn hình Settings.

    /**
     * Định nghĩa các trang con bên trong màn hình Settings.
     */
    public enum SettingsPage { MAIN, SOUND, CONTROLS }

    /** Trang Settings hiện tại đang được hiển thị. */
    private SettingsPage currentSettingsPage = SettingsPage.MAIN;

    // Tất cả các biến Rectangle của UI.
    private final Rectangle playButtonRect;
    private final Rectangle continueButtonRect;
    private final Rectangle highScoresButtonRect;
    private final Rectangle exitButtonRect;
    private final Rectangle backButtonRect;
    private final Rectangle pauseButtonRect;
    private final Rectangle pauseSettingsButtonRect;
    private final Rectangle menuSettingsButtonRect;
    private final Rectangle pauseContinueButton;
    private final Rectangle pauseRestartButton;
    private final Rectangle pauseExitButton;
    private final Rectangle settingsSoundButtonRect;
    private final Rectangle settingsBackRect;
    private final Rectangle sliderBgmRect;
    private final Rectangle sliderPaddleRect;
    private final Rectangle sliderBrickRect;
    private final Rectangle sliderWallRect;
    private final Rectangle sliderExplosionRect;
    private final Rectangle soundBackRect;

    /**
     * Hàm khởi tạo của UIManager.
     * Sẽ đưa các tham chiếu đến Manager và Handler,
     * đồng thời khởi tạo vị trí cho tất cả các nút UI.
     */
    public UIManager(GameManager gameManager, InputHandler inputHandler) {
        this.gameManager = gameManager;
        this.inputHandler = inputHandler;

        int GAME_WIDTH = GameManager.GAME_WIDTH;
        int GAME_HEIGHT = GameManager.GAME_HEIGHT;

        // Khởi tạo các nút ở Menu chính
        playButtonRect = new Rectangle();
        continueButtonRect = new Rectangle();
        highScoresButtonRect = new Rectangle();
        exitButtonRect = new Rectangle();
        backButtonRect = new Rectangle();
        menuSettingsButtonRect = new Rectangle();

        // Khởi tạo nút Pause
        int iconSize = 40;
        int padding = 10;
        pauseButtonRect = new Rectangle(GAME_WIDTH - iconSize - padding, padding, iconSize, iconSize);

        // Khởi tạo các nút trong màn hình Pause
        int buttonWidth = 250;
        int buttonHeight = 50;
        int centerX = GAME_WIDTH / 2 - (buttonWidth / 2);
        int gap = 20;
        int totalPauseButtonHeight = (buttonHeight * 4) + (gap * 3);
        int startY_Pause = GAME_HEIGHT / 2 - totalPauseButtonHeight / 2;

        pauseContinueButton = new Rectangle(centerX, startY_Pause, buttonWidth, buttonHeight);
        pauseRestartButton = new Rectangle(centerX, startY_Pause + (buttonHeight + gap), buttonWidth, buttonHeight);
        pauseSettingsButtonRect = new Rectangle(centerX, startY_Pause + (buttonHeight + gap) * 2, buttonWidth, buttonHeight);
        pauseExitButton = new Rectangle(centerX, startY_Pause + (buttonHeight + gap) * 3, buttonWidth, buttonHeight);

        // KHỞI TẠO RECT CHO SETTINGS
        int settingsBtnWidth = 300;
        int settingsBtnHeight = 60;
        int settingsCenterX = GAME_WIDTH / 2 - (settingsBtnWidth / 2);

        // Nút trong Main Settings
        settingsSoundButtonRect = new Rectangle(settingsCenterX, 250, settingsBtnWidth, settingsBtnHeight);
        settingsBackRect = new Rectangle(settingsCenterX, 300, settingsBtnWidth, settingsBtnHeight);

        // Nút trong Sound Settings (Sliders)
        int sliderWidth = 400;
        int sliderHeight = 20;
        int sliderCenterX = GAME_WIDTH / 2 - (sliderWidth / 2);

        sliderBgmRect = new Rectangle(sliderCenterX, 150, sliderWidth, sliderHeight);
        sliderPaddleRect = new Rectangle(sliderCenterX, 200, sliderWidth, sliderHeight);
        sliderBrickRect = new Rectangle(sliderCenterX, 250, sliderWidth, sliderHeight);
        sliderWallRect = new Rectangle(sliderCenterX, 300, sliderWidth, sliderHeight);
        sliderExplosionRect = new Rectangle(sliderCenterX, 350, sliderWidth, sliderHeight);
        soundBackRect = new Rectangle(settingsCenterX, 450, settingsBtnWidth, settingsBtnHeight);
    }

    /**
     * Hàm update chính của UIManager.
     * Được gọi 60 lần/giây từ GameManager.update
     * Sẽ lấy input từ InputHandler và xử lý nó.
     */
    public void processUIInput() {

        this.mouseX = inputHandler.getMouseX();
        this.mouseY = inputHandler.getMouseY();

        if (inputHandler.isDragging()) {
            handleMouseDrag(mouseX, mouseY);
        }

        Point clickPoint = inputHandler.consumeClick();
        if (clickPoint != null) {
            handleMouseClick(clickPoint.x, clickPoint.y);
        }
    }

    /**
     * Xử lý logic khi một cú click chuột được phát hiện
     * trong các trạng thái game khác nhau.
     */
    private void handleMouseClick(int x, int y) {

        GameState gameState = gameManager.getGameState();

        if (gameState == GameState.PLAYING) {
            if (pauseButtonRect.contains(x, y)) {
                gameManager.pauseGame();
            }
        }
        else if (gameState == GameState.PAUSED) {
            if (pauseButtonRect.contains(x, y)) {
                gameManager.resumeGame();
            } else if (pauseContinueButton.contains(x, y)) {
                gameManager.resumeGame();
            } else if (pauseRestartButton.contains(x, y)) {
                gameManager.restartRound();
            } else if (pauseSettingsButtonRect.contains(x, y)) {
                gameManager.playSound("ui_click");
                previousGameState = GameState.PAUSED;
                currentSettingsPage = SettingsPage.MAIN;
                gameManager.setGameState(GameState.SETTINGS);
            } else if (pauseExitButton.contains(x, y)) {
                gameManager.exitToMenu();
            }
        }
        else if (gameState == GameState.MENU) {
            // TH1: Đã có game đang chơi dở (hiện nút Continue)
            if (gameManager.isGameInProgress()) {
                if (continueButtonRect.contains(x, y)) {
                    gameManager.playSound("ui_click");
                    gameManager.setGameState(GameState.PAUSED);
                    gameManager.stopSound("bgm_menu");
                }
                else if (playButtonRect.contains(x, y)) {
                    // Chơi game mới.
                    gameManager.playSound("ui_click");
                    gameManager.startNewGame();
                }
                else if (highScoresButtonRect.contains(x, y)) {
                    gameManager.playSound("ui_click");
                    gameManager.navigateToHighScores();
                }
                else if (menuSettingsButtonRect.contains(x, y)) {
                    gameManager.playSound("ui_click");
                    previousGameState = GameState.MENU; // Lưu lại
                    currentSettingsPage = SettingsPage.MAIN;
                    gameManager.navigateToSettings();
                }
                else if (exitButtonRect.contains(x, y)) {
                    gameManager.playSound("ui_click");
                    System.exit(0);
                }
            }
            // TH2: Game mới hoàn toàn (không có nút Continue)
            else {
                if (playButtonRect.contains(x, y)) {
                    gameManager.playSound("ui_click");
                    gameManager.startNewGame();
                }
                else if (highScoresButtonRect.contains(x, y)) {
                    gameManager.playSound("ui_click");
                    gameManager.navigateToHighScores();
                }
                else if (menuSettingsButtonRect.contains(x, y)) {
                    gameManager.playSound("ui_click");
                    previousGameState = GameState.MENU;
                    currentSettingsPage = SettingsPage.MAIN;
                    gameManager.navigateToSettings();
                }
                else if (exitButtonRect.contains(x, y)) {
                    gameManager.playSound("ui_click");
                    System.exit(0);
                }
            }
        }
        else if (gameState == GameState.HIGH_SCORES) {
            if (backButtonRect.contains(x, y)) {
                gameManager.exitToMenu();
            }
        }
        else if (gameState == GameState.GAME_OVER || gameState == GameState.VICTORY) {
            // Chỉ cần click sẽ về menu.
            gameManager.playSound("ui_click");
            gameManager.initGame();
        }
        else if (gameState == GameState.SETTINGS) {
            // Phân nhánh logic dựa trên trang settings con
            switch (currentSettingsPage) {
                case MAIN:
                    if (settingsSoundButtonRect.contains(x, y)) {
                        gameManager.playSound("ui_click");
                        currentSettingsPage = SettingsPage.SOUND;
                    } else if (settingsBackRect.contains(x, y)) {
                        gameManager.playSound("ui_click");
                        gameManager.setGameState(previousGameState); // Quay về nơi đã gọi
                        if (previousGameState == GameState.MENU) {
                            gameManager.loopSound("bgm_menu"); // Bật lại nhạc nếu quay về Menu
                        }
                    }
                    break;
                case SOUND:
                    if (soundBackRect.contains(x, y)) {
                        gameManager.playSound("ui_click");
                        currentSettingsPage = SettingsPage.MAIN;
                    }
                    // Logic click để bấm con trượt
                    else if (sliderBgmRect.contains(x, y)) {
                        gameManager.playSound("ui_click");
                        gameManager.setVolumeBGM(calculateVolumeFromSlider(x, sliderBgmRect));
                    } else if (sliderPaddleRect.contains(x, y)) {
                        gameManager.playSound("ui_click");
                        gameManager.setVolumePaddle(calculateVolumeFromSlider(x, sliderPaddleRect));
                    } else if (sliderBrickRect.contains(x, y)) {
                        gameManager.playSound("ui_click");
                        gameManager.setVolumeBrick(calculateVolumeFromSlider(x, sliderBrickRect));
                    } else if (sliderWallRect.contains(x, y)) {
                        gameManager.playSound("ui_click");
                        gameManager.setVolumeWall(calculateVolumeFromSlider(x, sliderWallRect));
                    } else if (sliderExplosionRect.contains(x, y)) {
                        gameManager.playSound("ui_click");
                        gameManager.setVolumeExplosion(calculateVolumeFromSlider(x, sliderExplosionRect));
                    }
                    break;
                case CONTROLS:
                    break;
            }
        }
    }

    /**
     * Xử lý logic khi chuột được kéo.
     * Chỉ dùng cho các thanh trượt (slider) âm lượng.
     */
    private void handleMouseDrag(int x, int y) {
        if (gameManager.getGameState() != GameState.SETTINGS || currentSettingsPage != SettingsPage.SOUND) {
            return;
        }

        // Kiểm tra xem chuột đang kéo trên slider nào
        if (sliderBgmRect.contains(x, y)) {
            gameManager.setVolumeBGM(calculateVolumeFromSlider(x, sliderBgmRect));
        } else if (sliderPaddleRect.contains(x, y)) {
            gameManager.setVolumePaddle(calculateVolumeFromSlider(x, sliderPaddleRect));
        } else if (sliderBrickRect.contains(x, y)) {
            gameManager.setVolumeBrick(calculateVolumeFromSlider(x, sliderBrickRect));
        } else if (sliderWallRect.contains(x, y)) {
            gameManager.setVolumeWall(calculateVolumeFromSlider(x, sliderWallRect));
        } else if (sliderExplosionRect.contains(x, y)) {
            gameManager.setVolumeExplosion(calculateVolumeFromSlider(x, sliderExplosionRect));
        }
    }

    /**
     * Hàm để chuyển đổi tọa độ X của chuột thành giá trị âm lượng (từ 0.0f đến 1.0f).
     *
     * @param mouseX     Tọa độ X của chuột trên màn hình.
     * @param sliderRect Hình chữ nhật của thanh trượt (slider).
     * @return Giá trị float trong khoảng [0.0f, 1.0f].
     */
    private float calculateVolumeFromSlider(int mouseX, Rectangle sliderRect) {
        // 1. Tính vị trí tương đối của chuột bên trong thanh trượt (0 -> width)
        double relativeX = mouseX - sliderRect.getX();
        // 2. Tính tỷ lệ (0.0 -> 1.0)
        float volume = (float) (relativeX / sliderRect.getWidth());
        // Kẹp giá trị để không bấm quá được ra ngoài thanh trượt.
        return Math.max(0.0f, Math.min(1.0f, volume));
    }

    // Các hàm getter cho renderer.
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }
    public SettingsPage getCurrentSettingsPage() { return currentSettingsPage; }

    // ----- Getters cho tất cả các Hitbox của Nút -----

    public Rectangle getPlayButtonRect() { return playButtonRect; }
    public Rectangle getContinueButtonRect() { return continueButtonRect; }
    public Rectangle getHighScoresButtonRect() { return highScoresButtonRect; }
    public Rectangle getExitButtonRect() { return exitButtonRect; }
    public Rectangle getBackButtonRect() { return backButtonRect; }
    public Rectangle getPauseButtonRect() { return pauseButtonRect; }
    public Rectangle getPauseSettingsButtonRect() { return pauseSettingsButtonRect; }
    public Rectangle getMenuSettingsButtonRect() { return menuSettingsButtonRect; }
    public Rectangle getPauseContinueButton() { return pauseContinueButton; }
    public Rectangle getPauseRestartButton() { return pauseRestartButton; }
    public Rectangle getPauseExitButton() { return pauseExitButton; }
    public Rectangle getSettingsSoundButtonRect() { return settingsSoundButtonRect; }
    public Rectangle getSettingsBackRect() { return settingsBackRect; }
    public Rectangle getSliderBgmRect() { return sliderBgmRect; }
    public Rectangle getSliderPaddleRect() { return sliderPaddleRect; }
    public Rectangle getSliderBrickRect() { return sliderBrickRect; }
    public Rectangle getSliderWallRect() { return sliderWallRect; }
    public Rectangle getSliderExplosionRect() { return sliderExplosionRect; }
    public Rectangle getSoundBackRect() { return soundBackRect; }
}