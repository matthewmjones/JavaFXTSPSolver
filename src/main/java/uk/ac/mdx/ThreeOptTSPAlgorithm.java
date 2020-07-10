package uk.ac.mdx;

import java.util.stream.IntStream;

/**
 * Runs the three-opt algorithm for solving the current TSPProblem based on the
 * current path
 * @author Matthew M. Jones
 */
public class ThreeOptTSPAlgorithm extends TSPAlgorithm {

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
                iteration++;
                hasChanged = false;
                for (int i = 0; i < startingTour.size() - 2; i++) {
                    updateProgress(sequence[iteration - 1] + (sequence[iteration] - sequence[iteration - 1]) * (double) i / (double) startingTour.size(),1);

                    for (int j = 0; j < startingTour.size() - 1; j++) {

                        for (int k = 0; k < startingTour.size(); k++) {

                            if (TSPAlgorithmFactory.getInstance().isCancelled()) { break; }

                            Tour potentialTour = bestTour.clone();
                            potentialTour.threeOpt(i, j, k);

                            if (potentialTour.length() < bestLength) {

                                hasChanged = true;

                                bestLength = potentialTour.length();
                                bestTour = potentialTour.clone();

                                FXMLGUIController.GUIController().setCurrentPath(bestTour);
                            }
                        }
                    }
                }
            }
        } catch (CloneNotSupportedException ex) {}
        catch (Exception e) { System.out.println(e.toString()); }
        updateProgress(1,1);
        return null;

    }

}
