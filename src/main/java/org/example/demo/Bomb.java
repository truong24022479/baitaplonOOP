package org.example.demo;

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
                explode(); // Gọi phương thức phát nổ
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

                // Kiểm tra trong phạm vi bản đồ
                if (nx >= 0 && nx < map[0].length && ny >= 0 && ny < map.length) {
                    if (map[ny][nx] == 1) {
                        System.out.println("Phá hủy tường tại: " + nx + ", " + ny);
                        map[ny][nx] = 0; // Loại bỏ tường
                    } else if (map[ny][nx] == 2) {
                        System.out.println("Phá hủy gạch tại: " + nx + ", " + ny);
                        map[ny][nx] = 0; // Loại bỏ gạch
                    }
                }
            }
        }
    }

    // Getter để kiểm tra trạng thái bom
    public boolean isExploded() {
        return isExploded;
    }
}
