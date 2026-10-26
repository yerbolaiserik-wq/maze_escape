# Assignment 3. Maze Escape

Student ID: 254319
Name:
Group:
Maze size (rows x columns): 8 x 9

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment3.pdf`.

## Program output

The 13 lines your program prints on your data (`java -cp out Main`):

    Broken: 71
    Kept: 56
    Last broken wall: 112
    MST cost: 21802
    Heaviest broken wall: 712
    Path length: 19
    DFS visited: 51
    Dead ends: 23
    Zones: 72
    Largest zone: 1
    Is DAG: 1
    Flow stages: 17
    Fastest route cost: 17

My maze:

![maze](output/maze.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 0. What do find and union do, and what makes them fast in your code?

find returns the root of the set of a cell by following parent links; union finds both roots and hangs the smaller tree under the larger one, returning false if the roots are equal. Union by size keeps trees shallow (depth at most log n) and path compression in find re-points every visited cell directly to the root, so together the amortized cost is almost constant (inverse Ackermann).

Mission 1. Why is Broken always R*C - 1, and Kept always (R-1)*(C-1)?

The maze grid is connected, so the walls we break (the ones whose union succeeds) form a spanning tree of the R*C cells. Each successful union merges two sets and reduces the count by one, from R*C sets down to 1, so exactly R*C - 1 unions succeed (71 for my 8 x 9 maze). The grid has R*(C-1) + (R-1)*C = 2RC - R - C walls, and (2RC - R - C) - (RC - 1) = (R-1)*(C-1) stay (127 - 71 = 56 = 7 * 8 for me).

Mission 2. My MST cost from Kruskal and from Prim, and why they are equal:

Kruskal and Prim both give 21802 on my maze. All wall costs are different, so the minimum spanning tree is unique; Kruskal (cheapest wall that joins two components) and Prim (cheapest wall leaving the grown tree) both rely on the cut property and therefore build that same tree, so the costs are equal.

Mission 3. Why is there exactly one path between two cells? How did you avoid a stack overflow on a huge maze?

The open passages form a spanning tree: connected, so a path exists, and without cycles, so two different paths would create a cycle. I wrote DFS with an explicit int[] stack and a per-cell counter of the next direction to try, instead of recursion, so the depth of the maze does not use the call stack.

Mission 4. Why does the second pass of Kosaraju go on the reversed graph? Why do the slides have no cycle exactly when Zones equals the number of cells?

The cell finished last in pass 1 lies in a source zone. A DFS from it on the original slides could leak into later zones, but on the reversed slides it can only reach cells that can reach it, which is exactly its own zone; removing it and repeating gives the zones one by one. A zone with more than one cell contains a cycle, and a cycle forces its cells into one zone, so no cycle means every zone has one cell, i.e. Zones = n. In my data Zones = 72 = number of cells, so my slides are a DAG (Is DAG: 1).

Mission 5. Why is it enough to go through the zones in the order of their numbers? What does this pass cost?

Zone numbers are a topological order: every slide between zones goes from a smaller to a larger number. When I reach zone z, all slides entering it come from smaller zones that are already final, so its distance and chain length are final too and one relaxation per slide is enough, no priority queue. The pass costs O(n + m) (plus the counting sort of slides by zone).

## AI use

The syllabus requires you to say which AI tools you used and for what. Write the tool (ChatGPT, Copilot, other) and what you asked it: explain an idea, find an error, review code. Write "none" if you did not use any. If you took code or text from a book or a website, give the source here.

Claude : I used it to understand the assignment requirements and help to comprehend the code of Missions 0-5 (DisjointSet, Maze, Escape, Zones, extra DSU tests). I ran tools/Check.java and Bench myself and I must be able to explain every line.I had dive into some websites and Youtube videos about algorithms.

## Timing (Mission 7)

Milliseconds, 5 runs each, from `java -cp out Bench`.

| maze | Kruskal | Prim |
|---|---|---|
| 100 x 100 | 17 | 22 |
| 300 x 300 | 51 | 196 |
| 1000 x 1000 | 704 | 3167 |

What do the numbers say about how the time grows? Two or three sentences.

The maze has 9 times more cells from 100x100 to 300x300 and about 11 times more from 300x300 to 1000x1000, and the times grow by about that factor or a bit more (Kruskal 17 to 51 to 704), which fits n log n plus cache effects. Prim is slower than Kruskal and grows faster because of the priority queue with many small objects and random memory access, while Kruskal sorts a flat long array.

## Complexity

n is the number of cells, m the number of walls (or slides). Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| DSU find and union | O(alpha(n)) amortized | O(n) | union by size and path compression |
| Kruskal | O(m log m) | O(n + m) | sorting dominates; m is about 2n |
| Prim | O(m log m) | O(n + m) | binary heap with lazy deletion |
| DFS | O(n) | O(n) | maze is a tree, so m = n - 1; explicit stack |
| Kosaraju | O(n + m) | O(n + m) | two DFS passes, adjacency lists, iterative |
| Route in the zone graph | O(n + m) | O(n + m) | zones in topological order, no heap |

## Before you submit

- data/walls.txt, data/slides.txt and output/maze.png are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
