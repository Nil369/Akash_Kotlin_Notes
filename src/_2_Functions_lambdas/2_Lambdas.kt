package _2_Functions_lambdas

/**
 * This file demonstrates lambdas and higher-order functions in Kotlin
 * including various ways to create and use lambda expressions
 */
fun main() {
    println("===== BASIC LAMBDAS =====")
    
    // Lambda expression assigned to a variable
    val greet = { println("Hello, World!") }
    greet()
    
    // Lambda with parameters
    val greetPerson = { name: String -> println("Hello, $name!") }
    greetPerson("Alice")
    
    // Lambda with return value
    val sum = { a: Int, b: Int -> a + b }
    println("Sum: ${sum(5, 7)}")
    
    // Type annotation for lambdas
    val multiply: (Int, Int) -> Int = { a, b -> a * b }
    println("Product: ${multiply(4, 3)}")
    
    println("\n===== HIGHER-ORDER FUNCTIONS =====")
    
    // Function that takes a function as parameter
    processList(listOf(1, 2, 3, 4, 5)) { item ->
        println("Processing item: $item")
    }
    
    // Function that returns a function
    val adder = makeAdder(10)
    println("10 + 5 = ${adder(5)}")
    
    // Using standard higher-order functions
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
    // forEach
    println("\nForEach example:")
    numbers.forEach { println(it) }
    
    // map
    println("\nMap example:")
    val doubled = numbers.map { it * 2 }
    println("Doubled: $doubled")
    
    // filter
    println("\nFilter example:")
    val evens = numbers.filter { it % 2 == 0 }
    println("Even numbers: $evens")
    
    // reduce
    println("\nReduce example:")
    val total = numbers.reduce { acc, num -> acc + num }
    println("Sum of all numbers: $total")
    
    println("\n===== LAMBDA WITH RECEIVER =====")
    
    // Lambda with receiver
    val stringBuilder = buildString {
        append("Hello")
        append(", ")
        append("World!")
        append("\n")
        append("This is a string built with a lambda with receiver")
    }
    println(stringBuilder)
    
    println("\n===== REAL-WORLD EXAMPLE: TASK SCHEDULER =====")
    
    val taskScheduler = TaskScheduler()
    
    // Adding some tasks
    taskScheduler.addTask("Send email", { println("Email sent!") })
    
    taskScheduler.addTask("Process data", {
        println("Starting data processing...")
        Thread.sleep(100) // Simulate processing
        println("Data processing completed!")
    })
    
    taskScheduler.addDelayedTask("Clean database", 500) {
        println("Cleaning database...")
        Thread.sleep(100) // Simulate cleaning
        println("Database cleaned!")
    }
    
    // Priority task (will be executed first)
    taskScheduler.addPriorityTask("Critical backup", {
        println("Starting emergency backup...")
        Thread.sleep(100) // Simulate backup
        println("Emergency backup completed!")
    })
    
    // Run all tasks
    taskScheduler.runAllTasks()
    
    println("\n===== REAL-WORLD EXAMPLE: DATA PROCESSING PIPELINE =====")
    
    // Data processing with chained operations
    val rawData = listOf(
        "John,Doe,42,New York", 
        "Jane,Smith,38,Los Angeles",
        "Bob,Johnson,55,Chicago",
        "Alice,Brown,29,San Francisco",
        "Charlie,Williams,33,Boston"
    )
    
    // Define the processing steps
    val parser: (String) -> List<String> = { it.split(",") }
    val toUser: (List<String>) -> User = { parts ->
        User(
            firstName = parts[0],
            lastName = parts[1],
            age = parts[2].toInt(),
            city = parts[3]
        )
    }
    val ageFilter: (User) -> Boolean = { it.age >= 35 }
    val formatter: (User) -> String = { user ->
        "${user.firstName} ${user.lastName} (${user.age}) - ${user.city}"
    }
    
    // Apply processing pipeline
    val results = rawData
        .map(parser)
        .map(toUser)
        .filter(ageFilter)
        .map(formatter)
    
    println("Processing results:")
    results.forEach(::println)
    
    // Alternative approach with a custom process function
    println("\nAlternative processing approach:")
    val alternativeResults = processData(
        data = rawData,
        parser = parser,
        transformer = toUser,
        filter = ageFilter,
        formatter = formatter
    )
    alternativeResults.forEach(::println)
}

// Higher-order function that takes a function as a parameter
fun processList(items: List<Int>, processor: (Int) -> Unit) {
    for (item in items) {
        processor(item)
    }
}

// Function that returns a function
fun makeAdder(x: Int): (Int) -> Int {
    return { y -> x + y }
}

// Function with a lambda with receiver
fun buildString(builder: StringBuilder.() -> Unit): String {
    val sb = StringBuilder()
    sb.builder()
    return sb.toString()
}

// Data class for examples
data class User(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String
)

// Task Scheduler example
class TaskScheduler {
    private val regularTasks = mutableListOf<Pair<String, () -> Unit>>()
    private val priorityTasks = mutableListOf<Pair<String, () -> Unit>>()
    private val delayedTasks = mutableListOf<Triple<String, Long, () -> Unit>>()
    
    fun addTask(name: String, task: () -> Unit) {
        regularTasks.add(name to task)
        println("Added task: $name")
    }
    
    fun addPriorityTask(name: String, task: () -> Unit) {
        priorityTasks.add(name to task)
        println("Added priority task: $name")
    }
    
    fun addDelayedTask(name: String, delayMs: Long, task: () -> Unit) {
        delayedTasks.add(Triple(name, delayMs, task))
        println("Added delayed task: $name (delay: $delayMs ms)")
    }
    
    fun runAllTasks() {
        println("\nRunning all tasks...")
        
        // Run priority tasks first
        priorityTasks.forEach { (name, task) ->
            println("\nRunning priority task: $name")
            task()
        }
        
        // Run regular tasks
        regularTasks.forEach { (name, task) ->
            println("\nRunning task: $name")
            task()
        }
        
        // Run delayed tasks
        delayedTasks.forEach { (name, delay, task) ->
            println("\nRunning delayed task: $name after $delay ms")
            Thread.sleep(delay)
            task()
        }
        
        println("\nAll tasks completed!")
    }
}

// Custom processing pipeline function
fun <T, R, U> processData(
    data: List<T>,
    parser: (T) -> R,
    transformer: (R) -> U,
    filter: (U) -> Boolean,
    formatter: (U) -> String
): List<String> {
    return data
        .map { parser(it) }
        .map { transformer(it) }
        .filter { filter(it) }
        .map { formatter(it) }
} 