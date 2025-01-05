namespace firefly_algorithm_v2_electric_boogaloo.helpers.benchmark_functions;

public static class Functions
{
    public static Func<double[], double> Ackey = args =>
    {
        var subTerm1 = args.Aggregate(0D, (acc, x) => acc + Math.Pow(x, 2));

        var term1 = Math.Exp(-0.2 * Math.Sqrt(subTerm1 / args.Length));

        var subTerm2 = args.Aggregate(0D, (acc, x) => acc + Math.Cos(2 * Math.PI * x));

        var term2 = Math.Exp(subTerm2 / args.Length);

        return -20 * term1 - term2 + 20 + Math.E;
    };

    /*
     This sucks. This function's extreme is Math.pow(2.8081311800070053291, numberOfDimensions).
     I don't know to implement it, so it will be cut out of the final paper.
     Unfortunately, this means I will have to run the experiments again (at least for the new SunSquares Function).
     This makes me fucking angry >:(
     
     public static Func<double[], double> AlpineN2 = args =>
        args.Aggregate(1D, (acc, x) => acc * Math.Sqrt(x) * Math.Sin(x));
    */

    public static Func<double, double, double> Bohachevsky = (x, y) =>
        Math.Pow(x, 2) + Math.Pow(y, 2) - 0.3 * Math.Cos(3 * Math.PI * x + 4 * Math.PI * y) + 0.3;

    public static Func<double, double, double> DropWave = (x, y) =>
    {
        var numerator = 1 + Math.Cos(12 * Math.Sqrt(Math.Pow(x, 2) + Math.Pow(y, 2)));

        var denominator = 0.5 * (Math.Pow(x, 2) + Math.Pow(y, 2)) + 2;

        return -(numerator / denominator);
    };

    public static Func<double, double, double> Easom = (x, y) =>
        -Math.Cos(x) * Math.Cos(y) * Math.Exp(-Math.Pow(x - Math.PI, 2) - Math.Pow(y - Math.PI, 2));

    public static Func<double, double, double> Eggholder = (x, y) =>
        -(y + 47) * Math.Sin(Math.Sqrt(Math.Abs(y + x / 2.0 + 47)))
        - x * Math.Sin(Math.Sqrt(Math.Abs(x - (y + 47))));

    public static Func<double[], double> Griewank = args =>
    {
        var term1 = args.Aggregate(0D, (acc, x) => acc + Math.Pow(x, 2) / 4000D);

        var modifiedArgs = args.Select((x, i) => x / Math.Sqrt(i + 1));

        var term2 = modifiedArgs.Aggregate(1D, (acc, x) => acc * Math.Cos(x));

        return term1 - term2 + 1;
    };

    public static Func<double, double, double> Himmelblau = (x, y) =>
        Math.Pow(Math.Pow(x, 2) + y - 11, 2) + Math.Pow(x + Math.Pow(y, 2) - 7, 2);

    public static Func<double, double, double> LevyN13 = (x, y) =>
        Math.Pow(Math.Sin(3 * Math.PI * x), 2)
        + Math.Pow(x - 1, 2) * (1 + Math.Pow(Math.Sin(3 * Math.PI * y), 2))
        + Math.Pow(y - 1, 2) * (1 + Math.Pow(Math.Sin(2 * Math.PI * y), 2));

    public static Func<double, double> Negative = x => -x;

    public static Func<double[], double> Rastrigin = args =>
        args.Aggregate(
            10D * args.Length,
            (acc, x) => acc + (Math.Pow(x, 2) - 10D * Math.Cos(2 * Math.PI * x))
        );

    public static Func<double[], double> Schwefel = args =>
    {
        var term1 = 418.9829 * args.Length;

        var term2 = args.Aggregate(0D, (acc, x) => acc + x * Math.Sin(Math.Sqrt(Math.Abs(x))));

        return term1 - term2;
    };

    public static Func<double[], double> Sphere = args =>
        args.Aggregate(0D, (acc, x) => acc + Math.Pow(x, 2));

    public static Func<double[], double> SumSquares = args =>
        args.Select((x, i) => i * Math.Pow(x, 2)).Aggregate((acc, x) => acc + x);

    public static double[] GetExtremesFor(Func<double[], double> function)
    {
        // This is just plain ugly, but I don't have time nor patience to improve it :(
        if (
            function == Ackey
            || function == Griewank
            || function == Rastrigin
            || function == Schwefel
            || function == Sphere
            || function == SumSquares
        )
        {
            return [0D];
        }

        return [];
    }

    public static double[] GetExtremesFor(Func<double, double, double> function)
    {
        if (function == Bohachevsky || function == Himmelblau || function == LevyN13)
        {
            return [0D];
        }

        if (function == DropWave || function == Easom)
        {
            return [-1D];
        }

        if (function == Eggholder)
        {
            return [-959.6406627106155];
        }

        return [];
    }
}
