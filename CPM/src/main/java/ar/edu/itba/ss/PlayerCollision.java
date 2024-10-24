package ar.edu.itba.ss;

public class PlayerCollision implements Collision {
    private Player player1;
    private Player player2;
    private double time;

    public PlayerCollision(Player player1, Player player2, double time) {
        this.player1 = player1;
        this.player2 = player2;
        this.time = time;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public double getTime() {
        return time;
    }

    public Vector2D getNormalDirection() {
        Vector2D normal = player1.getPosition().subtract(player2.getPosition());
        return normal.versor();
    }

    public void adjustRadii() {
        player1.setRadius(player1.getMinRadius());
        player2.setRadius(player2.getMinRadius());
    }

    public void adjustRadii(Player maradona) {
        if (player1.equals(maradona)) {
            player1.setRadius(player1.getMinRadius());

            return;
        } else if (player2.equals(maradona)) {
            player2.setRadius(player2.getMinRadius());
            return;
        }
        adjustRadii();
    }

    public boolean isPlayerInvolved(Player player) {
        return player.equals(player1) || player.equals(player2);
    }
}
