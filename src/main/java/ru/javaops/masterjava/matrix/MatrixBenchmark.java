package ru.javaops.masterjava.matrix;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.*;

@Warmup(iterations = 10)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@Timeout(time = 5, timeUnit = TimeUnit.SECONDS)
public class MatrixBenchmark {
    @Param({"1000", "3000"})
    private int matrixSize;

    private final static int THREAD_COUNT = 10;
    private final static ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

    private static int[][] matrixA;
    private static int[][] matrixB;

    @Setup(Level.Trial)
    public void setUp() {
        matrixA = MatrixUtil.create(matrixSize);
        matrixB = MatrixUtil.create(matrixSize);
    }

    @Benchmark
    public int[][] singleThreadMultiply() {
        return MatrixUtil.singleThreadMultiplyOpt(matrixA, matrixB);
    }

    @Benchmark
    public int[][] concurrentMultiply() throws ExecutionException, InterruptedException, TimeoutException {
        return MatrixUtil.concurrentMultiply1(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiply2() throws ExecutionException, InterruptedException, TimeoutException {
        return MatrixUtil.concurrentMultiply2(matrixA, matrixB, executor);
    }

    @TearDown
    public void tearDown() {
        executor.shutdown();
    }
}
