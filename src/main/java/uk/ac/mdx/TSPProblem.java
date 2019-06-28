package uk.ac.mdx;

import java.util.Arrays;

/**
 * Class for holding details of a standard symmetric
 * TSP problem. This class deals with calculations of distances
 involved and positions of cities.
 Example: <p>
 City [] c = new City[10] <p>
 ==== create array of cities ==== <p>
 TSPProblem tsp = TSPProblem.getProblem(); <p>
 tsp.setListOfCities(c);  <p>
 * @author Matthew M. Jones
 */
public class TSPProblem {

    private City [] listOfCities;
    private double [][] distanceArray;
    private int numberOfCities;

	/* Implementing a singleton design so there is only one instance of TSPProblem available
	at any one time.
	*/

    private static TSPProblem tspProblem = null;

    private TSPProblem() {
    }

    /**
     * Returns the unique instance for the TSPProblem class.
     * To set a problem use the method:
     * setListOfCities()
     * @return current instance
     */
    public static TSPProblem getProblem() {
        if (tspProblem == null) {
            tspProblem = new TSPProblem();
        }
        return tspProblem;
    }

    /**
     *
     * @return an array of cities for the current problem
     */
    public City[] getListOfCities() {
        return listOfCities;
    }

    /**
     *
     * @return the array of distances for the current problem
     */
    public double[][] getDistanceArray() {
        return distanceArray;
    }

    /**
     *
     * @return the number of cities in the current problem
     */
    public int getNumberOfCities() {
        return numberOfCities;
    }

    /**
     * Use this method to set the cities for the current problem.
     * This method replaces the current problem
     * @param listOfCities an array of City
     */
    public void setListOfCities(City[] listOfCities) {
        this.listOfCities = listOfCities;
        this.generateDistanceArray();
        this.numberOfCities = listOfCities.length;
    }

    // generates the distance array for the current problem
    private void generateDistanceArray() {
        if (listOfCities != null) {
            distanceArray = new double[listOfCities.length][listOfCities.length];
            for (int i = 0; i < listOfCities.length; i++) {
                distanceArray[i][i] = 0;

                for (int j = 0; j < i; j++) {
                    distanceArray[i][j] = distanceArray[j][i]
                            = listOfCities[i].distanceTo(listOfCities[j]);
                }
            }
        } else {
            distanceArray = null;
        }
    }

    public double distance(int i,int j) {
        if (i < numberOfCities & j < numberOfCities & i >=0 & j >= 0) {
            return distanceArray[i][j];
        } else {
            return Double.NaN;
        }
    }

    /**
     * Returns the length of the path indicated by the indices in the parameter
     * int [] index
     * @param index - the position of the path for which we are querying the
     * length
     * @return length of the path at position index
     */
    public double pathLength(int [] index) {
        double dist = 0;
        for (int i = 1; i < index.length; i++) {
            dist += distance(index[i], index[i-1]);
        }
        return dist;
    }

    public double pathLength(Path path) {
        double dist = 0;
        for (int i = 1; i < path.size(); i++) {
            dist += distance(path.get(i), path.get(i-1));
        }
        return dist;
    }

    public double closedPathLength(Path path) {
        double dist = 0;
        for (int i = 1; i < path.size(); i++) {
            dist += distance(path.get(i), path.get(i-1));
        }
        dist += distance(path.get(0), path.get(path.size() - 1));
        return dist;
    }

    /**
     * Returns the length of the closed path indicated by the indices in the parameter
     * int [] index
     * @param index - the position of the closed path for which we are querying the
     * length
     * @return length of the closed path
     */
    public double closedPathLength(int [] index) {
        double dist = 0;
        for (int i = 1; i < index.length; i++) {
            dist += distance(index[i], index[i-1]);
        }
        dist += distance(index[0],index[index.length - 1]);
        return dist;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(listOfCities);
    }
}

