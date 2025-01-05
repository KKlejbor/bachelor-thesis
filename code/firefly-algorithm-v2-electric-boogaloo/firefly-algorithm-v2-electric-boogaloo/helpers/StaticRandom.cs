namespace firefly_algorithm_v2_electric_boogaloo.helpers;

public static class StaticRandom
{
    private static readonly Random Random = new(Guid.NewGuid().GetHashCode());

    public static double NextDouble()
    {
        return Random.NextDouble();
    }

    public static double NextDouble(double lowerBound, double upperBound)
    {
        return Random.NextDouble() * (upperBound - lowerBound) + lowerBound;
    }

    public static double[] NextDoubles(int amount)
    {
        var doubles = new double[amount];

        return doubles.Select(_ => NextDouble()).ToArray();
    }

    public static double[] NextDoubles(int amount, double lowerBound, double upperBound)
    {
        var doubles = new double[amount];

        return doubles.Select(_ => NextDouble(lowerBound, upperBound)).ToArray();
    }
}
