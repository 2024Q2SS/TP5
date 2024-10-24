package ar.edu.itba.ss;

public interface Collision {

    public Vector2D getNormalDirection();

    public void adjustRadii();

    public boolean isPlayerInvolved(Player player);
}
