# Kotlin Standard Library

## Core Functions

### Let, Run, With, Apply, Also

```kotlin
// let - converts an object to a different type/result
val name = "John Doe"
val nameLength = name.let { 
    println("Processing name: $it")
    it.length 
}  // Returns length (8)

// run - for object configuration and computing a result
val user = User().run {
    firstName = "John"
    lastName = "Doe"
    email = "john.doe@example.com"
    this  // Return the configured object
}

// with - for object configuration, non-extension function
val message = with(StringBuilder()) {
    append("Hello, ")
    append("World!")
    append(" How are you?")
    toString()  // Return result
}

// apply - for object configuration, returns the object
val user = User().apply {
    firstName = "John"
    lastName = "Doe"
    email = "john.doe@example.com"
}  // Returns the User object

// also - for side effects, returns the object
val numbers = mutableListOf(1, 2, 3)
numbers.also { println("List before: $it") }
    .add(4)
numbers.also { println("List after: $it") }
```

### Extensions on Built-in Types

```kotlin
// String extensions
"Hello, World!".reversed()        // "!dlroW ,olleH"
"  trim me  ".trim()             // "trim me"
"kotlin".capitalize()            // "Kotlin"
"one,two,three".split(",")       // [one, two, three]

// Collection extensions
listOf(1, 2, 3, 4).sum()          // 10
listOf(1, 2, 3, 4).average()      // 2.5
listOf(1, 2, 3, 4, 5).chunked(2)  // [[1, 2], [3, 4], [5]]
setOf(1, 2, 3).intersect(setOf(2, 3, 4))  // [2, 3]

// Number extensions
42.coerceIn(1, 100)    // 42
42.coerceIn(50, 100)   // 50
(-10).absoluteValue    // 10
5.0.pow(2.0)           // 25.0
```

## Collections Operations

### Advanced Collection Functions

```kotlin
// Transformations
val doubled = listOf(1, 2, 3).map { it * 2 }  // [2, 4, 6]
val flat = listOf(listOf(1, 2), listOf(3, 4)).flatten()  // [1, 2, 3, 4]
val flatMapped = listOf("abc", "def").flatMap { it.toList() }  // [a, b, c, d, e, f]

// Filtering
val evens = (1..10).filter { it % 2 == 0 }  // [2, 4, 6, 8, 10]
val notDivisibleBy3 = (1..10).filterNot { it % 3 == 0 }  // [1, 2, 4, 5, 7, 8, 10]
val nonNullElements = listOf("a", null, "b").filterNotNull()  // [a, b]

// Grouping
val grouped = listOf(1, 2, 3, 4, 5).groupBy { it % 2 == 0 }
// {false=[1, 3, 5], true=[2, 4]}

val wordLengths = listOf("one", "two", "three").associate { it to it.length }
// {one=3, two=3, three=5}

// Aggregation
val sum = listOf(1, 2, 3, 4).fold(0) { acc, next -> acc + next }  // 10
val concatenated = listOf("a", "b", "c").reduce { acc, next -> "$acc$next" }  // "abc"
val numbers = (1..100).runningFold(0) { acc, next -> acc + next }
// [0, 1, 3, 6, 10, 15, ...]
```

### Sequences

```kotlin
// Creating sequences
val seq1 = sequenceOf(1, 2, 3, 4, 5)
val seq2 = listOf(1, 2, 3, 4, 5).asSequence()
val seq3 = generateSequence(1) { it + 1 }.take(5)  // Infinite sequence, taking first 5

// Lazy evaluation with sequences
val result = listOf(1, 2, 3, 4, 5)
    .asSequence()
    .map { 
        println("Mapping $it")
        it * it 
    }
    .filter { 
        println("Filtering $it")
        it > 10 
    }
    .first()  // Only processes elements until a match is found

// Fibonacci sequence
val fibonacci = generateSequence(Pair(0, 1)) { (a, b) -> 
    Pair(b, a + b) 
}.map { it.first }
val first10Fibonacci = fibonacci.take(10).toList()  // [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
```

## Real-world Examples

### 1. Text Processing System

```kotlin
class TextProcessor {
    fun countWords(text: String): Int {
        return text.split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .count()
    }

    fun findMostFrequentWords(text: String, topN: Int = 10): List<Pair<String, Int>> {
        return text.lowercase()
            .replace(Regex("[^a-z\\s]"), "")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(topN)
    }

    fun summarizeText(text: String, sentenceCount: Int = 3): String {
        val sentences = text.split(Regex("[.!?]"))
            .filter { it.isNotBlank() }
            .map { it.trim() }
        
        val wordFrequency = findMostFrequentWords(text, 20).toMap()
        
        return sentences
            .map { sentence -> 
                val score = sentence.split(Regex("\\s+"))
                    .sumOf { wordFrequency[it.lowercase()] ?: 0 }
                sentence to score
            }
            .sortedByDescending { it.second }
            .take(sentenceCount)
            .map { it.first }
            .joinToString(". ") + "."
    }
}

// Usage
val processor = TextProcessor()
val text = "Kotlin is a modern programming language. It has many features that make it safer and more concise than Java. Kotlin is developed by JetBrains."
val wordCount = processor.countWords(text)  // 22
val frequentWords = processor.findMostFrequentWords(text)  // [(kotlin, 2), (it, 2), ...]
val summary = processor.summarizeText(text, 2)
```

### 2. Data Analysis System

```kotlin
class DataAnalyzer {
    fun calculateStatistics(data: List<Double>): Statistics {
        if (data.isEmpty()) throw IllegalArgumentException("Data cannot be empty")
        
        val sortedData = data.sorted()
        val count = data.size
        val sum = data.sum()
        val mean = sum / count
        val median = if (count % 2 == 0) {
            (sortedData[count / 2 - 1] + sortedData[count / 2]) / 2
        } else {
            sortedData[count / 2]
        }
        val variance = data.map { (it - mean).pow(2) }.average()
        val stdDev = sqrt(variance)
        val min = sortedData.first()
        val max = sortedData.last()
        
        return Statistics(count, mean, median, stdDev, min, max)
    }
    
    fun detectOutliers(data: List<Double>): List<Double> {
        val stats = calculateStatistics(data)
        val q1 = data.sorted()[data.size / 4]
        val q3 = data.sorted()[data.size * 3 / 4]
        val iqr = q3 - q1
        val lowerBound = q1 - 1.5 * iqr
        val upperBound = q3 + 1.5 * iqr
        
        return data.filter { it < lowerBound || it > upperBound }
    }
    
    fun groupDataByRanges(data: List<Double>, binSize: Double): Map<String, List<Double>> {
        if (data.isEmpty()) return emptyMap()
        
        val min = data.minOrNull()!!
        val max = data.maxOrNull()!!
        
        return data.groupBy { value ->
            val binIndex = ((value - min) / binSize).toInt()
            val binStart = min + binIndex * binSize
            val binEnd = binStart + binSize
            "[$binStart, $binEnd)"
        }
    }
}

data class Statistics(
    val count: Int,
    val mean: Double,
    val median: Double,
    val standardDeviation: Double,
    val min: Double,
    val max: Double
)

// Usage
val analyzer = DataAnalyzer()
val data = listOf(12.5, 10.0, 14.2, 15.7, 8.5, 9.0, 11.3, 13.8, 9.5, 11.0)
val stats = analyzer.calculateStatistics(data)
val outliers = analyzer.detectOutliers(data)
val grouped = analyzer.groupDataByRanges(data, 2.0)
```

### 3. Functional Transaction Processor

```kotlin
class TransactionProcessor {
    fun processTransactions(transactions: List<Transaction>): TransactionSummary {
        val totalAmount = transactions.sumOf { it.amount }
        
        val byCategory = transactions
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
        
        val byDate = transactions
            .groupBy { it.date.toLocalDate() }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            
        val largestTransaction = transactions.maxByOrNull { it.amount }
        
        val transactionsByMerchant = transactions
            .groupBy { it.merchant }
            .mapValues { entry -> entry.value.map { it.amount }.average() }
        
        return TransactionSummary(
            totalAmount = totalAmount,
            categorySummary = byCategory,
            dailySummary = byDate,
            largestTransaction = largestTransaction,
            averageByMerchant = transactionsByMerchant
        )
    }
    
    fun findMerchantsWithHighestSpending(
        transactions: List<Transaction>, 
        topN: Int = 5
    ): List<Pair<String, Double>> {
        return transactions
            .groupBy { it.merchant }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
            .take(topN)
    }
}

data class Transaction(
    val id: String,
    val date: LocalDateTime,
    val merchant: String,
    val category: String,
    val amount: Double
)

data class TransactionSummary(
    val totalAmount: Double,
    val categorySummary: Map<String, Double>,
    val dailySummary: Map<LocalDate, Double>,
    val largestTransaction: Transaction?,
    val averageByMerchant: Map<String, Double>
)

// Usage
val transactions = listOf(
    Transaction("1", LocalDateTime.now(), "Amazon", "Shopping", 75.99),
    Transaction("2", LocalDateTime.now(), "Grocery Store", "Food", 32.50),
    Transaction("3", LocalDateTime.now(), "Amazon", "Electronics", 250.00)
)
val processor = TransactionProcessor()
val summary = processor.processTransactions(transactions)
val topMerchants = processor.findMerchantsWithHighestSpending(transactions)
```

## Practice Exercises

1. Create a text analyzer that finds the most common n-grams in a document
2. Build a contact manager that enables filtering and grouping by various attributes
3. Implement a time-series data processor with statistics and trend analysis
4. Create a functional todo list manager with task filtering and grouping
5. Build a data transformation pipeline using sequences

Remember:
- Choose appropriate scope functions for your use case
- Use sequences for large data sets or computation-heavy operations
- Prefer immutable collections and functional operations
- Leverage extension functions for clean, readable code
- Use the standard library functions instead of reinventing the wheel 