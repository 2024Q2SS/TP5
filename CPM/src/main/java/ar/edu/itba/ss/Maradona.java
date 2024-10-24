package ar.edu.itba.ss;

import java.util.List;
import java.util.ArrayList;

public class Maradona extends Player {

    public Maradona(double tau, double minRadius, double maxRadius, double maxSpeed, double beta, Field field) {
        super(tau, minRadius, maxRadius, maxSpeed, beta, field);
    }

    @Override
    public void initialize() {
        // set position
        setPosition(new Vector2D(this.getField().getFieldWidth() - 2 * this.getMinRadius(),
                this.getField().getFieldHeight() / 2));
        setVelocity(new Vector2D(0, 0));
        setGoal(new Vector2D(0, getPosition().getY()));
        setRadius(getMinRadius());

    }

    public Vector2D getGoalDirection() {
        Vector2D tempGoal = getGoal().subtract(getPosition()).versor();
        double velocityModule = getVelocity().module();
        if (velocityModule == 0) {
            return tempGoal;
        }

        List<Player> players = getField().getPlayers();

        List<Player> obstaclesInFront = new ArrayList<>();

        double maradonaX = getPosition().getX();
        double maradonaY = getPosition().getY();
        double maradonaXVelocity = getVelocity().getX();
        double maradonaYVelocity = getVelocity().getY();

        for (Player player : players) {
            double obstacleX = player.getPosition().getX();
            double obstacleY = player.getPosition().getY();

            double projection = (obstacleX - maradonaX) * maradonaXVelocity
                    + (obstacleY - maradonaY) * maradonaYVelocity;
            projection = projection / (Math.pow(maradonaXVelocity, 2) + Math.pow(maradonaYVelocity, 2));
            if (projection > 0) {
                obstaclesInFront.add(player);
            }
        }
        if (obstaclesInFront.isEmpty()) {
            return tempGoal;
        }

        obstaclesInFront.sort((a, b) -> a.distanceTo(this) < b.distanceTo(this) ? -1 : 1);
        Player obstacle = obstaclesInFront.get(0);
        Vector2D obstacleDir = obstacle.getPosition().subtract(getPosition());
        Vector2D obstacleDirVersor = obstacleDir.versor();
        double distance = distanceTo(obstacle);
        double dotProduct = getVelocity().dot(obstacleDir);
        double cosThetaJ = dotProduct / (distance * velocityModule);
        double multiplier = getAp() * Math.exp(-distance / getBp()) * cosThetaJ;
        tempGoal = tempGoal.subtract(obstacleDirVersor.multiply(multiplier));

        return tempGoal.versor();
    }
}
