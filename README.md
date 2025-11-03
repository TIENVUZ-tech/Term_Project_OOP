👤 Tiến: Lập trình Gameplay Cốt lõi (Core Gameplay)
Chịu trách nhiệm chính về các cơ chế tương tác trực tiếp của người chơi và các thực thể động trong game.
•	Phát triển Lớp (Class Development):
o	Xây dựng các lớp cơ sở: GameObject, MovableObject.
o	Xây dựng các lớp gameplay chính: Paddle, Ball, và Bullet.
o	Phát triển các PowerUp liên quan đến Paddle/Ball: LaserPaddlePowerUp (GunPaddle), SuperBallPowerUp.
•	Lập trình Logic (Logic Programming):
o	Phát triển thuật toán cốt lõi(có sự hỗ trợ của Tuấn): va chạm giữa Ball và Paddle.
o	Thiết kế logic tính toán tốc độ và góc nảy của bóng.
•	Thiết kế & Hình ảnh (Design & Assets):
o	Tìm kiếm, thiết kế và tích hợp hình ảnh (render) cho Ball, Paddle, và các loại PowerUp.
o   Cắt và xóa back ground cho ảnh.
•	Kiểm thử (Testing):
o	Viết Unit Test cho các lớp: BulletTest, PaddleTest, BallTest.
•	Tài liệu (Documentation):
o	Soạn thảo Use-case cho các đối tượng: Ball, Bullet, Paddle, LaserPaddlePowerUp, SuperBallPowerUp.
________________________________________
👤 Tùng: Lập trình Môi trường & Hệ thống Phụ trợ (Environment & Sub-systems)
Chịu trách nhiệm về các vật cản, hiệu ứng, và các hệ thống hỗ trợ như âm thanh và các PowerUp đa dạng.
•	Phát triển Lớp (Class Development):
o	Phát triển hệ thống Gạch (Brick): Brick (lớp cha), NormalBrick, StrongBrick, QuiteBrick, ExplosiveBrick.
o	Xây dựng BrickFactory để tạo gạch theo màn chơi.
o	Phát triển hệ thống PowerUp: PowerUp (lớp cha), ExpandPaddlePowerUp, FastBallPowerup, MultiBallPowerUp.
o	Phát triển hiệu ứng: Explosion (vụ nổ).
o   Tìm ảnh cho các loại gạch, Ball, PowerUp.
•	Phát triển Hệ thống (System Development):
o	Xây dựng SoundManager: Xử lý toàn bộ âm thanh trong game (nhạc nền, va chạm bóng, va chạm tường, gạch vỡ).
•	Thiết kế & Hình ảnh (Design & Assets):
o	Tích hợp hình ảnh (render) cho tất cả các loại gạch.
•	Kiểm thử (Testing):
o	Viết Unit Test cho các lớp: BrickTest và PowerUpTest (đại diện).
•	Tài liệu (Documentation):
o	Vẽ Sequence Diagram (Sơ đồ tuần tự) cho kịch bản "Phá gạch".
o	Soạn thảo Use-case cho các đối tượng: Brick, Explosion và 3 PowerUp phụ trách.
________________________________________
👤 Tuấn: Kiến trúc Hệ thống & Quản lý Trạng thái (System Architecture & State Management)
Chịu trách nhiệm điều phối tổng thể, kiến trúc khung sườn của game, và quản lý luồng trạng thái của trò chơi.
•	Phát triển Lớp (Class Development):
o	Xây dựng các lớp quản lý cốt lõi: GameManager (điều phối chính), CollisionManager.
o	Xây dựng các lớp khung sườn (framework): Main, GameWindow, GamePanel, InputHandler.
o	Phát triển hệ thống màn chơi: LevelLoader (tải map) và GameState (quản lý trạng thái).
•	Lập trình Logic (Logic Programming):
o	Thiết kế và triển khai vòng lặp game (Game Loop).
o	Quản lý luồng trạng thái game (MENU -> PLAYING -> PAUSED -> GAME_OVER...).
o	Phát triển logic tính điểm, quản lý mạng sống (lives), và điều kiện kết thúc game.
•	Thiết kế & Hình ảnh (Design & Assets):
o	Tích hợp hình ảnh (render) cho giao diện người dùng (UI) chung: Điểm số, Mạng sống, Menu, màn hình GameOver.
•	Kiểm thử (Testing):
o	Test tích hợp (Integration Test): CollisionManagerTest, GameManagerTest.
•	Tài liệu (Documentation):
o	Vẽ Class Diagram (Sơ đồ lớp) tổng thể cho toàn bộ dự án.

