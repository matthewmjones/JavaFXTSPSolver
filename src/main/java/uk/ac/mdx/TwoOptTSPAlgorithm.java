package uk.ac.mdx;

import java.util.stream.IntStream;

/**
 * Runs the two-opt algorithm on the current TSPProblem and the current tour
 * @author Matthew M. Jones
 */
public class TwoOptTSPAlgorithm extends TSPAlgorithm {

    boolean slowed = ParameterRepository.Store().isSlowMotion();

    @Override
    public Void call() {
        try {
            Tour startingTour = new Tour(
                    FXMLGUIController.GUIController().getCurrentPath()
            );
            if (startingTour.size() == 0) {
                startingTour = Tour.generateDefaultTour();
                FXMLGUIController.GUIController().setCurrentPath(startingTour);
            }

            // For keeping track of progress
            double [] sequence = IntStream.range(0, 100).asDoubleStream().map(x -> 1 - Math.pow(2.0, -x)).toArray();
            int iteration = 0;

            updateProgress(0,1);

            Tour bestTour = startingTour.clone();
            double bestLength = bestTour.length();

            boolean hasChanged = true;

            while (hasChanged) {

                if (TSPAlgorithmFactory.getInstance().isCancelled()) { break; }

                iteration++;
                hasChanged = false;
                for (int i = 0; i < startingTour.size(); i++) {
                    updateProgress(sequence[iteration - 1] + (sequence[iteration] - sequence[iteration - 1]) * (double) i / (double) startingTour.size(),1);

                    for (int j = 0; j < i; j++) {

                        Tour potentialTour = bestTour.clone();
                        potentialTour.twoOpt(i, j);

                        if (potentialTour.length() < bestLength) {

                            hasChanged = true;

                            bestLength = potentialTour.length();
                            bestTour = potentialTour.clone();

                            FXMLGUIController.GUIController().setCurrentPath(bestTour);
                            if (slowed) {
                                Thread.sleep(100);
                            }
                        }
                    }
                }
            }
        } catch (CloneNotSupportedException | InterruptedException ex) {}
        updateProgress(1,1);
        return null;

    }

}
