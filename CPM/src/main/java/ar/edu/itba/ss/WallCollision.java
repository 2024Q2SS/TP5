package ar.edu.itba.ss;

public class WallCollision implements Collision {
    private Player player;
    private Vector2D wallPoint;
    private double time;

    public WallCollision(Player player, Vector2D wallPoint, double time) {
        this.player = player;
        this.wallPoint = wallPoint;
        this.time = time;
    }

    public Player getPlayer() {
        return player;
    }

    public double getTime() {
        return time;
    }

    public Vector2D getNormalDirection() {
        Vector2D normal = player.getPosition().subtract(wallPoint);
        return normal.versor();
    }
}
