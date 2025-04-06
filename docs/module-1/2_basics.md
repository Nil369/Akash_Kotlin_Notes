# Kotlin Basics

## Variables and Data Types

### Variable Declaration
```kotlin
// Immutable variable (like final in Java)
val name: String = "John"
val age: Int = 25
val height: Double = 1.75
val isStudent: Boolean = true

// Mutable variable
var score: Int = 100
var temperature: Double = 98.6
```

### Type Inference
```kotlin
// Kotlin can infer types
val inferredString = "Hello"  // Type: String
val inferredNumber = 42       // Type: Int
val inferredDecimal = 3.14    // Type: Double
```

### Real-world Example: User Profile
```kotlin
class UserProfile {
    val userId: String = "U12345"  // Immutable user ID
    var name: String = "John Doe"  // Mutable name
    var age: Int = 30
    var email: String = "john@example.com"
    var isActive: Boolean = true
    var lastLogin: Long = System.currentTimeMillis()
}
```

## Basic Operators

### Arithmetic Operators
```kotlin
val a = 10
val b = 3

val sum = a + b      // 13
val difference = a - b  // 7
val product = a * b    // 30
val quotient = a / b   // 3
val remainder = a % b  // 1
```

### Comparison Operators
```kotlin
val x = 5
val y = 10

val isEqual = x == y      // false
val isNotEqual = x != y   // true
val isLess = x < y        // true
val isGreater = x > y     // false
val isLessOrEqual = x <= y // true
val isGreaterOrEqual = x >= y // false
```

### Real-world Example: Shopping Cart
```kotlin
class ShoppingCart {
    private var total: Double = 0.0
    
    fun addItem(price: Double) {
        total += price
    }
    
    fun applyDiscount(percentage: Int) {
        total *= (1 - percentage / 100.0)
    }
    
    fun getTotal(): Double = total
}
```

## Control Flow

### If Expression
```kotlin
// Traditional if-else
val age = 18
if (age >= 18) {
    println("You are an adult")
} else {
    println("You are a minor")
}

// If as an expression
val status = if (age >= 18) "Adult" else "Minor"
```

### When Expression (Enhanced Switch)
```kotlin
val dayOfWeek = 3
val dayName = when (dayOfWeek) {
    1 -> "Monday"
    2 -> "Tuesday"
    3 -> "Wednesday"
    4 -> "Thursday"
    5 -> "Friday"
    6 -> "Saturday"
    7 -> "Sunday"
    else -> "Invalid day"
}
```

### Real-world Example: Grade Calculator
```kotlin
fun calculateGrade(score: Int): String {
    return when {
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        score >= 60 -> "D"
        else -> "F"
    }
}
```

## Loops

### For Loop
```kotlin
// Range-based for loop
for (i in 1..5) {
    println(i)  // Prints 1 to 5
}

// Iterating over a list
val fruits = listOf("Apple", "Banana", "Orange")
for (fruit in fruits) {
    println(fruit)
}
```

### While Loop
```kotlin
var count = 0
while (count < 5) {
    println(count)
    count++
}
```

### Real-world Example: Password Generator
```kotlin
fun generatePassword(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var password = ""
    
    for (i in 0 until length) {
        val randomIndex = (0 until chars.length).random()
        password += chars[randomIndex]
    }
    
    return password
}
```

## String Templates
```kotlin
val name = "Alice"
val age = 25
println("My name is $name and I am $age years old")

// Complex expression
val price = 10.5
val quantity = 3
println("Total cost: ${price * quantity}")
```

## Null Safety
```kotlin
// Nullable type
var nullableString: String? = null

// Safe call operator
val length = nullableString?.length  // Returns null if nullableString is null

// Elvis operator
val displayName = nullableString ?: "Anonymous"

// Not-null assertion (use with caution)
val forcedLength = nullableString!!.length  // Throws exception if null
```

### Real-world Example: User Authentication
```kotlin
class UserAuth {
    private var currentUser: String? = null
    
    fun login(username: String, password: String): Boolean {
        // Simulate authentication
        return if (username.isNotEmpty() && password.length >= 6) {
            currentUser = username
            true
        } else {
            false
        }
    }
    
    fun getCurrentUser(): String {
        return currentUser ?: throw IllegalStateException("No user logged in")
    }
}
```

## Practice Exercises

1. Create a temperature converter (Celsius to Fahrenheit)
2. Implement a simple calculator
3. Create a number guessing game
4. Build a basic to-do list manager

Remember to practice these concepts by implementing real-world scenarios and solving problems. The more you code, the better you'll understand these fundamentals.