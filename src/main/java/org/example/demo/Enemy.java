package org.example.demo;

import java.awt.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import java.util.Random;

public abstract class Enemy extends Component {
    static int TILE_SIZE = 32;
    protected int[][] map;
    protected int MAP_WIDTH;
    protected int MAP_HEIGHT;


    public void initMap(int[][] map, int tileSize, int mapWidth, int mapHeight) {
        this.map = map;
        this.TILE_SIZE = tileSize;
        this.MAP_WIDTH = mapWidth;
        this.MAP_HEIGHT = mapHeight;
    }

    @Override
    public void onUpdate(double tpf) {
    }

    public boolean canMove(int tileX, int tileY) {
        return tileX >= 0 && tileX < MAP_WIDTH && tileY >= 0 && tileY < MAP_HEIGHT
               && map[tileY][tileX] == 0;
    }


}
