package uk.ac.mdx;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * A class for dealing with populations of Tours
 * The population is stored as a Max Heap for ease of finding the best tour.
 * Also here are methods for merging populations, mutating them and finding
 * parents via the SUS algorithm.
 * @author Matthew M. Jones
 */
public class Population {

    private TourHeap thePopulation;

    Population() {
        thePopulation = new TourHeap();
    }

    Population(int populationSize, Tour prototypeTour) {
        thePopulation = new TourHeap();

        for (int i = 0; i < populationSize; i++) {
            try {
                Tour t = prototypeTour.clone();
                t.shuffle();
                thePopulation.insert(t);
            } catch (CloneNotSupportedException ex) { }
        }
    }

    Population(int populationSize) {
        thePopulation = new TourHeap();

        Tour prototypeTour = Tour.generateDefaultTour();

        for (int i = 0; i < populationSize; i++) {
            try {
                Tour t = prototypeTour.clone();
                t.shuffle();
                thePopulation.insert(t);
            } catch (CloneNotSupportedException ex) {}
        }

    }

    public void add(Tour t) { thePopulation.insert(t); }

    public int getSize()    { return thePopulation.size(); }

    public Tour remove()    { return thePopulation.delete(); }

    public Tour getItem(int i) { return thePopulation.getItem(i); }

    synchronized public Tour getFittest() {
        return thePopulation.maxItem();
    }

    public Tour [] toArray() {
        return thePopulation.toList().toArray(new Tour[thePopulation.size()]);
    }

    public double totalFitness() {

        return Arrays.asList(this.toArray())
                .stream()
                .mapToDouble(Tour::getFitness)
                .sum();
    }

    public Population getParents(int numberOfParents) {

        Population parents = new Population();

        double totalFitness = this.totalFitness();

        double p = totalFitness / (double) numberOfParents;
        double s = Math.random() * p;

        // Uses the Stream interface to find s + i * p for i = 0, 1, ..., n-1
        // where n = numberOfParents
        double [] points = IntStream
                .range(0, numberOfParents)
                .mapToDouble( x -> s + p * (double) x )
                .toArray();

        Tour [] tourList = this.toArray();


        for (int i = 0; i < numberOfParents; i++) {
            int index = 0;
            double fitness = tourList[index].getFitness();
            while (fitness < points[i]) {
                fitness += tourList[index++].getFitness();
            }
            parents.add(tourList[index]);
        }
        return parents;
    }


    public void mergeWith(Population anotherPopulation) {

        Tour[] anotherPopulationArray = anotherPopulation.toArray();

        for (Tour t : anotherPopulationArray) {
            this.add(t);
        }
    }

    public void mergeWithAndTruncate(Population anotherPopulation, int maxSize) {

        Tour[] anotherPopulationArray = anotherPopulation.toArray();

        for (Tour t : anotherPopulationArray) {
            this.add(t);
        }

        // This removes tours in the tail of the poulation array.
        // Although this is not strictly consistent with the algorithm,
        // these are the weaker tours by the design of the heap array model
        this.thePopulation.truncate(maxSize);
    }

    public void mutatePopulation(double rate) {

        for (int i = 0; i < thePopulation.size(); i++) {
            if (Math.random() < rate) {

                Tour t = thePopulation.removeItem(i);
                t.swap((int) (Math.random() * t.size()), (int) (Math.random() * t.size()));
                thePopulation.insert(t);
            }
        }
    }


}
