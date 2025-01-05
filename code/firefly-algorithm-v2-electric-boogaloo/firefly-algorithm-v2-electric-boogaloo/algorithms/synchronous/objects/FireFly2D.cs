using System.Text;
using firefly_algorithm_v2_electric_boogaloo.helpers.benchmark_functions;

namespace firefly_algorithm_v2_electric_boogaloo.algorithms.synchronous.objects;

public record FireFly2D((double, double) location, Func<double, double, double> objectiveFunction)
{
    public double GetIntensity(bool invert = false)
    {
        return invert
            ? Functions.Negative(objectiveFunction(location.Item1, location.Item2)) //starting from 1 :/
            : objectiveFunction(location.Item1, location.Item2);
    }

    public override string ToString()
    {
        // nice...
        return $"f({Math.Round(location.Item1, 16)}, {Math.Round(location.Item2, 16)} = {Math.Round(GetIntensity(), 16)}";
    }
}
