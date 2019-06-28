package uk.ac.mdx;

import javafx.concurrent.Task;
import java.util.concurrent.RejectedExecutionException;
import javafx.beans.property.ReadOnlyDoubleProperty;

/**
 * A singleton factory class for running a specified algorithm
 * @author Matthew M. Jones
 */
public class TSPAlgorithmFactory {

    public static final int BRUTEFORCE              = 0;
    public static final int NEAREST_NEIGHBOUR       = 1;
    public static final int SIMULATEDANNEALING      = 2;
    public static final int GENETICALGORITHM        = 3;
    public static final int TWO_OPT                 = 4;
    public static final int THREE_OPT               = 5;

    private Thread th;

    private static TSPAlgorithmFactory theTSPAlgorithmFactory = null;

    private static TSPAlgorithm theTask;

    private TSPAlgorithmFactory() {}

    public static TSPAlgorithmFactory getInstance() {
        if (theTSPAlgorithmFactory == null) {
            theTSPAlgorithmFactory = new TSPAlgorithmFactory();
        }
        return theTSPAlgorithmFactory;
    }

    public boolean isRunning() {    return theTask.isRunning();     }

    public boolean isCancelled() {  return theTask.isCancelled();   }

    public Task getTask() {         return theTask;                 }


    public void runAlgorithm(int algorithm) throws RejectedExecutionException {
        if (theTask == null) {
            theTask = new BruteForceTSPAlgorithm();
        }
        if (theTask.isRunning()) {
            throw new RejectedExecutionException("Task already running");
        } else {
            switch (algorithm) {
                case BRUTEFORCE :
                    theTask = new BruteForceTSPAlgorithm();
                    break;
                case NEAREST_NEIGHBOUR :
                    theTask = new NearestNeighbourTSPAlgorithm();
                    break;
                case SIMULATEDANNEALING :
                    theTask = new SimulatedAnnealingTSPAlgorithm();
                    break;
                case GENETICALGORITHM :
                    theTask = new GeneticAlgorithmTSPAlgorithm();
                    break;
                case TWO_OPT :
                    theTask = new TwoOptTSPAlgorithm();
                    break;
                case THREE_OPT :
                    theTask = new ThreeOptTSPAlgorithm();
                    break;
                default :
                    throw new RejectedExecutionException("No such algorithm.");
            }

            th = new Thread(theTask);
            th.setDaemon(true);
            th.start();
        }
    }


    public ReadOnlyDoubleProperty getProgressProperty() {
        if (theTask == null) { return null; }
        else {
            return theTask.progressProperty();
        }
    }

    public void stopAlgorithm() {
        if (theTask.isRunning()) { theTask.cancel(); }
    }
}
