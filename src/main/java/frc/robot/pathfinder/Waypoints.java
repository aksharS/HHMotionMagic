package frc.robot.pathfinder;

public class Waypoints extends CoordinatePoint {

    private CoordinatePoint point;
    private double curvatureAtPoint;
    private double targetVelocityAtPoint;
    private double distanceAlongPathAtPoint;

    public CoordinatePoint getPoint() {
        return point;
    }

    public void setPoint(CoordinatePoint point) {
        this.point = point;
    }

    public double getCurvatureAtPoint() {
        return curvatureAtPoint;
    }

    public void setCurvatureAtPoint(double curvatureAtPoint) {
        this.curvatureAtPoint = curvatureAtPoint;
    }

    public double getTargetVelocityAtPoint() {
        return targetVelocityAtPoint;
    }

    public void setTargetVelocityAtPoint(double targetVelocityAtPoint) {
        this.targetVelocityAtPoint = targetVelocityAtPoint;
    }

    public double getDistanceAlongPathAtPoint() {
        return distanceAlongPathAtPoint;
    }

    public void setDistanceAlongPathAtPoint(double distanceAlongPathAtPoint) {
        this.distanceAlongPathAtPoint = distanceAlongPathAtPoint;
    }
}

