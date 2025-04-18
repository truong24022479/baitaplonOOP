package org.example.demo;
/**
 * PHONG
 */

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
public class Balloon extends Enemy{
    private double vX = 0;
    private double vY = 0;
    private Entity balloon;
    private Random random = new Random();
    private double moveTargetX;
    private double moveTargetY;
    private boolean isMoving = false;

    @Override
    public void onAdded() {
        balloon = getEntity();
        BombermanApp app = (BombermanApp) FXGL.getApp();
        if (app instanceof BombermanApp) {
            BombermanApp bom = (BombermanApp) app;
            this.initMap(bom.getMap(), bom.TILE_SIZE, bom.MAP_WIDTH, bom.MAP_HEIGHT);
            moveBalloon();
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (isMoving) {
            balloon.translateX(vX * tpf);
            balloon.translateY(vY * tpf);

            if (Math.abs(balloon.getX() - moveTargetX) < 1 && Math.abs(balloon.getY() - moveTargetY) < 1) {
                balloon.setPosition(moveTargetX, moveTargetY); // snap to tile
                isMoving = false;
            }
        } else {
            moveBalloon();
        }
    }



    public void moveBalloon() {

        int tileX = (int) (balloon.getX() / TILE_SIZE);
        int tileY = (int) (balloon.getY() / TILE_SIZE);

        int direction = random.nextInt(4);
        int nextX = tileX;
        int nextY = tileY;

        switch (direction) {
            case 0 -> nextY--; // lên
            case 1 -> nextY++; // xuống
            case 2 -> nextX--; // trái
            case 3 -> nextX++; // phải
        }

        if (canMove(nextX, nextY)) {
            moveTargetX = nextX * TILE_SIZE;
            moveTargetY = nextY * TILE_SIZE;

            double dx = moveTargetX - balloon.getX();
            double dy = moveTargetY - balloon.getY();
            double length = Math.sqrt(dx * dx + dy * dy);

            vX = (dx / length) * ENEMY_SPEED;
            vY = (dy / length) * ENEMY_SPEED;

            isMoving = true;
        }
    }

    }

