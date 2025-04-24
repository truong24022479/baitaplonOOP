package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Bomb {
    private int x, y; // Tọa độ bom
    private int explosionRadius = 1; // Bán kính nổ
    private boolean isExploded; // Trạng thái bom
    private Entity owner; // Người đặt bom
    private int[][] map;// Tham chiếu tới bản đồ
    private static int TILE_SIZE = BombermanApp.TILE_SIZE;

    public static int DELAY_BOMB_TIME = 2;
    public static int TIME_SHOW_EXPLOSION = 1;
    private int timer = 3; // Thời gian đếm ngược (giây)

    public static int ENEMY_NUMBERS = GameInitializerMap.getNumOfBalloons()
            + GameInitializerMap.getNumOfOneals();

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
        //System.out.println("Bom phát nổ tại: " + x + ", " + y);
        affectSurrounding(); // Gây ảnh hưởng đến xung quanh
    }


    private void affectSurrounding() {
        int[][] dir = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}};
        showExplosion(x * BombermanApp.TILE_SIZE, y * BombermanApp.TILE_SIZE);
        for (int[] d : dir) {
            for (int i = 0; i <= explosionRadius; i++) {
                int nx = x + d[0] * i;
                int ny = y + d[1] * i;
                // Kiểm tra trong phạm vi bản đồ
                if (nx >= 0 && nx < map[0].length && ny >= 0 && ny < map.length) {
                    if (map[ny][nx] == 1) break;
                    if (map[ny][nx] == 0) {
                        //System.out.println("Khong pha dc tuong");
                        showExplosion(nx * BombermanApp.TILE_SIZE, ny * BombermanApp.TILE_SIZE);
                    } else if (map[ny][nx] == 2) {
                        //System.out.println("Phá hủy gạch tại: " + nx + ", " + ny);
                        map[ny][nx] = 0;// Loại bỏ gạch
                        //doi anh cua gach ve co
                        changeBrickToGrass(nx, ny);
                        showExplosion(nx * BombermanApp.TILE_SIZE, ny * BombermanApp.TILE_SIZE);
                    }
                    //no tai tam bom
                    //no quai tai 4 huong
                    FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
//                        double ex = (double) Math.round(enemy.getX() / Enemy.TILE_SIZE);
//                        double ey = (double) Math.round(enemy.getY() / Enemy.TILE_SIZE);
                        double ex = Math.round((enemy.getX() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
                        double ey = Math.round((enemy.getY() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
                        double a = Math.abs(ex - (double) nx);
                        double b = Math.abs(ey - (double) ny);
                        if (a <= 0.95 && b <= 0.95) {
                            if (enemy.hasComponent(Balloon.class)) {
                                enemy.getComponent(Balloon.class).removeEnemy();
                                ENEMY_NUMBERS--;
                                System.out.println("Kill Balloon\nenemy left " + ENEMY_NUMBERS);
                            } else if (enemy.hasComponent(Oneal.class)) {
                                enemy.getComponent(Oneal.class).removeEnemy();
                                ENEMY_NUMBERS--;
                                System.out.println("Kill Oneal\n nenemy left " + ENEMY_NUMBERS);
                            }
                        }

                    });
                    // Làm tròn tọa độ của player thành số thập phân với 2 chữ số sau dấu phẩy
                    double ex = Math.round((Player.getX() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
                    double ey = Math.round((Player.getY() / (double) BombermanApp.TILE_SIZE) * 100.0) / 100.0;
                    //System.out.println(ex + " " + ey + "\n" + nx + " " + ny);
                    double c = Math.abs(ex - (double) nx);
                    double e = Math.abs(ey - (double) ny);
                    if (c <= 0.95 && e <= 0.95) {
                        BombermanApp.removePlayer();
                        System.out.println("no banh xac");
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
        int tileX = (int) Math.round(player.getX() / 32.0);
        int tileY = (int) Math.round(player.getY() / 32.0);

        // Đặt bom tại tile hiện tại của player
        Bomb bomb = new Bomb(tileX, tileY, timer, explosionRadius, player, map);
        bomb.bombImage(tileX, tileY);
        bomb.activate(timer); // Bắt đầu đếm ngược và nổ

        //System.out.println("🔥 Bom đã được đặt tại: " + tileX + ", " + tileY);
    }

    private static void showExplosion(int x, int y) {
        ImageView explosionView = new ImageView(getAssetLoader().loadImage("sprites/explosion.png"));
        explosionView.setFitWidth(BombermanApp.TILE_SIZE);
        explosionView.setFitHeight(BombermanApp.TILE_SIZE);
        explosionView.setPreserveRatio(false);

        Entity explosion = entityBuilder()
                .at(x, y)
                .view(explosionView)
                .buildAndAttach();

        // Tự động xóa hiệu ứng nổ
        runOnce(explosion::removeFromWorld, Duration.seconds(TIME_SHOW_EXPLOSION));
    }

    private void bombImage(int nx, int ny) {
        GamePlay controller;
        try {
            FXMLLoader loader = new FXMLLoader(GameInitializerMap.class.getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
        }
        // Tạo bomb tại ô đứng
        ImageView bombView = controller.getBombImageView();
        bombView.setFitWidth(TILE_SIZE);
        bombView.setFitHeight(TILE_SIZE);
        bombView.setPreserveRatio(false);

        //BombermanApp.map[nx][ny]=2;
        Entity bomb = entityBuilder()
                .type(EntityType.BOMB)
                .at(nx * TILE_SIZE, ny * TILE_SIZE)
                .zIndex(1)
                .viewWithBBox(bombView)
                .buildAndAttach();
        runOnce(bomb::removeFromWorld, Duration.seconds(DELAY_BOMB_TIME));
        //BombermanApp.map[nx][ny]=0;
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

        entityBuilder()
                .type(EntityType.GRASS)
                .at(nx * TILE_SIZE, ny * TILE_SIZE)
                .zIndex(0)
                .viewWithBBox(grassView)
                .buildAndAttach();

    }


}