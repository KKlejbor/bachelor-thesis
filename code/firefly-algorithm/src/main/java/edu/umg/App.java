package edu.umg;

import edu.umg.algorithms.synchronous.FireflyAlgorithm;
import edu.umg.helpers.RastriginFunction;
import edu.umg.algorithms.synchronous.objects.Firefly;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            FireflyAlgorithm fireflyAlgorithm = new FireflyAlgorithm(1, 0.1, 0.8, 0.98, 90, 1000, new RastriginFunction(), 4, -5.12, 5.12, true);

            Firefly finalSolution = new Firefly(fireflyAlgorithm.run(), new RastriginFunction(), false);

            System.out.printf("Najlepsze rozwiązanie: \n%s\n\n", finalSolution);
        }
    }
}
