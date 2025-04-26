package org.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;


public class BombermanApp extends GameApplication {
    private Entity player;
    private int[][] map;
    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH = 15;
    public static final int MAP_HEIGHT = 15;
    private static final int PLAYER_SPEED = 1;
    private static final int MOVE_ERROR = 10;
    private KeyHandle keyH = new KeyHandle();
    private static GamePlay controller;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(MAP_WIDTH * TILE_SIZE);
        settings.setHeight(MAP_HEIGHT * TILE_SIZE);
        settings.setTitle("Bomberman Game");
        settings.setVersion("1.0");
    }

    protected void initUI() {
        GameInitializerMap.initUI();
    }

/// /////////////////
//    @Override
//    protected void initGame() {
//        GameInitializerMap.initUI();    // 👈 GỌI TRƯỚC để load FXML và gán controller
//        GameInitializerMap.initGame();  // 👈 Sau đó mới dùng controller
//    }
//    private void initializeMap(){
//        GameInitializerMap.initializeMap();
//    }

    protected void initGame() {
        getGameScene().setBackgroundColor(Color.BLACK);
        map = new int[MAP_HEIGHT][MAP_WIDTH];
        initializeMap();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/game_play.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải file game_play.fxml: " + e.getMessage());
        }


        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                ImageView view;
                EntityType type;

                if (map[row][col] == 1) {
                    view = controller.getWall();
                    type = EntityType.WALL;
                } else if (map[row][col] == 2) {
                    view = controller.getBrick();
                    type = EntityType.BRICK;
                } else {
                    view = controller.getGrass();
                    type = EntityType.GRASS;
                }


                entityBuilder()
                        .type(type)
                        .at(col * TILE_SIZE, row * TILE_SIZE)
                        .viewWithBBox(view)
                        .buildAndAttach();
            }
        }

        // Tao nhan vat nguoi choi
        ImageView playerView = controller.getPlayerImageView();

        playerView.setFitWidth(TILE_SIZE);
        playerView.setFitHeight(TILE_SIZE);
        playerView.setPreserveRatio(false);

        player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(TILE_SIZE, TILE_SIZE)
                .viewWithBBox(playerView)
                .buildAndAttach();


        // Tao ke dich
        Random random = new Random();
        int numOfBalloons = 5;
        for (int i = 0; i < numOfBalloons; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getEnemyImageView();

            enemyView.setFitWidth(TILE_SIZE);
            enemyView.setFitHeight(TILE_SIZE);
            enemyView.setPreserveRatio(false);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .viewWithBBox(enemyView)
                    .with(new Balloon() {{
                        initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);
                    }})
                    .buildAndAttach();
        }

        int numOfOneals = 1;
        for (int i = 0; i < numOfOneals; i++) {
            int x, y;
            do {
                x = random.nextInt(MAP_HEIGHT);
                y = random.nextInt(MAP_WIDTH);
            } while (map[x][y] != 0 || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)); // Ensure valid, non-player start spot

            ImageView enemyView = controller.getEnemyImageView();

            enemyView.setFitWidth(TILE_SIZE);
            enemyView.setFitHeight(TILE_SIZE);
            enemyView.setPreserveRatio(false);

            entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(y * TILE_SIZE, x * TILE_SIZE)
                    .viewWithBBox(enemyView)
                    .with(new Oneal() {{
                        initMap(map, TILE_SIZE, MAP_WIDTH, MAP_HEIGHT);
                    }})
                    .buildAndAttach();
        }
    }

    private void initializeMap() {
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                if (row == 0 || row == MAP_HEIGHT - 1 || col == 0 || col == MAP_WIDTH - 1) {
                    map[row][col] = 1; // Tường xung quanh
                } else if (row % 2 == 0 && col % 2 == 0) {
                    map[row][col] = 1; // Khối không thể phá hủy
                } else {
                    map[row][col] = (Math.random() > 0.7) ? 2 : 0; // Ngẫu nhiên tường gạch hoặc đường đi
                }
            }
        }
        map[1][1] = 0;
        map[1][2] = 0;
        map[2][1] = 0;
    }

//////////////////


    public int[][] getMap() {
    return map;
}

    @Override
    protected void initInput() {
        FXGL.getInput().clearAll(); // Xóa tất cả binding cũ

        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                keyH.upPressed = true;
            }

            @Override
            protected void onActionEnd() {
                keyH.upPressed = false;
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                keyH.downPressed = true;
            }

            @Override
            protected void onActionEnd() {
                keyH.downPressed = false;
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                keyH.leftPressed = true;
            }

            @Override
            protected void onActionEnd() {
                keyH.leftPressed = false;
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                keyH.rightPressed = true;
            }

            @Override
            protected void onActionEnd() {
                keyH.rightPressed = false;
            }
        }, KeyCode.D);
        // Đặt bomb bằng phím Space
        getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onActionBegin() {
                placeBomb(); // Gọi phương thức đặt bomb
            }
        }, KeyCode.SPACE);


    }

    @Override
    protected void onUpdate(double tpf) {
        double dx = 0, dy = 0;
        if (keyH.upPressed) dy -= PLAYER_SPEED;
        if (keyH.downPressed) dy += PLAYER_SPEED;
        if (keyH.leftPressed) dx -= PLAYER_SPEED;
        if (keyH.rightPressed) dx += PLAYER_SPEED;
        movePlayer(dx, dy);
    }

    private void movePlayer(double dx, double dy) {
        // Lấy vị trí hiện tại của nhân vật
        double newX = player.getX();
        double newY = player.getY();

        // Căn chỉnh di chuyển theo trục (ngang hoặc dọc)
        if (dx != 0 && dy == 0) {
            // Di chuyển theo trục X (ngang)
            double targetRow = Math.round(newY / TILE_SIZE);
            double alignedY = targetRow * TILE_SIZE;
            if (Math.abs(alignedY - newY) <= MOVE_ERROR) {
                newY = alignedY;
            }
            newX += dx;
        } else if (dy != 0 && dx == 0) {
            // Di chuyển theo trục Y (dọc)
            double targetCol = Math.round(newX / TILE_SIZE);
            double alignedX = targetCol * TILE_SIZE;
            if (Math.abs(alignedX - newX) <= MOVE_ERROR) {
                newX = alignedX;
            }
            newY += dy;
        } else {
            // Di chuyển chéo (cả dx và dy đều khác 0)
            newX += dx;
            newY += dy;
        }

        // Xác định các ô xung quanh nhân vật dựa trên vị trí mới
        int leftTile = (int) (newX / TILE_SIZE);
        int rightTile = (int) ((newX + TILE_SIZE - 1) / TILE_SIZE);
        int topTile = (int) (newY / TILE_SIZE);
        int bottomTile = (int) ((newY + TILE_SIZE - 1) / TILE_SIZE);

        // Kiểm tra giới hạn bản đồ
        if (leftTile < 0 || rightTile >= MAP_WIDTH || topTile < 0 || bottomTile >= MAP_HEIGHT) {
            return; // Không di chuyển nếu vượt ra ngoài giới hạn bản đồ
        }

        // Kiểm tra va chạm với các ô trên bản đồ
        for (int ty = topTile; ty <= bottomTile; ty++) {
            for (int tx = leftTile; tx <= rightTile; tx++) {
                // Nếu gặp ô chứa tường hoặc vật cản, chặn di chuyển
                if (map[ty][tx] != 0) {
                    return; // Chặn di chuyển nếu có vật cản
                }
            }
        }

        // Cập nhật vị trí của nhân vật nếu không có va chạm
        player.setPosition(newX, newY);
    }



    private void placeBomb() {
        int bombX = (int) (player.getX() / TILE_SIZE) * TILE_SIZE;
        int bombY = (int) (player.getY() / TILE_SIZE) * TILE_SIZE;
        int tileX = bombX / TILE_SIZE;
        int tileY = bombY / TILE_SIZE;

        // Không cho đặt bomb nếu ô hiện tại không phải đường đi (0) hoặc đã có bomb (3)
        if (map[tileY][tileX] != 0) {
            return;
        }

        // Đặt bomb lên map
        map[tileY][tileX] = 3;

        // Tạo entity cho bomb
        Entity bombEntity = entityBuilder()
                .type(EntityType.BOMB)
                .at(bombX, bombY)
                .bbox(new HitBox(BoundingShape.box(TILE_SIZE, TILE_SIZE)))
                .view(new ImageView(getAssetLoader().loadImage("sprites/bomb.png")) {{
                    setFitWidth(TILE_SIZE);
                    setFitHeight(TILE_SIZE);
                    setPreserveRatio(false);
                }})
                .buildAndAttach();

        // Sau 2 giây thì bomb phát nổ
        runOnce(() -> {
            explodeBomb(bombX, bombY);
            bombEntity.removeFromWorld(); // Xóa bomb khỏi thế giới
            map[tileY][tileX] = 0;        // Đánh dấu lại là đường đi
        }, Duration.seconds(2));
    }

    private void explodeBomb(int bombX, int bombY) {
        int tileX = bombX / TILE_SIZE;
        int tileY = bombY / TILE_SIZE;

        showExplosion(bombX, bombY); // Nổ tại trung tâm

        // 4 hướng: trên, phải, dưới, trái
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        for (int[] dir : directions) {
            for (int i = 1; i <= 1; i++) { // 1 ô xa (có thể mở rộng thành power-up sau này)
                int nx = tileX + dir[0] * i;
                int ny = tileY + dir[1] * i;

                // Kiểm tra trong giới hạn bản đồ
                if (nx < 0 || nx >= MAP_WIDTH || ny < 0 || ny >= MAP_HEIGHT)
                    break;

                int cell = map[ny][nx];

                if (cell == 1) {
                    break; // Tường cứng, không lan nổ
                }

                showExplosion(nx * TILE_SIZE, ny * TILE_SIZE);

                if (cell == 2) {
                    map[ny][nx] = 0; // Phá gạch
                    break; // Không nổ xuyên qua gạch
                }
            }
        }
    }

    private void showExplosion(int x, int y) {
        ImageView explosionView = new ImageView(getAssetLoader().loadImage("sprites/explosion.png"));
        explosionView.setFitWidth(TILE_SIZE);
        explosionView.setFitHeight(TILE_SIZE);
        explosionView.setPreserveRatio(false);

        Entity explosion = entityBuilder()
                .at(x, y)
                .view(explosionView)
                .buildAndAttach();

        // Tự động xóa hiệu ứng nổ sau 0.5s
        runOnce(explosion::removeFromWorld, Duration.seconds(0.5));
    }





}