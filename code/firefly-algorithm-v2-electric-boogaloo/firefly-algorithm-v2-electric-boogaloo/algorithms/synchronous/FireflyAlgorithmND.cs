using firefly_algorithm_v2_electric_boogaloo.algorithms.synchronous.objects;
using firefly_algorithm_v2_electric_boogaloo.helpers;
using firefly_algorithm_v2_electric_boogaloo.helpers.benchmark_functions;

namespace firefly_algorithm_v2_electric_boogaloo.algorithms.synchronous;

// ReSharper disable once InconsistentNaming
public class FireflyAlgorithmND
{
    private readonly double _maximalAttractiveness;
    private readonly double _lightAbsorptionCoefficient;
    private readonly double _reductionCoefficient;
    private readonly int _populationSize;
    private readonly int _maximumNumberOfGenerations;
    private readonly Iteration[] _iterations;
    private readonly double[][][] _locations;
    private readonly Func<double[], double> _objectiveFunction;
    private readonly int _numberOfDimensions;
    private readonly bool _minimalize;
    private readonly double _lowerBound;
    private readonly double _upperBound;
    private double _randomStepCoefficient;
    private FireflyND[] _population;
    private FireflyND _currentBestSolution;
    private FireflyND _theBestSolution;
    private const double Threshold = 0.00001;
    private int _currentRun;

    public FireflyAlgorithmND(
        double maximalAttractiveness,
        double lightAbsorptionCoefficient,
        double reductionCoefficient,
        int populationSize,
        int maximumNumberOfGenerations,
        Iteration[] iterations,
        double[][][] locations,
        Func<double[], double> objectiveFunction,
        int numberOfDimensions,
        double lowerBound,
        double upperBound,
        double randomStepCoefficient,
        bool minimalize = true
    )
    {
        _maximalAttractiveness = maximalAttractiveness;
        _lightAbsorptionCoefficient = lightAbsorptionCoefficient;
        _reductionCoefficient = reductionCoefficient;
        _populationSize = populationSize;
        _maximumNumberOfGenerations = maximumNumberOfGenerations;
        _iterations = iterations;
        _locations = locations;
        _objectiveFunction = objectiveFunction;
        _numberOfDimensions = numberOfDimensions;
        _minimalize = minimalize;
        _lowerBound = lowerBound;
        _upperBound = upperBound;
        _randomStepCoefficient = randomStepCoefficient;
    }

    public Iteration[] Run()
    {
        InitializePopulation();
        FindTheBestSolution();
        AddIteration();
        AddLocationAt(0);
        var numberOfRunsWithoutImprovements = 500;

        while (_currentRun < _maximumNumberOfGenerations)
        {
            for (var i = 0; i < _populationSize; i++)
            {
                for (var j = 0; j < _populationSize; j++)
                {
                    if (
                        _population[i].GetIntensity(_minimalize)
                        < _population[j].GetIntensity(_minimalize)
                    )
                    {
                        _population[i] = new FireflyND(
                            ComputeNewLocation(i, j),
                            _objectiveFunction
                        );
                    }
                }

                // Compiler! Stop with the fucking continues! That word is on the Sanctioned Words List
                // ReSharper disable once InvertIf
                if (
                    _population[i].GetIntensity(_minimalize)
                    > _currentBestSolution.GetIntensity(_minimalize)
                )
                {
                    _currentBestSolution = _population[i].GetCopy();

                    // This sucks! Continue sucks!
                    // ReSharper disable once InvertIf
                    if (
                        _population[i].GetIntensity(_minimalize)
                        > _theBestSolution.GetIntensity(_minimalize)
                    )
                    {
                        _theBestSolution = _currentBestSolution.GetCopy();
                        numberOfRunsWithoutImprovements = 500;
                    }
                }
            }

            _currentBestSolution = new FireflyND(ComputeNewLocation(), _objectiveFunction);
            ReduceRandomStepCoefficient();
            AddIteration(_currentRun + 1);
            AddLocationAt(_currentRun + 1);
            _currentRun++;

            if (--numberOfRunsWithoutImprovements == 0)
            {
                break;
            }

            if (HasReachedTheGoal())
            {
                break;
            }
        }

        var result = new Iteration[_currentRun + 1];

        _iterations.CopyTo(result, 0);

        return result;
    }

    private bool HasReachedTheGoal()
    {
        var extremes = Functions.GetExtremesFor(_objectiveFunction);
        var result = false;

        foreach (var firefly in _population)
        {
            foreach (var extreme in extremes)
            {
                result = AreFloatsEqual(firefly.GetIntensity(), extreme);

                if (result)
                {
                    return result;
                }
            }
        }

        return result;
    }

    private bool AreFloatsEqual(double d1, double d2)
    {
        return Math.Abs(d1 - d2) < Threshold;
    }

    private void ReduceRandomStepCoefficient()
    {
        _randomStepCoefficient *= _reductionCoefficient;
    }

    private double[] ComputeNewLocation()
    {
        var newLocation = new double[_numberOfDimensions];

        for (var i = 0; i < _numberOfDimensions; i++)
        {
            newLocation[i] = _currentBestSolution.Location[i] + ComputeRandomStep();

            if (newLocation[i] < _lowerBound)
            {
                newLocation[i] = _lowerBound;
            }

            if (newLocation[i] > _upperBound)
            {
                newLocation[i] = _upperBound;
            }
        }

        return newLocation;
    }

    private double ComputeRandomStep()
    {
        return _randomStepCoefficient * (StaticRandom.NextDouble() - 0.5);
    }

    private double[] ComputeNewLocation(int index1, int index2)
    {
        var attractiveness = ComputeAttractiveness(index1, index2);
        var newLocation = new double[_numberOfDimensions];

        for (var i = 0; i < _numberOfDimensions; i++)
        {
            newLocation[i] =
                _population[index1].Location[i]
                + attractiveness
                    * (_population[index2].Location[i] - _population[index1].Location[i])
                + ComputeRandomStep();

            if (newLocation[i] < _lowerBound)
            {
                newLocation[i] = _lowerBound;
            }

            if (newLocation[i] > _upperBound)
            {
                newLocation[i] = _upperBound;
            }
        }

        return newLocation;
    }

    private double ComputeAttractiveness(int index1, int index2)
    {
        return _maximalAttractiveness
            * Math.Exp(-_lightAbsorptionCoefficient * DistanceBetweenFireflies(index1, index2));
    }

    private double DistanceBetweenFireflies(int index1, int index2)
    {
        var result = 0D;

        for (var i = 0; i < _numberOfDimensions; i++)
        {
            result += Math.Pow(
                _population[index1].Location[i] - _population[index2].Location[i],
                2
            );
        }

        return result;
    }

    private void AddLocationAt(int index)
    {
        for (var i = 0; i < _populationSize; i++)
        {
            _population[i].Location.CopyTo(_locations[index], 0);
        }
    }

    private void AddIteration(int index = 0)
    {
        _iterations[index] = new Iteration(
            _population.Aggregate(0D, (acc, x) => acc + x.GetIntensity()),
            _theBestSolution.GetIntensity()
        );
    }

    private void FindTheBestSolution()
    {
        var locationOfTheBestSolution = 0;
        _currentBestSolution = _population[0];

        _currentBestSolution = _population[0];

        for (var i = 1; i < _populationSize; i++)
        {
            // No continues, thank you
            // ReSharper disable once InvertIf
            if (
                _population[i].GetIntensity(_minimalize)
                > _population[locationOfTheBestSolution].GetIntensity(_minimalize)
            )
            {
                locationOfTheBestSolution = i;
                _currentBestSolution = _population[locationOfTheBestSolution];
            }
        }
        _currentBestSolution = _population[locationOfTheBestSolution].GetCopy();
        _theBestSolution = _population[locationOfTheBestSolution].GetCopy();
    }

    private void InitializePopulation()
    {
        _population = new FireflyND[_populationSize];

        for (var i = 0; i < _populationSize; i++)
        {
            _population[i] = new FireflyND(
                StaticRandom.NextDoubles(_numberOfDimensions, _lowerBound, _upperBound),
                _objectiveFunction
            );
        }
    }
}
