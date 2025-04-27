package org.example.demo;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.ImageView;
import java.util.Random;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
public class GameInitializerMap {
    private static final int TILE_SIZE = BombermanApp.TILE_SIZE;
    private static final int MAP_WIDTH = BombermanApp.MAP_WIDTH;
    private static final int MAP_HEIGHT = BombermanApp.MAP_HEIGHT;
    private static int numOfOneals = 3;
    private static int numOfBalloons = 5;

    public static int getNumOfBalloons() {
        return numOfBalloons;
    }

    public static int getNumOfOneals() {
        return numOfOneals;
    }

    public static void initializeMap() {
        BombermanApp.map = new int[MAP_HEIGHT][MAP_WIDTH]; // Khởi tạo map ở đây
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                if (row == 0 || row == MAP_HEIGHT - 1 || col == 0 || col == MAP_WIDTH - 1) {
                    BombermanApp.map[row][col] = 1; // Tường xung quanh
                } else if (row % 2 == 0 && col % 2 == 0) {
                    BombermanApp.map[row][col] = 1; // Khối không thể phá hủy
                } else {
                    BombermanApp.map[row][col] = (Math.random() > 0.7) ? 2 : 0; // Ngẫu nhiên tường gạch hoặc đường đi
                }
            }
        }
        BombermanApp.map[1][1] = 0;
        BombermanApp.map[1][2] = 0;
        BombermanApp.map[2][1] = 0;
        BombermanApp.map[MAP_WIDTH - 2][MAP_HEIGHT - 2] = 4;
    }

    public static void spawnBalloom(GamePlay controller) { // Nhận controller làm tham số
        if (controller == null) {
            System.err.println("LỖI: GamePlay controller chưa được khởi tạo khi gọi spawnBalloom().");
            return;
        }
        Random random = new Random();

        for (int i = 0; i < numOfBalloons; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getBalloomImageView();
            if (enemyView != null) {
                enemyView.setFitWidth(TILE_SIZE);
                enemyView.setFitHeight(TILE_SIZE);
                enemyView.setPreserveRatio(false);

                entityBuilder()
                        .type(EntityType.ENEMY)
                        .at(y * TILE_SIZE, x * TILE_SIZE)
                        .zIndex(10)
                        .viewWithBBox(enemyView)
                        .with(new Balloon())
                        .buildAndAttach();
            } else {
                System.err.println("LỖI: balloomImageView từ controller là null.");
            }
        }
    }

    public static void spawnOneal(GamePlay controller) { // Nhận controller làm tham số
        if (controller == null) {
            System.err.println("LỖI: GamePlay controller chưa được khởi tạo khi gọi spawnOneal().");
            return;
        }
        Random random = new Random();

        for (int i = 0; i < numOfOneals; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (BombermanApp.map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getOnealImageView();
            if (enemyView != null) {
                enemyView.setFitWidth(TILE_SIZE);
                enemyView.setFitHeight(TILE_SIZE);
                enemyView.setPreserveRatio(false);

                entityBuilder()
                        .type(EntityType.ENEMY)
                        .at(y * TILE_SIZE, x * TILE_SIZE)
                        .zIndex(10)
                        .viewWithBBox(enemyView)
                        .with(new Oneal())
                        .buildAndAttach();
            } else {
                System.err.println("LỖI: onealImageView từ controller là null.");
            }
        }
    }
}