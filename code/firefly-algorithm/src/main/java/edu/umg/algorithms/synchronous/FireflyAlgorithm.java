package edu.umg.algorithms.synchronous;

import edu.umg.algorithms.synchronous.objects.Firefly;
import edu.umg.helpers.Iteration;
import edu.umg.helpers.benchmark_functions.BenchmarkFunction;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class FireflyAlgorithm {

    private final double maximalAttractiveness;
    private final double lightAbsorptionCoefficient;
    private final double reductionCoefficient;
    private final int populationSize;
    private final int maximumNumberOfGenerations;
    private final Iteration[] iterations;
    private final Double[][][] locations;
    private final BenchmarkFunction<Double[], Double> objectiveFunction;
    private final int numberOfDimensions;
    private final boolean minimalize;
    private final double lowerBound;
    private final double upperBound;
    private double randomStepCoefficient;
    private Firefly[] population;
    private Firefly currentBestSolution;
    private Firefly theBestSolution;
    private final double THRESHOLD = 0.00001;

    public FireflyAlgorithm(
        double maximalAttractiveness,
        double lightAbsorptionCoefficient,
        double randomStepCoefficient,
        double reductionCoefficient,
        int populationSize,
        int maximumNumberOfGenerations,
        BenchmarkFunction<Double[], Double> objectiveFunction,
        int numberOfDimensions,
        double lowerBound,
        double upperBound,
        boolean minimalize
    ) {
        this.maximalAttractiveness = maximalAttractiveness;
        this.lightAbsorptionCoefficient = lightAbsorptionCoefficient;
        this.randomStepCoefficient = randomStepCoefficient;
        this.reductionCoefficient = reductionCoefficient;
        this.populationSize = populationSize;
        this.maximumNumberOfGenerations = maximumNumberOfGenerations;
        this.objectiveFunction = objectiveFunction;
        this.numberOfDimensions = numberOfDimensions;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.minimalize = minimalize;
        this.iterations = new Iteration[maximumNumberOfGenerations + 1];
        this.locations = new Double[maximumNumberOfGenerations +
        1][populationSize][numberOfDimensions];
    }

    public Iteration[] run() {
        initializePopulation();
        findTheBestSolution();
        addIteration(0);
        addLocationAt(0);

        for (int n = 0; n < maximumNumberOfGenerations; n++) {
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < populationSize; j++) {
                    if (population[i].getIntensity(minimalize) < population[j].getIntensity(minimalize)) {
                        population[i] = new Firefly(
                            computeNewLocation(i, j),
                            objectiveFunction
                        );
                    }
                }

                if (population[i].getIntensity(minimalize) > currentBestSolution.getIntensity(minimalize)) {
                    currentBestSolution = population[i].getCopy();

                    if (population[i].getIntensity(minimalize) > theBestSolution.getIntensity(minimalize)) {
                        theBestSolution = currentBestSolution.getCopy();
                    }
                }
            }

            currentBestSolution = new Firefly(computeNewLocation(), objectiveFunction);
            reduceRandomStepCoefficient();
            addIteration(n + 1);
            addLocationAt(n + 1);
        }

        return iterations;
    }

    private void initializePopulation() {
        population = new Firefly[populationSize];

        for (int i = 0; i < populationSize; i++) {
            population[i] = new Firefly(
                ThreadLocalRandom.current()
                    .doubles(numberOfDimensions, lowerBound, upperBound)
                    .boxed()
                    .toArray(Double[]::new),
                objectiveFunction
            );
        }
    }

    private void findTheBestSolution() {
        int locationOfTheBestSolution = 0;
        currentBestSolution = population[0];

        for (int i = 1; i < populationSize; i++) {
            if (
                population[i].getIntensity(minimalize) >
                population[locationOfTheBestSolution].getIntensity(minimalize)
            ) {
                locationOfTheBestSolution = i;
                currentBestSolution = population[locationOfTheBestSolution];
            }
        }
        currentBestSolution = population[locationOfTheBestSolution].getCopy();
        theBestSolution = population[locationOfTheBestSolution].getCopy();
    }

    private double distanceBetweenFireflies(int index1, int index2) {
        double result = 0;

        for (int i = 0; i < numberOfDimensions; i++) {
            result +=
            Math.pow(
                population[index1].location()[i] - population[index2].location()[i],
                2
            );
        }

        return result;
    }

    private double computeAttractiveness(int index1, int index2) {
        return (
            maximalAttractiveness *
            Math.exp(
                -lightAbsorptionCoefficient * distanceBetweenFireflies(index1, index2)
            )
        );
    }

    private Double[] computeNewLocation(int index1, int index2) {
        double attractiveness = computeAttractiveness(index1, index2);
        Double[] newLocation = new Double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            newLocation[i] = population[index1].location()[i] +
            attractiveness *
                (population[index2].location()[i] - population[index1].location()[i]) +
            computeRandomStep();

            if (newLocation[i] < lowerBound) {
                newLocation[i] = lowerBound;
            }

            if (newLocation[i] > upperBound) {
                newLocation[i] = upperBound;
            }
        }

        return newLocation;
    }

    private double computeRandomStep() {
        return randomStepCoefficient * (ThreadLocalRandom.current().nextDouble() - 0.5);
    }

    private Double[] computeNewLocation() {
        Double[] newLocation = new Double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            newLocation[i] = currentBestSolution.location()[i] + computeRandomStep();

            if (newLocation[i] < lowerBound) {
                newLocation[i] = lowerBound;
            }

            if (newLocation[i] > upperBound) {
                newLocation[i] = upperBound;
            }
        }

        return newLocation;
    }

    private void reduceRandomStepCoefficient() {
        randomStepCoefficient *= reductionCoefficient;
    }

    private void addIteration(int index) {
        double result = 0;

        for (Firefly firefly : population) {
            result += firefly.getIntensity();
        }

        iterations[index] = new Iteration(result, theBestSolution.getIntensity());
    }

    private void addLocationAt(int index) {
        for (int i = 0; i < populationSize; i++) {
            System.arraycopy(
                population[i].location(),
                0,
                locations[index][i],
                0,
                numberOfDimensions
            );
        }
    }

    public Firefly getFinalSolution() {
        return theBestSolution.getCopy();
    }

    public Double[][][] getLocations() {
        return locations;
    }

    public double[] getIntensities() {
        double[] intensities = new double[populationSize];

        for (int i = 0; i < populationSize; i++) {
            intensities[i] = population[i].getIntensity();
        }

        return intensities;
    }

    public boolean hasReachedTheGoal(){
        Double[] extremes = objectiveFunction.getExtremes();

        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < extremes.length; j++) {
                return areFloatsEqual(population[i].getIntensity(), extremes[j]);
            }
        }

        return false;
    }

    private boolean areFloatsEqual(double d1, double d2){
        return Math.abs(d1 - d2) < THRESHOLD;
    }
}
