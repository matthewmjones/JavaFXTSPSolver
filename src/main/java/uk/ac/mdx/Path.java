package uk.ac.mdx;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Generic path class
 * @author Matthew M. Jones
 */
public class Path implements Cloneable {

    protected ArrayList<Integer> thePath;
    protected double length;
    protected boolean calculateLength;

    Path() {
        this.thePath = new ArrayList<>();
    }

    Path(int sizeOfPath) {
        thePath = new ArrayList<>(sizeOfPath);
        length = -1.0;
        calculateLength = true;

    }

    Path(Integer[] thePath) throws IllegalArgumentException {
        this.thePath = new ArrayList<>();
        for (Integer i : thePath) {
            if (i < 0) {
                throw new IllegalArgumentException("Parameters in path array must be non-negative.");
            } else {
                this.thePath.add(i);
            }
        }
        length = -1.0;
        calculateLength = true;
    }

    /**
     * @return Returns the number of cities in the path
     */
    public int size() {
        return thePath.size();
    }

    /**
     * Swaps cities at positions i and j
     * @param i - the first city
     * @param j - the second city
     */
    public void swap(int i, int j) {
        Collections.swap(thePath, i, j);
        calculateLength = true;
    }

    /**
     * Permutes the path
     */
    public void shuffle() {
        Collections.shuffle(thePath);
        calculateLength = true;
    }

    /**
     * returns the Integer at position i
     * @param i - the position i
     * @return the path at position i
     */
    public Integer get(int i) {
        return thePath.get(i);
    }

    public void add(Integer i) {
        thePath.add(i);
        calculateLength = true;
    }

    /**
     * Finds the length in the current TSP Problem
     * of the tour
     * @return the length of the path
     */
    public double length() {
        if (calculateLength) {
            length = TSPProblem.getProblem().pathLength(this);
            calculateLength = false;
        }
        return length;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(thePath.toArray());
    }

    public boolean equals(Path path) {
        boolean eq = true;
        if (this.size() != path.size()) { eq = false; }
        else {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i) != path.get(i)) {
                    eq = false;
                    break;
                }
            }
        }
        return eq;
    }

    @Override
    protected Path clone() throws CloneNotSupportedException {
        Path clonedPath = new Path(this.size());
        for (int i = 0; i < this.size(); i++) {
            clonedPath.add(this.get(i));
        }
        return clonedPath;
    }

    public Integer [] toArray() {
        return thePath.toArray(new Integer[thePath.size()]);
    }
}
