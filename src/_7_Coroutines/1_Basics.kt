package _7_Coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

/**
 * This file demonstrates the basics of Kotlin coroutines
 * including launching coroutines, suspension functions, coroutine scope, and context.
 *
 * Note: To run this example, add the kotlinx-coroutines-core dependency:
 * implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3'
 */
fun main() = runBlocking {
    println("===== BASIC COROUTINE LAUNCH =====")
    
    // Print the current thread
    println("Main program starts on: ${Thread.currentThread().name}")
    
    // Launch a coroutine in the GlobalScope
    val job = GlobalScope.launch {
        // Delay for 1 second (suspending function)
        delay(1000L)
        println("Coroutine executed on: ${Thread.currentThread().name}")
    }
    
    println("Main program continues while coroutine is scheduled")
    
    // Wait for the coroutine to finish
    job.join()
    println("Main program waits until coroutine is done")
    
    println("\n===== COROUTINES VS THREADS =====")
    
    val time = measureTimeMillis {
        // Launch 100,000 coroutines
        val jobs = List(100_000) {
            GlobalScope.launch {
                delay(1000L)
                // Just a small computation
                val result = it * it
            }
        }
        // Wait for all coroutines to finish
        jobs.forEach { it.join() }
    }
    println("Launched 100,000 coroutines in $time ms")
    
    println("\n===== SUSPENDING FUNCTIONS =====")
    
    println("Calling a suspending function")
    val result = fetchUserData(123)
    println("User data: $result")
    
    println("\n===== COROUTINE SCOPE =====")
    
    // Using coroutineScope builder to create a scope
    coroutineScope {
        // Launch multiple coroutines in the same scope
        launch {
            delay(500L)
            println("Task 1 executed with coroutineScope")
        }
        
        launch {
            delay(300L)
            println("Task 2 executed with coroutineScope")
        }
        
        println("coroutineScope: All tasks are launched")
    }
    println("coroutineScope: All tasks are completed")
    
    println("\n===== COROUTINE CONTEXT & DISPATCHERS =====")
    
    // Default dispatcher - shared thread pool
    launch {
        println("Default dispatcher: Running on ${Thread.currentThread().name}")
    }
    
    // IO dispatcher - optimized for IO-intensive operations
    launch(Dispatchers.IO) {
        println("IO dispatcher: Running on ${Thread.currentThread().name}")
    }
    
    // Main dispatcher - main UI thread (in Android or Swing apps)
    // Note: In a console app, this will crash unless we set a main dispatcher
    try {
        launch(Dispatchers.Main) {
            println("Main dispatcher: Running on ${Thread.currentThread().name}")
        }
    } catch (e: Exception) {
        println("Main dispatcher not available in console app: ${e.message}")
    }
    
    // Unconfined dispatcher - not confined to any specific thread
    launch(Dispatchers.Unconfined) {
        println("Unconfined initially: ${Thread.currentThread().name}")
        delay(100)
        println("Unconfined after delay: ${Thread.currentThread().name}")
    }
    
    // Custom dispatcher - with a fixed thread pool of size 4
    val customDispatcher = newFixedThreadPoolContext(4, "CustomPool")
    launch(customDispatcher) {
        println("Custom dispatcher: Running on ${Thread.currentThread().name}")
    }
    
    delay(500) // Wait for all coroutines to complete
    
    println("\n===== JOB HIERARCHY & CANCELLATION =====")
    
    // Create a parent job
    val parentJob = launch {
        println("Parent job started")
        
        // Create child jobs
        val child1 = launch {
            try {
                repeat(10) { i ->
                    println("Child 1: Step $i")
                    delay(200)
                }
            } catch (e: CancellationException) {
                println("Child 1 was cancelled: ${e.message}")
            }
        }
        
        val child2 = launch {
            try {
                repeat(10) { i ->
                    println("Child 2: Step $i")
                    delay(200)
                }
            } catch (e: CancellationException) {
                println("Child 2 was cancelled: ${e.message}")
            }
        }
        
        // Delay to let children run for a bit
        delay(500)
        println("Cancelling parent job (which will cancel all children)")
        
        // Cancel this parent job (will cancel all children)
        this.coroutineContext.job.cancel("Parent decided to cancel")
    }
    
    // Wait for parent job to complete (it will be cancelled)
    parentJob.join()
    println("Parent job and all children are completed or cancelled")
    
    println("\n===== COROUTINE EXCEPTIONS =====")
    
    // Exception propagation in coroutines
    try {
        coroutineScope {
            launch {
                delay(200)
                println("Coroutine 1 completed normally")
            }
            
            launch {
                delay(100)
                throw RuntimeException("Oops! Something went wrong in coroutine 2")
            }
            
            println("This line will be printed before the exception")
        }
        println("This line will NOT be printed due to exception")
    } catch (e: Exception) {
        println("Caught exception: ${e.message}")
    }
    
    // Using supervisorScope to prevent exception propagation
    println("\nUsing supervisorScope for exception handling:")
    supervisorScope {
        val job1 = launch {
            delay(200)
            println("Coroutine 1 in supervisorScope completed normally")
        }
        
        val job2 = launch {
            try {
                delay(100)
                throw RuntimeException("Oops! Something went wrong in coroutine 2")
            } catch (e: Exception) {
                println("Exception handled in coroutine 2: ${e.message}")
            }
        }
        
        // Wait for both jobs to complete
        joinAll(job1, job2)
        println("supervisorScope: This line WILL be printed despite exception")
    }
    
    println("\n===== STRUCTURED CONCURRENCY =====")
    
    println("Starting structured concurrency example")
    
    // Parent coroutine with children - demonstrating structured concurrency
    val structuredJob = launch {
        // Launch two child coroutines
        launch { 
            delay(200)
            println("Child 1 in structured concurrency")
        }
        
        launch {
            delay(300) 
            println("Child 2 in structured concurrency")
        }
        
        println("Parent in structured concurrency: Children are now running")
    }
    
    // Wait for the parent and all its children to complete
    structuredJob.join()
    println("All coroutines in structured concurrency completed")
    
    println("\n===== REAL-WORLD EXAMPLE: ASYNC TASKS =====")
    
    println("Starting asynchronous tasks example...")
    
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    fun logTime(msg: String) = println("${LocalTime.now().format(timeFormatter)}: $msg")
    
    logTime("Starting asynchronous data fetching")
    
    // Using async to execute tasks concurrently and get their results
    val userDataDeferred = async { fetchUserData(42) }
    val productDataDeferred = async { fetchProductData(123) }
    val orderDataDeferred = async { fetchOrderData(987) }
    
    // Wait for all data to be fetched and combine the results
    val userData = userDataDeferred.await()
    val productData = productDataDeferred.await()
    val orderData = orderDataDeferred.await()
    
    logTime("All data fetched, combining results")
    
    // Combine the results
    val combinedData = """
        User: $userData
        Product: $productData
        Order: $orderData
    """.trimIndent()
    
    logTime("Combined data: $combinedData")
    
    println("\n===== REAL-WORLD EXAMPLE: BACKGROUND PROCESSING =====")
    
    val dataProcessor = DataProcessor()
    println("Starting background processing...")
    
    // Process items in the background
    val items = listOf("Item1", "Item2", "Item3", "Item4", "Item5")
    dataProcessor.processItemsAsync(items) { processed ->
        println("All items processed: $processed")
    }
    
    println("Main function continues while processing happens in background")
    delay(3000) // Wait for processing to complete for demo purposes
    
    println("\n===== REAL-WORLD EXAMPLE: PARALLEL PROCESSING =====")
    
    println("Starting parallel processing example...")
    val numbers = (1..10).toList()
    
    // Sequential processing
    val seqTime = measureTimeMillis {
        val sequentialResult = processSequentially(numbers)
        println("Sequential result: $sequentialResult")
    }
    println("Sequential processing took $seqTime ms")
    
    // Parallel processing
    val parallelTime = measureTimeMillis {
        val parallelResult = processInParallel(numbers)
        println("Parallel result: $parallelResult")
    }
    println("Parallel processing took $parallelTime ms")
    
    println("Speedup factor: ${seqTime.toDouble() / parallelTime}")
}

// Suspending function example
suspend fun fetchUserData(userId: Int): String {
    println("Fetching user data for user $userId...")
    delay(1000) // Simulate network request
    return "User $userId: John Doe"
}

// Additional suspending functions for the async example
suspend fun fetchProductData(productId: Int): String {
    println("Fetching product data for product $productId...")
    delay(1500) // Simulate network request
    return "Product $productId: Smartphone"
}

suspend fun fetchOrderData(orderId: Int): String {
    println("Fetching order data for order $orderId...")
    delay(800) // Simulate network request
    return "Order $orderId: 2 items, $125.99"
}

// Real-world example: Background data processor
class DataProcessor {
    private val processingScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    fun processItemsAsync(items: List<String>, onComplete: (List<String>) -> Unit) {
        processingScope.launch {
            val processedItems = items.map { processItem(it) }
            withContext(Dispatchers.Default) {
                onComplete(processedItems)
            }
        }
    }
    
    private suspend fun processItem(item: String): String {
        println("Processing $item...")
        delay(500) // Simulate processing time
        return "$item - Processed"
    }
    
    fun shutdown() {
        processingScope.cancel()
    }
}

// Sequential and parallel processing examples
suspend fun processSequentially(numbers: List<Int>): Int {
    var result = 0
    for (number in numbers) {
        result += performExpensiveCalculation(number)
    }
    return result
}

suspend fun processInParallel(numbers: List<Int>): Int = coroutineScope {
    val deferreds = numbers.map { number ->
        async {
            performExpensiveCalculation(number)
        }
    }
    deferreds.awaitAll().sum()
}

suspend fun performExpensiveCalculation(number: Int): Int {
    delay(300) // Simulate expensive calculation
    return number * number
} 