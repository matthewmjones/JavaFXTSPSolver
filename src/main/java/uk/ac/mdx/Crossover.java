package uk.ac.mdx;

/**
 * An abstract class for use in implementing crossover
 * algorithms
 * @author Matthew M. Jones
 */
interface Crossover {

    Tour makeChild(Tour parentA, Tour parentB);

    Population makeChildren(Population parents, int numberOfChildren);
}
