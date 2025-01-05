using System.Text;
using firefly_algorithm_v2_electric_boogaloo.helpers.benchmark_functions;

namespace firefly_algorithm_v2_electric_boogaloo.algorithms.synchronous.objects;

public record FireflyND(double[] location, Func<double[], double> objectiveFunction)
{
    public double GetIntensity(bool invert = false)
    {
        return invert
            ? Functions.Negative(objectiveFunction(location))
            : objectiveFunction(location);
    }

    public override string ToString()
    {
        var stringBuilder = new StringBuilder("f(");

        for (var i = 0; i < location.Length - 1; i++)
        {
            stringBuilder.Append($"{Math.Round(location[i], 16)}, ");
        }

        return stringBuilder
            .Append($"{Math.Round(location[^1], 16)}) = {Math.Round(GetIntensity(), 16)}")
            .ToString();
    }
}
