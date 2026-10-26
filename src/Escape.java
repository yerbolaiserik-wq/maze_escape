/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The DFS uses an explicit stack, so a maze of 1000 by 1000 cells cannot overflow the call stack.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells(), cols = w.cols();
        int[][] g = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int[] list : g) if (list.length == 1) deadEnds++;

        int exit = n - 1;
        int[] parent = new int[n];
        int[] nextDir = new int[n];   // next direction (0 up, 1 right, 2 down, 3 left) to try in each cell
        boolean[] seen = new boolean[n];
        int[] stack = new int[n];
        int top = 0, visited = 1;
        seen[0] = true;
        parent[0] = -1;
        stack[top++] = 0;
        while (top > 0 && !seen[exit]) {
            int u = stack[top - 1];
            if (nextDir[u] == 4) { top--; continue; }
            int d = nextDir[u]++;
            int v;
            switch (d) {
                case 0 -> v = u - cols;
                case 1 -> v = (u % cols == cols - 1) ? -1 : u + 1;
                case 2 -> v = u + cols;
                default -> v = (u % cols == 0) ? -1 : u - 1;
            }
            if (v < 0 || v >= n || seen[v] || !contains(g[u], v)) continue;
            seen[v] = true;
            visited++;
            parent[v] = u;
            stack[top++] = v;
        }

        int len = 0;
        if (seen[exit]) for (int x = exit; x != -1; x = parent[x]) len++;
        int[] path = new int[len];
        int i = len - 1;
        if (seen[exit]) for (int x = exit; x != -1; x = parent[x]) path[i--] = x;
        return new Result(path, visited, deadEnds);
    }

    private static boolean contains(int[] list, int v) {
        for (int x : list) if (x == v) return true;
        return false;
    }
}
