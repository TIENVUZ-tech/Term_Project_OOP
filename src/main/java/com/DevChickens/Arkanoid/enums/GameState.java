package com.DevChickens.Arkanoid.enums;

/**
 * Enum GameState định nghĩa các trạng thái khác nhau của game Arkanoid.
 * Dùng trong GameManager để điều phối vòng đời game.
 */
public enum GameState {
    STARTING,   // Game mới khởi động (màn hình intro hoặc chuẩn bị)
    RUNNING,    // Game đang chạy
    PAUSED,     // Game đang tạm dừng
    GAME_OVER,  // Người chơi thua hết mạng
    LEVEL_COMPLETE, // Người chơi đã phá hết gạch trong level
    VICTORY     // Thắng toàn bộ game (qua màn cuối)
}
