package com.DevChickens.Arkanoid.enums;

/**
 * GameState định nghĩa các trạng thái khác nhau của game Arkanoid.
 * Được sử dụng trong GameManager để điều khiển luồng xử lý.
 */
public enum GameState {
    MENU,
    LEVEL_SELECT,
    PLAYING,
    PAUSED,
    GAME_OVER,
    VICTORY,
    NEXT_ROUND,
    HIGH_SCORES,
    SETTINGS
}


