package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
public class Balloon extends Enemy{
    private Entity balloon;
    private Random random = new Random();

    @Override
    public void onAdded() {
        balloon = getEntity();
        BombermanApp app = (BombermanApp) FXGL.getApp();
        if (app instanceof BombermanApp) {
            BombermanApp bom = (BombermanApp) app;
            this.initMap(bom.getMap(), bom.TILE_SIZE, bom.MAP_WIDTH, bom.MAP_HEIGHT);
            FXGL.getGameTimer().runAtInterval(this::moveBalloon, Duration.seconds(0.5));
        }
    }


    public void moveBalloon() {
        System.out.println("Attempting move for balloon: " + balloon.getPosition()); // Added Print
        if (map == null) { // Add a check here too just in case
            System.err.println("Balloon cannot move: map reference is null!");
            return;
        }
        int tileX = (int) (balloon.getX() / TILE_SIZE);
        int tileY = (int) (balloon.getY() / TILE_SIZE);

        int direction = random.nextInt(4);
        switch (direction) {
            case 0:
                tileY--;
                break;
            case 1:
                tileY++;
                break;
            case 2:
                tileX--;
                break;
            case 3:
                tileX++;
                break;
        }
        if (canMove(tileX, tileY)) {
            balloon.setPosition(tileX * TILE_SIZE, tileY * TILE_SIZE);
        }
    }
}
