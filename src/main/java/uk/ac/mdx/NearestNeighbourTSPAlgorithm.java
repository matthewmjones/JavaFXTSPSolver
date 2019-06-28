package uk.ac.mdx;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Runs the greedy algorithm for the current TSPProblem
 * @author Matthew M. Jones
 */
public class NearestNeighbourTSPAlgorithm extends TSPAlgorithm {

    boolean slowed = ParameterRepository.Store().isSlowMotion();

    @Override
    public Void call() {

        TSPProblem theTSPProblem = TSPProblem.getProblem();

        List<Integer> indexArray = IntStream
                .range(0, theTSPProblem.getNumberOfCities())
                .boxed()
                .collect(Collectors.toList());

        Integer [] solutionArray = new Integer[theTSPProblem.getNumberOfCities()];

        // find origin of the tour
        Integer origin = (int) (Math.random() * theTSPProblem.getNumberOfCities());
        solutionArray[0] = origin;
        indexArray.remove(origin);

        int solIndex = 0;

        while (indexArray.size() > 0) {

            updateProgress(solIndex, theTSPProblem.getNumberOfCities());

            try {
                if (TSPAlgorithmFactory.getInstance().isCancelled()) { break; }

                double closestDistance = theTSPProblem.distance(indexArray.get(0), solutionArray[solIndex]);
                int closestIndex = 0;
                for (int i = 0; i < indexArray.size(); i++) {
                    if (theTSPProblem.distance(indexArray.get(i), solutionArray[solIndex]) < closestDistance) {
                        closestIndex = i;
                        closestDistance = theTSPProblem.distance(indexArray.get(i), solutionArray[solIndex]);
                    }
                }
                solIndex++;

                solutionArray[solIndex] = indexArray.get(closestIndex);
                indexArray.remove(closestIndex);

                FXMLGUIController.GUIController().setCurrentPath(new Path(Arrays.copyOf(solutionArray,solIndex)));
                if (slowed) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) { }
        }

        updateProgress(1, 1);


        FXMLGUIController.GUIController().setCurrentPath(new Tour(solutionArray));
        return null;
    }

}
