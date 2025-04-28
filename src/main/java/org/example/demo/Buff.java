package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class Buff {
    private int x, y;
    private Entity entity;
    private static final int TILE_SIZE = BombermanApp.TILE_SIZE;


    private static List<Integer> availableBuffs = new ArrayList<>();

    // Khởi tạo danh sách với các số từ 1 đến 6
    static {
        for (int i = 1; i <= 6; i++) {
            availableBuffs.add(i);
        }
    }


    public Buff(int x, int y, ImageView view) {
        this.x = x;
        this.y = y;

        if (view != null) {
            this.entity = entityBuilder()
                    .at(x * TILE_SIZE, y * TILE_SIZE)
                    .zIndex(1)
                    .viewWithBBox(view)
                    .buildAndAttach();
        }
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
        FXGL.getGameWorld().getEntitiesByType(EntityType.BUFF).forEach(buff -> {
            int bx = (int) (buff.getX() / TILE_SIZE);
            int by = (int) (buff.getY() / TILE_SIZE);
            if (bx == x && by == y) {
                FXGL.getGameWorld().removeEntity(buff);
            }
        });
    }

    public static void applySpeedUp() {
        // Bạn sẽ tự viết logic cho SPEED_UP
        System.out.println("Applied SPEED_UP buff");
    }

    public static void applyBombRange() {
        // Bạn sẽ tự viết logic cho BOMB_RANGE
        System.out.println("Applied BOMB_RANGE buff");
    }

    public static void applyBombPass() {
        // Bạn sẽ tự viết logic cho BOMB_PASS
        System.out.println("Applied BOMB_PASS buff");
    }

    public static void applyBombs() {
        // Bạn sẽ tự viết logic cho BOMBS
        System.out.println("Applied BOMBS buff");
    }

    public static void applyDetonator() {
        // Bạn sẽ tự viết logic cho DETONATOR
        System.out.println("Applied DETONATOR buff");
    }

    public static void applyWallPass() {
        // Bạn sẽ tự viết logic cho WALL_PASS
        System.out.println("Applied WALL_PASS buff");
    }

    public static void receiveBuff() {
        if (availableBuffs.isEmpty()) {
            System.out.println("No more buffs available!");
            return;
        }

        Random random = new Random();
        int index = random.nextInt(availableBuffs.size());
        int selectedBuff = availableBuffs.get(index);

        // Gọi hàm tương ứng với loại buff
        switch (selectedBuff) {
            case 1:
                applySpeedUp();
                break;
            case 2:
                applyBombRange();
                break;
            case 3:
                applyBombPass();
                break;
            case 4:
                applyBombs();
                break;
            case 5:
                applyDetonator();
                break;
            case 6:
                applyWallPass();
                break;
        }

        // Xóa loại buff đã chọn khỏi danh sách
        availableBuffs.remove(index);
        System.out.println("Buffs remaining: " + availableBuffs.size());
    }
}