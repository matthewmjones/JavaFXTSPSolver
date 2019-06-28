package uk.ac.mdx;

import java.util.ArrayList;

/**
 * Extends Heap&#60;Tour&#60; but also includes functionality to work with underlying
 * ArrayList&#60;Tour&#60;
 * @author Matthew M. Jones
 */
public class TourHeap extends Heap<Tour> {

    public void truncate(int size) {

        items = new ArrayList<>(items.subList(0, size));

    }

    public Tour removeItem(int i) {

        Tour t = items.remove(i);
        siftDown();
        return t;
    }

    public Tour getItem(int i) {

        return items.get(i);

    }

}
