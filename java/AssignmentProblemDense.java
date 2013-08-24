/*************************************************************************
 *  Compilation:  javac AssignmentProblemDense.java
 *  Execution:    java AssignmentProblemDense N
 *  Dependencies: DenseDijkstraSP.java AdjMatrixEdgeWeightedDigraph.java DirectedEdge.java
 *
 *  Solve an N-by-N assignment problem using successive shortest path
 *  algorithm in N^3 time.
 *
 *  Assumes N-by-N cost matrix is nonnegative. If not, can add positive
 *  constant to all entries.
 *
 *********************************************************************/

public class AssignmentProblemDense {
    private static final int UNMATCHED = -1;

    private int N;              // number of rows and columns
    private double[][] weight;  // the N-by-N cost matrix
    private double[] px;        // px[i] = dual variable for row i
    private double[] py;        // py[j] = dual variable for col j
    private int[] xy;           // xy[i] = j means i-j is a match
    private int[] yx;           // yx[j] = i means i-j is a match


    public AssignmentProblemDense(double[][] weight) {
        this.weight = weight;
        N = weight.length;

        // dual variables
        px = new double[N];
        py = new double[N];

        // initial matching is empty
        xy = new int[N];
        yx = new int[N];
        for (int i = 0; i < N; i++) xy[i] = UNMATCHED;
        for (int j = 0; j < N; j++) yx[j] = UNMATCHED;

        // add N edges to matching
        for (int k = 0; k < N; k++) {
            assert isDualFeasible();
            assert isComplementarySlack();
            augment();
        }
        assert check();
    }

    // find shortest augmenting path and upate
    private void augment() {

        // build residual graph
        AdjMatrixEdgeWeightedDigraph G = new AdjMatrixEdgeWeightedDigraph(2*N+2);
        int s = 2*N, t = 2*N+1;
        for (int i = 0; i < N; i++) {
            if (xy[i] == UNMATCHED) G.addEdge(new DirectedEdge(s, i, 0.0));
        }
        for (int j = 0; j < N; j++) {
            if (yx[j] == UNMATCHED) G.addEdge(new DirectedEdge(N+j, t, py[j]));
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (xy[i] == j) G.addEdge(new DirectedEdge(N+j, i, 0.0));
                else            G.addEdge(new DirectedEdge(i, N+j, reduced(i, j)));
            }
        }

        // compute shortest path from s to every other vertex
        DenseDijkstraSP spt = new DenseDijkstraSP(G, s);

        // augment along alternating path
        for (DirectedEdge e : spt.pathTo(t)) {
            int v = e.from(), w = e.to() - N;
            if (v < N) {
                xy[v] = w;
                yx[w] = v;
            }
        }

        // update dual variables
        for (int i = 0; i < N; i++) px[i] += spt.distTo(i);
        for (int j = 0; j < N; j++) py[j] += spt.distTo(N+j);
    }

    // reduced cost of i-j
    private double reduced(int i, int j) {
        return weight[i][j] + px[i] - py[j];
    }

    // total weight of min weight perfect matching
    public double weight() {
        double total = 0.0;
        for (int i = 0; i < N; i++) {
            if (xy[i] != UNMATCHED)
                total += weight[i][xy[i]];
        }
        return total;
    }

    public int sol(int i) {
        return xy[i];
    }

    // check that dual variables are feasible
    private boolean isDualFeasible() {
        // check that all edges have >= 0 reduced cost
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (reduced(i, j) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // check that primal and dual variables are complementary slack
    private boolean isComplementarySlack() {

        // check that all matched edges have 0-reduced cost
        for (int i = 0; i < N; i++) {
            if ((xy[i] != UNMATCHED) && (reduced(i, xy[i]) != 0)) {
                return false;
            }
        }
        return true;
    }

    // check that primal variables are a perfect matching
    private boolean isPerfectMatching() {

        // check that xy[] is a perfect matching
        boolean[] perm = new boolean[N];
        for (int i = 0; i < N; i++) {
            if (perm[xy[i]]) {
                return false;
            }
            perm[xy[i]] = true;
        }

        // check that xy[] and yx[] are inverses
        for (int j = 0; j < N; j++) {
            if (xy[yx[j]] != j) {
                return false;
            }
        }
        for (int i = 0; i < N; i++) {
            if (yx[xy[i]] != i) {
                return false;
            }
        }

        return true;
    }

    // check optimality conditions
    private boolean check() {
        return isPerfectMatching() && isDualFeasible() && isComplementarySlack();
    }
}
