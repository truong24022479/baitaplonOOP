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
    private static final double MINVO_SPEED = 40;

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

    public void onUpdate(double tpf) {
        if (isDead) {
            return;
        }
        if (isMoving) {
            minvo.translateX(vX * tpf);
            minvo.translateY(vY * tpf);

            frameTimer += tpf;
            if (frameTimer >= FRAME_DURATION) {
                frameIndex = (frameIndex + 1) % 2;
                frameTimer = 0;

                if (vX < 0) {
                    view.setImage(leftFrames[frameIndex].getImage());
                } else if (vX > 0) {
                    view.setImage(rightFrames[frameIndex].getImage());
                }
            }

            if (Math.abs(minvo.getX() - moveTargetX) < 1 && Math.abs(minvo.getY() - moveTargetY) < 1) {
                minvo.setPosition(moveTargetX, moveTargetY); // snap to tile
                isMoving = false;
            }

        } else {
            moveMinvo();
        }
    }

//    private void moveMinvo() {
//        int startX = (int) (minvo.getX() / TILE_SIZE);
//        int startY = (int) (minvo.getY() / TILE_SIZE);
//        int endX = (int) (player.getX() / TILE_SIZE);
//        int endY = (int) (player.getY() / TILE_SIZE);
//
//        List<int[]> path = findPath(startX, startY, endX, endY);
//        if (path.size() >= 2) {
//            int[] nextMove = path.get(1);
//            if (nextMove[0] >= 0 && nextMove[0] < MAP_WIDTH && nextMove[1] >= 0 && nextMove[1] <= MAP_HEIGHT
//                    && canMove(nextMove[0], nextMove[1])) {
//                moveTargetX = nextMove[0] * TILE_SIZE;
//                moveTargetY = nextMove[1] * TILE_SIZE;
//
//                double mx = moveTargetX - minvo.getX();
//                double my = moveTargetY - minvo.getY();
//                if (Math.abs(mx) > 0 && Math.abs(my) > 0) {
//                    mx = 0;
//                    my = 0;
//                }
//
//                double length = Math.sqrt(Math.pow(mx, 2) + Math.pow(my, 2));
//                vX = (mx / length) * MINVO_SPEED;
//                vY = (my / length) * MINVO_SPEED;
//                isMoving = true;
//            }
//
//        }
//    }

    private void moveMinvo() {
        int startX = (int) (minvo.getX() / TILE_SIZE);
        int startY = (int) (minvo.getY() / TILE_SIZE);
        int endX = (int) (player.getX() / TILE_SIZE);
        int endY = (int) (player.getY() / TILE_SIZE);

        List<int[]> path = findPath(startX, startY, endX, endY);

        if (path.size() > 1) {
            int[] nextMove = path.get(1);
            int nx = nextMove[0];
            int ny = nextMove[1];

            if (nx >= 0 && nx < MAP_WIDTH && ny >= 0 && ny < MAP_HEIGHT && canMove(nx, ny)) {
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
            if (nx >= 0 && nx < MAP_WIDTH && ny >= 0 && ny < MAP_HEIGHT && canMove(nx, ny)) {
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
                vX = (dx / length) * MINVO_SPEED;
                vY = (dy / length) * MINVO_SPEED;
                isMoving = true;
            }
        }
    }


//    private List<int[]> findPath(int startX, int startY, int endX, int endY) {
//        Queue<int[]> queue = new LinkedList<>();
//        boolean[][] visited = new boolean[MAP_HEIGHT][MAP_WIDTH];
//        Map<String, String> parent = new HashMap<>();
//
//        queue.add(new int[]{startX, startY});
//        visited[startY][startX] = true;
//
//        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
//
//        while (!queue.isEmpty()) {
//            int[] currentPoint = queue.poll();
//            int pointX = currentPoint[0];
//            int pointY = currentPoint[1];
//
//            if (pointX == endX && pointY == endY) {
//                break;
//            }
//
//            for (int[] direction : directions) {
//                int newX = pointX + direction[0];
//                int newY = pointY + direction[1];
//
//                if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT
//                        && canMove(newX, newY) && !visited[newY][newX]) {
//                    queue.add(new int[]{newX, newY});
//                    visited[newY][newX] = true;
//                    parent.put(newX + "," + newY, pointX + "," + pointY);
//                }
//            }
//        }
//
//        List<int[]> backPath = new ArrayList<>();
//        String currentEnd = endX + "," + endY;
//        if (parent.containsKey(currentEnd)) {
//            String[] currentParts = currentEnd.split(",");
//            int x = Integer.parseInt(currentParts[0]);
//            int y = Integer.parseInt(currentParts[1]);
//            backPath.add(new int[]{x, y});
//            currentEnd = parent.get(currentEnd);
//        }
//        backPath.add(new int[]{startX, startY});
//        Collections.reverse(backPath);
//        return backPath;
//    }

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

                if (nx >= 0 && nx < MAP_WIDTH && ny >= 0 && ny < MAP_HEIGHT && canMove(nx, ny) && !visited[ny][nx]) {
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

    @Override
    public boolean canMove(int x, int y) {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT)
            return false;

        if (map[y][x] == 1 || map[y][x] == 2 || map[y][x] == 3)
            return false;

        if (isDangerous(x, y))
            return false;

        return true;
    }

    public boolean isDangerous(int x, int y) {
        bombs = FXGL.getGameWorld().getEntitiesByType(EntityType.BOMB);
        for (Entity bomb : bombs) {
            int bombX = (int) (bomb.getX() / TILE_SIZE);
            int bombY = (int) (bomb.getY() / TILE_SIZE);

            if (bombX == x && bombY == y) return true;

            for (int i = 1; i <= Bomb.explosionRadius; i++) {
                if ((bombX + i == x) || (bombX - i == x)
                    || bombY + i == y || bombY - i == y) return true;
            }
        }
        return false;
    }

    public void minvoDie() {
        isMoving = false;
        if (isDead) return;
        isDead = true;

        Image deadImage = new Image(getClass().getResource("/org/example/demo/sprites/minvo_dead (1).png").toExternalForm());
        view.setImage(deadImage);

        // Cho hiệu ứng tồn tại 0.5s rồi xoá khỏi map
        FXGL.getGameTimer().runOnceAfter(() -> {
            if (getEntity().isActive()) {
                getEntity().removeFromWorld();
            }
        }, Duration.seconds(0.5));
    }
}
