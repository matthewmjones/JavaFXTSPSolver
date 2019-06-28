package uk.ac.mdx;

/**
 * A class for storing city positions
 * @author Matthew M. Jones
 */
public class City {

    // Define the range of possible x and y values
    public static final double MAX_X = 1000;
    public static final double MAX_Y = 1000;

    // The coordinates of the City
    private double xPos;
    private double yPos;

    City() {
        xPos = Math.random() * MAX_X;
        yPos = Math.random() * MAX_Y;
    }

    City(double x, double y) {
        this.xPos = x;
        this.yPos = y;
    }

    City(City anotherCity) {
        this.xPos = anotherCity.getxPos();
        this.yPos = anotherCity.getxPos();
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double distanceTo(City anotherCity) {
        double xDiff = this.xPos - anotherCity.getxPos();
        double yDiff = this.yPos - anotherCity.getyPos();
        return Math.sqrt( xDiff * xDiff + yDiff * yDiff);
    }

    @Override
    public String toString() {
        return "(" + this.xPos + ", " + this.yPos + ")";
    }
}

