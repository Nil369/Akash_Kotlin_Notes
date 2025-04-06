# Functions in Kotlin

## Basic Function Declaration

### Simple Function
```kotlin
fun greet(name: String) {
    println("Hello, $name!")
}
```

### Function with Return Type
```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}
```

### Single Expression Function
```kotlin
fun multiply(a: Int, b: Int): Int = a * b
```

## Function Parameters

### Default Parameters
```kotlin
fun createUser(
    name: String,
    age: Int = 18,
    email: String = "user@example.com"
) {
    println("User created: $name, $age, $email")
}

// Usage
createUser("John")  // Uses default values for age and email
createUser("Alice", 25, "alice@example.com")  // All parameters specified
```

### Named Parameters
```kotlin
fun formatAddress(
    street: String,
    city: String,
    state: String,
    zipCode: String
) {
    println("$street, $city, $state $zipCode")
}

// Usage
formatAddress(
    street = "123 Main St",
    city = "New York",
    state = "NY",
    zipCode = "10001"
)
```

### Variable Number of Arguments (Vararg)
```kotlin
fun sum(vararg numbers: Int): Int {
    return numbers.sum()
}

// Usage
val total = sum(1, 2, 3, 4, 5)  // Returns 15
```

## Real-world Examples

### 1. User Registration System
```kotlin
class UserRegistration {
    fun registerUser(
        username: String,
        password: String,
        email: String = "",
        phone: String = "",
        isAdmin: Boolean = false
    ): Boolean {
        // Validate input
        if (username.length < 3 || password.length < 6) {
            return false
        }
        
        // Create user account
        val user = User(
            username = username,
            email = email,
            phone = phone,
            isAdmin = isAdmin
        )
        
        // Save user to database (simulated)
        return saveUser(user)
    }
    
    private fun saveUser(user: User): Boolean {
        // Simulate database operation
        return true
    }
}

data class User(
    val username: String,
    val email: String,
    val phone: String,
    val isAdmin: Boolean
)
```

### 2. Shopping Cart Calculator
```kotlin
class ShoppingCart {
    fun calculateTotal(
        items: List<Item>,
        discount: Double = 0.0,
        tax: Double = 0.1
    ): Double {
        val subtotal = items.sumOf { it.price }
        val discountAmount = subtotal * discount
        val afterDiscount = subtotal - discountAmount
        val taxAmount = afterDiscount * tax
        return afterDiscount + taxAmount
    }
}

data class Item(
    val name: String,
    val price: Double
)
```

### 3. File Processing Utility
```kotlin
class FileProcessor {
    fun processFiles(
        vararg files: String,
        encoding: String = "UTF-8",
        onError: (Exception) -> Unit = { e -> println("Error: ${e.message}") }
    ): List<String> {
        return files.mapNotNull { file ->
            try {
                // Simulate file reading
                "Content of $file"
            } catch (e: Exception) {
                onError(e)
                null
            }
        }
    }
}
```

## Function Overloading

```kotlin
class Calculator {
    fun add(a: Int, b: Int): Int = a + b
    fun add(a: Double, b: Double): Double = a + b
    fun add(a: String, b: String): String = a + b
}

// Usage
val calc = Calculator()
println(calc.add(5, 3))      // 8
println(calc.add(5.5, 3.2)) // 8.7
println(calc.add("Hello ", "World")) // "Hello World"
```

## Extension Functions

```kotlin
// Add functionality to String class
fun String.addExclamation(): String = "$this!"

// Add functionality to Int class
fun Int.isEven(): Boolean = this % 2 == 0

// Usage
val greeting = "Hello".addExclamation()  // "Hello!"
val number = 42
println(number.isEven())  // true
```

## Practice Exercises

1. Create a function that calculates the factorial of a number
2. Implement a function that reverses a string
3. Write a function that checks if a number is prime
4. Create a function that generates Fibonacci sequence
5. Implement a function that sorts a list of numbers

Remember:
- Functions should be single-purpose and follow the Single Responsibility Principle
- Use meaningful names for functions and parameters
- Consider using default parameters to make functions more flexible
- Extension functions are powerful but should be used judiciously
- Always handle edge cases and provide appropriate error handling 