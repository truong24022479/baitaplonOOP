package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getDialogService;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static org.example.demo.Bomb.explosionRadius;
import static org.example.demo.BombermanApp.*;

public class Buff {
    private int x, y;
    private Entity entity;
    private static final int TILE_SIZE = BombermanApp.TILE_SIZE;


    static List<Integer> availableBuffs = new ArrayList<>();


    public static void createBuff() {
        availableBuffs.clear();
        for (int i = 1; i <= 6; i++) {
            availableBuffs.add(i);
        }
    }

    public static void resetBuff() {
        Player.PLAYER_SPEED = 1;
        explosionRadius = 1;
        //Buff.timeSetBomb = 2000;
        Buff.playerStrong = false;
        ENEMY_NUMBERS_LEFT = numOfBallooms + numOfMinvos + numOfDolls + numOfOneals;
        Bomb.portalSpawned = false;
        Bomb.BRICK_NUMS = 0;
        Bomb.remainingBuffsToSpawn = 6;

        Enemy.ENEMY_SPEED = 15;
        Oneal.ONEAL_SPEED = 30;
        Doll.DOLL_SPEED = 15;
        Minvo.MINVO_SPEED = 40;

//        Player.PLAYER_SPEED=2;
//        Buff.timeSetBomb = 500;
//        explosionRadius = 2;
    }

    public static void printBuff(String s) {
        if (availableBuffs.size() == 1) {
            System.out.println();
            getDialogService().showMessageBox(s + "\nNo more buffs available!" + "\nPress OK to continue.", () -> {
            });
            return;
        }
        getDialogService().showMessageBox(s + "\nBuffs remaining: " + (availableBuffs.size() - 1) + "\nPress OK to continue.", () -> {
        });
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
        printBuff("SPEED_UP");
        Player.PLAYER_SPEED = 1.5;
    }

    public static void applyBombRange() {
        // Bạn sẽ tự viết logic cho BOMB_RANGE
        printBuff("BOMB_RANGE");
        explosionRadius = 2;
    }

    public static boolean playerStrong = false;

    public static void applyBombPass() {
        // Bạn sẽ tự viết logic cho BOMB_PASS
        printBuff("You will not die because of explosion.");
        playerStrong = true;
    }

    public static void applySpawnEnemies() {
        // Bạn sẽ tự viết logic cho BOMBS
        printBuff("Enemies spawned");
        GameInitializerMap.spawnBalloom(1);
        GameInitializerMap.spawnOneal(1);
        GameInitializerMap.spawnDoll(1);
        GameInitializerMap.spawnMinvo(1);
        ENEMY_NUMBERS_LEFT += 4;
    }

    public static long timeSetBomb = 2000;

    public static void speedDown() {
        // Bạn sẽ tự viết logic cho DETONATOR
        printBuff("Enemy will be faster in next 1 minute.");
        final long DURATION_MS = 15 * 1000; // 1 phút = 60 giây
        Enemy.ENEMY_SPEED *= 3;
        Oneal.ONEAL_SPEED += 20;
        Doll.DOLL_SPEED *= 3;
        Minvo.MINVO_SPEED *= 1.5;

        new Thread(() -> {
            try {
                Thread.sleep(DURATION_MS);
                javafx.application.Platform.runLater(() -> {
                    // Khôi phục tốc độ ban đầu
                    Enemy.ENEMY_SPEED /= 3;
                    Oneal.ONEAL_SPEED -= 20;
                    Doll.DOLL_SPEED /= 3;
                    Minvo.MINVO_SPEED /= 1.5;
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted while restoring player speed: " + e.getMessage());
            }
        }).start();
    }

    /**
     * last buff
     */
    public static void applyWallPass() {
        // Bạn sẽ tự viết logic cho WALL_PASS
        printBuff("Nothing. Lucky next time!");

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
                applySpawnEnemies();
                break;
            case 5:
                speedDown();
                break;
            case 6:
                applyWallPass();
                break;
        }

        availableBuffs.remove(index);
        SoundManager.playGetBuffs();
        System.out.println("Buffs remaining: " + availableBuffs.size());
    }
}