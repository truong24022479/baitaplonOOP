package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

public abstract class Enemy extends Component {
    static int TILE_SIZE = BombermanApp.TILE_SIZE;
    protected int[][] map;
    protected int MAP_WIDTH;
    protected int MAP_HEIGHT;
    public static final double ENEMY_SPEED=15;
    private boolean isMoving = false;
    private boolean isDead = false;

    public void initMap(int[][] map, int tileSize, int mapWidth, int mapHeight) {
        this.map = map;
        this.TILE_SIZE = tileSize;
        this.MAP_WIDTH = mapWidth;
        this.MAP_HEIGHT = mapHeight;
    }

    public boolean canMove(double tileX, double tileY) {
        int dy = (int) tileY;
        int dx = (int) tileX;
        return tileX >= 0 && tileX < MAP_WIDTH && tileY >= 0 && tileY < MAP_HEIGHT
                && map[dy][dx] == 0;
    }

//    public void removeEnemy() {
//        FXGL.getGameWorld().removeEntity(getEntity());

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
