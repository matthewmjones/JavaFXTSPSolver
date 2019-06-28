package uk.ac.mdx;

import java.util.ArrayList;
import java.util.Collections;

/**
 * An implementation of the Order Based Crossover
 * This is O(n^2) where n is the size of the Tour
 * @author Matthew M. Jones
 */
public class OrderBasedCrossover extends Crossover {

    @Override
    public Tour makeChild(Tour parentA, Tour parentB) {

        Integer [] parentAArray = parentA.toArray();
        Integer [] parentBArray = parentB.toArray();

        ArrayList<Integer> randomSubsetofA = new ArrayList<>();

        for (int i = 0; i < parentA.size(); i++) {
            if (Math.random() < 0.5) {
                randomSubsetofA.add(parentAArray[i]);
            }
        }

        // find the positions of these values in parentB
        ArrayList<Integer> positions = new ArrayList<>();
        for (Integer i : randomSubsetofA) {
            for (int j = 0; j < parentB.size(); j++) {
                if (parentBArray[j].intValue() == i.intValue()) {
                    positions.add(j);
                    break;
                }
            }
        }
        Collections.sort(positions);

        Integer [] childArray = parentBArray.clone();

        for (int i = 0; i < positions.size(); i++) {
            childArray[positions.get(i)] = randomSubsetofA.get(i);
        }
        return new Tour(childArray);
    }

    @Override
    public Population makeChildren(Population parents, int numberOfChildren) {
        Population childrenPopulation = new Population();

        for (int i = 0; i < numberOfChildren; i++) {
            childrenPopulation
                    .add(makeChild(parents.getItem((int) (Math.random() * parents.getSize())), parents.getItem((int) (Math.random() * parents.getSize()))));
        }
        return childrenPopulation;
    }
}
