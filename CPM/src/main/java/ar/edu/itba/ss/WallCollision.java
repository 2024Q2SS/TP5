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

    public void adjustRadii() {
        player.setRadius(player.getMinRadius());
    }

    public boolean isPlayerInvolved(Player player) {
        return player.equals(this.player);
    }

    public Vector2D getRelativeNormalDirection(Player player) {
        if (player.equals(this.player)) {
            return player.getPosition().subtract(wallPoint).versor();
        }
        return Vector2D.ZERO();
    }

}
