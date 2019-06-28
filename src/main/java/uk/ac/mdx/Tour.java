package uk.ac.mdx;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * A class for dealing with tours of a TSP problem instance
 * An interface to the current problem allows the calculation of distances and
 * the implementation of an ordering
 * @author Matthew M. Jones
 */
public class Tour extends Path implements Comparable<Tour> {


    Tour() {
        super();
    }

    Tour(Integer[] thePath) throws IllegalArgumentException {
        super(thePath);
    }

    Tour(int size) {
        super(size);
    }

    Tour(Path path) {
        this.thePath = path.thePath;
        calculateLength = true;
        length = -1.0;
    }

    /**
     * This generates the default tour going from city i to city i+1
     * for i = 0, 1, ..., n-1
     * @return The default tour {0,1,..., n-1}
     */
    public static Tour generateDefaultTour() {
        // Use the Stram API to generate an Integer []
        // {0, 1, 2, ..., n-1} where n is the number of Cities
        Integer [] l = Arrays
                .stream(
                        IntStream
                                .range(0, TSPProblem.getProblem().getNumberOfCities())
                                .toArray())
                .boxed()
                .toArray(Integer[]::new);

        return new Tour(l);
    }

    /**
     * Finds the length in the current TSP Problem
     * of the tour
     * @return the length of the tour
     */
    @Override
    public double length() {
        if (calculateLength) {
            length = TSPProblem.getProblem().closedPathLength(this);
            calculateLength = false;
        }
        return length;
    }

    public double getFitness() {
        return (1 / this.length());
    }

    public void twoOpt(int i, int j) {
        int firstIndex = Math.min(i, j);
        int secondIndex = Math.max(i, j);

        Collections.reverse(thePath.subList(firstIndex, secondIndex + 1));
        calculateLength = true;
    }

    public void threeOpt(int i, int j, int k) {
        int [] ind = {i,j,k};
        Arrays.sort(ind);

        Tour [] newTours = new Tour[4];

        for (int n = 0; n < newTours.length; n++) {
            newTours[n] = new Tour();
        }

        // newTour[0] 0 -> i-1, j -> k-1, i -> j-1, k -> 0-1
        for (int n = 0; n < ind[0]; n++) { newTours[0].add(this.get(n));}
        for (int n = ind[1]; n < ind[2]; n++) { newTours[0].add(this.get(n));}
        for (int n = ind[0]; n < ind[1]; n++) { newTours[0].add(this.get(n));}
        for (int n = ind[2]; n < this.size(); n++) { newTours[0].add(this.get(n));}

        // newTour[1] i-1 -> 0, 0-1 -> k, j -> k-1, i -> j-1
        for (int n = ind[0]; n >= 0; n--) { newTours[1].add(this.get(n));}
        for (int n = this.size() - 1; n >= ind[2]; n--) { newTours[1].add(this.get(n));}
        for (int n = ind[1]; n < ind[2]; n++) { newTours[1].add(this.get(n));}
        for (int n = ind[0]; n < ind[1]; n++) { newTours[1].add(this.get(n));}

        // newTour[2] 0 -> i-1, k-1 -> j, i -> j-1, k -> 0-1
        for (int n = 0; n < ind[0]; n++) { newTours[2].add(this.get(n));}
        for (int n = ind[2] - 1; n >= ind[1]; n--) { newTours[2].add(this.get(n));}
        for (int n = ind[0]; n < ind[1]; n++) { newTours[2].add(this.get(n));}
        for (int n = ind[2]; n < this.size(); n++) { newTours[2].add(this.get(n));}

        // newTour[3]
        for (int n = 0; n < ind[0]; n++) { newTours[3].add(this.get(n));}
        for (int n = ind[1]; n < ind[2]; n++) { newTours[3].add(this.get(n));}
        for (int n = ind[1] - 1; n >= ind[0]; n--) { newTours[3].add(this.get(n));}
        for (int n = ind[2]; n < this.size(); n++) { newTours[3].add(this.get(n));}

        int bestIndex = 0;
        double bestLength = newTours[bestIndex].length();

        for (int n = 1; n < 4; n++) {

            if (newTours[n].length() < bestLength) {
                bestIndex = n;
                bestLength = newTours[n].length();
            }
        }
        this.thePath = newTours[bestIndex].thePath;
    }


    @Override
    public int compareTo(Tour t) {
        double diff = this.length() - t.length();
        if (diff == 0) { return 0; }
        else {
            return diff < 0 ? 1 : -1;
        }
    }

    @Override
    protected Tour clone() throws CloneNotSupportedException {
        Tour clonedPath = new Tour(this.size());
        for (int i = 0; i < this.size(); i++) {
            clonedPath.add(this.get(i));
        }
        return clonedPath;
    }

}

