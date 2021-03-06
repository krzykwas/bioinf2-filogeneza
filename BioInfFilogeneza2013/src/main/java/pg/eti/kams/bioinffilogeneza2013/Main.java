package pg.eti.kams.bioinffilogeneza2013;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
import org.jblas.Solve;

/**
 * Hello world!
 *
 */

/*
 * DFS
 *
 * enum VertexState { White, Gray, Black }
 *
 * public void DFS() { VertexState state[] = new VertexState[vertexCount]; for
 * (int i = 0; i < vertexCount; i++) state[i] = VertexState.White; runDFS(0,
 * state); }
 *
 * public void runDFS(int u, VertexState[] state) { state[u] = VertexState.Gray;
 * for (int v = 0; v < vertexCount; v++) if (isEdge(u, v) && state[v] ==
 * VertexState.White) runDFS(v, state); state[u] = VertexState.Black; }
 *
 */
public class Main {

    public enum VertexState {

        White, Gray, Black
    }

    public static void DFS(DoubleMatrix A, DoubleMatrix d, DoubleMatrix g, ArrayList[] V, int vertNum) {
        int krawedzie = 0;
        for (int i = 0; i <= vertNum; i++) {
            krawedzie += V[i].size();
        }

        krawedzie /= 2;
        //System.out.println("A wymiar: " + A.columns + " rows: "+A.rows);

        int t = 0;

        VertexState state[] = new VertexState[(d.rows - 1) * d.rows / 2];//tyle stanów

        for (int i = 0; i < d.rows - 1; i++) {
            for (int j = i + 1; j < d.columns; j++) {
                for (int x = 0; x < state.length; x++) {
                    state[x] = VertexState.White;//na początku białe
                }
                g.put(t, 0, d.get(i, j));
                runDFS(i, j, state, d, V, vertNum);
                for (int ii = 0; ii < krawedzie; ii++) {
                    if (state[ii] == VertexState.Black) {
                        A.put(t, ii, 1);
                    }
                }

                t++;
            }
        }
        System.out.println(g);
        System.out.println("t:" + t);
        return;
    }

    public static boolean isEdge(int u, int v, ArrayList[] V) {
        if (V[u].contains(v)) {
            return true;
        }
        return false;

    }

    public static VertexState runDFS(int i, int j, VertexState[] state, DoubleMatrix d, ArrayList[] V, int vertNum) {
        System.out.println(i);
        state[i] = VertexState.Gray;
        if (!isEdge(i, j, V)) {
            for (int v = 0; v <= vertNum; v++) {
                if (isEdge(i, v, V) && state[v] == VertexState.White) {
                    if (runDFS(v, j, state, d, V, vertNum) == VertexState.Black){
                                    state[i] = VertexState.Black;
                                    return VertexState.Black;}
                }
            }
        } else {
            state[j] = VertexState.Black;
            return VertexState.Black;
        }
        return VertexState.Gray;
    }

    public static double nk(DoubleMatrix A, DoubleMatrix d, DoubleMatrix g, ArrayList[] V, int vertNum) {
        DFS(A, d, g, V, vertNum);
        return 0;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        int nn = n;
        DoubleMatrix d = DoubleMatrix.zeros(2 * n, 2 * n);
        DoubleMatrix d2 = DoubleMatrix.zeros(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                d.put(i, j, s.nextDouble());
                d2.put(i, j, d.get(i, j));
            }
        }
        //d2 = new DoubleMatrix(d.data);
        System.out.println(d);
        System.out.println(d);

        List<Integer> L = new ArrayList<Integer>(2 * n);
        List<Double> R = new ArrayList<Double>(2 * n);
        for (int i = 0; i < 2 * n; i++) {
            L.add(0);
            R.add(0.0);
        }
        //inicjalizacja lisci - jako etykiety kolejne liczby od 0
        for (int i = 0; i < n; i++) {
            L.set(i, i);
        }

        //V - drzewo addytywne, które tworzymy
        ArrayList[] V = new ArrayList[2 * n];
        for (int i = 0; i < V.length; i++) {
            V[i] = new ArrayList<Integer>();
        }

        double suma, rmin, rr;
        int i, j, vertNum = n;

        while (n > 3) {
            //wyznaczanie r dla każdego liścia
            for (int a = 0; a < n; a++) {
                suma = 0;
                for (int b = 0; b < n; b++) {
                    suma = suma + d.get(L.get(a), L.get(b));
                }
                suma = suma / (n - 2);
                R.set(a, suma);
            }
            //wyznaczania sąsiadów na podstawie r
            i = 0;
            j = 1;
            rmin = d.get(L.get(0), L.get(1)) - (R.get(0) + R.get(1));
            for (int a = 0; a < n - 1; a++) {
                for (int b = a + 1; b < n; b++) {
                    rr = d.get(L.get(a), L.get(b)) - (R.get(a) + R.get(b));
                    if (rr < rmin) {
                        rmin = rr;
                        i = a;
                        j = b;
                    }
                }
            }

            //usuniecie ze zbioru lisci i,j oraz dodanie k
            L.set(n, vertNum);
            vertNum++;
            i = L.remove(i);
            j = L.remove(j - 1);
            n = n - 1;

            //uaktualnienie d dla każdego pozostałego liścia
            for (int l = 0; l < n - 1; l++) {
                double value = (d.get(i, L.get(l)) + d.get(j, L.get(l)) - d.get(i, j)) / 2;
                d.put(L.get(n - 1), L.get(l), value);
                d.put(L.get(l), L.get(n - 1), value);
            }

            //dodanie odpowiednich krawędzi do tworzonego drzewa
            V[i].add((vertNum - 1));
            V[j].add((vertNum - 1));
            V[vertNum - 1].add(i);
            V[vertNum - 1].add(j);

            //wyznaczenie odległości między nowym wierzchołkiem oraz i,j
            double value = (d.get(i, j) + d.get(i, L.get(0)) - d.get(j, L.get(0))) / 2;
            d.put(i, vertNum - 1, value);
            d.put(vertNum - 1, i, value);
            d.put(j, vertNum - 1, d.get(i, j) - d.get(i, vertNum - 1));
            d.put(vertNum - 1, j, d.get(i, j) - d.get(i, vertNum - 1));
        }

        //3 elementowe drzewo
        double value;
        value = (d.get(L.get(0), L.get(1)) + d.get(L.get(0), L.get(2)) - d.get(L.get(1), L.get(2))) / 2;
        d.put(L.get(0), vertNum, value);
        d.put(vertNum, L.get(0), value);

        value = (d.get(L.get(0), L.get(1)) + d.get(L.get(1), L.get(2)) - d.get(L.get(0), L.get(2))) / 2;
        d.put(L.get(1), vertNum, value);
        d.put(vertNum, L.get(1), value);

        value = (d.get(L.get(0), L.get(2)) + d.get(L.get(1), L.get(2)) - d.get(L.get(0), L.get(1))) / 2;
        d.put(L.get(2), vertNum, value);
        d.put(vertNum, L.get(2), value);

        V[vertNum].add(L.get(0));
        V[vertNum].add(L.get(1));
        V[vertNum].add(L.get(2));
        V[L.get(0)].add(vertNum);
        V[L.get(1)].add(vertNum);
        V[L.get(2)].add(vertNum);

        //wypisanie wyników
        System.out.println(d);


        //DoubleMatrix w2 = DoubleMatrix.zeros(2*n, 2*n);
        ArrayList w = new ArrayList<Integer>();

        for (int a = 0; a <= vertNum; a++) {
            System.out.print(a);
            System.out.print(" : ");
            for (int b = 0; b < V[a].size(); b++) {
                System.out.print(V[a].get(b));
                System.out.print(" ");

                //w2.put(a,b,Integer.parseInt(V[a].get(b).toString()));
                w.add(V[a].get(b));

            }
            System.out.println("");
        }

        DoubleMatrix A = DoubleMatrix.zeros((nn * (nn - 1)) / 2, vertNum);
        DoubleMatrix g = DoubleMatrix.zeros((nn * (nn - 1)) / 2, 1);
        double blad = nk(A, d2, g, V, vertNum);//wrzucam to do siebie - mkd

        System.out.println(A);
        
        DoubleMatrix p = (new DoubleMatrix(A.rows,A.columns,A.data)).transpose();
        System.out.println(p.rows + " " + p.columns);
        DoubleMatrix p2 = (new DoubleMatrix(p.rows,p.columns,p.data)).mmul((new DoubleMatrix(A.rows,A.columns,A.data)));
        System.out.println("p2: " + p2);
        DoubleMatrix p3 = MatrixFunctions.pow(p2,-1);
        System.out.println("p3: " + p3);
        DoubleMatrix p4 = p3.mmul(p);
        DoubleMatrix b = p4.mmul(g);
        
        //DoubleMatrix b = MatrixFunctions.pow(A.transpose().mmul(A), -1).mmul(A.transpose()).mmul(g);
        System.out.println(g);
        System.out.println(b);
        System.out.println("Kwadrat bledu wynosi " + blad);

    }
}
