## Usage

```
mvn clean install
java -jar target/benchmarks.jar
```

## Benchmarked Methods

```
int foo(int x) {
    return (a % b) - (x * b);
}
```

```
int bar(int x) {
    return (x * b) % 100 == 0 ? (int) Math.sin(x + 1) : x;
}
```

```
int barOptimized(int x) {
    return (x * b) % 100 == 0 ? barOptimized(x + 1) : x;
}
```

## Example Output

```
Benchmark                                  Mode  Samples     Score  Score error  Units
i.c.MyBenchmark.test1_onlyFoo              avgt        5   707.868        6.364  ns/op
i.c.MyBenchmark.test2_onlyBar              avgt        5  3468.826       14.276  ns/op
i.c.MyBenchmark.test3_combined             avgt        5  4027.042       31.676  ns/op
i.c.MyBenchmark.test4_onlyBarOptimized     avgt        5  2971.668       10.474  ns/op
i.c.MyBenchmark.test5_combinedOptimized    avgt        5  7369.246       77.448  ns/op
```