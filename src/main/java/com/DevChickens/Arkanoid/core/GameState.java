package com.DevChickens.Arkanoid.core;

/**
 * Đại diện cho các trạng thái chính của trò chơi Arkanoid.
 * <p>
 * Mỗi trạng thái quyết định cách game loop hoạt động và cách người chơi tương tác.
 * </p>
 *
 * Luồng chính:
 * <pre>
 * START  -> RUNNING <-> PAUSED -> GAME_OVER
 * </pre>
 */
public enum GameState {
    /** Game ở trạng thái chờ bắt đầu. */
    START,

    /** Game đang chạy. */
    RUNNING,

    /** Game đang tạm dừng. */
    PAUSED,

    /** Game đã kết thúc. */
    GAME_OVER
}
