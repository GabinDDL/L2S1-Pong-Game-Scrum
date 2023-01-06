package model;

/**
 * This class contains methods to compute the intersection of two lines in 2D
 * space.
 */
public class IntersectionCalculator2D {

    private IntersectionCalculator2D() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * SOlves a system of two linear equations in two variables using Cramer's rule.
     * 
     * @param u     The x and y coefficients of the first line
     * @param v     The x and y coefficients of the second line
     * @param point The x and y coordinates of the point
     * @return The solution of the system, or null if the system is not solvable
     */
    public static Vector2 cramer(Vector2 u, Vector2 v, Vector2 point) {
        Vector2 solution = null;

        double detUV = u.getXdir() * v.getYdir() - u.getYdir() * v.getXdir();

        if (detUV != 0) {

            double x = (point.getXdir() * v.getYdir() - point.getYdir() * v.getXdir());
            double y = (point.getYdir() * u.getXdir() - point.getXdir() * u.getYdir());
            solution = new Vector2(x / detUV, y / detUV);
        }

        return solution;
    }

    /**
     * Computes the intersection of two lines in 2D space.
     * 
     * @param u1     First line vector
     * @param point1 A point on the first line
     * @param u2     Second line vector
     * @param point2 A point on the second line
     * @return The intersection point of the two lines, or null if the lines are
     *         parallel or coincident
     */
    public static Vector2 computeLineIntersection(Vector2 u1, Vector2 point1, Vector2 u2, Vector2 point2) {
        Vector2 point2MinusPoint1 = new Vector2(point1);
        point2MinusPoint1.addAngle(Math.PI);
        point2MinusPoint1.add(point2);
        Vector2 minusU2 = new Vector2(u2);
        minusU2.addAngle(Math.PI);
        Vector2 intersection = new Vector2(u1);
        intersection.scalarMultiplication(cramer(u1, minusU2, point2MinusPoint1).getXdir());
        intersection.add(point1);
        intersection.roundCoords();
        return intersection;
    }
}