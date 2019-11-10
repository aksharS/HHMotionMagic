package frc.robot.pathfinder;

import java.util.ArrayList;
import java.util.Collections;

public class Paths {

    private int idxOfLastClosestPoint = 0;

    public static double[][] straightLine = {
            {5, 0},
            {10, 0},
            {15, 0}
    };

    /**
     *
     * @param path
     * @param a
     * @param b
     * @param tolerance
     * @return
     */
    public static double[][] smoother(double[][] path, double a, double b, double tolerance){
        //copy array
        double[][] newPath = {};
        double change = tolerance;
        while(change >= tolerance){
            change = 0.0;
            for(int i=1; i<path.length-1; i++) {
                for (int j = 0; j < path[i].length; j++) {
                    double aux = newPath[i][j];
                    newPath[i][j] += a * (path[i][j] - newPath[i][j]) + b * (newPath[i - 1][j] + newPath[i + 1][j] - (2.0 * newPath[i][j]));
                    change += Math.abs(aux - newPath[i][j]);
                }
            }
        }
        return newPath;
    }


    /**
     *
     * @param path array of coordinate points
     * @param robotX current robot X position
     * @param robotY current robot Y position
     * @return a point on path that is closest to the robot
     */
    public CoordinatePoint findClosestPoint(CoordinatePoint[] path, double robotX, double robotY){
        CoordinatePoint robotLocation = new CoordinatePoint(robotX, robotY);
        ArrayList<Double> distances = new ArrayList<Double>();
        for (int i = this.idxOfLastClosestPoint; i < path.length; i++){
            distances.add(path[i].distanceTo(robotLocation));
        }
        int pointIndex = distances.indexOf(Collections.min(distances));
        return path[pointIndex];
    }

    /**
     *
     * @param E Is the starting point of the line segment
     * @param L Is the end point of the line segment
     * @param C Is the center of the circle (Robot Location in Coordinate Point)
     * @param lookaheadDistance is the radius of the circle, or lookahead distance
     * @return Coordinate Point on path
     */
    public CoordinatePoint findLookaheadPoint(CoordinatePoint E, CoordinatePoint L, CoordinatePoint C, double lookaheadDistance){
        Vector d = new Vector((L.getX()-E.getX()), (L.getY()-E.getY()));
        Vector f = new Vector((E.getX()-C.getX()), (E.getY()-C.getY()));

        double a = d.Dot(d);
        double b = 2 * (f.Dot(d));
        double c = f.Dot(f) - (lookaheadDistance*lookaheadDistance);
        double discriminant = b*b - 4*a*c;
        double t = 0;

        if (discriminant < 0){
            System.out.println("No Intersection");
        } else {
            discriminant = Math.sqrt(discriminant);
            double t1 = (-b - discriminant)/(2*a);
            double t2 = (-b + discriminant)/(2*a);

            if (t1 >= 0 && t1 <= 1) {
                t = t1;
            } else if (t2 >= 0 && t2<= 1){
                t = t2;
            } else {
                System.out.println("No Intersection");
            }
        }
        d.Scale(t);
        return new CoordinatePoint((E.getX()+d.getXComponent()), (E.getY()+d.getYComponent()));
    }

}
