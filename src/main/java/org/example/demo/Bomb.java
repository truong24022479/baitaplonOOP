package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
/// /////phong

public class Bomb {
    private int x, y; // Tọa độ bom
    private int timer; // Thời gian đếm ngược (giây)
    private int explosionRadius; // Bán kính nổ
    private boolean isExploded; // Trạng thái bom
    private Entity owner; // Người đặt bom
    private int[][] map; // Tham chiếu tới bản đồ

    public Bomb(int x, int y, int timer, int explosionRadius, Entity owner, int[][] map) {
        this.x = x;
        this.y = y;
        this.timer = timer;
        this.explosionRadius = explosionRadius;
        this.isExploded = false;
        this.owner = owner;
        this.map = map; // Lưu tham chiếu tới bản đồ
    }

    // Kích hoạt bom với cơ chế đếm ngược
    public void activate() {
        new Thread(() -> {
            try {
                Thread.sleep(timer * 1000); // Đếm ngược theo giây
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
        System.out.println("Bom phát nổ tại: " + x + ", " + y);
        affectSurrounding(); // Gây ảnh hưởng đến xung quanh
    }

    // Xử lý các đối tượng trong phạm vi nổ
    private void affectSurrounding() {
        for (int dx = -explosionRadius; dx <= explosionRadius; dx++) {
            for (int dy = -explosionRadius; dy <= explosionRadius; dy++) {
                int nx = x + dx;
                int ny = y + dy;
/// /////////////
                // Kiểm tra trong phạm vi bản đồ
                if (nx >= 0 && nx < map[0].length && ny >= 0 && ny < map.length) {
                     if (map[ny][nx] == 2) {
                         System.out.println("Phá hủy gạch tại: " + nx + ", " + ny);
                         map[ny][nx] = 0; // Loại bỏ gạch

                         FXGL.getGameWorld().getEntitiesByType(EntityType.BRICK).forEach(brick -> {
                             int brickX = (int) (brick.getX() / BombermanApp.TILE_SIZE);
                             int brickY = (int) (brick.getY() / BombermanApp.TILE_SIZE);
                             if (brickX == nx && brickY == ny) {
                                 FXGL.getGameWorld().removeEntity(brick);
                             }
                         });
                     }

                    FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
                        int ex = (int) (enemy.getX() / Enemy.TILE_SIZE);
                        int ey = (int) (enemy.getY() / Enemy.TILE_SIZE);

                        if (ex == nx && ey == ny) {
                            if (enemy.hasComponent(Balloon.class)) {
                                enemy.getComponent(Balloon.class).removeEnemy();
                                System.out.println("Kill Balloon");
                            } else if (enemy.hasComponent(Oneal.class)) {
                                enemy.getComponent(Oneal.class).removeEnemy();
                                System.out.println("Kill Oneal");
                            }
                        }
                    });

                    int px = (int) (BombermanApp.player.getX() / Enemy.TILE_SIZE);
                    int py = (int) (BombermanApp.player.getY() / Enemy.TILE_SIZE);

                    if (px == nx && py == ny) {
                        BombermanApp.removePlayer();
                    }
                }
            }
        }
    }

    // Getter để kiểm tra trạng thái bom
    public boolean isExploded() {
        return isExploded;
    }

    public static void setBomb(Entity player, int[][] map, int explosionRadius, int timer) {
        int tileX = (int) (player.getX() / 32);
        int tileY = (int) (player.getY() / 32);

        // Đặt bom tại tile hiện tại của player
        Bomb bomb = new Bomb(tileX, tileY, timer, explosionRadius, player, map);
        bomb.activate(); // Bắt đầu đếm ngược và nổ

        System.out.println("🔥 Bom đã được đặt tại: " + tileX + ", " + tileY);
    }

}

//package org.example.demo;
//
//import com.almasb.fxgl.dsl.FXGL;
//import com.almasb.fxgl.entity.Entity;
//import com.almasb.fxgl.entity.component.Component;
//import javafx.scene.image.ImageView;
//import javafx.util.Duration;
//
//import java.util.List;
//
//import static com.almasb.fxgl.dsl.FXGL.*;
//
//public class Bomb extends Component {
//
//    private static final int TILE_SIZE = 32;
//    private static final int EXPLOSION_RADIUS = 1;
//
//    private boolean isActivated = false;
//    private boolean isBomberCanEscape = true;
//
//    private int[][] map;
//    private int mapWidth, mapHeight;
//    private GamePlay controller;
//
//    public Bomb(int[][] map, int mapWidth, int mapHeight, GamePlay controller) {
//        this.map = map;
//        this.mapWidth = mapWidth;
//        this.mapHeight = mapHeight;
//        this.controller = controller;
//    }
//
//    @Override
//    public void onAdded() {
//        startActivationTimer();
//    }
//
//    public void startActivationTimer() {
//        runOnce(() -> {
//            isActivated = true;
//            isBomberCanEscape = false;
//            explode(); // Kích hoạt nổ
//        }, Duration.seconds(2));
//    }
//
//    private void explode() {
//        int tileX = (int)(getEntity().getX() / TILE_SIZE);
//        int tileY = (int)(getEntity().getY() / TILE_SIZE);
//
//        // Xoá hình bomb
//        getEntity().getViewComponent().clearChildren();
//
//        // Hiệu ứng nổ trung tâm
//        showExplosion(tileX, tileY);
//
//        // Nổ 4 hướng
//        int[][] directions = {{0,-1},{1,0},{0,1},{-1,0}};
//        for (int[] dir : directions) {
//            for (int i = 1; i <= EXPLOSION_RADIUS; i++) {
//                int nx = tileX + dir[0] * i;
//                int ny = tileY + dir[1] * i;
//
//                if (nx < 0 || nx >= mapWidth || ny < 0 || ny >= mapHeight)
//                    break;
//
//                int cell = map[ny][nx];
//
//                if (cell == 1) break; // tường cứng
//
//                showExplosion(nx, ny);
//
//                if (cell == 2) {
//                    map[ny][nx] = 0; // Xoá gạch
//
//                    // Xoá entity gạch
//                    FXGL.getGameWorld().getEntitiesByType(EntityType.BRICK).forEach(brick -> {
//                        int bx = (int)(brick.getX() / TILE_SIZE);
//                        int by = (int)(brick.getY() / TILE_SIZE);
//                        if (bx == nx && by == ny) {
//                            FXGL.getGameWorld().removeEntity(brick);
//                        }
//                    });
//
//                    // Tạo GRASS tại ô vừa phá
//                    ImageView grassView = controller.getGrass();
//                    grassView.setFitWidth(TILE_SIZE);
//                    grassView.setFitHeight(TILE_SIZE);
//                    grassView.setPreserveRatio(false);
//
//                    entityBuilder()
//                            .type(EntityType.GRASS)
//                            .at(nx * TILE_SIZE, ny * TILE_SIZE)
//                            .viewWithBBox(grassView)
//                            .buildAndAttach();
//
//                    break; // Nổ chặn lại ở brick
//                }
//            }
//        }
//
//        // Xoá enemy nếu trúng nổ
//        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
//            int ex = (int)(enemy.getX() / TILE_SIZE);
//            int ey = (int)(enemy.getY() / TILE_SIZE);
//            int px = tileX;
//            int py = tileY;
//
//            if (Math.abs(ex - px) + Math.abs(ey - py) <= EXPLOSION_RADIUS) {
//                enemy.getComponentOptional(Enemy.class).ifPresent(Enemy::removeEnemy);
//            }
//        });
//
//        // Xoá player nếu trúng nổ
//        Entity player = FXGL.getGameWorld().getSingleton(EntityType.PLAYER);
//        int px = (int)(player.getX() / TILE_SIZE);
//        int py = (int)(player.getY() / TILE_SIZE);
//        if (Math.abs(px - tileX) + Math.abs(py - tileY) <= EXPLOSION_RADIUS) {
//            FXGL.getGameWorld().removeEntity(player);
//            System.out.println("Player chết do bom!");
//        }
//
//        // Xoá bomb entity sau hiệu ứng
//        runOnce(() -> getEntity().removeFromWorld(), Duration.seconds(0.5));
//    }
//
//    private void showExplosion(int tileX, int tileY) {
//        ImageView explosionView = new ImageView(getAssetLoader().loadImage("sprites/explosion_horizontal.png"));
//        explosionView.setFitWidth(TILE_SIZE);
//        explosionView.setFitHeight(TILE_SIZE);
//        explosionView.setPreserveRatio(false);
//
//        Entity explosion = entityBuilder()
//                .at(tileX * TILE_SIZE, tileY * TILE_SIZE)
//                .view(explosionView)
//                .buildAndAttach();
//
//        runOnce(explosion::removeFromWorld, Duration.seconds(0.5));
//    }
//}
