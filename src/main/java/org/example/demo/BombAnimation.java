package org.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

import static org.example.demo.EntityType.BOMB;

public class BombAnimation {
    // Các ImageView cho bom
    public static ImageView bomb;
    public static ImageView bomb1;
    public static ImageView bomb2;

    // Các ImageView cho vụ nổ trung tâm
    public static ImageView center;
    public static ImageView center1;
    public static ImageView center2;

    // Các ImageView cho vụ nổ theo chiều ngang
    public static ImageView horizontal;
    public static ImageView horizontal1;
    public static ImageView horizontal2;

    // Các ImageView cho vụ nổ theo chiều ngang (biên trái và phải)
    public static ImageView horizontalLeftLast;
    public static ImageView horizontalLeftLast1;
    public static ImageView horizontalLeftLast2;
    public static ImageView horizontalRightLast;
    public static ImageView horizontalRightLast1;
    public static ImageView horizontalRightLast2;

    // Các ImageView cho vụ nổ theo chiều dọc
    public static ImageView vertical;
    public static ImageView vertical1;
    public static ImageView vertical2;

    // Các ImageView cho vụ nổ theo chiều dọc (biên trên và dưới)
    public static ImageView verticalTopLast;
    public static ImageView verticalTopLast1;
    public static ImageView verticalTopLast2;
    public static ImageView verticalDownLast;
    public static ImageView verticalDownLast1;
    public static ImageView verticalDownLast2;

    private final int[][] map;

    public BombAnimation(int[][] map) {
        this.map = map;
        initialize();
    }

    public void initialize() {
        // Khởi tạo các ImageView
        bomb = new ImageView();
        bomb1 = new ImageView();
        bomb2 = new ImageView();
        center = new ImageView();
        center1 = new ImageView();
        center2 = new ImageView();
        horizontal = new ImageView();
        horizontal1 = new ImageView();
        horizontal2 = new ImageView();
        horizontalLeftLast = new ImageView();
        horizontalLeftLast1 = new ImageView();
        horizontalLeftLast2 = new ImageView();
        horizontalRightLast = new ImageView();
        horizontalRightLast1 = new ImageView();
        horizontalRightLast2 = new ImageView();
        vertical = new ImageView();
        vertical1 = new ImageView();
        vertical2 = new ImageView();
        verticalTopLast = new ImageView();
        verticalTopLast1 = new ImageView();
        verticalTopLast2 = new ImageView();
        verticalDownLast = new ImageView();
        verticalDownLast1 = new ImageView();
        verticalDownLast2 = new ImageView();

        // Gán hình ảnh cho các ImageView
        try {
            bomb.setImage(new Image(getClass().getResource("/org/example/demo/sprites/bomb.png").toExternalForm()));
            bomb1.setImage(new Image(getClass().getResource("/org/example/demo/sprites/bomb_1.png").toExternalForm()));
            bomb2.setImage(new Image(getClass().getResource("/org/example/demo/sprites/bomb_2.png").toExternalForm()));
            center.setImage(new Image(getClass().getResource("/org/example/demo/sprites/bomb_exploded.png").toExternalForm()));
            center1.setImage(new Image(getClass().getResource("/org/example/demo/sprites/bomb_exploded1.png").toExternalForm()));
            center2.setImage(new Image(getClass().getResource("/org/example/demo/sprites/bomb_exploded2.png").toExternalForm()));
            horizontal.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal.png").toExternalForm()));
            horizontal1.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal1.png").toExternalForm()));
            horizontal2.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal2.png").toExternalForm()));
            horizontalLeftLast.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal_left_last.png").toExternalForm()));
            horizontalLeftLast1.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal_left_last1.png").toExternalForm()));
            horizontalLeftLast2.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal_left_last2.png").toExternalForm()));
            horizontalRightLast.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal_right_last.png").toExternalForm()));
            horizontalRightLast1.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal_right_last1.png").toExternalForm()));
            horizontalRightLast2.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_horizontal_right_last2.png").toExternalForm()));
            vertical.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical.png").toExternalForm()));
            vertical1.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical1.png").toExternalForm()));
            vertical2.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical2.png").toExternalForm()));
            verticalTopLast.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical_top_last.png").toExternalForm()));
            verticalTopLast1.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical_top_last1.png").toExternalForm()));
            verticalTopLast2.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical_top_last2.png").toExternalForm()));
            verticalDownLast.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical_down_last.png").toExternalForm()));
            verticalDownLast1.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical_down_last1.png").toExternalForm()));
            verticalDownLast2.setImage(new Image(getClass().getResource("/org/example/demo/sprites/explosion_vertical_down_last2.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Error loading explosion images: " + e.getMessage());
            e.printStackTrace();
        }

        // Log để kiểm tra
        logImageView("bomb", bomb);
        logImageView("bomb1", bomb1);
        logImageView("bomb2", bomb2);
        logImageView("center", center);
        logImageView("center1", center1);
        logImageView("center2", center2);
        logImageView("horizontal", horizontal);
        logImageView("horizontal1", horizontal1);
        logImageView("horizontal2", horizontal2);
        logImageView("horizontalLeftLast", horizontalLeftLast);
        logImageView("horizontalLeftLast1", horizontalLeftLast1);
        logImageView("horizontalLeftLast2", horizontalLeftLast2);
        logImageView("horizontalRightLast", horizontalRightLast);
        logImageView("horizontalRightLast1", horizontalRightLast1);
        logImageView("horizontalRightLast2", horizontalRightLast2);
        logImageView("vertical", vertical);
        logImageView("vertical1", vertical1);
        logImageView("vertical2", vertical2);
        logImageView("verticalTopLast", verticalTopLast);
        logImageView("verticalTopLast1", verticalTopLast1);
        logImageView("verticalTopLast2", verticalTopLast2);
        logImageView("verticalDownLast", verticalDownLast);
        logImageView("verticalDownLast1", verticalDownLast1);
        logImageView("verticalDownLast2", verticalDownLast2);
    }

    private void logImageView(String name, ImageView imageView) {
        if (imageView == null) {
            System.out.println(name + " ImageView is null");
        } else {
            System.out.println(name + " ImageView initialized");
            if (imageView.getImage() == null) {
                System.out.println(name + " ImageView has no image");
            } else {
                System.out.println(name + " ImageView has image: " + imageView.getImage().getUrl());
            }
        }
    }

    // Hàm kiểm tra các hướng có thể nổ
    public Set<String> checkDirections(int x, int y) {
        Set<String> directions = new HashSet<>();
        int[][] dir = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}}; // phải, xuống, lên, trái
        String[] dirNames = {"right", "down", "up", "left"};

        for (int d = 0; d < dir.length; d++) {
            for (int i = 1; i <= Bomb.explosionRadius; i++) {
                int nx = x + dir[d][0] * i;
                int ny = y + dir[d][1] * i;
                if (map[ny][nx] == 1) {
                    break;
                }
                directions.add(dirNames[d]);
                if (map[ny][nx] == 2) break; // Dừng nếu gặp gạch có thể phá
            }
        }
        //System.out.println("Explosion at (" + x + ", " + y + ") can expand to directions: " + directions);
        return directions;
    }

    // Hàm hiển thị animation hướng phải
    private void rightExplode(int x, int y, boolean isLast) {
        ImageView[] frames = isLast ? new ImageView[]{horizontalRightLast, horizontalRightLast1, horizontalRightLast2} : new ImageView[]{horizontal, horizontal1, horizontal2};
        playAnimation(x, y, frames, "Explosion", "right", Bomb.TIME_SHOW_EXPLOSION);
    }

    // Hàm hiển thị animation hướng trái
    private void leftExplode(int x, int y, boolean isLast) {
        ImageView[] frames = isLast ? new ImageView[]{horizontalLeftLast, horizontalLeftLast1, horizontalLeftLast2} : new ImageView[]{horizontal, horizontal1, horizontal2};
        playAnimation(x, y, frames, "Explosion", "left", Bomb.TIME_SHOW_EXPLOSION);
    }

    // Hàm hiển thị animation hướng lên
    private void upExplode(int x, int y, boolean isLast) {
        ImageView[] frames = isLast ? new ImageView[]{verticalTopLast, verticalTopLast1, verticalTopLast2} : new ImageView[]{vertical, vertical1, vertical2};
        playAnimation(x, y, frames, "Explosion", "up", Bomb.TIME_SHOW_EXPLOSION);
    }

    // Hàm hiển thị animation hướng xuống
    private void downExplode(int x, int y, boolean isLast) {
        ImageView[] frames = isLast ? new ImageView[]{verticalDownLast, verticalDownLast1, verticalDownLast2} : new ImageView[]{vertical, vertical1, vertical2};
        playAnimation(x, y, frames, "Explosion", "down", Bomb.TIME_SHOW_EXPLOSION);
    }

    // Hàm hiển thị animation trung tâm
    public static void centerExplode(int x, int y) {
        ImageView[] frames = new ImageView[]{center, center1, center2};
        playAnimation(x, y, frames, "Explosion", "center", Bomb.TIME_SHOW_EXPLOSION);
    }

    public static void horizontalExplode(int x, int y) {
        ImageView[] frames = new ImageView[]{horizontal, horizontal1, horizontal2};
        playAnimation(x, y, frames, "Explosion", "center", Bomb.TIME_SHOW_EXPLOSION);
    }

    // Hàm chung để chạy animation
    private static void playAnimation(int x, int y, ImageView[] frames, String type, String direction, double duration) {
        ImageView view = new ImageView();
        view.setFitWidth(BombermanApp.TILE_SIZE);
        view.setFitHeight(BombermanApp.TILE_SIZE);
        view.setPreserveRatio(false);

        Entity entity = FXGL.entityBuilder().type(BOMB)
                .at(x, y)
                .view(view)
                .buildAndAttach();

        final double FRAME_DURATION = 0.1;
        final int[] frameIndex = {0};
        TimerAction animation = FXGL.run(() -> {
            if (frameIndex[0] < frames.length) {
                if (frames[frameIndex[0]] != null && frames[frameIndex[0]].getImage() != null) {
                    view.setImage(frames[frameIndex[0]].getImage());
                    //System.out.println(type + " frame " + frameIndex[0] + " at (" + x + ", " + y + "), direction: " + direction);
                }
                frameIndex[0]++;
            }
        }, Duration.seconds(FRAME_DURATION), frames.length);

        FXGL.runOnce(() -> {
            animation.expire();
            entity.removeFromWorld();
        }, Duration.seconds(duration));
    }

    // Hiển thị animation của bom
    public void showBombAnimation(int x, int y) {
        ImageView[] frames = new ImageView[]{bomb, bomb1, bomb2, bomb, bomb1, bomb2, bomb, bomb1, bomb2, bomb, bomb1, bomb2, bomb, bomb1, bomb2};
        playAnimation(x, y, frames, "Bomb", "", Bomb.DELAY_BOMB_TIME);
    }

    // Hiển thị animation vụ nổ
    public void showExplosion(int x, int y, boolean isCenter, boolean isLast, String direction) {
        if (isCenter) {
            int tileX = x / BombermanApp.TILE_SIZE;
            int tileY = y / BombermanApp.TILE_SIZE;
            Set<String> directions = checkDirections(tileX, tileY);

            // Hiển thị animation cho các hướng có thể nổ
            for (String dir : directions) {
                switch (dir) {
                    case "right" -> rightExplode(x, y, false);
                    case "left" -> leftExplode(x, y, false);
                    case "up" -> upExplode(x, y, false);
                    case "down" -> downExplode(x, y, false);
                }
            }

            // Sau khi hiển thị các hướng, hiển thị trung tâm
            centerExplode(x, y);
        } else {
            // Hiển thị animation cho các ô xung quanh
            switch (direction) {
                case "right" -> rightExplode(x, y, isLast);
                case "left" -> leftExplode(x, y, isLast);
                case "up" -> upExplode(x, y, isLast);
                case "down" -> downExplode(x, y, isLast);
            }
        }
    }
}