package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.*;

public class Minvo extends Enemy {
    private Entity minvo;
    private Entity player;
    public static double MINVO_SPEED = 40;

    private double vX = 0;
    private double vY = 0;

    private double moveTargetX;
    private double moveTargetY;
    private List<Entity> bombs = new ArrayList<>();

    private ImageView view;
    private ImageView[] leftFrames;
    private ImageView[] rightFrames;
    private int frameIndex = 0;
    private double frameTimer = 0;
    private final double FRAME_DURATION = 0.2;

    public void onAdded() {
        minvo = getEntity();
        view = new ImageView(getClass().getResource("/org/example/demo/sprites/minvo_dead (1).png").toExternalForm());
        // hoặc image mặc định
        view.setFitWidth(TILE_SIZE);
        view.setFitHeight(TILE_SIZE);
        view.setPreserveRatio(false); // hoặc true nếu cần
        getEntity().getViewComponent().addChild(view); // rất quan trọng!


        MinvoAnimation anim = new MinvoAnimation();
        anim.initializeImageView();
        leftFrames = new ImageView[]{MinvoAnimation.left1, MinvoAnimation.left2, MinvoAnimation.left3};
        rightFrames = new ImageView[]{MinvoAnimation.right1, MinvoAnimation.right2, MinvoAnimation.right3};

        player = FXGL.getGameWorld().getSingleton(EntityType.PLAYER);
        BombermanApp app = (BombermanApp) FXGL.getApp();
        initMap(app.getMap(), app.TILE_SIZE, app.MAP_WIDTH, app.MAP_HEIGHT);
    }

//    public void onUpdate(double tpf) {
//        if (isDead) {
//            return;
//        }
//        if (isMoving) {
//
//            minvo.translateX(vX * tpf);
//            minvo.translateY(vY * tpf);
//
//            frameTimer += tpf;
//            if (frameTimer >= FRAME_DURATION) {
//                frameIndex = (frameIndex + 1) % 3;
//                frameTimer = 0;
//
//                if (vX < 0) {
//                    view.setImage(leftFrames[frameIndex].getImage());
//                } else if (vX > 0) {
//                    view.setImage(rightFrames[frameIndex].getImage());
//                }
//            }
//
//            if (Math.abs(minvo.getX() - moveTargetX) < 1 && Math.abs(minvo.getY() - moveTargetY) < 1) {
//                minvo.setPosition(moveTargetX, moveTargetY); // snap to tile
//                isMoving = false;
//            }
//
//        } else {
//            moveMinvo();
//        }
//    }

    public void onUpdate(double tpf) {
        if (isDead) {
            return;
        }

        if (isMoving) {
            // *** ADD THIS CHECK ***
            // Get the tile coordinates of the current target
            int targetTileX = (int) (moveTargetX / TILE_SIZE);
            int targetTileY = (int) (moveTargetY / TILE_SIZE);

            // Re-check if the target tile is still valid (using the core logic of canMove)
            boolean targetStillValid = true;
            // Check map obstacles
            if (targetTileX < 0 || targetTileX >= MAP_WIDTH || targetTileY < 0 || targetTileY >= MAP_HEIGHT ||
                    map[targetTileY][targetTileX] == 1 || map[targetTileY][targetTileX] == 2 || map[targetTileY][targetTileX] == 3) {
                targetStillValid = false;
            } else {
                // Check specifically for bombs on the target tile
                for (Entity bomb : FXGL.getGameWorld().getEntitiesByType(EntityType.BOMB)) {
                    int bombX = (int) (bomb.getX() / TILE_SIZE);
                    int bombY = (int) (bomb.getY() / TILE_SIZE);
                    if (bombX == targetTileX && bombY == targetTileY) {
                        System.out.println("Bomb found at target tile");
                        targetStillValid = false;
                        break; // Found a bomb, no need to check further
                    }
                }
                // Also check if the target tile became dangerous (part of avoiding explosions)
                if (targetStillValid && isDangerous(targetTileX, targetTileY)) {
                    System.out.println("Danger found at target tile");
                    targetStillValid = false;
                }
            }


            if (!targetStillValid) {
                // The target tile is now blocked or dangerous! Stop moving.
                System.out.println("Target tile is not valid");
                isMoving = false;
                vX = 0;
                vY = 0;
                // Optional: Snap to the *current* grid position for cleaner state
                minvo.setPosition(Math.round(minvo.getX() / TILE_SIZE) * TILE_SIZE,
                        Math.round(minvo.getY() / TILE_SIZE) * TILE_SIZE);
                // Let the next frame call moveMinvo() to find a new path
                return; // Exit onUpdate for this frame
            }

            minvo.translateX(vX * tpf);
            minvo.translateY(vY * tpf);

            frameTimer += tpf;
            if (frameTimer >= FRAME_DURATION) {
                // Use modulo 3 for 3 frames
                frameIndex = (frameIndex + 1) % 3; // Changed from % 2
                frameTimer = 0;

                // Ensure view component exists and arrays are initialized
                if (view != null && leftFrames != null && rightFrames != null &&
                        leftFrames.length > frameIndex && rightFrames.length > frameIndex) {
                    if (vX < 0 && leftFrames[frameIndex] != null) {
                        view.setImage(leftFrames[frameIndex].getImage());
                    } else if (vX > 0 && rightFrames[frameIndex] != null) {
                        view.setImage(rightFrames[frameIndex].getImage());
                    }
                    // Optional: Add idle or up/down frames if needed
                }
            }


            // Check if close enough to the target
            if (Math.abs(minvo.getX() - moveTargetX) < 1 && Math.abs(minvo.getY() - moveTargetY) < 1) {
                minvo.setPosition(moveTargetX, moveTargetY); // snap to tile
                isMoving = false;
                vX = 0; // Reset velocity
                vY = 0;
            }

        } else {

            // Only move if not dead
            if (!isDead) {
                moveMinvo();
            }
        }
    }

    public void moveMinvo() {
        int startX = (int) (minvo.getX() / TILE_SIZE);
        int startY = (int) (minvo.getY() / TILE_SIZE);
        int endX = (int) (player.getX() / TILE_SIZE);
        int endY = (int) (player.getY() / TILE_SIZE);

        List<int[]> path = findPath(startX, startY, endX, endY);

        if (path.size() > 1) {
            int[] nextMove = path.get(1);
            int nx = nextMove[0];
            int ny = nextMove[1];

            if (isDangerous(nx, ny)) {
                moveRandom();
                return;
            }

            if (canMove(nx, ny)) {
                moveTargetX = nx * TILE_SIZE;
                moveTargetY = ny * TILE_SIZE;

                double dx = moveTargetX - minvo.getX();
                double dy = moveTargetY - minvo.getY();

                // Chỉ đi ngang hoặc dọc, không đi chéo
                if (Math.abs(dx) > 0 && Math.abs(dy) > 0) {
                    dx = 0;
                    dy = 0;
                }

                double length = Math.sqrt(dx * dx + dy * dy);
                if (length != 0) {
                    System.out.println("Minvo moveMinvo: Setting isMoving = true");
                    vX = (dx / length) * MINVO_SPEED;
                    vY = (dy / length) * MINVO_SPEED;
                    isMoving = true;
                }
            } else {
                // Không thể đi -> Random một hướng mới
                moveRandom();
            }
        } else {
            // Không tìm thấy đường -> Random một hướng
            moveRandom();
        }
    }

    private void moveRandom() {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        List<int[]> possibleMoves = new ArrayList<>();

        int x = (int) (minvo.getX() / TILE_SIZE);
        int y = (int) (minvo.getY() / TILE_SIZE);

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (canMove(nx, ny)) {
                possibleMoves.add(new int[]{nx, ny});
            }
        }

        if (!possibleMoves.isEmpty()) {
            int[] move = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
            moveTargetX = move[0] * TILE_SIZE;
            moveTargetY = move[1] * TILE_SIZE;

            double dx = moveTargetX - minvo.getX();
            double dy = moveTargetY - minvo.getY();
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length != 0) {
//                System.out.println("Minvo moveRandom: Setting isMoving = true");
                vX = (dx / length) * MINVO_SPEED;
                vY = (dy / length) * MINVO_SPEED;
                isMoving = true;
            } else {
//                System.out.println("Minvo moveRandom: length is 0, not moving");
            }
        }
    }

    private List<int[]> findPath(int startX, int startY, int endX, int endY) {
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[MAP_HEIGHT][MAP_WIDTH];
        Map<String, String> parent = new HashMap<>();

        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == endX && y == endY) {
                break;
            }

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (canMove(nx, ny) && !visited[ny][nx]) {
                    queue.add(new int[]{nx, ny});
                    visited[ny][nx] = true;
                    parent.put(nx + "," + ny, x + "," + y);
                }
            }
        }

        List<int[]> path = new ArrayList<>();
        String current = endX + "," + endY;

        while (parent.containsKey(current)) {
            String[] parts = current.split(",");
            path.add(0, new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
            current = parent.get(current);
        }

        if (!path.isEmpty()) {
            path.add(0, new int[]{startX, startY});
        }

        return path;
    }



    public boolean isDangerous(int x, int y) {
        for (Entity bomb : FXGL.getGameWorld().getEntitiesByType(EntityType.BOMB)) {
            int bombX = (int) (bomb.getX() / TILE_SIZE);
            int bombY = (int) (bomb.getY() / TILE_SIZE);

            if (bombX == x && bombY == y) return true;

            for (int i = 1; i <= Bomb.explosionRadius; i++) {
                if ((bombX + i == x && bombY == y) || (bombX - i == x && bombY == y)
                    || (bombX == x && bombY + i == y) || (bombX == x && bombY - i == y)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean canMove(int x, int y) {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT)
            return false;

        if (map[y][x] == 1 || map[y][x] == 2 || map[y][x] == 3)
            return false;

        // 👇 Check entity bomb trên map (vị trí hiện tại có bomb không)
        for (Entity bomb : FXGL.getGameWorld().getEntitiesByType(EntityType.BOMB)) {
            int bombX = (int) (bomb.getX() / TILE_SIZE);
            int bombY = (int) (bomb.getY() / TILE_SIZE);
            if (bombX == x && bombY == y) {
                return false;
            }
        }

        return !isDangerous(x, y);
    }


    public void minvoDie() {
        isMoving = false;
        if (isDead) return;
        isDead = true;

        Image deadImage = new Image(getClass().getResource("/org/example/demo/sprites/minvo_dead (1).png").toExternalForm());
        view.setImage(deadImage);

        // Cho hiệu ứng tồn tại 0.5s rồi xoá khỏi map
        FXGL.getGameTimer().runOnceAfter(() -> {
//            if (getEntity().isActive()) {
//                getEntity().removeFromWorld();
//            }
            getEntity().removeFromWorld();
        }, Duration.seconds(0.5));
        GameInitializerMap.spawnOneal(2);
        BombermanApp.ENEMY_NUMBERS_LEFT+=2;
    }
}
