package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

public class Oneal extends Enemy {
    private Entity oneal;
    private Entity player;
<<<<<<< HEAD
    private static final double ONEAL_SPEED = 30;
=======
    private static final double ONEAL_SPEED = 20;
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
    private double vX = 0;
    private double vY = 0;
    private double moveTargetX;
    private double moveTargetY;
    private boolean isMoving = false;

    public void onAdded() {
        oneal = getEntity();
        player = FXGL.getGameWorld().getSingleton(EntityType.PLAYER);
        BombermanApp app = (BombermanApp) FXGL.getApp();
        initMap(app.getMap(), app.TILE_SIZE, app.MAP_WIDTH, app.MAP_HEIGHT);
    }

    public void onUpdate(double tpf) {
        double distX = player.getX() - oneal.getX();
        double distY = player.getY() - oneal.getY();
        double distance = Math.sqrt(distX * distX + distY * distY);

        double onealSpeed = (distance < 3 * TILE_SIZE) ? ONEAL_SPEED : ENEMY_SPEED;

        if (isMoving) {
            oneal.translateX(vX * tpf);
            oneal.translateY(vY * tpf);

            if (Math.abs(oneal.getX() - moveTargetX) < 1 && Math.abs(oneal.getY() - moveTargetY) < 1) {
                oneal.setPosition(moveTargetX, moveTargetY); // snap to tile
                isMoving = false;
            }
            return;
        }

        int onealTileX = (int) (oneal.getX() / TILE_SIZE);
        int onealTileY = (int) (oneal.getY() / TILE_SIZE);

        int playerTileX = (int) player.getX() / TILE_SIZE;
        int playerTileY = (int) player.getY() / TILE_SIZE;

        int dx = Integer.compare(playerTileX, onealTileX);
        int dy = Integer.compare(playerTileY, onealTileY);

<<<<<<< HEAD
        int nextOnealTileX = onealTileX;
        int nextOnealTileY = onealTileY;
        if (dx != 0 && canMove((onealTileX + dx), onealTileY)) {
            nextOnealTileX = onealTileX + dx;
        } else if (dy != 0 && canMove(onealTileX, (onealTileY + dy))) {
            nextOnealTileY = onealTileY + dy;
        }
=======
        int nextOnealTileX = onealTileX + dx;
        int nextOnealTileY = onealTileY + dy;
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d

        if (canMove(nextOnealTileX, nextOnealTileY)) {
            moveTargetX = nextOnealTileX * TILE_SIZE;
            moveTargetY = nextOnealTileY * TILE_SIZE;

            double length = Math.sqrt((moveTargetX - oneal.getX()) * (moveTargetX - oneal.getX())
                    + (moveTargetY - oneal.getY()) * (moveTargetY - oneal.getY()));
<<<<<<< HEAD
            if (length == 0) return;
=======
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
            vX = ((moveTargetX - oneal.getX()) / length) * onealSpeed;
            vY = ((moveTargetY - oneal.getY()) / length) * onealSpeed;
            isMoving = true;
        }
<<<<<<< HEAD
    }
}
=======
        System.out.println("canMove = " + canMove(nextOnealTileX, nextOnealTileY));

        //double nextX = oneal.getX() + vX * tpf;
        //double nextY = oneal.getY() + vY * tpf;

        //int leftTile   = (int)(nextX / TILE_SIZE);
        //int rightTile  = (int)((nextX + TILE_SIZE - 1) / TILE_SIZE);
        //int topTile    = (int)(nextY / TILE_SIZE);
        //int bottomTile = (int)((nextY + TILE_SIZE - 1) / TILE_SIZE);

        //if (canMove(leftTile, topTile) &&
          //      canMove(rightTile, topTile) &&
             //   canMove(leftTile, bottomTile) &&
               // canMove(rightTile, bottomTile)) {

            //oneal.setPosition(nextX, nextY);
        //}



    }
}
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d
