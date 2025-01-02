import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Task2 {

    public static void main(String[] args) {
        Random random = new Random();
        AtomicLong startTime = new AtomicLong(System.nanoTime());
        AtomicLong elapsed1 = new AtomicLong();
        AtomicLong elapsed2 = new AtomicLong();
        AtomicLong elapsed3 = new AtomicLong();
        CompletableFuture<Void> task = CompletableFuture.supplyAsync(() -> {
            System.out.println("Generating sequence of random numbers...");
            double[] sequence = random.doubles(20, 1.0, 100.0).toArray();
            printArray("Generated sequence", sequence);
            elapsed1.set(System.nanoTime() - startTime.get());
            startTime.set(System.nanoTime());
            return sequence;
        }).thenApplyAsync(sequence -> {
            System.out.println("Calculating result...");
            double result = 1.0;
            for (int i = 1; i < sequence.length; i++) {
                result *= (sequence[i] - sequence[i - 1]);
            }
            elapsed2.set(System.nanoTime() - startTime.get());
            startTime.set(System.nanoTime());
            return result;
        }).thenAcceptAsync(result -> {
            System.out.printf("Final result: %.4f%n", result);
            elapsed3.set(System.nanoTime() - startTime.get());
            startTime.set(System.nanoTime());
        }).thenRunAsync(() -> {
            System.out.printf("First task completed in %d ms.%n", TimeUnit.NANOSECONDS.toMillis(elapsed1.get()));
            System.out.printf("Second task completed in %d ms.%n", TimeUnit.NANOSECONDS.toMillis(elapsed2.get()));
            System.out.printf("Third task completed in %d ms.%n", TimeUnit.NANOSECONDS.toMillis(elapsed3.get()));
        });

        task.join();
    }

    private static void printArray(String message, double[] array) {
        System.out.print(message + ": ");
        for (double value : array) {
            System.out.printf("%.2f ", value);
        }
        System.out.println();
    }
}
