package frc.robot.pathfinder;

public class CoordinatePoint {

    private double x;
    private double y;

    public CoordinatePoint() {
        x = y = 0;
    }

    public CoordinatePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setCoordinatePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return ("(" + x + ", " + y + ")");
    }

    /*
     * curvature is defined as (4*A)/(|p1-p2|*|p2-p3|*|p3-p1|) where A is the
     * area enclosed by the triangle formed by three points, p1 ("this"
     * coordinate point), p2, and p3, and |p1-p2| is the distance between points
     * p1 and p2.
     */
    public double getCurvatureFromThreePoints(CoordinatePoint p1, CoordinatePoint p2) {
        double len1 = this.distanceTo(p1);
        double len2 = p1.distanceTo(p2);
        double len3 = p2.distanceTo(this);
        double semiPerimeter = 0.5 * (len1 + len2 + len3);
        double area = (Math.sqrt(semiPerimeter * (semiPerimeter - len1) * (semiPerimeter - len2) * (semiPerimeter - len3))); // Heron's Formula
        double curvature = (4 * area) / (len1 * len2 * len3);
        return curvature;
    }

    /*
     * returns distance between "this" coordinate point and p1
     */
    public double distanceTo(CoordinatePoint p1) {
        return (Math.sqrt(Math.pow(p1.getX() - this.getX(), 2.0) + Math.pow(p1.getY() - this.getY(), 2.0)));
    }
}