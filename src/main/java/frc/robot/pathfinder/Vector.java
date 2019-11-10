package frc.robot.pathfinder;

public class Vector {
    private double XComponent;
    private double YComponent;
    private double magnitude;

    /*
     * create a vector pointing from CoordinatePoint p1 to CoordinatePoint p2
     */
    public Vector(CoordinatePoint p1, CoordinatePoint p2) {
        XComponent = p2.getX() - p1.getX();
        YComponent = p2.getY() - p1.getY();
        magnitude = p1.distanceTo(p2);
    }

    /*
     * create a vector with components XComponent and YComponent
     */
    public Vector(double XComponent, double YComponent) {
        this.XComponent = XComponent;
        this.YComponent = YComponent;
        magnitude = Math.sqrt((XComponent*XComponent) + (YComponent*YComponent));
    }

    public double getXComponent() {
        return XComponent;
    }

    public double getYComponent() {
        return YComponent;
    }

    public double getmagnitude() {
        return magnitude;
    }

    public String toString()
    {
        return("<"+XComponent+", "+YComponent+">");
    }

    /*
     * returns "this" vector crossed with v1
     */
    public double Cross(Vector v1) {
        return ((this.XComponent * v1.getYComponent()) - (this.YComponent * v1.getXComponent()));
    }

    public void Scale(double s){
        this.XComponent *= s;
        this.YComponent *= s;
    }

    /**
     *
     * @param v1
     * @return "this vector dotted with v1
     */
    public double Dot(Vector v1) {
        return ((this.XComponent * v1.getXComponent()) + (this.YComponent + v1.getYComponent()));
    }
}
