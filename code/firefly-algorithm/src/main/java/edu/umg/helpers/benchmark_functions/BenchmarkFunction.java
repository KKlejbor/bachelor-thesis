package edu.umg.helpers.benchmark_functions;

import java.util.function.Function;

public interface BenchmarkFunction<T, R> extends Function<T, R> {
    R[] getExtremes();
}
