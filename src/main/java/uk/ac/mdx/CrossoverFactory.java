package uk.ac.mdx;

/**
 * A factory class for returning different crossover algorithms
 * @author Matthew M. Jones
 */
public class CrossoverFactory {

    public static final int ORDER_BASED_CROSSOVER   = 0;
    public static final int ORDER_CROSSOVER         = 1;

    private static CrossoverFactory thisCrossoverFactory = null;

    CrossoverFactory() {}

    public static CrossoverFactory chooser() {
        if (thisCrossoverFactory == null) {
            thisCrossoverFactory = new CrossoverFactory();
        }
        return thisCrossoverFactory;
    }

    public Crossover getCrossoverType(int type) {
        Crossover crossover;
        switch (type) {
            case ORDER_BASED_CROSSOVER :
                crossover = new OrderBasedCrossover();
                break;
            default :
                crossover = null;
        }
        return crossover;
    }

}
