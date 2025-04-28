package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import java.io.IOException;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Bomb {
    private int x, y; // Tọa độ bom
    public static int explosionRadius = 1; // Bán kính nổ
    private boolean isExploded; // Trạng thái bom
    private Entity owner; // Người đặt bom
    private int[][] map; // Tham chiếu tới bản đồ
    private static int TILE_SIZE = BombermanApp.TILE_SIZE;

    public static int DELAY_BOMB_TIME = 2;
    public static int TIME_SHOW_EXPLOSION = 1;
    private int timer = 3; // Thời gian đếm ngược (giây)

    public static int ENEMY_NUMBERS_LEFT = GameInitializerMap.getNumOfBallooms()
            + GameInitializerMap.getNumOfOneals() + GameInitializerMap.getNumOfDolls();

    static BombAnimation bombAnimation;

    public Bomb(int x, int y, int timer, int explosionRadius, Entity owner, int[][] map) {
        this.x = x;
        this.y = y;
        this.timer = timer;
        this.explosionRadius = explosionRadius;
        this.isExploded = false;
        this.owner = owner;
        this.map = map;

        if (bombAnimation == null) {
            bombAnimation = new BombAnimation(map, explosionRadius); // Truyền explosionRadius
        }
    }

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

        bombAnimation.showExplosion(x * BombermanApp.TILE_SIZE, y * BombermanApp.TILE_SIZE, true, false, "");
        hitBomb(x, y);
        for (int d = 0; d < dir.length; d++) {
            int[] direction = dir[d];
            String directionStr = directions[d];
            boolean stopped = false;

            int nx = x + direction[0];
            int ny = y + direction[1];
            if (nx < 0 || nx >= map[0].length || ny < 0 || ny >= map.length || map[ny][nx] == 1) {
                continue;
            }

            for (int i = 1; i <= explosionRadius; i++) {
                nx = x + direction[0] * i;
                ny = y + direction[1] * i;

                if (nx >= 0 && nx < map[0].length && ny >= 0 && ny < map.length) {
                    if (map[ny][nx] == 1) {
                        stopped = true;
                        break;
                    }

                    boolean isLast = (i == explosionRadius);
                    if (!isLast && ny + direction[1] >= 0 && ny + direction[1] < map.length && nx + direction[0] >= 0 && nx + direction[0] < map[0].length) {
                        isLast = (map[ny + direction[1]][nx + direction[0]] == 1);
                    }

                    if (map[ny][nx] == 0) {
                        bombAnimation.showExplosion(nx * BombermanApp.TILE_SIZE, ny * BombermanApp.TILE_SIZE, false, isLast, directionStr);
                    } else if (map[ny][nx] == 2) {
                        map[ny][nx] = 0;
                        changeBrickToGrass(nx, ny);
                        bombAnimation.showExplosion(nx * BombermanApp.TILE_SIZE, ny * BombermanApp.TILE_SIZE, false, isLast, directionStr);
                        stopped = true;
                        break;
                    } // Dừng sau khi phá gạch
                    hitBomb(nx, ny);
                } else {
                    stopped = true;
                    break;
                }
            }
        }
    }

    public static void hitBomb(int nx, int ny) {
        double ex = Math.round((Player.getX() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
        double ey = Math.round((Player.getY() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
        double c = Math.abs(ex - (double) nx);
        double e = Math.abs(ey - (double) ny);
        if (c <= 0.95 && e <= 0.95) {
            BombermanApp.removePlayer();
            System.out.println("no banh xac");
        }

        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
            double fx = Math.round((enemy.getX() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
            double fy = Math.round((enemy.getY() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
            double a = Math.abs(fx - (double) nx);
            double b = Math.abs(fy - (double) ny);
            if (a <= 0.95 && b <= 0.95) {
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
        Bomb bomb = new Bomb(tileX, tileY, timer, explosionRadius, player, map);
        bombAnimation.showBombAnimation(tileX * TILE_SIZE, tileY * TILE_SIZE);
        bomb.activate(timer);
    }

//    private void bombImage(int nx, int ny) {
//        GamePlay controller;
//        try {
//            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
//            Parent root = loader.load();
//            controller = loader.getController();
//        } catch (IOException e) {
//            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
//        }
//        // Tạo bomb tại ô đứng
//        ImageView bombView = controller.getBombImageView();
//        bombView.setFitWidth(TILE_SIZE);
//        bombView.setFitHeight(TILE_SIZE);
//        bombView.setPreserveRatio(false);
//
//        Entity bomb = entityBuilder()
//                .type(EntityType.BOMB)
//                .at(nx * TILE_SIZE, ny * TILE_SIZE)
//                .zIndex(1)
//                .viewWithBBox(bombView)
//                .buildAndAttach();
//        runOnce(bomb::removeFromWorld, Duration.seconds(DELAY_BOMB_TIME));
//    }

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

        entityBuilder()
                .type(EntityType.GRASS)
                .at(nx * TILE_SIZE, ny * TILE_SIZE)
                .zIndex(0)
                .viewWithBBox(grassView)
                .buildAndAttach();
    }
}