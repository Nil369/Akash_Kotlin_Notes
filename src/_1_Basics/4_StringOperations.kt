package _1_Basics

/**
 * This file demonstrates various string operations in Kotlin
 * including string templates, manipulation functions, and formatting.
*/

fun main() {
    println("===== STRING BASICS =====")
    
    // String declaration
    val simpleString = "Hello, Kotlin!"
    val multilineString = """
        This is a multi-line string.
        It can span multiple lines
        without the need for escape characters.
    """.trimIndent()
    
    println(simpleString)
    println(multilineString)
    


    println("\n===== STRING TEMPLATES =====")
    
    // Simple variable interpolation
    val name = "Akash"
    val greeting = "Hello, $name!"
    println(greeting)
    
    // Expression interpolation
    val a = 10
    val b = 20
    println("Sum of $a and $b is ${a + b}")
    
    // Object properties and function calls
    val user = User("Akash", 20)
    println("User ${user.name} is ${user.getAgeDescription()}")
    
    println("\n===== STRING FUNCTIONS =====")
    
    val text = "Kotlin Programming Language"
    
    // Length
    println("Length: ${text.length}")
    
    // Accessing characters
    println("First character: ${text[0]}")
    println("Last character: ${text[text.length - 1]}")
    
    // Case conversion
    println("Uppercase: ${text.uppercase()}")
    println("Lowercase: ${text.lowercase()}")
    
    // Checking content
    println("Starts with 'Kot': ${text.startsWith("Kot")}")
    println("Ends with 'age': ${text.endsWith("age")}")
    println("Contains 'gram': ${text.contains("gram")}")
    
    // Substring
    println("Substring (7-18): ${text.substring(7, 18)}")
    
    // Replace
    println("Replace 'Programming' with 'Coding': ${text.replace("Programming", "Coding")}")
    
    // Split
    val wordArray = text.split(" ")
    println("Split by space: ${wordArray.joinToString(", ")}")
    
    // Trimming
    val textWithSpaces = "   Trim me   "
    println("Original: '$textWithSpaces'")
    println("Trimmed: '${textWithSpaces.trim()}'")
    
    println("\n===== STRING FORMATTING =====")
    
    // Basic formatting
    val pi = 3.14159265359
    println("Formatted PI (2 decimals): %.2f".format(pi))
    
    // Multiple arguments
    val quantity = 5
    val price = 19.99
    val total = quantity * price
    println("Receipt: %d items at $%.2f each, total: $%.2f".format(quantity, price, total))
    
    // Named arguments in format (fixed using string templates)
    val userName = "Bob"
    val userAge = 30
    val formatted = "User $userName is $userAge years old"
    println(formatted)
    
    println("\n===== REAL-WORLD EXAMPLE: EMAIL VALIDATOR =====")
    
    // Email validator function
    val emails = arrayOf(
        "user@example.com",
        "invalid@email",
        "user.name@company.co.uk",
        "@missinguser.com",
        "user@.com",
        "user@domain.",
        "user name@domain.com"
    )
    
    println("Email Validation Results:")
    for (email in emails) {
        val isValid = isValidEmail(email)
        println("$email: ${if (isValid) "Valid" else "Invalid"}")
    }
    
    println("\n===== REAL-WORLD EXAMPLE: TEXT PROCESSOR =====")
    
    val article = """
        Kotlin is a modern programming language that makes developers happier.
        It's concise, safe, interoperable with Java, and provides many features
        that developers love. Kotlin works great for Android, server-side, web,
        and native development. Many developers are switching from Java to Kotlin
        due to its improved syntax and features.
    """.trimIndent()
    
    // Word count
    val words = article.split(Regex("\\s+"))
    println("Word count: ${words.size}")
    
    // Sentence count
    val sentences = article.split(Regex("[.!?]")).filter { it.isNotBlank() }
    println("Sentence count: ${sentences.size}")
    
    // Find most frequent words
    val wordFrequency = mutableMapOf<String, Int>()
    for (word in words) {
        val cleanWord = word.lowercase().replace(Regex("[^a-z]"), "")
        if (cleanWord.isNotEmpty()) {
            wordFrequency[cleanWord] = wordFrequency.getOrDefault(cleanWord, 0) + 1
        }
    }
    
    val topWords = wordFrequency.entries.sortedByDescending { it.value }.take(5)
    println("Top 5 most frequent words:")
    for ((word, count) in topWords) {
        println("'$word': appears $count times")
    }
    
    // Generate summary (first sentence)
    println("\nSummary: ${sentences.first()}...")
}

// Helper class for demonstration
class User(val name: String, private val age: Int) {
    fun getAgeDescription(): String {
        return when {
            age < 18 -> "a minor"
            age in 18..65 -> "$age years old"
            else -> "a senior"
        }
    }
}

// Email validation function
fun isValidEmail(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return regex.matches(email)
} 