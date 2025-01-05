using firefly_algorithm_v2_electric_boogaloo.helpers.benchmark_functions;

namespace firefly_algorithm_v2_electric_boogaloo;

public abstract class Program
{
    public static void Main(string[] args)
    {
        Console.WriteLine("Hello world!");
        Console.WriteLine(nameof(Functions.Ackey));
        Console.WriteLine(
            Functions.GetExtremesFor(Functions.Ackey).Aggregate("", (str, x) => str + ' ' + x)
        );
        Console.WriteLine(nameof(Functions.Easom));
        Console.WriteLine(
            Functions.GetExtremesFor(Functions.Easom).Aggregate("", (str, x) => str + ' ' + x)
        );
    }
}
