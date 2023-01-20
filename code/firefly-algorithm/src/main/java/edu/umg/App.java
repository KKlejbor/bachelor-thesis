package edu.umg;

import edu.umg.algorithms.synchronous.FireflyAlgorithm;
import edu.umg.helpers.RastriginFunction;
import edu.umg.objects.Firefly;

import java.util.function.Function;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        for (int i = 0; i < 10; i++) {
            FireflyAlgorithm fireflyAlgorithm = new FireflyAlgorithm(1, 0.1, 0.8, 0.98, 90, 1000, new RastriginFunction(), 4, -5.12, 5.12);

            // FIXME: 20.01.2023 Remove after fixing add a flag for the type of optimization
            Function<Double[], Double> rastrigin = new Function<Double[], Double>() {
                @Override
                public Double apply(Double[] doubles) {
                    double result = 10 * doubles.length;

                    for (int i = 0; i < doubles.length; i++) {
                        result += Math.pow(doubles[i], 2) - 10 * Math.cos(2 * Math.PI * doubles[i]);
                    }
                    
                    return result;
                }
            };

            Firefly finalSolution = new Firefly(fireflyAlgorithm.run(), rastrigin);

            System.out.printf("Najlepsze rozwiązanie: \n%s\n", finalSolution);
        }
    }
}
