package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

public abstract class Enemy extends Component {
    static int TILE_SIZE = BombermanApp.TILE_SIZE;
    protected int[][] map;
    protected int MAP_WIDTH;
    protected int MAP_HEIGHT;
    public static double ENEMY_SPEED=15;
    protected boolean isMoving = false;
    protected boolean isDead = false;

    public void initMap(int[][] map, int tileSize, int mapWidth, int mapHeight) {
        this.map = map;
        this.TILE_SIZE = tileSize;
        this.MAP_WIDTH = mapWidth;
        this.MAP_HEIGHT = mapHeight;
    }

    public boolean canMove(int tileX, int tileY) {
        if (tileX < 0 || tileX >= MAP_WIDTH || tileY < 0 || tileY >= MAP_HEIGHT) {
            return false;
        }

        if (map[tileY][tileX] == 1 || map[tileY][tileX] == 2 || map[tileY][tileX] == 4) {
            return false;
        }

        for (Entity bomb : FXGL.getGameWorld().getEntitiesByType(EntityType.BOMB)) {
            int bombX = (int) (bomb.getX() / TILE_SIZE);
            int bombY = (int) (bomb.getY() / TILE_SIZE);
            if (bombX == tileX && bombY == tileY) {
                return false;
            }
        }
        return true;
    }

    public void enemyDie() {
        isMoving = false; // dừng di chuyển
        if (isDead) return;
        isDead = true;

        FXGL.getGameTimer().runOnceAfter(() -> {
            if (getEntity().isActive()) {
                getEntity().removeFromWorld();
            }
        }, Duration.seconds(0.5));
    }
}
