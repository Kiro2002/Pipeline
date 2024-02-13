import java.util.concurrent.*

class Watek1(private val latch: CountDownLatch) : Runnable {
    override fun run() {
        for (i in 1..5) {
            println("Wątek 1: Licznik $i")
            Thread.sleep(1000)
        }
        latch.countDown()
    }
}

class Watek2(private val latch: CountDownLatch) : Runnable {
    override fun run() {
        latch.await() // Wątek 2 czeka na zakończenie wątku 1
        for (j in 1..5) {
            println("Wątek 2: Licznik $j")
            Thread.sleep(1500)
        }
    }
}

fun main() {
    println("Rozpoczęcie programu")

    // Licznik do synchronizacji wątków
    val latch = CountDownLatch(1)

    // 1. Utworzenie puli wątków z dwoma wątkami
    val executor: ExecutorService = Executors.newFixedThreadPool(2)

    // 2. Zleć zadania do wykonania w puli wątków
    executor.submit(Watek2(latch))
    executor.submit(Watek1(latch))

    // 3. Zakończ pulę wątków po zakończeniu zadań
    executor.shutdown()

    try {
        // Oczekiwanie na zakończenie wszystkich zadań w puli wątków
        if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
            // Przerywa zadania, jeśli nie zakończą się w ciągu 1 godziny
            executor.shutdownNow()
        }
    } catch (e: InterruptedException) {
        // Przechwytuje wyjątek, jeśli wystąpi
        e.printStackTrace()
        executor.shutdownNow()
    }

    println("Zakończenie programu")
}
