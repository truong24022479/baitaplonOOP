package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import java.io.IOException;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class Buff {
    private BuffType type;
    private int x, y;
    private Entity entity;
    private static final int TILE_SIZE = BombermanApp.TILE_SIZE;

    public Buff(BuffType type, int x, int y, ImageView view) {
        this.type = type;
        this.x = x;
        this.y = y;

        if (view != null) {
            this.entity = entityBuilder()
                    .type(EntityType.BUFF)
                    .at(x * TILE_SIZE, y * TILE_SIZE)
                    .zIndex(1)
                    .viewWithBBox(view)
                    .with("powerUpType", type)
                    .buildAndAttach();
        }
    }

    public BuffType getType() {
        return type;
    }

    public void setType(BuffType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Entity getEntity() {
        return entity;
    }

    public static void ChangeBuffToGrass(int x, int y) {
       // BombermanApp.map[x][y] = 0;
        FXGL.getGameWorld().getEntitiesByType(EntityType.BUFF).forEach(buff -> {
            int bx = (int) (buff.getX() / TILE_SIZE);
            int by = (int) (buff.getY() / TILE_SIZE);
            if (bx == x && by == y) {
                FXGL.getGameWorld().removeEntity(buff);
            }
        });
    }
}