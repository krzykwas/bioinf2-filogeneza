package pg.eti.kams.bioinffilogeneza2013;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import org.jblas.DoubleMatrix;

/**
 * Hello world!
 *
 */
public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        DoubleMatrix d = DoubleMatrix.zeros(2*n, 2*n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                d.put(i,j, s.nextDouble());
            }
        }

        System.out.println(d);

        List<Integer> L = new ArrayList<Integer>(2*n);
        List<Double> R = new ArrayList<Double>(2*n);
        for (int i = 0; i < 2*n; i++) {
            L.add(0);
            R.add(0.0);
        }
        //inicjalizacja lisci - jako etykiety kolejne liczby od 0
        for (int i = 0; i < n; i++) {
            L.set(i,i);
        }
        
        //V - drzewo addytywne, które tworzymy
        ArrayList[] V = new ArrayList[2*n];
        for(int i=0;i<V.length;i++) V[i] = new ArrayList<Integer>();
        
        double suma, rmin,rr;
        int i,j,vertNum = n;
        while (n > 3){
            //wyznaczanie r dla każdego liścia
            for (int a=0;a<n;a++){
                suma = 0;
                for (int b=0;b<n;b++){
                    suma = suma + d.get(L.get(a),L.get(b));
                }
                suma = suma / (n - 2);
                R.set(a,suma);
            }
            //wyznaczania sąsiadów na podstawie r
            i = 0;
            j = 1;
            rmin = d.get(L.get(0),L.get(1)) - (R.get(0) + R.get(1));
            for (int a = 0; a< n-1;a++){
                for (int b=a+1; b< n;b++){
                    rr = d.get(L.get(a),L.get(b)) - (R.get(a) + R.get(b));
                    if (rr < rmin){
                        rmin = rr;
                        i = a;
                        j = b;
                    }
                }
            }
            
            //usuniecie ze zbioru lisci i,j oraz dodanie k
            L.set(n,vertNum);
            vertNum++;
            i = L.remove(i);
            j = L.remove(j-1);
            n=n-1;
            
            //uaktualnienie d dla każdego pozostałego liścia
            for (int l = 0;l < n-1; l++){
                double value = (d.get(i,L.get(l)) + d.get(j,L.get(l)) - d.get(i,j))/2;
                d.put(L.get(n-1),L.get(l), value);
                d.put(L.get(l),L.get(n-1), value);
            }
            
            //dodanie odpowiednich krawędzi do tworzonego drzewa
            V[i].add((vertNum-1));
            V[j].add((vertNum-1));
            V[vertNum-1].add(i);
            V[vertNum-1].add(j);
            
            //wyznaczenie odległości między nowym wierzchołkiem oraz i,j
            double value = (d.get(i,j) + d.get(i,L.get(0)) - d.get(j,L.get(0)))/2;
            d.put(i,vertNum-1,value);
            d.put(vertNum-1,i,value);
            d.put(j,vertNum-1,d.get(i,j)-d.get(i,vertNum-1));
            d.put(vertNum-1,j,d.get(i,j)-d.get(i,vertNum-1));
        }
        
        //3 elementowe drzewo
        double value;
        value = (d.get(L.get(0),L.get(1)) + d.get(L.get(0),L.get(2)) - d.get(L.get(1),L.get(2)))/2;
        d.put(L.get(0),vertNum,value);
        d.put(vertNum,L.get(0),value);
        
        value = (d.get(L.get(0),L.get(1)) + d.get(L.get(1),L.get(2)) - d.get(L.get(0),L.get(2)))/2;
        d.put(L.get(1),vertNum,value);
        d.put(vertNum,L.get(1),value);
        
        value = (d.get(L.get(0),L.get(2)) + d.get(L.get(1),L.get(2)) - d.get(L.get(0),L.get(1)))/2;
        d.put(L.get(2),vertNum,value);
        d.put(vertNum,L.get(2),value);
        
        V[vertNum].add(L.get(0));
        V[vertNum].add(L.get(1));
        V[vertNum].add(L.get(2));
        V[L.get(0)].add(vertNum);
        V[L.get(1)].add(vertNum);
        V[L.get(2)].add(vertNum);
        
        //wypisanie wyników
        System.out.println(d);
        
        for (int a = 0 ; a <= vertNum; a++){
            System.out.print(a);
            System.out.print(" : ");
            for (int b = 0 ; b < V[a].size(); b++){
                System.out.print(V[a].get(b));
                System.out.print(" ");
            }
            System.out.println("");
        }
        
    }
}
