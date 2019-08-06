package uk.ac.mdx;

import java.util.stream.IntStream;

/**
 * Finds the best tour for the current TSPProblem based on a brute force search
 * of all possible tours
 * @author Matthew M. Jones
 */
public class BruteForceTSPAlgorithm extends TSPAlgorithm {

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    @Override
    public Void call() throws CloneNotSupportedException {
        Tour bestTour = Tour.generateDefaultTour();
        double bestLength = bestTour.length();

        FXMLGUIController.GUIController().setCurrentPath(bestTour);

        updateProgress(0, 1);

        // Heap's algorithm

        int n = TSPProblem.getProblem().getNumberOfCities();

        int [] c = IntStream.range(0,n).map(i -> 0).toArray();

        int [] tempArray = IntStream.range(0,n).map(i -> n - i - 1).toArray();

        int i = 0;

        while (i < n - 1) {

            if (TSPAlgorithmFactory.getInstance().isCancelled()) { break; }

            updateProgress(0,1);

            if (c[i] < i) {
                if (i % 2 == 0) {
                    swap(tempArray,0,i);
                } else {
                    swap(tempArray,c[i],i);
                }

                // check best Tour
                Tour nextTour = new Tour(
                        // Use of the Stream API to create an array copy of A
                        IntStream.of( tempArray ).boxed().toArray( Integer[]::new )
                );
                if (nextTour.length() < bestLength) {
                    try {
                        bestTour = nextTour.clone();
                        bestLength = nextTour.length();
                        FXMLGUIController.GUIController().setCurrentPath(bestTour);
                    } catch (CloneNotSupportedException ex) {
                        ex.printStackTrace();
                        throw new CloneNotSupportedException();
                    }
                }
                c[i] += 1;
                i = 0;
            } else {
                c[i] = 0;
                i += 1;
            }

        }

        updateProgress(1,1);
        return null;
    }
}
