package _7_Coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * This file demonstrates Kotlin coroutines Channels and Flow
 * for communication between coroutines and processing streams of data.
 *
 * Note: To run this example, add the kotlinx-coroutines-core dependency:
 * implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3'
 */
fun main() = runBlocking {
    println("===== BASIC CHANNEL =====")
    
    // Create a basic channel
    val channel = Channel<Int>()
    
    // Launch a producer coroutine
    val producer = launch {
        println("Producer sending values...")
        for (i in 1..5) {
            channel.send(i)
            println("Sent $i")
            delay(100)
        }
        channel.close() // Close the channel when done
        println("Producer completed")
    }
    
    // Consumer coroutine
    val consumer = launch {
        println("Consumer receiving values...")
        for (value in channel) {
            println("Received $value")
            delay(200) // Process slower than producer
        }
        println("Consumer completed (channel closed)")
    }
    
    joinAll(producer, consumer)
    
    println("\n===== CHANNEL TYPES =====")
    
    // Rendezvous channel (default) - no buffer
    val rendezvousChannel = Channel<Int>()
    testChannel("Rendezvous", rendezvousChannel)
    
    // Buffered channel - fixed-size buffer
    val bufferedChannel = Channel<Int>(3)
    testChannel("Buffered (size 3)", bufferedChannel)
    
    // Unlimited channel - unbounded buffer
    val unlimitedChannel = Channel<Int>(Channel.UNLIMITED)
    testChannel("Unlimited", unlimitedChannel)
    
    // Conflated channel - keeps only the latest value
    val conflatedChannel = Channel<Int>(Channel.CONFLATED)
    testChannel("Conflated", conflatedChannel)
    
    // Channel with onBufferOverflow
    val customBufferChannel = Channel<Int>(
        capacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    testChannel("Custom (DROP_OLDEST)", customBufferChannel)
    
    println("\n===== MULTIPLE PRODUCERS & CONSUMERS =====")
    
    val sharedChannel = Channel<String>()
    
    // Multiple producers
    val producers = List(3) { producerId ->
        launch {
            for (i in 1..3) {
                val message = "Message $i from Producer $producerId"
                sharedChannel.send(message)
                println("Producer $producerId sent: $message")
                delay(100)
            }
        }
    }
    
    // Multiple consumers
    val consumers = List(2) { consumerId ->
        launch {
            for (i in 1..4) { // Each consumer tries to receive 4 messages
                try {
                    val message = sharedChannel.receive()
                    println("Consumer $consumerId received: $message")
                    delay(150)
                } catch (e: ClosedReceiveChannelException) {
                    println("Consumer $consumerId: Channel was closed")
                    break
                }
            }
        }
    }
    
    // Let producers and consumers run for a while
    delay(1000)
    
    // Close the channel
    sharedChannel.close()
    println("Shared channel closed")
    
    // Wait for all coroutines to complete
    joinAll(*(producers + consumers).toTypedArray())
    
    println("\n===== FAN-OUT (ONE PRODUCER, MANY CONSUMERS) =====")
    
    val produceChannel = produce {
        for (i in 1..10) {
            send(i)
            println("Produced $i")
            delay(100)
        }
    }
    
    // Multiple consumers processing from the same channel
    val fanOutConsumers = List(3) { consumerId ->
        launch {
            for (value in produceChannel) {
                println("Consumer $consumerId processing value: $value")
                delay(300) // Simulate processing time
            }
            println("Consumer $consumerId completed")
        }
    }
    
    // Wait for all consumers to complete
    joinAll(*fanOutConsumers.toTypedArray())
    
    println("\n===== FAN-IN (MANY PRODUCERS, ONE CONSUMER) =====")
    
    val fanInChannel = Channel<String>()
    
    // Multiple producers sending to the same channel
    val fanInProducers = List(3) { producerId ->
        launch {
            for (i in 1..3) {
                val message = "Message $i from Producer $producerId"
                fanInChannel.send(message)
                println("Producer $producerId sent: $message")
                delay(100)
            }
        }
    }
    
    // Launch a separate coroutine to close the channel after all producers are done
    launch {
        joinAll(*fanInProducers.toTypedArray())
        println("All producers completed, closing channel")
        fanInChannel.close()
    }
    
    // Single consumer receiving from all producers
    for (message in fanInChannel) {
        println("Consumer received: $message")
        delay(50) // Process faster than producers
    }
    
    println("Fan-in consumer completed")
    
    println("\n===== BASIC FLOW =====")
    
    // Create a simple flow
    val flow = flow {
        for (i in 1..5) {
            println("Emitting $i")
            emit(i)
            delay(100)
        }
    }
    
    // Collect values from the flow
    println("Collecting flow values:")
    flow.collect { value ->
        println("Collected $value")
        delay(200) // Processing time
    }
    
    println("\n===== FLOW OPERATORS =====")
    
    // Create a source flow
    val numbersFlow = (1..10).asFlow()
    
    // Apply various operators
    println("Flow with operators:")
    numbersFlow
        .filter { it % 2 == 0 } // Only even numbers
        .map { it * it }        // Square them
        .take(3)                // Take only first 3 results
        .collect { println("Result: $it") }
    
    // More complex flow transformation
    println("\nMore complex flow transformation:")
    (1..5).asFlow()
        .transform { value ->
            emit("Starting process for $value")
            delay(100)
            emit("Finished processing $value with result ${value * value}")
        }
        .collect { println(it) }
    
    println("\n===== FLOW CONTEXT =====")
    
    // Flow context demonstration
    val flowWithContext = flow {
        println("Flow started in ${Thread.currentThread().name}")
        for (i in 1..3) {
            emit(i)
            delay(100)
        }
    }
    
    // Collect with a different context
    withContext(Dispatchers.Default) {
        println("Collection started in ${Thread.currentThread().name}")
        flowWithContext.collect { value ->
            println("Collected $value in ${Thread.currentThread().name}")
        }
    }
    
    // flowOn operator changes the context of flow emission
    println("\nUsing flowOn to change emission context:")
    flow {
        println("Upstream: ${Thread.currentThread().name}")
        for (i in 1..3) {
            emit(i)
            delay(100)
        }
    }
    .flowOn(Dispatchers.IO) // Changes the context of upstream flow
    .collect { value ->
        println("Downstream received $value in ${Thread.currentThread().name}")
    }
    
    println("\n===== FLOW EXCEPTIONS =====")
    
    // Handling exceptions in flow
    try {
        flow {
            emit(1)
            emit(2)
            throw RuntimeException("Oops! Something went wrong in the flow")
            emit(3) // This won't be emitted
        }.collect { value ->
            println("Collected $value")
        }
    } catch (e: Exception) {
        println("Caught flow exception: ${e.message}")
    }
    
    // Using catch operator to handle exceptions
    println("\nUsing catch operator:")
    flow {
        emit(1)
        emit(2)
        throw RuntimeException("Error in flow!")
        emit(3) // This won't be emitted
    }
    .catch { e -> 
        println("Caught exception: ${e.message}")
        emit(999) // Emit a fallback value
    }
    .collect { value ->
        println("Collected $value")
    }
    
    println("\n===== FLOW COMPLETION =====")
    
    // Using onCompletion to handle both normal and exceptional completion
    flow {
        emit(1)
        emit(2)
        emit(3)
    }
    .onCompletion { cause ->
        if (cause != null) {
            println("Flow completed exceptionally with: ${cause.message}")
        } else {
            println("Flow completed successfully")
        }
    }
    .collect { value ->
        println("Collected $value")
    }
    
    // Flow with exception and onCompletion
    println("\nFlow with exception and onCompletion:")
    flow {
        emit(1)
        throw RuntimeException("Error in flow!")
        emit(2) // This won't be emitted
    }
    .onCompletion { cause ->
        if (cause != null) {
            println("Flow completed exceptionally with: ${cause.message}")
        } else {
            println("Flow completed successfully")
        }
    }
    .catch { e -> 
        println("Caught exception: ${e.message}")
    }
    .collect { value ->
        println("Collected $value")
    }
    
    println("\n===== FLOW BUFFERING AND CONCURRENCY =====")
    
    // Demonstrate buffer operator
    val time = measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100) // Emit every 100ms
                emit(i)
                println("Emitted $i")
            }
        }
        .buffer() // Buffer emissions without waiting for collection to complete
        .collect { value ->
            delay(300) // Process for 300ms
            println("Collected $value")
        }
    }
    println("Total time with buffer: $time ms")
    
    // Without buffer for comparison
    val timeWithoutBuffer = measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100) // Emit every 100ms
                emit(i)
                println("Emitted $i (no buffer)")
            }
        }
        .collect { value ->
            delay(300) // Process for 300ms
            println("Collected $value (no buffer)")
        }
    }
    println("Total time without buffer: $timeWithoutBuffer ms")
    
    // Demonstrate conflation
    println("\nUsing conflation to skip intermediate values:")
    flow {
        for (i in 1..5) {
            delay(100)
            emit(i)
            println("Emitted $i")
        }
    }
    .conflate() // Conflate emissions, keeping only latest value if collector is too slow
    .collect { value ->
        delay(300) // Process for 300ms
        println("Collected $value")
    }
    
    // Demonstrate collectLatest
    println("\nUsing collectLatest - cancels slow collector on new emission:")
    flow {
        for (i in 1..5) {
            delay(100)
            emit(i)
            println("Emitted $i for collectLatest")
        }
    }
    .collectLatest { value ->
        println("Collecting $value...")
        delay(250) // This is slower than emission rate
        println("Finished collecting $value") // May not be printed for all values
    }
    
    println("\n===== REAL-WORLD EXAMPLE: STOCK TICKER =====")
    
    val stockTicker = StockTicker()
    
    // Start collecting stock updates
    val job = launch {
        stockTicker.stockUpdates()
            .filter { update -> update.changePercent > 0.5 || update.changePercent < -0.5 }
            .collect { update ->
                val changeSymbol = if (update.changePercent > 0) "🔼" else "🔽"
                println("${update.timestamp} - ${update.symbol}: $${update.price} $changeSymbol ${update.changePercent}%")
            }
    }
    
    // Let it run for a while
    delay(3000)
    job.cancel()
    println("Stock ticker stopped")
    
    println("\n===== REAL-WORLD EXAMPLE: SENSOR DATA PROCESSING =====")
    
    val sensorDataProcessor = SensorDataProcessor()
    
    // Process 10 seconds of sensor data
    val processingJob = launch {
        sensorDataProcessor.processSensorData()
            .buffer(10) // Buffer up to 10 readings
            .map { 
                // Convert raw reading to temperature in Celsius
                SensorReading(it.timestamp, it.sensorId, (it.rawValue * 0.1) - 40.0)
            }
            .filter { it.value in -10.0..50.0 } // Filter out unlikely values
            .collect { reading ->
                println("Sensor ${reading.sensorId} @ ${reading.timestamp}: ${String.format("%.1f", reading.value)}°C")
            }
    }
    
    // Let it run for a while
    delay(2000)
    processingJob.cancel()
    println("Sensor processing stopped")
}

// Helper function to test different channel types
suspend fun testChannel(name: String, channel: Channel<Int>) = coroutineScope {
    println("\n--- $name Channel ---")
    
    val sender = launch {
        try {
            repeat(5) { i ->
                println("Sending $i to $name channel")
                channel.send(i)
                println("Sent $i to $name channel")
            }
        } catch (e: Exception) {
            println("Exception while sending to $name channel: ${e.message}")
        } finally {
            channel.close()
        }
    }
    
    delay(100) // Give some time for sending
    
    val receiver = launch {
        for (value in channel) {
            println("Received $value from $name channel")
            delay(50)
        }
    }
    
    joinAll(sender, receiver)
}

// Real-world example: Stock Ticker
data class StockUpdate(
    val symbol: String,
    val price: Double,
    val changePercent: Double,
    val timestamp: String
)

class StockTicker {
    private val stocks = listOf("AAPL", "GOOG", "MSFT", "AMZN", "FB")
    private val random = Random()
    private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS")
    
    // Base prices for the stocks
    private val basePrices = mapOf(
        "AAPL" to 150.0,
        "GOOG" to 2800.0,
        "MSFT" to 300.0,
        "AMZN" to 3400.0,
        "FB" to 350.0
    )
    
    // Current prices of the stocks
    private val currentPrices = basePrices.toMutableMap()
    
    // Flow that emits stock updates
    fun stockUpdates(): Flow<StockUpdate> = flow {
        while (true) {
            // Choose a random stock
            val symbol = stocks.random()
            
            // Adjust price by a small random amount
            val basePrice = basePrices[symbol]!!
            val currentPrice = currentPrices[symbol]!!
            val change = basePrice * (random.nextDouble() * 0.02 - 0.01) // -1% to +1%
            val newPrice = (currentPrice + change).coerceIn(basePrice * 0.8, basePrice * 1.2)
            currentPrices[symbol] = newPrice
            
            // Calculate percent change from base price
            val percentChange = ((newPrice - basePrice) / basePrice) * 100
            
            // Create update with formatted timestamp
            val update = StockUpdate(
                symbol,
                newPrice,
                percentChange,
                dateFormat.format(Date())
            )
            
            // Emit the update
            emit(update)
            
            // Wait a bit before the next update
            delay(100)
        }
    }
}

// Real-world example: Sensor Data Processing
data class SensorReading(
    val timestamp: String,
    val sensorId: Int,
    val value: Double
) {
    constructor(timestamp: String, sensorId: Int, rawValue: Int) : 
        this(timestamp, sensorId, rawValue.toDouble())
}

class SensorDataProcessor {
    private val random = Random()
    private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS")
    
    // Flow that simulates a stream of sensor readings
    fun processSensorData(): Flow<SensorReading> = flow {
        val sensorCount = 3
        
        while (true) {
            // Generate a reading for a random sensor
            val sensorId = random.nextInt(sensorCount)
            
            // Generate a raw sensor value (temperature sensor that returns raw ADC values)
            // Range 0-1023, representing -40°C to 62.3°C
            val rawValue = random.nextInt(1024)
            
            // Create a reading with current timestamp
            val reading = SensorReading(
                dateFormat.format(Date()),
                sensorId,
                rawValue
            )
            
            // Emit the reading
            emit(reading)
            
            // Sensors report at different rates
            delay(50L + random.nextInt(150).toLong())
        }
    }
} 