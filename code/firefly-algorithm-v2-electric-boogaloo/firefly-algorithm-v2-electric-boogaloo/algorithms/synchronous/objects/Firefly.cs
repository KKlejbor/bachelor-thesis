using System.Text;
using firefly_algorithm_v2_electric_boogaloo.helpers.benchmark_functions;

namespace firefly_algorithm_v2_electric_boogaloo.algorithms.synchronous.objects;

// ReSharper disable once InconsistentNaming
public record FireflyND(double[] Location, Func<double[], double> ObjectiveFunction)
{
    public double GetIntensity(bool invert = false)
    {
        return invert
            ? Functions.Negative(ObjectiveFunction(Location))
            : ObjectiveFunction(Location);
    }

    public override string ToString()
    {
        var stringBuilder = new StringBuilder("f(");

        for (var i = 0; i < Location.Length - 1; i++)
        {
            stringBuilder.Append($"{Math.Round(Location[i], 16)}, ");
        }

        return stringBuilder
            .Append($"{Math.Round(Location[^1], 16)}) = {Math.Round(GetIntensity(), 16)}")
            .ToString();
    }
}
