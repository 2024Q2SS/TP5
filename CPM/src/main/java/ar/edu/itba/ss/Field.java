package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final Player maradona;
    private final List<Player> players = new ArrayList<>();
    private final List<Collision> collisions = new ArrayList<>();
    private final List<Player> playersNotColliding = new ArrayList<>();
    private final double fieldWidth;
    private final double fieldHeight;
    private final double dt;

    public Field(Config conf) {
        this.fieldWidth = conf.getFieldWidth();
        this.fieldHeight = conf.getFieldHeight();
        maradona = new Maradona(conf.getRedTau(), conf.getMinRadius(), conf.getMaxRadius(), conf.getRedMaxSpeed(),
                conf.getBeta(),
                this);
        dt = conf.getMinRadius() / (conf.getRedMaxSpeed() * 2);

        for (int i = 0; i < conf.getBlueN(); i++) {
            players.add(new BluePlayer(conf.getBlueTau(), conf.getMinRadius(), conf.getMaxRadius(),
                    conf.getBlueMaxSpeed()), this);
        }
    }

    public Player getMaradona() {
        return maradona;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public double getFieldWidth() {
        return fieldWidth;
    }

    public double getFieldHeight() {
        return fieldHeight;
    }

    public double getDt() {
        return dt;
    }

    public boolean finishConditionsMet() {
        // TODO: CHECK OVERLAP WITH MARADONA OR MARADONA TRY
        return false;
    }

    public void run() {
        double time = 0;
        while (!finishConditionsMet()) {
            // Iteration 1: Find contacts and calculate V_e
            collisions.clear();
            playersNotColliding.addAll(players);
            playersNotColliding.add(maradona);
            for (Player player : players) {
                // Check wall collisions
                // check top wall
                if (player.getPosition().getY() + player.getRadius() > fieldHeight) {
                    Collision wallCollision = new WallCollision(player,
                            new Vector2D(player.getPosition().getX(), fieldHeight), time);
                    collisions.add(wallCollision);
                    playersNotColliding.remove(player);
                }

                // check bottom wall
                if (player.getPosition().getY() - player.getRadius() < 0) {
                    Collision wallCollision = new WallCollision(player, new Vector2D(player.getPosition().getX(), 0),
                            time);
                    collisions.add(wallCollision);
                    playersNotColliding.remove(player);
                }
                // check between player collisions
                for (Player other : players) {
                    if (player != other) {
                        double distance = player.distanceTo(other);
                        if (distance < 0) {
                            Collision collision = new PlayerCollision(player, other, time);
                            collisions.add(collision);
                            playersNotColliding.remove(player);
                            playersNotColliding.remove(other);
                        }
                    }
                }
                // check between maradona and player collisions
                double distance = player.distanceTo(maradona);
                if (distance < 0) {
                    Collision collision = new PlayerCollision(player, maradona, time);
                    collisions.add(collision);
                    playersNotColliding.remove(maradona);
                }
            }
            // Check wall collisions for Maradona
            // check top wall
            if (maradona.getPosition().getY() + maradona.getRadius() > fieldHeight) {
                Collision wallCollision = new WallCollision(maradona,
                        new Vector2D(maradona.getPosition().getX(), fieldHeight), time);
                collisions.add(wallCollision);
                playersNotColliding.remove(maradona);
            }

            // check bottom wall
            if (maradona.getPosition().getY() - maradona.getRadius() < 0) {
                Collision wallCollision = new WallCollision(maradona, new Vector2D(maradona.getPosition().getX(), 0),
                        time);
                collisions.add(wallCollision);
                playersNotColliding.remove(maradona);
            }
            // Iteration 2: Adjust radii
            for (Collision collision : collisions) {
                collision.adjustRadii();
            }
            for (Player player : playersNotColliding) {
                player.setRadius(Math.min(player.getRadius() + player.getMaxRadius() * this.dt / player.getTau(),
                        player.getMaxRadius()));
            }
            // Iteration 3:
            // - Compute direction and sense of V_d considering current position and target
            // locations
            // - Compute magnitude of V_d depending on radius
            // Iteration 4: update position and speed
        }
    }
}
