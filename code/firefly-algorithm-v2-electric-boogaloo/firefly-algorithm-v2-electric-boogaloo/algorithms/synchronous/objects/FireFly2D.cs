using System.Text;
using firefly_algorithm_v2_electric_boogaloo.helpers.benchmark_functions;

namespace firefly_algorithm_v2_electric_boogaloo.algorithms.synchronous.objects;

public record FireFly2D((double, double) Location, Func<double, double, double> ObjectiveFunction)
{
    public double GetIntensity(bool invert = false)
    {
        return invert
            ? Functions.Negative(ObjectiveFunction(Location.Item1, Location.Item2)) //starting from 1 :/
            : ObjectiveFunction(Location.Item1, Location.Item2);
    }

    public override string ToString()
    {
        // nice...
        return $"f({Math.Round(Location.Item1, 16)}, {Math.Round(Location.Item2, 16)} = {Math.Round(GetIntensity(), 16)}";
    }
}
