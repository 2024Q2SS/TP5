package ar.edu.itba.ss;

public abstract class Player {

    private final double tau;
    private double radius;
    private final double maxSpeed;
    private final double minRadius;
    private final double maxRadius;
    private final double beta;
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D goal;
    private final Field field;
    private final double Ap = 1;
    private final double Bp = 1;

    private Vector2D nextVelocity;

    public Player(double tau, double minRadius, double maxRadius, double maxSpeed, double beta, Field field) {
        this.tau = tau;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.maxSpeed = maxSpeed;
        this.field = field;
        this.beta = beta;
        initialize();
    }

    public abstract void initialize();

    public double getBeta() {
        return beta;
    }

    public Field getField() {
        return field;
    }

    public double getTau() {
        return tau;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getMinRadius() {
        return minRadius;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getGoal() {
        return goal;
    }

    public void setGoal(Vector2D goal) {
        this.goal = goal;
    }

    public double distanceTo(Player other) {
        return position.distance(other.getPosition()) - radius - other.getRadius();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }

    public double getScapeSpeed() {
        return getMaxSpeed();
    }

    public double getTargetedSpeed() {
        double speed = getMaxSpeed()
                * Math.pow((getRadius() - getMinRadius()) / (getMaxRadius() - getMinRadius()), getBeta());
        return speed;

    }

    public void computeNextVelocity(Vector2D direction, double magnitude) {
        nextVelocity = direction.multiply(magnitude);
    }

    public void updateVelocity() {
        velocity = nextVelocity;
    }

    public void updatePosition() {
        Vector2D newPosition = position.add(velocity.multiply(field.getDt()));
        setPosition(newPosition);
    }

    public abstract Vector2D getGoalDirection();

    public double getAp() {
        return Ap;
    }

    public double getBp() {
        return Bp;
    }

}
