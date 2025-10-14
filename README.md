PHÂN CHIA CÔNG VIỆC:
👤 Tiến (Gameplay cơ bản)
•	Thiết kế & code:
o	GameObject, MovableObject
o	Paddle (điều khiển trái/phải, nhận power-up)
o	Ball (chuyển động, va chạm paddle/brick)
o	Renderer giao diện của Ball, Paddle.
o	Thiết kế LaserPaddlePowerUp, SuperBallPowerUp và hiển thị hai PowerUp này.
•	Logic chính: va chạm Ball-Paddle, tính toán tốc độ & hướng bóng.
•	Kiểm thử: test Ball-Paddle, di chuyển paddle, phản xạ vật lý.
•	Tài liệu: viết phần Use-case Ball & Paddle trong báo cáo UML.
________________________________________
👤 Tùng (Khối vật cản & power-up)
•	Thiết kế & code:
o	Brick, NormalBrick, StrongBrick
o	PowerUp, ExpandPaddlePowerUp, FastBallPowerup, MultiBallPowerUp.
•	Logic chính: tính điểm, HP brick, sinh power-up khi brick bị phá.
•	Kiểm thử: test Ball-Brick, phát sinh & áp dụng power-up.
•	Tài liệu: vẽ sequence diagram phá Brick và xử lý power-up.
•	Xử lý âm thanh khi bóng va chạm với brick và khi gạch vỡ. Xử lý âm thanh nền của game.
•	Renderer các loại gạch.
________________________________________
👤 Tuấn (Điều phối & giao diện)
•	Thiết kế & code:
o	GameManager (quản lý vòng đời game, lives, score, level)
o	Thiết kế GamePanel, GameWindow, Main
o	GameState, BrickType, PowerUpType (enum)
o	Renderer giao diện trò chơi, thiết kế logic khi nào game sẽ kết thúc.
•	Logic chính: vòng lặp game, chuyển trạng thái START → RUNNING → GAME_OVER.
•	Kiểm thử: test giao diện, hiển thị score/lives, load map.
•	Tài liệu: viết class diagram tổng thể và phần flow trạng thái game.
