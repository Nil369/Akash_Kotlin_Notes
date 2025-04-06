# Lambdas and Higher-Order Functions

## Lambda Expressions

### Basic Lambda Syntax
```kotlin
// Basic lambda
val square = { x: Int -> x * x }

// Lambda with multiple parameters
val add = { a: Int, b: Int -> a + b }

// Lambda with no parameters
val getRandom = { Math.random() }

// Usage
println(square(5))  // 25
println(add(3, 4))  // 7
println(getRandom()) // Random number
```

### Lambda with Receiver
```kotlin
// String extension lambda
val appendExclamation = String::plus.curry("!")

// Usage
val greeting = "Hello".let(appendExclamation)  // "Hello!"
```

## Higher-Order Functions

### Function as Parameter
```kotlin
fun calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

// Usage
val sum = calculate(5, 3) { a, b -> a + b }  // 8
val multiply = calculate(5, 3) { a, b -> a * b }  // 15
```

### Function as Return Type
```kotlin
fun getOperation(operationType: String): (Int, Int) -> Int {
    return when (operationType) {
        "add" -> { a, b -> a + b }
        "multiply" -> { a, b -> a * b }
        else -> { a, b -> a - b }
    }
}

// Usage
val operation = getOperation("add")
println(operation(5, 3))  // 8
```

## Real-world Examples

### 1. Event Handling System
```kotlin
class EventManager {
    private val listeners = mutableMapOf<String, MutableList<() -> Unit>>()

    fun addEventListener(event: String, listener: () -> Unit) {
        listeners.getOrPut(event) { mutableListOf() }.add(listener)
    }

    fun removeEventListener(event: String, listener: () -> Unit) {
        listeners[event]?.remove(listener)
    }

    fun triggerEvent(event: String) {
        listeners[event]?.forEach { it() }
    }
}

// Usage
val eventManager = EventManager()

// Add event listeners
eventManager.addEventListener("userLogin") {
    println("User logged in!")
}

eventManager.addEventListener("userLogout") {
    println("User logged out!")
}

// Trigger events
eventManager.triggerEvent("userLogin")  // Prints: User logged in!
```

### 2. Data Processing Pipeline
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

// Add processing steps
numberProcessor.addProcessor { it * 2 }  // Double the number
numberProcessor.addProcessor { it + 1 }  // Add 1
numberProcessor.addProcessor { it * 3 }  // Triple the result

val result = numberProcessor.process(5)  // ((5 * 2 + 1) * 3) = 33
println(result)
```

### 3. Validation System
```kotlin
class Validator<T> {
    private val validations = mutableListOf<(T) -> Boolean>()

    fun addValidation(validation: (T) -> Boolean) {
        validations.add(validation)
    }

    fun validate(data: T): Boolean {
        return validations.all { it(data) }
    }
}

// Usage
val userValidator = Validator<String>()

// Add validation rules
userValidator.addValidation { it.length >= 3 }
userValidator.addValidation { it.contains("@") }
userValidator.addValidation { it.length <= 50 }

val isValid = userValidator.validate("john@example.com")  // true
```

## Common Higher-Order Functions

### Collections Operations
```kotlin
val numbers = listOf(1, 2, 3, 4, 5)

// map
val doubled = numbers.map { it * 2 }  // [2, 4, 6, 8, 10]

// filter
val evenNumbers = numbers.filter { it % 2 == 0 }  // [2, 4]

// reduce
val sum = numbers.reduce { acc, num -> acc + num }  // 15

// forEach
numbers.forEach { println(it) }  // Prints each number
```

### Real-world Example: E-commerce Filtering
```kotlin
data class Product(
    val name: String,
    val price: Double,
    val category: String,
    val inStock: Boolean
)

class ProductFilter {
    fun filterProducts(
        products: List<Product>,
        priceRange: ClosedFloatingPointRange<Double>? = null,
        category: String? = null,
        inStock: Boolean? = null
    ): List<Product> {
        return products.filter { product ->
            (priceRange == null || product.price in priceRange) &&
            (category == null || product.category == category) &&
            (inStock == null || product.inStock == inStock)
        }
    }
}

// Usage
val products = listOf(
    Product("Laptop", 999.99, "Electronics", true),
    Product("Phone", 499.99, "Electronics", false),
    Product("Book", 29.99, "Books", true)
)

val filter = ProductFilter()
val filteredProducts = filter.filterProducts(
    products = products,
    priceRange = 0.0..500.0,
    category = "Electronics",
    inStock = true
)
```

## Practice Exercises

1. Create a custom `filter` function that works with any type
2. Implement a `map` function that transforms elements of a list
3. Write a `reduce` function that combines elements of a list
4. Create a custom `forEach` function that applies an action to each element
5. Implement a `find` function that returns the first element matching a condition

Remember:
- Lambdas are concise ways to define functions
- Higher-order functions make code more flexible and reusable
- Use type inference when possible to make code more concise
- Consider using named parameters for complex lambdas
- Always handle edge cases in your lambda expressions 