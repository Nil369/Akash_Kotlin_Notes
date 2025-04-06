package _4_Collections

/**
 * This file demonstrates functional programming concepts in Kotlin
 * including higher-order functions, lambdas, and functional operations.
 */
fun main() {
    println("===== BASIC FUNCTIONAL CONCEPTS =====")
    
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println("Original list: $numbers")
    
    // Mapping: transform each element
    val squared = numbers.map { it * it }
    println("Squared: $squared")
    
    // Filtering: keep elements that satisfy a predicate
    val evens = numbers.filter { it % 2 == 0 }
    println("Even numbers: $evens")
    
    // Chaining operations
    val sumOfSquaredEvens = numbers
        .filter { it % 2 == 0 }
        .map { it * it }
        .sum()
    println("Sum of squared even numbers: $sumOfSquaredEvens")
    
    println("\n===== FUNCTION COMPOSITION =====")
    
    // Composing functions
    val addOne = { x: Int -> x + 1 }
    val multiplyByTwo = { x: Int -> x * 2 }
    
    // Manual composition
    val addOneThenMultiplyByTwo = { x: Int -> multiplyByTwo(addOne(x)) }
    println("Using composed function: 5 -> ${addOneThenMultiplyByTwo(5)}")
    
    // Let's define a compose function (for demonstration)
    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
        return { x -> f(g(x)) }
    }
    
    val composed = compose(multiplyByTwo, addOne)
    println("Using compose helper: 5 -> ${composed(5)}")
    
    println("\n===== HIGHER-ORDER FUNCTIONS =====")
    
    // Function that takes a function
    fun transformAndPrint(numbers: List<Int>, transform: (Int) -> Int) {
        val transformed = numbers.map(transform)
        println("Transformed: $transformed")
    }
    
    transformAndPrint(numbers, { it * it }) // Pass lambda
    transformAndPrint(numbers) { it + 10 }  // Trailing lambda syntax
    
    // Function that returns a function
    fun createMultiplier(factor: Int): (Int) -> Int {
        return { x -> x * factor }
    }
    
    val timesThree = createMultiplier(3)
    val timesFive = createMultiplier(5)
    
    println("Using returned functions:")
    println("5 * 3 = ${timesThree(5)}")
    println("5 * 5 = ${timesFive(5)}")
    
    println("\n===== FUNCTIONAL COLLECTION OPERATIONS =====")
    
    // forEach: execute an action for each element
    println("forEach:")
    numbers.forEach { print("$it ") }
    println()
    
    // forEachIndexed: execute an action for each element with its index
    println("forEachIndexed:")
    numbers.forEachIndexed { index, value -> println("numbers[$index] = $value") }
    
    // map: transform each element
    val doubled = numbers.map { it * 2 }
    println("Doubled: $doubled")
    
    // mapIndexed: transform each element using both the element and its index
    val indexPlusValue = numbers.mapIndexed { index, value -> index + value }
    println("Index + value: $indexPlusValue")
    
    // flatMap: transform each element to a list and flatten the results
    val nestedLists = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
    val flattened = nestedLists.flatMap { it }
    println("Flattened: $flattened")
    
    // reduce: combine all elements into a single value
    val sum = numbers.reduce { acc, i -> acc + i }
    println("Sum using reduce: $sum")
    
    // fold: similar to reduce but with an initial value
    val sumWithInitial = numbers.fold(100) { acc, i -> acc + i }
    println("Sum with initial 100: $sumWithInitial")
    
    // groupBy: group elements by a key
    val oddEvenGroups = numbers.groupBy { if (it % 2 == 0) "even" else "odd" }
    println("Grouped by odd/even: $oddEvenGroups")
    
    // associate: create a map from elements
    val numberMap = numbers.associate { it to it.toString() }
    println("Number map: $numberMap")
    
    // zip: combine two collections
    val letters = listOf("a", "b", "c", "d", "e")
    val zipped = numbers.zip(letters)
    println("Zipped with letters: $zipped")
    
    // take, drop, first, last, etc.
    println("First 3 numbers: ${numbers.take(3)}")
    println("Without first 3 numbers: ${numbers.drop(3)}")
    println("First even number: ${numbers.first { it % 2 == 0 }}")
    println("Last odd number: ${numbers.last { it % 2 != 0 }}")
    
    println("\n===== SEQUENCES =====")
    
    // Sequences are evaluated lazily
    val sequenceResults = numbers.asSequence()
        .filter { 
            println("Filtering $it")
            it % 2 == 0 
        }
        .map { 
            println("Mapping $it")
            it * it 
        }
        .take(2)
        .toList()
    
    println("Sequence results (notice the execution order): $sequenceResults")
    
    // Comparing sequence with regular collection operations
    val regularStart = System.currentTimeMillis()
    val regularResult = (1..1000000)
        .filter { it % 2 == 0 }
        .map { it * it }
        .take(5)
        .toList()
    val regularTime = System.currentTimeMillis() - regularStart
    
    val sequenceStart = System.currentTimeMillis()
    val sequenceResult = (1..1000000).asSequence()
        .filter { it % 2 == 0 }
        .map { it * it }
        .take(5)
        .toList()
    val sequenceTime = System.currentTimeMillis() - sequenceStart
    
    println("\nPerformance comparison:")
    println("Regular collections took $regularTime ms: $regularResult")
    println("Sequences took $sequenceTime ms: $sequenceResult")
    
    println("\n===== REAL-WORLD EXAMPLE: DATA PROCESSING PIPELINE =====")
    
    // Define a data class for our data
    data class SalesRecord(
        val id: Int,
        val product: String,
        val category: String,
        val price: Double,
        val quantity: Int,
        val date: String,
        val isDiscounted: Boolean
    )
    
    // Sample sales data
    val sales = listOf(
        SalesRecord(1, "Laptop", "Electronics", 1200.0, 1, "2023-01-15", false),
        SalesRecord(2, "Smartphone", "Electronics", 800.0, 2, "2023-01-16", true),
        SalesRecord(3, "T-shirt", "Clothing", 25.0, 5, "2023-01-16", false),
        SalesRecord(4, "Jeans", "Clothing", 60.0, 2, "2023-01-17", true),
        SalesRecord(5, "Book", "Books", 15.0, 3, "2023-01-18", false),
        SalesRecord(6, "Tablet", "Electronics", 300.0, 1, "2023-01-18", true),
        SalesRecord(7, "Socks", "Clothing", 10.0, 6, "2023-01-19", false),
        SalesRecord(8, "Monitor", "Electronics", 250.0, 2, "2023-01-20", true),
        SalesRecord(9, "Novel", "Books", 20.0, 4, "2023-01-21", false),
        SalesRecord(10, "Keyboard", "Electronics", 45.0, 3, "2023-01-22", true)
    )
    
    // Analysis 1: Total revenue by category
    val revenueByCategory = sales
        .groupBy { it.category }
        .mapValues { (_, records) -> 
            records.sumOf { it.price * it.quantity }
        }
    
    println("\nTotal revenue by category:")
    revenueByCategory.forEach { (category, revenue) ->
        println("$category: $${String.format("%.2f", revenue)}")
    }
    
    // Analysis 2: Top selling products by quantity
    val topSellingProducts = sales
        .groupBy { it.product }
        .mapValues { (_, records) -> records.sumOf { it.quantity } }
        .entries
        .sortedByDescending { it.value }
        .take(3)
    
    println("\nTop 3 selling products by quantity:")
    topSellingProducts.forEachIndexed { index, (product, quantity) ->
        println("${index + 1}. $product: $quantity units")
    }
    
    // Analysis 3: Daily revenue
    val dailyRevenue = sales
        .groupBy { it.date }
        .mapValues { (_, records) -> 
            records.sumOf { it.price * it.quantity }
        }
        .toList()
        .sortedBy { it.first }
    
    println("\nDaily revenue:")
    dailyRevenue.forEach { (date, revenue) ->
        println("$date: $${String.format("%.2f", revenue)}")
    }
    
    // Analysis 4: Average price by category
    val avgPriceByCategory = sales
        .groupBy { it.category }
        .mapValues { (_, records) -> 
            records.map { it.price }.average()
        }
    
    println("\nAverage price by category:")
    avgPriceByCategory.forEach { (category, avgPrice) ->
        println("$category: $${String.format("%.2f", avgPrice)}")
    }
    
    // Analysis 5: Revenue from discounted vs. non-discounted items
    val (discounted, nonDiscounted) = sales.partition { it.isDiscounted }
    val discountedRevenue = discounted.sumOf { it.price * it.quantity }
    val nonDiscountedRevenue = nonDiscounted.sumOf { it.price * it.quantity }
    
    println("\nRevenue breakdown:")
    println("Discounted items: $${String.format("%.2f", discountedRevenue)}")
    println("Non-discounted items: $${String.format("%.2f", nonDiscountedRevenue)}")
    println("Percentage from discounted items: ${String.format("%.1f", discountedRevenue / (discountedRevenue + nonDiscountedRevenue) * 100)}%")
    
    // Analysis 6: Most valuable customers (if we had customer data)
    // For demo purposes, let's use the sale ID as a dummy customer ID
    val customerValue = sales
        .groupBy { it.id % 3 } // Group by dummy customer ID (0, 1, or 2)
        .mapValues { (_, records) -> 
            records.sumOf { it.price * it.quantity }
        }
        .entries
        .sortedByDescending { it.value }
    
    println("\nCustomer value (demo):")
    customerValue.forEachIndexed { index, (customerId, value) ->
        println("Customer ${index + 1} (ID: $customerId): $${String.format("%.2f", value)}")
    }
    
    println("\n===== REAL-WORLD EXAMPLE: FUNCTIONAL TEXT PROCESSING =====")
    
    val text = """
        Functional programming is a programming paradigm where programs are constructed
        by applying and composing functions. It is a declarative programming paradigm
        in which function definitions are trees of expressions that map values to other
        values, rather than a sequence of imperative statements which update the running
        state of the program.
        
        In functional programming, functions are treated as first-class citizens, meaning
        that they can be bound to names (including local identifiers), passed as arguments,
        and returned from other functions, just as any other data type can. This allows
        programs to be written in a declarative and composable style, where small functions
        are combined in a modular manner.
        
        Functional programming is sometimes treated as synonymous with purely functional
        programming, a subset of functional programming which treats all functions as
        deterministic mathematical functions, or pure functions. When a pure function is
        called with some given arguments, it will always return the same result, and cannot
        be affected by any mutable state or other side effects.
    """.trimIndent()
    
    // Word count
    val wordCount = text.split(Regex("\\s+")).size
    println("\nWord count: $wordCount")
    
    // Character count (excluding whitespace)
    val charCount = text.filter { !it.isWhitespace() }.length
    println("Character count (excluding whitespace): $charCount")
    
    // Word frequency
    val wordFrequency = text.split(Regex("\\s+|\\n|,|\\.|\\(|\\)"))
        .filter { it.isNotEmpty() }
        .groupBy { it.lowercase() }
        .mapValues { it.value.size }
        .entries
        .sortedByDescending { it.value }
        .take(10)
    
    println("\nTop 10 most frequent words:")
    wordFrequency.forEachIndexed { index, (word, count) ->
        println("${index + 1}. $word: $count occurrences")
    }
    
    // Sentence count
    val sentenceCount = text.split(Regex("[.!?]\\s")).size
    println("\nSentence count: $sentenceCount")
    
    // Average words per sentence
    val sentences = text.split(Regex("[.!?]\\s"))
    val wordsPerSentence = sentences.map { it.split(Regex("\\s+")).size }
    val avgWordsPerSentence = wordsPerSentence.average()
    println("Average words per sentence: ${String.format("%.1f", avgWordsPerSentence)}")
    
    // Word length distribution
    val wordLengths = text.split(Regex("\\s+|\\n|,|\\.|\\(|\\)"))
        .filter { it.isNotEmpty() }
        .map { it.length }
        .groupBy { it }
        .mapValues { it.value.size }
        .toList()
        .sortedBy { it.first }
    
    println("\nWord length distribution:")
    wordLengths.forEach { (length, count) ->
        println("$length characters: $count words")
    }
    
    // Find paragraphs
    val paragraphs = text.split("\n\n")
    println("\nNumber of paragraphs: ${paragraphs.size}")
    
    // Check if text contains specific words
    val keywords = listOf("functional", "programming", "pure", "functions", "declarative")
    val keywordPresence = keywords.associateWith { keyword ->
        text.split(Regex("\\s+|\\n|,|\\.|\\(|\\)"))
            .filter { it.isNotEmpty() }
            .count { it.lowercase() == keyword }
    }
    
    println("\nKeyword presence:")
    keywordPresence.forEach { (keyword, count) ->
        println("'$keyword': $count occurrences")
    }
} 