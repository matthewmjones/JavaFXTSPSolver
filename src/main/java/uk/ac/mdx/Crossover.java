package uk.ac.mdx;

/**
 * An abstract class for use in implementing crossover
 * algorithms
 * @author Matthew M. Jones
 */
abstract class Crossover {

    public abstract Tour makeChild(Tour parentA, Tour parentB);

    public abstract Population makeChildren(Population parents, int numberOfChildren);
}
