package main;

import java.util.*;

// import static org.junit.Assert.assertEquals;

public class App {
    public static void main(String[] args) {
        /*
         * Results
         * 
         * N time seq time par3 time par4 speedup3 speedup4 64 2.44 ms 14.06 ms 1.90 ms
         * 0.17 1.29 128 10.68 ms 24.54 ms 2.24 ms 0.44 4.76 256 27.65 ms 33.94 ms 7.34
         * ms 0.81 3.77 512 206.34 ms 70.79 ms 31.53 ms 2.91 6.54 1024 2046.71 ms 249.26
         * ms 135.56 ms 8.21 15.10 2048 44873.42 ms 1362.23 ms 1090.91 ms 32.94 41.13
         * 4096 433737.82 ms 11397.14 ms 9498.48 ms 38.06 45.66
         * 
         */
        int N = 32;
        int M = N;
        Timer timer = new Timer();
        double[][] results = new double[7][6];
        int cnt = 0;
        for (int i = 0; i < 7; i++) {

            N = N * 2;

            Matrix a = Matrix.createRandomized(N, M);
            Matrix b = Matrix.createRandomized(M, N);

            timer.start();
            Matrix cSerial = Matrix.multSerial(a, b);
            double timeSerial = timer.getMilliseconds();

            timer.start();
            Matrix cParallel3 = Matrix.multParallel3(a, b);
            double timeParallel3 = timer.getMilliseconds();
            ;

            timer.start();
            Matrix cParallel4 = Matrix.multParallel4(a, b);
            double timeParallel4 = timer.getMilliseconds();

            double speedup3 = timeSerial / timeParallel3;

            double speedup4 = timeSerial / timeParallel4;
            if (cSerial.equals(cParallel3) && cSerial.equals(cParallel4))
                cnt++;

            results[i] = new double[] { N, timeSerial, timeParallel3, timeParallel4, speedup3, speedup4 };
        }

        System.out.println();
        System.out.println("Available processes: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Correctness  " + (cnt == 7));
        System.out.println("N" + "\t" + "Seq" + "\t" + "Prl3" + "\t" + "Prl4" + "\t" + "spdUp3" + "\t" + "spdUp4");
        N = 32;
        for (int i = 0; i < 7; i++) {
            N = N * 2;
            System.out.printf("%d \t", N);
            for (int j = 1; j < 6; j++) {
                System.out.printf("%.2f \t", results[i][j]);
            }
            System.out.printf("\n");
        }
    }
}
