package ar.edu.itba.ss;

import java.io.FileWriter;
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
                conf.getBeta(), conf.getA(), conf.getB(),
                this);
        dt = conf.getMinRadius() / (conf.getRedMaxSpeed() * 2);

        for (int i = 0; i < conf.getBlueN(); i++) {
            players.add(new BluePlayer(conf.getBlueTau(), conf.getMinRadius(), conf.getMaxRadius(),
                    conf.getBlueMaxSpeed(), conf.getBeta(), this));
        }
    }

    public Player getMaradona() {
        return maradona;
    }

    public List<Player> getPlayers() {
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

        if (maradona.getPosition().getX() - maradona.getRadius() <= 0) {
            System.out.println("Mardona hizo try");
            return true;
        }
        for (Player player : players) {
            if (player.distanceTo(maradona) <= 0) {
                System.out.println("Lo taclearon a maradona");
                return true;
            }
        }
        return false;
    }

    public void run() {
        double time = 0;

        StringBuilder builder = new StringBuilder();
        builder.append("t,mx,my,mr");
        for (int i = 0; i < players.size(); i++) {
            builder.append(",p" + i + "x,p" + i + "y,p" + i + "r");
        }
        builder.append("\n");
        try (FileWriter fw = new FileWriter("output.csv")) {
            fw.write(builder.toString());
            builder.setLength(0);
            builder.append(time + "," + maradona.getPosition().getX() + "," + maradona.getPosition().getY() + ","
                    + maradona.getRadius());
            for (Player player : players) {
                builder.append("," + player.getPosition().getX() + "," + player.getPosition().getY() + ","
                        + player.getRadius());
            }
            builder.append("\n");
            fw.write(builder.toString());

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
                        Collision wallCollision = new WallCollision(player,
                                new Vector2D(player.getPosition().getX(), 0),
                                time);
                        collisions.add(wallCollision);
                        playersNotColliding.remove(player);
                    }
                    // check right wall
                    //
                    if (player.getPosition().getX() + player.getRadius() > fieldWidth) {
                        Collision wallCollision = new WallCollision(player,
                                new Vector2D(fieldWidth, player.getPosition().getY()), time);
                        collisions.add(wallCollision);
                        playersNotColliding.remove(player);
                    }

                    // check between player collisions
                    for (Player other : players) {
                        if (player != other) {
                            double distance = player.distanceTo(other);
                            if (distance < 0) {
                                System.out.println("COLLISION");
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
                    Collision wallCollision = new WallCollision(maradona,
                            new Vector2D(maradona.getPosition().getX(), 0),
                            time);
                    collisions.add(wallCollision);
                    playersNotColliding.remove(maradona);
                }

                // check right wall
                if (maradona.getPosition().getX() + maradona.getRadius() > fieldWidth) {
                    Collision wallCollision = new WallCollision(maradona,
                            new Vector2D(fieldWidth, maradona.getPosition().getY()), time);
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
                //
                for (Player player : playersNotColliding) {
                    Vector2D direction = player.getGoalDirection();
                    player.computeNextVelocity(direction, player.getTargetedSpeed());
                }
                List<Player> auxList = new ArrayList<>();
                auxList.addAll(players);
                auxList.removeAll(playersNotColliding);
                for (Player player : auxList) {
                    Vector2D direction = new Vector2D(0, 0);
                    System.out.println("Player colliding: " + player);
                    double magnitude = player.getScapeSpeed();
                    for (Collision collision : collisions) {
                        if (collision.isPlayerInvolved(player)) {
                            direction = direction.add(collision.getRelativeNormalDirection(player));

                        }
                    }
                    System.out.println(direction);

                    player.computeNextVelocity(direction.versor(), magnitude);
                }
                // MARADONA COLLISION CALCULATION
                if (!playersNotColliding.contains(maradona)) {
                    Vector2D direction = new Vector2D(0, 0);
                    double magnitude = maradona.getScapeSpeed();
                    for (Collision collision : collisions) {
                        if (collision.isPlayerInvolved(maradona)) {
                            direction = direction.add(collision.getRelativeNormalDirection(maradona));
                        }
                    }
                    maradona.computeNextVelocity(direction.versor(), magnitude);
                }
                // Iteration 4: update velocity and speed
                for (Player player : players) {
                    player.updateVelocity();
                    player.updatePosition();
                }
                maradona.updateVelocity();
                maradona.updatePosition();

                time += dt;

                for (Player player : players) {
                    player.setGoal(maradona.getPosition());
                }
                maradona.setGoal(new Vector2D(0, maradona.getPosition().getY()));

                builder.setLength(0);
                builder.append(time + "," + maradona.getPosition().getX() + "," + maradona.getPosition().getY() + ","
                        + maradona.getRadius());
                for (Player player : players) {
                    builder.append("," + player.getPosition().getX() + "," + player.getPosition().getY() + ","
                            + player.getRadius());
                }
                builder.append("\n");
                fw.write(builder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
