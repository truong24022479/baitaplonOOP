package org.example.demo;

import org.example.demo.EntityType;

public class Entity {
    private int x, y;          // Tọa độ của thực thể
    private int speed;         // Tốc độ di chuyển
    private int width, height; // Kích thước của thực thể
    private EntityType type;   // Loại thực thể (PLAYER, ENEMY, BOMB, etc.)

    // Constructors
    public Entity() {}

    public Entity(int x, int y, int speed, int width, int height, EntityType type) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    // Getters and Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    // Method to update the position of the entity
    public void updatePosition(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    // Collision detection logic
    public boolean isColliding(Entity other) {
        return this.x < other.x + other.width &&
                this.x + this.width > other.x &&
                this.y < other.y + other.height &&
                this.y + this.height > other.y;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "x=" + x +
                ", y=" + y +
                ", speed=" + speed +
                ", width=" + width +
                ", height=" + height +
                ", type=" + type +
                '}';
    }
}
