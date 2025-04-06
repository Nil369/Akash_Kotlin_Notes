# Functional Programming in Kotlin

## Pure Functions

### Basic Pure Functions
```kotlin
// Pure function - same input always produces same output
fun add(a: Int, b: Int): Int = a + b

// Pure function with multiple parameters
fun calculateArea(width: Double, height: Double): Double = width * height

// Pure function with type parameters
fun <T> identity(value: T): T = value
```

### Real-world Example: Financial Calculations
```kotlin
object FinancialCalculator {
    // Pure function for compound interest
    fun calculateCompoundInterest(
        principal: Double,
        rate: Double,
        time: Int,
        compoundsPerYear: Int = 12
    ): Double {
        return principal * (1 + rate / compoundsPerYear).pow(compoundsPerYear * time)
    }

    // Pure function for loan payment
    fun calculateMonthlyPayment(
        loanAmount: Double,
        annualInterestRate: Double,
        loanTermYears: Int
    ): Double {
        val monthlyRate = annualInterestRate / 12 / 100
        val numberOfPayments = loanTermYears * 12
        return loanAmount * (monthlyRate * (1 + monthlyRate).pow(numberOfPayments)) /
               ((1 + monthlyRate).pow(numberOfPayments) - 1)
    }
}
```

## Immutability

### Immutable Data Structures
```kotlin
// Immutable list
val numbers = listOf(1, 2, 3, 4, 5)

// Create new list with modifications
val doubled = numbers.map { it * 2 }
val filtered = numbers.filter { it % 2 == 0 }

// Immutable map
val user = mapOf(
    "name" to "John",
    "age" to 30
)

// Create new map with modifications
val updatedUser = user + ("city" to "New York")
```

### Real-world Example: User Profile Management
```kotlin
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val preferences: Map<String, Any>
)

class UserProfileManager {
    private val profiles = mutableMapOf<String, UserProfile>()

    fun updateUserPreferences(
        userId: String,
        newPreferences: Map<String, Any>
    ): UserProfile? {
        val currentProfile = profiles[userId] ?: return null
        return currentProfile.copy(preferences = newPreferences).also {
            profiles[userId] = it
        }
    }

    fun addUserInterest(userId: String, interest: String): UserProfile? {
        val currentProfile = profiles[userId] ?: return null
        val currentInterests = currentProfile.preferences["interests"] as? List<String> ?: emptyList()
        val updatedPreferences = currentProfile.preferences + 
            ("interests" to (currentInterests + interest))
        return currentProfile.copy(preferences = updatedPreferences).also {
            profiles[userId] = it
        }
    }
}
```

## Higher-Order Functions

### Function Composition
```kotlin
// Function composition
fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { f(g(it)) }

// Usage
val addOne = { x: Int -> x + 1 }
val multiplyByTwo = { x: Int -> x * 2 }
val addOneThenMultiplyByTwo = compose(multiplyByTwo, addOne)
println(addOneThenMultiplyByTwo(5))  // 12
```

### Real-world Example: Data Processing Pipeline
```kotlin
class DataProcessor<T> {
    private val processors = mutableListOf<(T) -> T>()

    fun addProcessor(processor: (T) -> T) {
        processors.add(processor)
    }

    fun process(data: T): T {
        return processors.fold(data) { acc, processor -> processor(acc) }
    }
}

// Usage
val numberProcessor = DataProcessor<Int>()
numberProcessor.addProcessor { it * 2 }  // Double
numberProcessor.addProcessor { it + 1 }  // Add 1
numberProcessor.addProcessor { it * 3 }  // Triple

val result = numberProcessor.process(5)  // ((5 * 2 + 1) * 3) = 33
```

## Recursion

### Basic Recursion
```kotlin
// Factorial using recursion
fun factorial(n: Int): Int = when {
    n < 0 -> throw IllegalArgumentException("Factorial is not defined for negative numbers")
    n == 0 -> 1
    else -> n * factorial(n - 1)
}

// Fibonacci using recursion
fun fibonacci(n: Int): Int = when {
    n < 0 -> throw IllegalArgumentException("Fibonacci is not defined for negative numbers")
    n <= 1 -> n
    else -> fibonacci(n - 1) + fibonacci(n - 2)
}
```

### Tail Recursion
```kotlin
// Tail recursive factorial
tailrec fun factorialTailRec(n: Int, acc: Int = 1): Int = when {
    n < 0 -> throw IllegalArgumentException("Factorial is not defined for negative numbers")
    n == 0 -> acc
    else -> factorialTailRec(n - 1, n * acc)
}

// Tail recursive list sum
tailrec fun sumList(list: List<Int>, acc: Int = 0): Int = when {
    list.isEmpty() -> acc
    else -> sumList(list.drop(1), acc + list.first())
}
```

### Real-world Example: File System Traversal
```kotlin
sealed class FileSystemNode {
    data class File(val name: String, val size: Long) : FileSystemNode()
    data class Directory(val name: String, val children: List<FileSystemNode>) : FileSystemNode()
}

class FileSystemAnalyzer {
    tailrec fun calculateTotalSize(node: FileSystemNode, acc: Long = 0): Long = when (node) {
        is FileSystemNode.File -> acc + node.size
        is FileSystemNode.Directory -> {
            node.children.fold(acc) { total, child ->
                calculateTotalSize(child, total)
            }
        }
    }

    fun findLargestFile(node: FileSystemNode): FileSystemNode.File? = when (node) {
        is FileSystemNode.File -> node
        is FileSystemNode.Directory -> {
            node.children
                .mapNotNull { findLargestFile(it) }
                .maxByOrNull { it.size }
        }
    }
}

// Usage
val fileSystem = FileSystemNode.Directory(
    "root",
    listOf(
        FileSystemNode.File("file1.txt", 100),
        FileSystemNode.Directory(
            "docs",
            listOf(
                FileSystemNode.File("doc1.pdf", 1000),
                FileSystemNode.File("doc2.pdf", 2000)
            )
        )
    )
)

val analyzer = FileSystemAnalyzer()
val totalSize = analyzer.calculateTotalSize(fileSystem)  // 3100
val largestFile = analyzer.findLargestFile(fileSystem)   // doc2.pdf (2000)
```

## Practice Exercises

1. Implement a pure function to calculate the greatest common divisor (GCD)
2. Create an immutable stack data structure using lists
3. Write a higher-order function that implements function memoization
4. Implement a tail-recursive function to reverse a list
5. Create a functional implementation of a binary search tree

Remember:
- Pure functions should have no side effects
- Use immutable data structures when possible
- Leverage higher-order functions for code reuse
- Use tail recursion for better performance
- Consider using sequences for lazy evaluation
- Use function composition to build complex operations 