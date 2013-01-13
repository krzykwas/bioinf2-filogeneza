package pg.eti.kams.bioinffilogeneza2013;

import java.util.Scanner;
import org.jblas.DoubleMatrix;

/**
 * Hello world!
 *
 */
public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        DoubleMatrix d = new DoubleMatrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                d.put(j * n + i, s.nextDouble());
            }
        }

        System.out.println(d);
    }
}
