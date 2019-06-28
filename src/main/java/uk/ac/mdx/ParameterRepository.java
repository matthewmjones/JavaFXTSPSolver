package uk.ac.mdx;

/**
 * Storage for a number of important parameters involved in the algorithms
 * @author Matthew M. Jones
 */
public class ParameterRepository {

    private boolean slowMotion = false;

    // Simulated annealing
    private double initialTemperature = 10000;
    private double finalTemperature = 0.001;
    private double coolingConstant = 0.999;

    // Genetic algorithm
    private int populationSize = 400;
    private int numberOfParents = 100;
    private int numberOfGenerations = 1000;
    private double birthRate = 1.6;
    private double mutationRate = 0.1;
    private boolean elitism = true;
    private int crossoverType = CrossoverFactory.ORDER_BASED_CROSSOVER;

    private static ParameterRepository thisStore= null;

    private ParameterRepository() {

    }

    /**
     * Returns the singleton instance of this class
     * @return the instance of this class
     */
    public static ParameterRepository Store() {
        if (thisStore == null) {
            thisStore = new ParameterRepository();
        }
        return thisStore;
    }

    public void setSlowMotion(boolean slowMotion) {
        this.slowMotion = slowMotion;
    }

    public void setInitialTemperature(double initialTemperature) {
        this.initialTemperature = initialTemperature;
    }

    public void setFinalTemperature(double finalTemperature) {
        this.finalTemperature = finalTemperature;
    }

    public void setCoolingConstant(double coolingConstant) {
        this.coolingConstant = coolingConstant;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setNumberOfParents(int numberOfParents) {
        this.numberOfParents = numberOfParents;
    }

    public void setNumberOfGenerations(int numberOfGenerations) {
        this.numberOfGenerations = numberOfGenerations;
    }

    public void setBirthRate(double birthRate) {
        this.birthRate = birthRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setElitism(boolean elitism) {
        this.elitism = elitism;
    }

    public void setCrossoverType(int crossoverType) {
        this.crossoverType = crossoverType;
    }

    public boolean isSlowMotion() {
        return slowMotion;
    }

    public double getInitialTemperature() {
        return initialTemperature;
    }

    public double getFinalTemperature() {
        return finalTemperature;
    }

    public double getCoolingConstant() {
        return coolingConstant;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getNumberOfParents() {
        return numberOfParents;
    }

    public int getNumberOfGenerations() {
        return numberOfGenerations;
    }

    public double getBirthRate() {
        return birthRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public boolean isElitism() {
        return elitism;
    }

    public int getCrossoverType() {
        return crossoverType;
    }

}
