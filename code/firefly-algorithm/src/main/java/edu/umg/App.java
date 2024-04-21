package edu.umg;

import edu.umg.algorithms.synchronous.FireflyAlgorithm;
import edu.umg.helpers.benchmark_functions.multidimensional.Rastrigin;

import java.util.Arrays;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Rastrigin Rastrigin = new Rastrigin();
            FireflyAlgorithm fireflyAlgorithm = new FireflyAlgorithm(1, 0.1, 0.8, 0.98, 90, 2000, Rastrigin, 4, -5.12, 5.12, true);

            Double[] finalSolution = fireflyAlgorithm.run();

            System.out.printf("Najlepsze rozwiązanie: \n%s = %1.6f\n\n", Arrays.toString(finalSolution), Rastrigin.apply(finalSolution));
        }
    }
}
