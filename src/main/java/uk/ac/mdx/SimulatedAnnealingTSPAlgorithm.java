package uk.ac.mdx;

/**
 * Runs the simulated annealing algorithm on the current TSPProblem
 * @author Matthew M. Jones
 */
public class SimulatedAnnealingTSPAlgorithm extends TSPAlgorithm {

    double initialTemperature = ParameterRepository.Store().getInitialTemperature();
    double finalTemperature = ParameterRepository.Store().getFinalTemperature();
    double coolingConstant = ParameterRepository.Store().getCoolingConstant();
    boolean slowed = ParameterRepository.Store().isSlowMotion();


    @Override
    public Void call() {

        updateProgress(0,1);

        double maxIterations = Math.floor(Math.log(finalTemperature / initialTemperature) / Math.log(coolingConstant));
        long iterations = 0;

        Tour currentTour = Tour.generateDefaultTour();
        currentTour.shuffle();

        FXMLGUIController.GUIController().setCurrentPath(currentTour);

        double currentTemperature = initialTemperature;
        double delta;
        int randomIndex1;
        int randomIndex2;

        while (currentTemperature > finalTemperature) {

            if (TSPAlgorithmFactory.getInstance().isCancelled()) { break; }

            randomIndex1 = (int) (Math.random() * currentTour.size());
            randomIndex2 = (int) (Math.random() * currentTour.size());

            Tour potentialTour;
            try {
                potentialTour = currentTour.clone();

                potentialTour.twoOpt(randomIndex1, randomIndex2);
                delta = potentialTour.length() - currentTour.length();

                if (delta <= 0 | (delta > 0 & (Math.random() < Math.exp(-delta / currentTemperature)))) {
                    currentTour = potentialTour.clone();
                    currentTemperature *= coolingConstant;

                    iterations++;
                    updateProgress((double) iterations, maxIterations);

                    FXMLGUIController.GUIController().setCurrentPath(currentTour);

                    if (slowed) {
                        Thread.sleep(1);
                    }
                }

            } catch (CloneNotSupportedException | InterruptedException e) {}
        }
        updateProgress(1, 1);
        return null;

    }

}
