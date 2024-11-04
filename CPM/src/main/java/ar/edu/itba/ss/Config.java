package ar.edu.itba.ss;

public class Config {
    private double fieldWidth;
    private double fieldHeight;
    private double minRadius;
    private double maxRadius;
    private double blueTau;
    private double redTau;
    private double blueMaxSpeed;
    private double redMaxSpeed;
    private int blueN;
    private double beta;
    private double a;
    private double b;

    public Config(double fieldWidth, double fieldHeight, double minRadius, double maxRadius, double blueTau,
            double redTau,
            double blueMaxSpeed, double redMaxSpeed, int blueN, double beta, double a, double b) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.blueTau = blueTau;
        this.redTau = redTau;
        this.blueMaxSpeed = blueMaxSpeed;
        this.redMaxSpeed = redMaxSpeed;
        this.blueN = blueN;
        this.beta = beta;
        this.a = a;
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(double fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public double getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(double fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public double getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(double minRadius) {
        this.minRadius = minRadius;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(double maxRadius) {
        this.maxRadius = maxRadius;
    }

    public double getBlueTau() {
        return blueTau;
    }

    public void setBlueTau(double blueTau) {
        this.blueTau = blueTau;
    }

    public double getRedTau() {
        return redTau;
    }

    public void setRedTau(double redTau) {
        this.redTau = redTau;
    }

    public double getBlueMaxSpeed() {
        return blueMaxSpeed;
    }

    public void setBlueMaxSpeed(double blueMaxSpeed) {
        this.blueMaxSpeed = blueMaxSpeed;
    }

    public double getRedMaxSpeed() {
        return redMaxSpeed;
    }

    public void setRedMaxSpeed(double redMaxSpeed) {
        this.redMaxSpeed = redMaxSpeed;
    }

    public int getBlueN() {
        return blueN;
    }

    public void setBlueN(int blueN) {
        this.blueN = blueN;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }
}
