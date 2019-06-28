package uk.ac.mdx;

/**
 * Runs the genetic algorithm solver for the current TSPProblem
 * @author Matthew M. Jones
 */
public class GeneticAlgorithmTSPAlgorithm extends TSPAlgorithm {

    int populationSize = ParameterRepository.Store().getPopulationSize();
    int numberOfParents = ParameterRepository.Store().getNumberOfParents();
    int numberOfGenerations = ParameterRepository.Store().getNumberOfGenerations();
    double birthRate = ParameterRepository.Store().getBirthRate();
    double mutationRate = ParameterRepository.Store().getMutationRate();
    boolean elitism = ParameterRepository.Store().isElitism();
    int crossoverType = ParameterRepository.Store().getCrossoverType();

    boolean slowed = ParameterRepository.Store().isSlowMotion();


    synchronized private Population nextGeneration(Population currentPopulation) {

        Population children = new Population();

        int elitismOffset = 0;

        if (elitism) {
            children.add(currentPopulation.getFittest());
            elitismOffset = 1;
        }

        Population parents = currentPopulation.getParents(numberOfParents);

        for (int i = elitismOffset; i < Math.floor(birthRate * numberOfParents); i++) {
            int parentAIndex = (int) (Math.random() * numberOfParents);
            int parentBIndex = (int) (Math.random() * numberOfParents);
            while (parentAIndex == parentBIndex) {
                parentBIndex = (int) (Math.random() * numberOfParents);
            }

            children.add(
                    CrossoverFactory.
                            chooser().
                            getCrossoverType(crossoverType).
                            makeChild(parents.getItem(parentAIndex), parents.getItem(parentBIndex))
            );
        }

        currentPopulation.mergeWithAndTruncate(children, populationSize);

        if (mutationRate > 0) {
            currentPopulation.mutatePopulation(mutationRate);
        }

        return currentPopulation;

    }

    @Override
    public Void call() {
        updateProgress(0,1);
        Population population = new Population(populationSize);

        FXMLGUIController.GUIController().setCurrentPath(population.getFittest());

        for (int i = 0; i < numberOfGenerations; i++) {
            updateProgress(i,numberOfGenerations);

            if (TSPAlgorithmFactory.getInstance().isCancelled()) { break; }

            updateProgress(i, numberOfGenerations);

            population = nextGeneration(population);

            FXMLGUIController.GUIController().setCurrentPath(population.getFittest());
            try {
                if (slowed) {
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {}
        }
        updateProgress(numberOfGenerations, numberOfGenerations);
        return null;
    }
}
