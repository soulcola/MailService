package ru.javaops.masterjava.matrix;

import java.util.*;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply1(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException, TimeoutException {

        final int matrixSize = matrixA.length;
        final int taskNumber = 10;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[][] matrixBT = new int[matrixSize][matrixSize];

        Set<Callable<Void>> threads = new HashSet<>();

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrixBT[j][i] = matrixB[i][j];
            }
        }
        int startIndex = 0;
        for (int l = 0; l < taskNumber; l++) {
            final int finalStartIndex = startIndex;
            final int endIndex = Math.min(startIndex + (matrixSize / taskNumber), matrixSize);
            threads.add(() -> {
                for (int i = finalStartIndex; i < endIndex; i++) {
                    for (int j = 0; j < matrixSize; j++) {
                        int sum = 0;
                        for (int k = 0; k < matrixSize; k++) {
                            sum += matrixA[i][k] * matrixBT[j][k];
                        }
                        matrixC[i][j] = sum;
                    }
                }
                return null;
            });
            startIndex += matrixSize / taskNumber;
        }

        executor.invokeAll(threads);
        return matrixC;
    }

    public static int[][] concurrentMultiply2(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException, TimeoutException {

        class MatrixResult {
            final int index;
            final int[] columnC;

            public MatrixResult(int index, int[] columnC) {
                this.index = index;
                this.columnC = columnC;
            }
        }

        final CompletionService<MatrixResult> completionService = new ExecutorCompletionService<>(executor);

        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            final int col = i;
            final int[] resultColumn = new int[matrixSize];
            final int[] columnB = new int[matrixSize];
            for (int k = 0; k < matrixSize; k++) {
                columnB[k] = matrixB[k][col];
            }
            completionService.submit(() -> {
                for (int row = 0; row < matrixSize; row++) {
                    int sum = 0;
                    final int[] rowA = matrixA[row];
                    for (int k = 0; k < matrixSize; k++) {
                        sum += rowA[k] * columnB[k];
                    }
                    resultColumn[row] = sum;
                }
                return new MatrixResult(col, resultColumn);
            });
        }

        for (int i = 0; i < matrixSize; i++) {
            MatrixResult matrixResult = completionService.take().get();
            for (int j = 0; j < matrixSize; j++) {
                matrixC[j][matrixResult.index] = matrixResult.columnC[j];
            }
        }
//        Arrays.stream(matrixC).forEach(m -> System.out.println(Arrays.toString(m)));
        return matrixC;
    }


    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] singleThreadMultiplyOpt(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixBT = new int[matrixSize][matrixSize];
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrixBT[j][i] = matrixB[i][j];
            }
        }

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixBT[j][k];
                }
                matrixC[i][j] = sum;
            }
        }
//        Arrays.stream(matrixC).forEach(m -> System.out.println(Arrays.toString(m)));
        return matrixC;
    }

    public static int[][] singleThreadMultiplyOpt2(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];


        for (int col = 0; col < matrixSize; col++) {
            final int[] columnB = new int[matrixSize];
            for (int k = 0; k < matrixSize; k++) {
                columnB[k] = matrixB[k][col];
            }

            for (int row = 0; row < matrixSize; row++) {
                int sum = 0;
                final int[] rowA = matrixA[row];
                for (int k = 0; k < matrixSize; k++) {
                    sum += rowA[k] * columnB[k];
                }
                matrixC[row][col] = sum;
            }
        }
//        Arrays.stream(matrixC).forEach(m -> System.out.println(Arrays.toString(m)));
        return matrixC;
    }


    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
