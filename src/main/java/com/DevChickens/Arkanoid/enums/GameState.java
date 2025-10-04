package com.DevChickens.Arkanoid.enums;

/**
 * GameState định nghĩa các trạng thái khác nhau của game Arkanoid.
 * Được sử dụng trong GameManager để điều khiển luồng xử lý.
 */
public enum GameState {
    MENU,       // Màn hình menu chính
    PLAYING,    // Đang chơi game
    PAUSED,     // Game bị tạm dừng
    GAME_OVER,  // Thua hết mạng
    VICTORY     // Hoàn thành màn chơi (thắng)
}

