import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Task1 {

    public static void main(String[] args) {
        Random random = new Random();
        long startTime = System.nanoTime();

        CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
            System.out.println("Generating initial array...");
        }).thenApplyAsync(ignored -> {
            int[] array = random.ints(10, 0, 100).toArray();
            printArray("Generated array", array);
            long elapsed = System.nanoTime() - startTime;
            System.out.printf("completed in %d ms.%n", TimeUnit.NANOSECONDS.toMillis(elapsed));
            return array;
        }).thenApplyAsync(array -> {
            System.out.println("Adding 10 to each element...");
            for (int i = 0; i < array.length; i++) {
                array[i] += 10;
            }
            printArray("Array after addition", array);
            long elapsed = System.nanoTime() - startTime;
            System.out.printf("completed in %d ms.%n", TimeUnit.NANOSECONDS.toMillis(elapsed));
            return array;
        }).thenApplyAsync(array -> {
            System.out.println("Dividing each element by 2...");
            double[] resultArray = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                resultArray[i] = array[i] / 2.0;
            }
            long elapsed = System.nanoTime() - startTime;
            System.out.printf("completed in %d ms.%n", TimeUnit.NANOSECONDS.toMillis(elapsed));
            return resultArray;
        }).thenAcceptAsync(resultArray -> {
            printArray("Result after division", resultArray);
            long elapsed = System.nanoTime() - startTime;
            System.out.printf("completed in %d ms.%n", TimeUnit.NANOSECONDS.toMillis(elapsed));
        }).thenRunAsync(() -> {
            long elapsed = System.nanoTime() - startTime;
            System.out.printf("Task 1 completed in %d ms.%n", TimeUnit.NANOSECONDS.toMillis(elapsed));
        });

        task.join();
    }

    private static void printArray(String message, int[] array) {
        System.out.print(message + ": ");
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private static void printArray(String message, double[] array) {
        System.out.print(message + ": ");
        for (double value : array) {
            System.out.printf("%.2f ", value);
        }
        System.out.println();
    }
}
