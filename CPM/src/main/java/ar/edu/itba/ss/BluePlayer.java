package ar.edu.itba.ss;

import java.util.List;
import java.util.ArrayList;

public class BluePlayer extends Player {

    public BluePlayer(double blueTau, double minRadius, double maxRadius, double blueMaxSpeed, double beta,
            Field field) {
        super(blueTau, minRadius, maxRadius, blueMaxSpeed, beta, field);
    }

    public boolean collidesWithOtherPlayer(List<Player> players) {
        for (Player player : players) {
            if (player.equals(this) || player.getPosition() == null) {
                continue;
            }
            if (this.getPosition().distance(player.getPosition()) < this.getRadius() + player.getRadius()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void initialize() {
        do {
            double randomX = Math.random() * (this.getField().getFieldWidth() - 2 * this.getRadius())
                    + this.getRadius();
            double randomY = Math.random() * (this.getField().getFieldHeight() - 2 * this.getRadius())
                    + this.getRadius();
            setPosition(new Vector2D(randomX, randomY));

        } while (collidesWithOtherPlayer(this.getField().getPlayers()));
        setVelocity(new Vector2D(0, 0));
        setRadius(getMinRadius());
        setGoal(getField().getMaradona().getPosition());
    }

    public Vector2D getGoalDirection() {
        Vector2D tempGoal = getGoal().subtract(getPosition()).versor();
        return tempGoal.versor();
    }
}
