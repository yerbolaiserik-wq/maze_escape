import java.util.*;

/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.count()];
        int m = 0;
        for (int k = 0; k < w.count(); k++) {
            if (dsu.union(w.a()[k], w.b()[k])) broken[m++] = k;
        }
        return Arrays.copyOf(broken, m);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int total = w.count();
        long[] keys = new long[total];   // cost in the high bits, wall index in the low bits
        for (int k = 0; k < total; k++) keys[k] = ((long) w.cost()[k] << 32) | k;
        Arrays.sort(keys);
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[total];
        int m = 0;
        for (long key : keys) {
            int k = (int) (key & 0xFFFFFFFFL);
            if (dsu.union(w.a()[k], w.b()[k])) broken[m++] = k;
        }
        return Arrays.copyOf(broken, m);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells(), total = w.count();
        int[] start = new int[n + 1];   // walls of cell u are adj[start[u] .. start[u+1])
        for (int k = 0; k < total; k++) { start[w.a()[k] + 1]++; start[w.b()[k] + 1]++; }
        for (int i = 0; i < n; i++) start[i + 1] += start[i];
        int[] fill = new int[n];
        int[] adj = new int[2 * total];
        for (int k = 0; k < total; k++) {
            adj[start[w.a()[k]] + fill[w.a()[k]]++] = k;
            adj[start[w.b()[k]] + fill[w.b()[k]]++] = k;
        }
        boolean[] in = new boolean[n];
        PriorityQueue<long[]> pq = new PriorityQueue<>((x, y) -> Long.compare(x[0], y[0]));   // {cost, target cell}
        long sum = 0;
        int taken = 0;
        pq.add(new long[]{0, 0});
        while (!pq.isEmpty() && taken < n) {
            long[] top = pq.poll();
            int v = (int) top[1];
            if (in[v]) continue;
            in[v] = true;
            taken++;
            sum += top[0];
            for (int i = start[v]; i < start[v + 1]; i++) {
                int k = adj[i];
                int u = w.a()[k] == v ? w.b()[k] : w.a()[k];
                if (!in[u]) pq.add(new long[]{w.cost()[k], u});
            }
        }
        return sum;
    }
}
