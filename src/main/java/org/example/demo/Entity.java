package org.example.demo;

public class Entity{
    /**
     * Toạ độ x , y của đối tượng.
     */
    public int x, y;
    /**
     * Tốc độ của đối tượng
     */
    public int speed;

    /**
     * Constructor mặc định khởi tạo các giá trị ban đầu về 0.
     */
    public Entity() {
        this.x = 0;
        this.y = 0;
        this.speed = 0;
    }

    /**
     * Khởi tạo Entity với các giá trị cụ thể.
     * @param x Tọa độ x ban đầu.
     * @param y Tọa độ y ban đầu.
     * @param speed Tốc độ của vật ban đầu.
     */
    public Entity(int x,int y,int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }
}
