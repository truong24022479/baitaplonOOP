package org.example.demo;

<<<<<<< HEAD
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public abstract class Enemy extends Component {
    static int TILE_SIZE = BombermanApp.TILE_SIZE;
=======
import java.awt.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import java.util.Random;

public abstract class Enemy extends Component {
    static int TILE_SIZE = 32;
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
    protected int[][] map;
    protected int MAP_WIDTH;
    protected int MAP_HEIGHT;
    public static final double ENEMY_SPEED=15;

<<<<<<< HEAD
=======

>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
    public void initMap(int[][] map, int tileSize, int mapWidth, int mapHeight) {
        this.map = map;
        this.TILE_SIZE = tileSize;
        this.MAP_WIDTH = mapWidth;
        this.MAP_HEIGHT = mapHeight;
    }

<<<<<<< HEAD
=======

//    public boolean canMove(int tileX, int tileY) {
//        return tileX >= 0 && tileX < MAP_WIDTH && tileY >= 0 && tileY < MAP_HEIGHT
//                && map[tileY][tileX] == 0;
//    }
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
    public boolean canMove(double tileX, double tileY) {
        int dy = (int) tileY;
        int dx = (int) tileX;
        return tileX >= 0 && tileX < MAP_WIDTH && tileY >= 0 && tileY < MAP_HEIGHT
                && map[dy][dx] == 0;
    }

<<<<<<< HEAD
    public void removeEnemy() {
        FXGL.getGameWorld().removeEntity(getEntity());

//        System.out.println("❌ Có enemy còn sống: ");
//        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(e -> {
//            System.out.println(" -> Enemy tại " + e.getX() + ", " + e.getY());
//        });

    }
=======


>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
}