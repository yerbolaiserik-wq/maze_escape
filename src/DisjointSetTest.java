/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);
        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");
        d.union(2, 3); d.union(1, 3);
        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5, "a single cell is its own root");

        int n = 1_000_000;
        DisjointSet chain = new DisjointSet(n);
        for (int i = 1; i < n; i++) chain.union(i - 1, i);
        check(chain.count() == 1, "a chain of a million cells is one set");
        check(chain.connected(0, n - 1), "the ends of the long chain are connected");
        check(!chain.union(5, 7), "union inside one set returns false");
        DisjointSet e = new DisjointSet(4);
        check(!e.union(2, 2), "union of a cell with itself returns false");
        e.union(0, 1); e.union(2, 3); e.union(0, 2);
        check(e.count() == 1, "count is 1 after 3 unions of 4 cells");

        System.out.println(failed == 0 ? "All checks passed." : failed + " checks failed.");
    }
}
