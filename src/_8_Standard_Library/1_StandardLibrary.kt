package _8_Standard_Library

import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * This file demonstrates the Kotlin standard library features and utilities
 * including collections, scope functions, time measurement, and more.
 */
fun main() {
    println("===== SCOPE FUNCTIONS =====")
    
    // let - execute a block with the object as 'it'
    val letResult = "Hello".let {
        println("The string is: $it")
        it.uppercase() // The last expression is returned
    }
    println("let result: $letResult")
    
    // run - execute a block with the object as 'this'
    val runResult = "Hello".run {
        println("The string is: $this")
        this.uppercase() // The last expression is returned
    }
    println("run result: $runResult")
    
    // with - non-extension version of run
    val withResult = with("Hello") {
        println("The string is: $this")
        this.uppercase() // The last expression is returned
    }
    println("with result: $withResult")
    
    // apply - execute a block with the object as 'this' and return the object itself
    val applyResult = StringBuilder("Hello").apply {
        append(" World")
        append("!")
    }
    println("apply result: $applyResult")
    
    // also - execute a block with the object as 'it' and return the object itself
    val alsoResult = "Hello".also {
        println("The string is: $it")
    }
    println("also result: $alsoResult")
    
    // Practical example: file operations with scope functions
    File("temp.txt").apply {
        // Create a new file
        createNewFile()
        // Write some content
        writeText("Hello, Kotlin!")
        // Append more content
        appendText("\nScope functions are great!")
    }.also {
        // Print file info
        println("\nCreated file: ${it.absolutePath}")
        println("File size: ${it.length()} bytes")
        // Read and print the content
        println("Content: ${it.readText()}")
    }.let {
        // Delete the file and return the result of deletion
        it.delete()
    }.also {
        // Print the result of deletion
        println("File deleted: $it")
    }
    
    println("\n===== COLLECTION OPERATIONS =====")
    
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
    // Filtering
    val evenNumbers = numbers.filter { it % 2 == 0 }
    println("Even numbers: $evenNumbers")
    
    // Mapping
    val squared = numbers.map { it * it }
    println("Squared numbers: $squared")
    
    // FlatMap
    val pairs = listOf(Pair(1, 2), Pair(3, 4), Pair(5, 6))
    val flattened = pairs.flatMap { listOf(it.first, it.second) }
    println("Flattened pairs: $flattened")
    
    // Reducing
    val sum = numbers.reduce { acc, i -> acc + i }
    println("Sum using reduce: $sum")
    
    // Folding (with initial value)
    val sumWithInitial = numbers.fold(100) { acc, i -> acc + i }
    println("Sum with initial 100: $sumWithInitial")
    
    // Grouping
    val grouped = numbers.groupBy { if (it % 2 == 0) "even" else "odd" }
    println("Grouped by even/odd: $grouped")
    
    // Chunking
    val chunks = numbers.chunked(3)
    println("Chunked by 3: $chunks")
    
    // Windowing (sliding window)
    val windows = numbers.windowed(3, 1)
    println("Windows of size 3, step 1: $windows")
    
    // Zipping
    val chars = listOf('a', 'b', 'c', 'd', 'e')
    val zipped = numbers.zip(chars)
    println("Zipped with chars: $zipped")
    
    // Partition
    val (evens, odds) = numbers.partition { it % 2 == 0 }
    println("Partitioned - evens: $evens, odds: $odds")
    
    // Associate
    val numberMap = numbers.associateWith { it * it }
    println("Associated with squares: $numberMap")
    
    // Find operations
    println("First even number: ${numbers.first { it % 2 == 0 }}")
    println("Last odd number: ${numbers.last { it % 2 != 0 }}")
    println("Find number > 5: ${numbers.find { it > 5 }}")
    
    // Count operations
    println("Count of even numbers: ${numbers.count { it % 2 == 0 }}")
    
    // Any, all, none
    println("Any number > 9?: ${numbers.any { it > 9 }}")
    println("All numbers < 11?: ${numbers.all { it < 11 }}")
    println("None are negative?: ${numbers.none { it < 0 }}")
    
    println("\n===== STRING UTILITIES =====")
    
    val sampleText = "  Kotlin Standard Library is great!  "
    
    // Trimming
    println("Trimmed: '${sampleText.trim()}'")
    println("Trim start: '${sampleText.trimStart()}'")
    println("Trim end: '${sampleText.trimEnd()}'")
    
    // Padding
    println("Padded to 40: '${sampleText.trim().padEnd(40, '*')}'")
    
    // Case conversion
    println("Uppercase: '${sampleText.uppercase()}'")
    println("Lowercase: '${sampleText.lowercase()}'")
    println("Capitalized: '${sampleText.trim().replaceFirstChar { it.uppercase() }}'")
    
    // Replacements
    println("Replace 'great' with 'awesome': '${sampleText.replace("great", "awesome")}'")
    
    // Regular expressions
    val regex = Regex("\\w+")
    val words = regex.findAll(sampleText).map { it.value }.toList()
    println("Words using regex: $words")
    
    // String comparison
    val str1 = "apple"
    val str2 = "banana"
    println("str1 compared to str2: ${str1.compareTo(str2)}")
    println("str1 equals str2 (ignore case): ${str1.equals(str2, ignoreCase = true)}")
    
    // String operations
    println("First word: ${sampleText.trim().substringBefore(' ')}")
    println("Last word: ${sampleText.trim().substringAfterLast(' ')}")
    
    // String template formatting
    val name = "Alice"
    val age = 30
    val formatted = "Name: $name, Age: $age, Next year: ${age + 1}"
    println("Formatted using template: '$formatted'")
    
    println("\n===== MATH OPERATIONS =====")
    
    // Math constants
    println("π (pi): ${PI}")
    println("e: ${E}")
    
    // Basic operations
    println("Square root of 16: ${sqrt(16.0)}")
    println("4 raised to power 3: ${4.0.pow(3)}")
    println("Absolute value of -5: ${abs(-5)}")
    
    // Rounding
    println("Round 3.7: ${round(3.7)}")
    println("Floor 3.7: ${floor(3.7)}")
    println("Ceiling 3.7: ${ceil(3.7)}")
    println("Truncate 3.7: ${truncate(3.7)}")
    
    // Min and max
    println("Min of 5 and 10: ${min(5, 10)}")
    println("Max of 5 and 10: ${max(5, 10)}")
    println("5 coerced between 10 and 20: ${5.coerceIn(10, 20)}")
    
    // Trigonometric functions
    println("Sine of 90° (π/2 radians): ${sin(PI / 2)}")
    println("Cosine of 0°: ${cos(0.0)}")
    println("Tangent of 45° (π/4 radians): ${tan(PI / 4)}")
    
    // Logarithms
    println("Natural log of e: ${ln(E)}")
    println("Log base 10 of 100: ${log10(100.0)}")
    println("Log base 2 of 8: ${log2(8.0)}")
    
    println("\n===== DATE AND TIME =====")
    
    // Current date and time
    val now = LocalDateTime.now()
    println("Current date and time: $now")
    
    // Formatting dates
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    println("Formatted date and time: ${now.format(formatter)}")
    
    // Parsing dates
    val dateString = "2023-01-15"
    val parsedDate = LocalDate.parse(dateString)
    println("Parsed date: $parsedDate")
    
    // Date operations
    val tomorrow = LocalDate.now().plusDays(1)
    println("Tomorrow: $tomorrow")
    
    val nextMonth = LocalDate.now().plusMonths(1)
    println("Next month: $nextMonth")
    
    val lastYear = LocalDate.now().minusYears(1)
    println("Last year: $lastYear")
    
    // Java Date (legacy)
    val javaDate = Date()
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    println("Java formatted date: ${simpleDateFormat.format(javaDate)}")
    
    println("\n===== RANDOM UTILITIES =====")
    
    // Random numbers
    val random = Random
    println("Random int: ${random.nextInt()}")
    println("Random int between 1 and 100: ${random.nextInt(1, 101)}")
    println("Random double: ${random.nextDouble()}")
    println("Random boolean: ${random.nextBoolean()}")
    
    // Random elements from collections
    val fruits = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry")
    println("Random fruit: ${fruits.random()}")
    println("3 random fruits: ${fruits.shuffled().take(3)}")
    
    // Generating sequences
    val randomNumbers = generateSequence { random.nextInt(100) }
    println("10 random numbers: ${randomNumbers.take(10).toList()}")
    
    println("\n===== MEASUREMENTS =====")
    
    // Measure execution time
    val time = measureTimeMillis {
        // Some operation to measure
        var sum = 0L
        for (i in 1..1_000_000) {
            sum += i
        }
        println("Sum of numbers from 1 to 1,000,000: $sum")
    }
    println("Execution time: $time ms")
    
    println("\n===== MISCELLANEOUS UTILITIES =====")
    
    // Lazy initialization
    val lazyValue: String by lazy {
        println("Computing lazy value...")
        "I am lazy"
    }
    println("Lazy value not accessed yet")
    println("Lazy value: $lazyValue") // First access triggers computation
    println("Lazy value again: $lazyValue") // Second access uses cached value
    
    // Type checking
    val anyValue: Any = "Hello"
    if (anyValue is String) {
        println("anyValue is a String of length ${anyValue.length}")
    }
    
    // Safe casts
    val number: Any = 42
    val stringValue: String? = number as? String
    println("Safe cast result: $stringValue") // null, as number is not a String
    
    // Use function (resource handling)
    File("temp2.txt").apply {
        writeText("This file is temporary.")
    }.inputStream().use { input ->
        println("Reading from file: ${input.readBytes().toString(Charsets.UTF_8)}")
    }
    File("temp2.txt").delete()
    
    // Tuples (Pair and Triple)
    val pair = Pair("name", "Alice")
    println("Pair: first = ${pair.first}, second = ${pair.second}")
    
    val triple = Triple("name", "Alice", 30)
    println("Triple: first = ${triple.first}, second = ${triple.second}, third = ${triple.third}")
    
    // Destructuring
    val (name2, age2) = Pair("Bob", 25)
    println("Destructured pair: name = $name2, age = $age2")
    
    // Running external processes
    try {
        val process = Runtime.getRuntime().exec("echo Hello from subprocess")
        val output = process.inputStream.bufferedReader().readText()
        println("Subprocess output: $output")
    } catch (e: Exception) {
        println("Failed to run subprocess: ${e.message}")
    }
    
    println("\n===== REAL-WORLD EXAMPLE: DATA PROCESSING =====")
    
    // Sample data
    val users = listOf(
        User("Alice", 28, listOf("Design", "Photography")),
        User("Bob", 35, listOf("Programming", "Gaming", "Reading")),
        User("Charlie", 22, listOf("Music", "Art", "Photography")),
        User("David", 42, listOf("Programming", "Cooking")),
        User("Eve", 31, listOf("Sports", "Reading", "Cooking"))
    )
    
    // Find users by age range
    val youngAdults = users.filter { it.age in 20..30 }
    println("Young adults (20-30): ${youngAdults.map { it.name }}")
    
    // Group users by age decade
    val usersByAgeGroup = users.groupBy { it.age / 10 * 10 }
    println("\nUsers by age group:")
    usersByAgeGroup.forEach { (ageGroup, usersInGroup) ->
        println("${ageGroup}s: ${usersInGroup.map { it.name }}")
    }
    
    // Find common hobbies
    val allHobbies = users.flatMap { it.hobbies }.distinct()
    println("\nAll unique hobbies: $allHobbies")
    
    // Count users by hobby
    val usersByHobby = allHobbies.associateWith { hobby ->
        users.count { user -> hobby in user.hobbies }
    }
    println("\nUsers per hobby:")
    usersByHobby.forEach { (hobby, count) ->
        println("$hobby: $count users")
    }
    
    // Find pairs of users with common hobbies
    println("\nUsers with common hobbies:")
    for (i in users.indices) {
        for (j in i + 1 until users.size) {
            val user1 = users[i]
            val user2 = users[j]
            val commonHobbies = user1.hobbies.intersect(user2.hobbies.toSet())
            if (commonHobbies.isNotEmpty()) {
                println("${user1.name} and ${user2.name} share: $commonHobbies")
            }
        }
    }
    
    // Average age by hobby
    val avgAgeByHobby = allHobbies.associateWith { hobby ->
        users.filter { user -> hobby in user.hobbies }
            .map { it.age }
            .average()
    }
    println("\nAverage age by hobby:")
    avgAgeByHobby.forEach { (hobby, avgAge) ->
        println("$hobby: ${String.format("%.1f", avgAge)} years")
    }
    
    println("\n===== REAL-WORLD EXAMPLE: TEXT PROCESSING =====")
    
    val text = """
        The Kotlin Standard Library provides living essentials for everyday work with Kotlin. 
        These include:
        - Higher-order functions implementing idiomatic patterns (let, apply, use, synchronized, etc).
        - Extension functions providing querying operations for collections (groupBy, zipWithNext, etc).
        - Various utilities for working with strings and char sequences.
        - Extensions for JDK classes making it convenient to work with files, IO, and threading.
        Kotlin Standard Library is a lightweight library. It focuses on providing fundamental 
        higher-level abstractions for solving everyday problems.
    """.trimIndent()
    
    // Word count
    val wordCount = text.split(Regex("\\s+")).size
    println("Word count: $wordCount")
    
    // Sentence count
    val sentenceCount = text.split(Regex("[.!?]\\s")).size
    println("Sentence count: $sentenceCount")
    
    // Frequency analysis
    val wordFrequency = text.split(Regex("[\\s.,!?:;()-]"))
        .filter { it.isNotEmpty() }
        .groupingBy { it.lowercase() }
        .eachCount()
        .filter { it.value > 1 } // Only words occurring more than once
    
    println("\nWord frequency (words appearing more than once):")
    wordFrequency.entries
        .sortedByDescending { it.value }
        .forEach { (word, count) ->
            println("$word: $count occurrences")
        }
    
    // Line-by-line processing
    println("\nLine-by-line summary:")
    text.lines().forEachIndexed { index, line ->
        println("Line ${index + 1}: ${line.take(50)}${if (line.length > 50) "..." else ""} (${line.length} chars)")
    }
    
    // Find all occurrences of a pattern
    val pattern = Regex("\\bKotlin\\b")
    val matches = pattern.findAll(text)
    println("\nOccurrences of 'Kotlin':")
    matches.forEach { match ->
        val start = max(0, match.range.first - 10)
        val end = min(text.length, match.range.last + 10)
        println("...${text.substring(start, end)}...")
    }
    
    // Simple text statistics
    val charCount = text.count { !it.isWhitespace() }
    val avgWordLength = text.split(Regex("\\s+"))
        .map { it.count { char -> char.isLetterOrDigit() } }
        .average()
    
    println("\nText statistics:")
    println("Character count (excluding whitespace): $charCount")
    println("Average word length: ${String.format("%.1f", avgWordLength)} characters")
}

data class User(
    val name: String,
    val age: Int,
    val hobbies: List<String>
) 