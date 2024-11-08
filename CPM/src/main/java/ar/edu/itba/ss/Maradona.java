package ar.edu.itba.ss;

import java.util.List;
import java.util.ArrayList;

public class Maradona extends Player {
    private double Ap = 1;
    private double Bp = 1;

    public double getAp() {
        return Ap;
    }

    public double getBp() {
        return Bp;
    }

    public Maradona(double tau, double minRadius, double maxRadius, double maxSpeed, double beta, double a, double b,
            Field field) {
        super(tau, minRadius, maxRadius, maxSpeed, beta, field);
        this.Ap = a;
        this.Bp = b;
    }

    @Override
    public void initialize() {
        // set position
        setPosition(new Vector2D(this.getField().getFieldWidth() - this.getMinRadius(),
                this.getField().getFieldHeight() / 2));
        setVelocity(new Vector2D(0, 0));
        setGoal(new Vector2D(0, getPosition().getY()));
        setRadius(getMinRadius());

    }

    // TODO: AGREGAR PAREDES ?
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

        Vector2D obstacleSum = Vector2D.ZERO();
        for (Player obstacle : obstaclesInFront) {
            Vector2D obstacleDirectionVersor = this.getPosition().subtract(obstacle.getPosition()).versor();
            double distance = this.distanceTo(obstacle);
            double multiplier = this.getAp() * Math.exp(-distance / this.getBp());
            Vector2D ncVector = obstacleDirectionVersor.multiply(multiplier);
            obstacleSum = obstacleSum.add(ncVector);
        }
        tempGoal = tempGoal.add(obstacleSum);
        return tempGoal.versor();
    }

    private double distanceWithMinRadius(Player player) {
        return this.getPosition().distance(player.getPosition()) - this.getMinRadius() - player.getMinRadius();
    }
}
