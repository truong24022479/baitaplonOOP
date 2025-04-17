package org.example.demo;
import com.almasb.fxgl.entity.Entity;
import static com.almasb.fxgl.dsl.FXGL.*;

public class Enemy extends Entity {

    private int speed;
    private boolean isTrackingBomber;

    /**
     * Constructor mặc định.
     * @param x Tọa độ x ban đầu.
     * @param y Tọa độ y ban đầu.
     * @param speed Tốc độ của Enemy.
     * @param isTrackingBomber Nếu true, Enemy sẽ theo dõi Bomber.
     */
    public Enemy(int x,int y,int speed,boolean isTrackingBomber) {
        setPosition(x,y);
        this.speed = speed;
        this.isTrackingBomber = isTrackingBomber;
    }

    /**
     * Xử lý di chuyển Enemy.
     * Nếu không theo dõi Bomber, Enemy sẽ di chuyển ngẫu nhiên.
     * Nếu theo dõi Bomber, Enemy sẽ tiếp cận vị trí của Bomber.
     */
    public void move(int[][] map, Entity bomber) {
        if (isTrackingBomber) {

            double bomberX = bomber.getX();
            double bomberY = bomber.getY();

            if (bomberX > getX()) {
                setPosition(getX() + speed,getY());
            }
            else if (bomberX < getX()) {
                setPosition(getX() - speed,getY());
            }

            if (bomberY > getY()) {
                setPosition(getX(),getY() + speed);
            }
            else if (bomberY < getY()) {
                setPosition(getX(), getY() - speed);
            }
        }
        else {
            // Di chuyển ngẫu nhiên.
            int randomDirection = (int) (Math.random() * 4);

            switch(randomDirection) {
                case 0: // UP
                    if (canMoveTo(getX(),getY() - speed,map)) setPosition(getX(),getY() - speed);
                    break;
                case 1: // DOWN
                    if (canMoveTo(getX(), getY() + speed, map)) setPosition(getX(), getY() + speed);
                    break;
                case 2: // LEFT
                    if (canMoveTo(getX() - speed, getY(), map)) setPosition(getX() - speed, getY());
                    break;
                case 3: // RIGHT
                    if (canMoveTo(getX() + speed, getY(), map)) setPosition(getX() + speed, getY());
            }
        }
    }
    /**
     * Kiểm tra xem Enemy có thể di chuyển đến vị trí mới không.
     * @param x Tọa độ x mới.
     * @param y Tọa độ y mới.
     * @param map Bản đồ trò chơi.
     * @return True nếu vị trí hợp lệ, ngược lại là False.
     */
    private boolean canMoveTo(double x, double y, int[][] map) {
        int tileX = (int) (x / 32); // TILE_SIZE = 32
        int tileY = (int) (y / 32);

        // Kiểm tra không ra ngoài bản đồ và không va vào tường/gạch
        return tileX >= 0 && tileX < map[0].length &&
                tileY >= 0 && tileY < map.length &&
                map[tileY][tileX] == 0; // 0 là ô trống1
    }
    /**
     * Phát hiện va chạm với Bomber.
     * @param bomber Thực thể Bomber.
     * @return True nếu Enemy va chạm với Bomber.
     */
    public boolean detectCollision(Entity bomber) {
        return getBoundingBoxComponent().isCollidingWith(bomber.getBoundingBoxComponent());
    }
}
