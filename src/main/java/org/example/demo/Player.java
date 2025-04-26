package org.example.demo;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;
import static org.example.demo.BombermanApp.removePlayer;

public class Player implements EntityFactory {
    private static Entity player;
    private static double x;
    private static double y;
    private double PLAYER_SPEED = 4;
    private int TILE_SIZE = BombermanApp.TILE_SIZE;
    private int MAP_WIDTH = BombermanApp.MAP_WIDTH;
    private int MAP_HEIGHT = BombermanApp.MAP_HEIGHT;
    public static int MOVE_ERROR = 10;
    private KeyHandle keyH;
    private int[][] map;
    public static boolean atPortal;

    private PlayerSpriteManager spriteManager;

    public Entity getPlayerEntity() {
        return player;
    }

    public static double getX() {
        if (player != null) {
            return player.getX();
        }
        return BombermanApp.TILE_SIZE; // Giá trị mặc định nếu player chưa được khởi tạo
    }

    public static double getY() {
        if (player != null) {
            return player.getY();
        }
        return BombermanApp.TILE_SIZE; // Giá trị mặc định nếu player chưa được khởi tạo
    }

    public Player(int TILE_SIZE, int mapWidth, int mapHeight, int moveError) {
        this.TILE_SIZE = TILE_SIZE;
        this.MAP_WIDTH = mapWidth;
        this.MAP_HEIGHT = mapHeight;
        this.PLAYER_SPEED = 1; // Matches PLAYER_SPEED from BombermanApp
        this.MOVE_ERROR = moveError;
        this.keyH = new KeyHandle();
        this.x = TILE_SIZE; // Starting position (1,1) in tile coordinates
        this.y = TILE_SIZE;
        this.atPortal = false;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public void spawnPlayer(){
        spriteManager = new PlayerSpriteManager();

        // Lấy ImageView từ PlayerSpriteManager
        ImageView playerView = spriteManager.getPlayerImageView();
        if (playerView == null) {
            System.out.println("playerView is null from spriteManager.getPlayerImageView()");
            return;
        }
        playerView.setFitWidth(TILE_SIZE);
        playerView.setFitHeight(TILE_SIZE);
        playerView.setPreserveRatio(false);

        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(TILE_SIZE, TILE_SIZE)
                .zIndex(10)
                .viewWithBBox(playerView)
                .buildAndAttach();

        // Gán player cho spriteManager để quản lý animation
        spriteManager.setPlayer(player);
    }

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
        getInput().addAction(new UserAction("Đặt bom") {
            @Override
            protected void onActionBegin() {
                Bomb.setBomb(player, map, 1, Bomb.DELAY_BOMB_TIME); // bán kính 1, hẹn giờ 2s
            }
        }, KeyCode.SPACE);
    }

    protected void onUpdate(double tpf) {
        double dx = 0, dy = 0;
        if (keyH.upPressed) dy -= PLAYER_SPEED;
        if (keyH.downPressed) dy += PLAYER_SPEED;
        if (keyH.leftPressed) dx -= PLAYER_SPEED;
        if (keyH.rightPressed) dx += PLAYER_SPEED;
        //cap nhat animation trc khi di chuyen
        spriteManager.updateAnimation(tpf, keyH.upPressed, keyH.downPressed, keyH.leftPressed, keyH.rightPressed);

        movePlayer(dx, dy);

        int playerTileX = (int) (player.getX() / TILE_SIZE);
        int playerTileY = (int) (player.getY() / TILE_SIZE);

        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> {
            int enemyTileX = (int) (enemy.getX() / TILE_SIZE);
            int enemyTileY = (int) (enemy.getY() / TILE_SIZE);

            if (playerTileX == enemyTileX && playerTileY == enemyTileY) {
                removePlayer();
                System.out.println("bi cho can");
            }
        });
    }

    private void movePlayer(double dx, double dy) {
        double newX = player.getX();
        double newY = player.getY();

        if (dx != 0 && dy == 0) {
            double targetRow = Math.round(newY / TILE_SIZE);
            double alignedY = targetRow * TILE_SIZE;
            if (Math.abs(alignedY - newY) <= MOVE_ERROR) {
                newY = alignedY;
            }
            newX += dx;
        } else if (dy != 0 && dx == 0) {
            double targetCol = Math.round(newX / TILE_SIZE);
            double alignedX = targetCol * TILE_SIZE;
            if (Math.abs(alignedX - newX) <= MOVE_ERROR) {
                newX = alignedX;
            }
            newY += dy;
        } else {
            newX += dx;
            newY += dy;
        }

        int leftTile = (int) (newX / TILE_SIZE);
        int rightTile = (int) ((newX + TILE_SIZE - 1) / TILE_SIZE);
        int topTile = (int) (newY / TILE_SIZE);
        int bottomTile = (int) ((newY + TILE_SIZE - 1) / TILE_SIZE);

        if (leftTile < 0 || rightTile >= MAP_WIDTH || topTile < 0 || bottomTile >= MAP_HEIGHT) return;

        for (int ty = topTile; ty <= bottomTile; ty++) {
            for (int tx = leftTile; tx <= rightTile; tx++) {
                if (map[ty][tx] != 0 && map[ty][tx] != 4) {
                    return;
                }
            }
        }

        //System.out.println("at "+ newX+","+newY+"\nat portal "+atPortal);
//        if (newX / TILE_SIZE == MAP_WIDTH - 2 && newY / TILE_SIZE == MAP_HEIGHT - 2) {
//            atPortal = true;
//            BombermanApp.GG();
//        } else atPortal = false;
        int tileX = (int) (newX / BombermanApp.TILE_SIZE);
        int tileY = (int) (newY / BombermanApp.TILE_SIZE);
        if (map[tileY][tileX] == 4){
            atPortal = true;
            BombermanApp.GG();
        } else atPortal = false;
        player.setPosition(newX, newY);
    }
}
