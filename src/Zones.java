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
        int n = s.n();
        int[][] out = Graphs.slidesFrom(s), in = Graphs.slidesInto(s);

        // pass 1: DFS on the slides, cells written in the order of finishing
        int[] order = new int[n];
        int finished = 0;
        boolean[] seen = new boolean[n];
        int[] pos = new int[n];   // how many slides of the cell are already looked at
        int[] stack = new int[n];
        for (int root = 0; root < n; root++) {
            if (seen[root]) continue;
            int top = 0;
            stack[top++] = root;
            seen[root] = true;
            while (top > 0) {
                int u = stack[top - 1];
                if (pos[u] < out[u].length) {
                    int v = s.to()[out[u][pos[u]++]];
                    if (!seen[v]) { seen[v] = true; stack[top++] = v; }
                } else {
                    order[finished++] = u;
                    top--;
                }
            }
        }

        // pass 2: from the last finished cell to the first, DFS on the reversed slides
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zones = 0;
        for (int i = n - 1; i >= 0; i--) {
            int root = order[i];
            if (zoneOf[root] != -1) continue;
            int top = 0;
            stack[top++] = root;
            zoneOf[root] = zones;
            while (top > 0) {
                int u = stack[--top];
                for (int slide : in[u]) {
                    int v = s.from()[slide];
                    if (zoneOf[v] == -1) { zoneOf[v] = zones; stack[top++] = v; }
                }
            }
            zones++;
        }
        return zoneOf;
    }

    /**
     * Mission 5. The graph of zones: the edge X to Y costs the cheapest slide from X to Y, walking inside a zone is free.
     * Goes through the zones in the order of their numbers (no priority queue). Returns two numbers:
     * [0] flow stages: the number of zones on the longest chain of zones;
     * [1] the cost of the cheapest route from the zone of cell 0 to the zone of the last cell.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        int n = s.n(), m = s.count();
        // slides grouped by the zone they leave (counting sort)
        int[] first = new int[zoneCount + 1];
        for (int i = 0; i < m; i++) first[zoneOf[s.from()[i]] + 1]++;
        for (int z = 0; z < zoneCount; z++) first[z + 1] += first[z];
        int[] fill = Arrays.copyOf(first, zoneCount);
        int[] byZone = new int[m];
        for (int i = 0; i < m; i++) byZone[fill[zoneOf[s.from()[i]]]++] = i;

        long inf = Long.MAX_VALUE / 4;
        long[] chain = new long[zoneCount];
        long[] dist = new long[zoneCount];
        Arrays.fill(chain, 1);
        Arrays.fill(dist, inf);
        dist[zoneOf[0]] = 0;
        long stages = 1;
        for (int z = 0; z < zoneCount; z++) {
            stages = Math.max(stages, chain[z]);
            for (int j = first[z]; j < first[z + 1]; j++) {
                int i = byZone[j];
                int y = zoneOf[s.to()[i]];
                if (y == z) continue;   // slide inside a zone
                chain[y] = Math.max(chain[y], chain[z] + 1);
                if (dist[z] < inf) dist[y] = Math.min(dist[y], dist[z] + s.time()[i]);
            }
        }
        return new long[]{stages, dist[zoneOf[n - 1]]};
    }
}
