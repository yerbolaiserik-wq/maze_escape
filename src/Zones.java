import java.util.Arrays;

/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i]. There are no slides from a cell to itself, and the exit can always be reached from the start.
 */
public class Zones {

    /**
     * Mission 4. Kosaraju with explicit stacks. Returns zoneOf: zoneOf[cell] = number of the zone, numbers 0 .. zones-1.
     * The zones are numbered in the order in which the second pass finds them: a topological order of the zone graph.
     */
    public static int[] kosaraju(Input.Slides s) {
        // TODO
        return new int[s.n()];
    }

    /**
     * Mission 5. The graph of zones: the edge X to Y costs the cheapest slide from X to Y, walking inside a zone is free.
     * Goes through the zones in the order of their numbers (no priority queue). Returns two numbers:
     * [0] flow stages: the number of zones on the longest chain of zones;
     * [1] the cost of the cheapest route from the zone of cell 0 to the zone of the last cell.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        // TODO
        return new long[]{0, 0};
    }
}
