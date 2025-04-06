# Exception Handling in Kotlin

## Basic Exception Handling

### Try-Catch Blocks
```kotlin
// Basic try-catch
try {
    val result = 10 / 0
} catch (e: ArithmeticException) {
    println("Cannot divide by zero: ${e.message}")
}

// Multiple catch blocks
try {
    val number = "123".toInt()
    val result = 10 / number
} catch (e: NumberFormatException) {
    println("Invalid number format: ${e.message}")
} catch (e: ArithmeticException) {
    println("Arithmetic error: ${e.message}")
}

// Finally block
try {
    val file = File("data.txt").readText()
    println(file)
} catch (e: FileNotFoundException) {
    println("File not found: ${e.message}")
} finally {
    println("Cleanup completed")
}
```

### Custom Exceptions
```kotlin
// Custom exception class
class ValidationException(message: String) : Exception(message)

// Custom exception with additional data
class PaymentException(
    message: String,
    val amount: Double,
    val transactionId: String
) : Exception(message)

// Usage
fun validateAge(age: Int) {
    when {
        age < 0 -> throw ValidationException("Age cannot be negative")
        age > 150 -> throw ValidationException("Age seems invalid")
        else -> println("Age is valid: $age")
    }
}

fun processPayment(amount: Double, transactionId: String) {
    if (amount <= 0) {
        throw PaymentException("Invalid payment amount", amount, transactionId)
    }
    // Process payment...
}
```

## Real-world Examples

### 1. File Processing System
```kotlin
class FileProcessor {
    fun readAndProcessFile(filePath: String): List<String> {
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                throw FileNotFoundException("File not found: $filePath")
            }
            
            file.useLines { lines ->
                lines.map { line ->
                    try {
                        processLine(line)
                    } catch (e: IllegalArgumentException) {
                        println("Invalid line format: ${e.message}")
                        null
                    }
                }.filterNotNull()
            }.toList()
        } catch (e: IOException) {
            println("Error reading file: ${e.message}")
            emptyList()
        }
    }

    private fun processLine(line: String): String {
        val parts = line.split(",")
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid line format: $line")
        }
        return "${parts[0].trim()} - ${parts[1].trim()}"
    }
}

// Usage
val processor = FileProcessor()
val results = processor.readAndProcessFile("data.csv")
```

### 2. Network Request Handler
```kotlin
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
}

class NetworkClient {
    suspend fun makeRequest(url: String): NetworkResult<String> {
        return try {
            val response = withContext(Dispatchers.IO) {
                URL(url).readText()
            }
            NetworkResult.Success(response)
        } catch (e: MalformedURLException) {
            NetworkResult.Error(e)
        } catch (e: IOException) {
            NetworkResult.Error(e)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun fetchUserData(userId: String): NetworkResult<User> {
        return try {
            val response = makeRequest("https://api.example.com/users/$userId")
            when (response) {
                is NetworkResult.Success -> {
                    val user = parseUserJson(response.data)
                    NetworkResult.Success(user)
                }
                is NetworkResult.Error -> NetworkResult.Error(response.exception)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    private fun parseUserJson(json: String): User {
        // JSON parsing logic here
        return User("", "", 0) // Placeholder
    }
}

// Usage
val client = NetworkClient()
runBlocking {
    when (val result = client.fetchUserData("123")) {
        is NetworkResult.Success -> println("User data: ${result.data}")
        is NetworkResult.Error -> println("Error: ${result.exception.message}")
    }
}
```

### 3. Database Operations
```kotlin
class DatabaseManager {
    private val connection = mutableMapOf<String, Any>()

    fun executeQuery(query: String): Result<List<Map<String, Any>>> {
        return try {
            validateQuery(query)
            val result = executeQueryInternal(query)
            Result.success(result)
        } catch (e: SQLException) {
            Result.failure(e)
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        }
    }

    private fun validateQuery(query: String) {
        if (query.isBlank()) {
            throw IllegalArgumentException("Query cannot be empty")
        }
        if (!query.startsWith("SELECT")) {
            throw IllegalArgumentException("Only SELECT queries are allowed")
        }
    }

    private fun executeQueryInternal(query: String): List<Map<String, Any>> {
        // Simulated database operation
        return listOf(
            mapOf("id" to 1, "name" to "John"),
            mapOf("id" to 2, "name" to "Jane")
        )
    }
}

// Usage
val dbManager = DatabaseManager()
dbManager.executeQuery("SELECT * FROM users")
    .onSuccess { results ->
        println("Query results: $results")
    }
    .onFailure { error ->
        println("Query failed: ${error.message}")
    }
```

## Best Practices

### 1. Exception Hierarchy
```kotlin
sealed class AppException : Exception() {
    data class ValidationError(override val message: String) : AppException()
    data class NetworkError(override val message: String, val code: Int) : AppException()
    data class DatabaseError(override val message: String, val query: String) : AppException()
}

class ErrorHandler {
    fun handleError(exception: AppException) {
        when (exception) {
            is AppException.ValidationError -> {
                // Handle validation errors
                println("Validation error: ${exception.message}")
            }
            is AppException.NetworkError -> {
                // Handle network errors
                println("Network error: ${exception.message} (Code: ${exception.code})")
            }
            is AppException.DatabaseError -> {
                // Handle database errors
                println("Database error: ${exception.message} (Query: ${exception.query})")
            }
        }
    }
}
```

### 2. Resource Management
```kotlin
class ResourceManager {
    fun processFile(filePath: String) {
        File(filePath).use { file ->
            // File will be automatically closed after use
            val content = file.readText()
            processContent(content)
        }
    }

    fun processDatabaseConnection() {
        val connection = DatabaseConnection()
        connection.use {
            // Connection will be automatically closed after use
            executeQueries(it)
        }
    }
}
```

## Practice Exercises

1. Create a custom exception hierarchy for a banking system
2. Implement a file reader that handles various file-related exceptions
3. Build a network client with proper error handling and retry logic
4. Create a database transaction manager with rollback support
5. Implement a configuration loader with validation and error handling

Remember:
- Use specific exception types for different error cases
- Include meaningful error messages
- Clean up resources using `use` blocks
- Consider using `Result` type for better error handling
- Use sealed classes for custom exception hierarchies
- Handle exceptions at appropriate levels
- Log exceptions with proper context
- Consider using coroutines for async error handling 