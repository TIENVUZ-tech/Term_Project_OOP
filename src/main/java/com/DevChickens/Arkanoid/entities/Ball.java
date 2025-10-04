package com.DevChickens.Arkanoid.entities;
import java.awt.Graphics;

public class Ball extends MovableObject {
    /*Tốc độ của bóng. Đơn vị là pixel */
    private double speed;
    /*Hướng di chuyển của bóng theo phương ngang. */
    private int directionX;
    /*Hướng di chuyển của bóng theo phương dọc. */
    private int directionY;

    /**
     * Phương thức khởi tạo Ball 3 tham số đầu vào.
     * @param x (tọa độ x)
     * @param y (toạ độ y)
     * @param width (độ rộng)
     * @param height (độ cao)
     * @param dx (tốc độ di chuyển theo trục x)
     * @param dy (tốc độ di chuyển theo chiều y)
     * @param speed (tốc độ di chuyển)
     * @param directionX (hướng di chuyển theo phương ngang)
     * @param directionY (hướng di chuyển theo phương dọc)
     */
    public Ball(double x, double y, double width, double height, double dx, double dy,
     double speed, int directionX, int directionY) {
        super(x, y, width, height, dx, dy);
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
    }

    /**Phương thức setter và getter của speed. */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    /**Phương thức setter và getter của directionX. */
    public void setDirectionX(int directionX) {
        if (directionX == 0) { // đảm bảo luôn có hướng
            this.directionX = 1;
        } else {
            this.directionX = directionX;
        }
    }

    public int getDirectionX() {
        return this.directionX;
    }

    /**Phương thức setter và getter của directionY. */
    public void setDirectionY(int directionY) {
        if (directionY == 0) {
            this.directionY = 1;
        } else {
            this.directionY = directionY;
        }
    }

    public int getDirectionY() {
        return this.directionY;
    }

    @Override
    public void move() {
        /*Di chuyển bóng dựa vào speed và hướng. */
        setX(getX() + speed * directionX); // cập nhật vị trí trên trục x.
        setY(getY() + speed * directionY); // cập nhật vị trí trên trục y.
    }

    @Override
    public void update() {
        /*Đảm bảo bóng tự chạy. */
        move();
    }

    @Override
    public void render() {}

    @Override
    public void render(Graphics g) {}

    /**
     * Phương thức checkCollistion().
     * Phương thức này dùng để kiểm tra 2 hình chữ nhật có giao nhau không (AABB collision).
     * Lưu ý: Mặc định, tọa độ (x, y) của vật thể là góc trên bên trái của hình chữ nhật bao quanh vật thể.
     * @param other
     * @return true (nếu bóng va chạm với vật thể).
     * @return false (nếu bóng không va chạm với vật thể).
     */
    public boolean checkCollision(GameObject other) {
        boolean conditionOne = this.getX() < other.getX() + other.getWidth();
        boolean conditionTwo = this.getX() + this.getWidth() > other.getX();
        boolean conditionThree = this.getY() < other.getY() + other.getHeight();
        boolean conditionFour = this.getY() + this.getHeight() > other.getY();
        return conditionOne && conditionTwo && conditionThree && conditionFour;
    }

    /**
     * Phương thức bounceOff().
     * Xử lý logic bật ngược (đổi hướng) khi bóng va chạm với một vật thể khác (ví dụ: Paddle, Brick, tường).
     * Đối với khung trò chơi thì gốc tọa độ nằm ở góc trên bên trái.
     * @param other (đối tượng GameObject)
     */
    public void bounceOff(GameObject other) {
        /*Tính toán lại tâm bóng.*/
        double ballCentreX = this.getX() + this.getWidth()/2;
        double ballCentreY = this.getY() + this.getHeight()/2;

        /*Tính toán lại tâm của vật thể. */
        double otherCentreX = other.getX() + other.getWidth()/2;
        double otherCentreY = other.getY() + other.getHeight()/2;

        /*dx là khoảng cách giữa tâm bóng và tâm vật thể theo phương ngang, dx > 0 bóng nằm bên phải tâm vật thể
         * và ngược lại.
         */
        double dx = ballCentreX - otherCentreX;

        /*dy là khoảng cách giữa tâm bóng và tâm vật thể theo phương dọc. dy > 0 bóng nằm dưới vật thể
         * và ngược lại.
         */
        double dy = ballCentreY - otherCentreY;

        if (Math.abs(dx) > Math.abs(dy)) { // bóng có xu hướng di chuyển theo phương ngang nhiều hơn
            directionX = - directionX;
        } else {
            directionY = -directionY;
        }
    }

    /**
     * phương thức thay đổi tốc độ của quả bóng.
     * @param factor hệ số thay đổi tốc độ.
     */
    public void multiplySpeed(double factor) {
        this.setDx(this.getDx() * factor);
        this.setDy(this.getDy() * factor);
    }
}
