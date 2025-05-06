package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.almasb.fxgl.dsl.FXGL.*;
import static org.example.demo.BombermanApp.*;
import static org.example.demo.Buff.playerStrong;
import static org.example.demo.GameInitializerMap.spawnBuff;
import static org.example.demo.GameInitializerMap.spawnPortal;

public class Bomb {
    private int x, y; // Tọa độ bom
    public static int explosionRadius = 1; // Bán kính nổ
    private boolean isExploded; // Trạng thái bom
    private Entity owner; // Người đặt bom
    protected static int[][] map; // Tham chiếu tới bản đồ
    private static int TILE_SIZE = BombermanApp.TILE_SIZE;

    public static int DELAY_BOMB_TIME = 2;
    public static int TIME_SHOW_EXPLOSION = 1;
    private int timer = 3; // Thời gian đếm ngược (giây)

    static BombAnimation bombAnimation;

    public static double PORTAL_CHANCE = 0.15; // Tỷ lệ xuất hiện portal (15%)
    public static double POWERUP_CHANCE = 0.25; // Tỷ lệ xuất hiện buff (25%)
    public static boolean portalSpawned = false;
    public static int BRICK_NUMS = 0;
    public static int remainingBuffsToSpawn = 6;

    public Bomb(int x, int y, int timer, Entity owner, int[][] map) {
        this.x = x;
        this.y = y;
        this.timer = timer;
        this.isExploded = false;
        this.owner = owner;
        this.map = map;

        if (bombAnimation == null) {
            bombAnimation = new BombAnimation(map); // Truyền explosionRadius
        }
    }

    static int k = 0;

    // Kích hoạt bom với cơ chế đếm ngược
    public void activate(int timeInSeconds) {
        new Thread(() -> {
            try {
                Thread.sleep(timeInSeconds * 1000); // Đếm ngược theo thời gian được truyền vào
                javafx.application.Platform.runLater(() -> {
                    explode(); // Gọi phương thức phát nổ
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Luồng bị gián đoạn: " + e.getMessage());
            }
        }).start();
    }

    // Phương thức phát nổ
    private void explode() {
        isExploded = true;
//        SoundManager.playExplosion();
        affectSurrounding(); // Gây ảnh hưởng đến xung quanh
    }

    private void affectSurrounding() {
        int[][] dir = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}};
        String[] directions = {"right", "down", "up", "left"};
        Set<Object> killed = new HashSet<>();

        BombAnimation.centerExplode(x * BombermanApp.TILE_SIZE, y * BombermanApp.TILE_SIZE);
        hitCenterBomb(x, y, killed);

        for (int d = 0; d < dir.length; d++) {
            int[] direction = dir[d];
            String directionStr = directions[d];
            for (int i = 1; i <= explosionRadius; i++) {
                int nx = x + direction[0] * i;
                int ny = y + direction[1] * i;

                if (map[ny][nx] == 1) {
                    break;
                }

                boolean isLast = (i == explosionRadius)
                        || (ny + direction[1] < 0 || ny + direction[1] >= map.length
                        || nx + direction[0] < 0 || nx + direction[0] >= map[0].length)
                        || (map[ny + direction[1]][nx + direction[0]] == 1);
                hitBomb(nx, ny, killed);
                bossHitBomb(nx, ny);
                if (map[ny][nx] == 0) {
                    bombAnimation.showExplosion(nx * BombermanApp.TILE_SIZE, ny * BombermanApp.TILE_SIZE, false, isLast, directionStr);
                } else if (map[ny][nx] == 2) {
                    map[ny][nx] = 0;
                    changeBrickToGrass(nx, ny);
                    bombAnimation.showExplosion(nx * BombermanApp.TILE_SIZE, ny * BombermanApp.TILE_SIZE, false, isLast, directionStr);
                    break;
                }
            }
        }
    }

    public static void bossHitBomb(int nx, int ny) {
        if(level==1) return;
        for (int i = 6; i <= 8; i++) {
            for (int j = 6; j <= 8; j++) {
                if (nx == i && ny == j) {
                    Boss.health--;
                    System.out.println("Boss health " + Boss.health);
                    Boss.spawnEnemy();
                }
            }
        }
    }

    public static void hitBomb(int nx, int ny, Set<Object> killed) {

        double ex = Math.round((Player.getX() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
        double ey = Math.round((Player.getY() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
        double c = Math.abs(ex - (double) nx);
        double e = Math.abs(ey - (double) ny);
        if (c <= 0.95 && e <= 0.95 && !killed.contains("player") && playerStrong == false) {
            killed.add("player");
            BombermanApp.removePlayer();
            System.out.println("no banh xac" + k++);
        }

        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
            if (!killed.contains(enemy)) {
                double fx = Math.round((enemy.getX() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
                double fy = Math.round((enemy.getY() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
                double a = Math.abs(fx - (double) nx);
                double b = Math.abs(fy - (double) ny);
                if (a <= 0.95 && b <= 0.95) {
                    killed.add(enemy);
                    if (enemy.hasComponent(Balloom.class)) {
                        enemy.getComponent(Balloom.class).balloomDie();
                        ENEMY_NUMBERS_LEFT--;
                        System.out.println("Kill Balloom\nenemy left " + ENEMY_NUMBERS_LEFT);
                    } else if (enemy.hasComponent(Oneal.class)) {
                        enemy.getComponent(Oneal.class).onealDie();
                        ENEMY_NUMBERS_LEFT--;
                        System.out.println("Kill Oneal\nenemy left " + ENEMY_NUMBERS_LEFT);
                    } else if (enemy.hasComponent(Doll.class)) {
                        enemy.getComponent(Doll.class).dollDie();
                        ENEMY_NUMBERS_LEFT--;
                        System.out.println("Kill Doll\nenemy left " + ENEMY_NUMBERS_LEFT);
                    } else if (enemy.hasComponent(Minvo.class)) {
                        enemy.getComponent(Minvo.class).minvoDie();
                        ENEMY_NUMBERS_LEFT--;
                        System.out.println("Kill Minvo\nenemy left " + ENEMY_NUMBERS_LEFT);
                    }
                }
            }
        });

    }

    public static void hitCenterBomb(int nx, int ny, Set<Object> killed) {
        double ex = Math.round((Player.getX() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
        double ey = Math.round((Player.getY() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
        double c = Math.abs(ex - (double) nx);
        double e = Math.abs(ey - (double) ny);
        if (c <= 0.95 && e <= 0.95 && !killed.contains("player") && playerStrong == false) {
            killed.add("player");
            BombermanApp.removePlayer();
            System.out.println("no banh xac" + k++);
        }

        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
            if (!killed.contains(enemy)) {
                double fx = Math.round((enemy.getX() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
                double fy = Math.round((enemy.getY() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
                double a = Math.abs(fx - (double) nx);
                double b = Math.abs(fy - (double) ny);
                if (a <= 0.95 && b <= 0.95) {
                    killed.add(enemy);
                    if (enemy.hasComponent(Balloom.class)) {
                        enemy.getComponent(Balloom.class).balloomDie();
                        ENEMY_NUMBERS_LEFT--;
                        System.out.println("Kill Balloom\nenemy left " + ENEMY_NUMBERS_LEFT);
                    } else if (enemy.hasComponent(Oneal.class)) {
                        enemy.getComponent(Oneal.class).onealDie();
                        ENEMY_NUMBERS_LEFT--;
                        System.out.println("Kill Oneal tại\nenemy left " + ENEMY_NUMBERS_LEFT);
                    } else if (enemy.hasComponent(Doll.class)) {
                        enemy.getComponent(Doll.class).dollDie();
                        ENEMY_NUMBERS_LEFT--;
                        System.out.println("Kill Doll\nenemy left " + ENEMY_NUMBERS_LEFT);
                    } else if (enemy.hasComponent(Minvo.class)) {
                        enemy.getComponent(Minvo.class).minvoDie();
                        ENEMY_NUMBERS_LEFT--;
                        System.out.println("Kill Minvo\nenemy left " + ENEMY_NUMBERS_LEFT);
                    } else if (enemy.hasComponent(Boss.class)) {
                        Boss.health--;
                        System.out.println("Boss health: " + Boss.health);
                    }
                }
            }
        });
    }

    public boolean isExploded() {
        return isExploded;
    }

    public static void setBomb(Entity player, int[][] map, int explosionRadius, int timer) {
        int tileX = (int) Math.round(player.getX() / 32.0);
        int tileY = (int) Math.round(player.getY() / 32.0);

        // Đặt bom tại tile hiện tại của player
        Bomb bomb = new Bomb(tileX, tileY, timer, player, map);
        bombAnimation.showBombAnimation(tileX * TILE_SIZE, tileY * TILE_SIZE);
        bomb.activate(timer);
    }

    private void changeBrickToGrass(int nx, int ny) {
        FXGL.getGameWorld().getEntitiesByType(EntityType.BRICK).forEach(brick -> {
            int bx = (int) (brick.getX() / TILE_SIZE);
            int by = (int) (brick.getY() / TILE_SIZE);
            if (bx == nx && by == ny) {
                FXGL.getGameWorld().removeEntity(brick);
            }
        });
        GamePlay controller;
        try {
            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
        }
        // Tạo GRASS tại ô vừa phá
        ImageView grassView = controller.getGrass();
        grassView.setFitWidth(TILE_SIZE);
        grassView.setFitHeight(TILE_SIZE);
        grassView.setPreserveRatio(false);

        entityBuilder().type(EntityType.GRASS)
                .at(nx * TILE_SIZE, ny * TILE_SIZE)
                .zIndex(0)
                .viewWithBBox(grassView)
                .buildAndAttach();
//
//        // int totalItemsNeeded = 7;
//        int itemsRemaining = (remainingBuffsToSpawn + (portalSpawned ? 0 : 1));
//
//        if (BRICK_NUMS <= itemsRemaining && itemsRemaining > 0) {
//            if (!portalSpawned) {
//                spawnPortal(nx, ny, controller);
//            } else if (remainingBuffsToSpawn > 0) {
//                spawnBuff(nx, ny, controller);
//            }
//        } else {
//            Random random = new Random();
//            if (!portalSpawned && random.nextDouble() < PORTAL_CHANCE) {
//                spawnPortal(nx, ny, controller);
//            } else if (remainingBuffsToSpawn > 0 && random.nextDouble() < POWERUP_CHANCE) {
//                spawnBuff(nx, ny, controller);
//            }
//        }
        if (level == 1) {
            int itemsRemaining = remainingBuffsToSpawn + (portalSpawned ? 0 : 1);
            Random random = new Random();

            // Ưu tiên spawn buff nếu còn buff cần spawn và số gạch còn lại đủ
            if (remainingBuffsToSpawn > 0 && BRICK_NUMS <= itemsRemaining + 6) {
                spawnBuff(nx, ny, controller);
            }
            // Spawn portal nếu đã đủ 6 buff và chưa có portal
            else if (!portalSpawned && remainingBuffsToSpawn == 0 && random.nextDouble() < PORTAL_CHANCE) {
                spawnPortal(nx, ny, controller);
            }
            // Nếu vẫn còn buff cần spawn nhưng không đủ ưu tiên, thử spawn với xác suất
            else if (remainingBuffsToSpawn > 0 && random.nextDouble() < POWERUP_CHANCE) {
                spawnBuff(nx, ny, controller);
            }
        }
    }
}