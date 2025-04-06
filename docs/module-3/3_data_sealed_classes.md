# Data Classes and Sealed Classes

## Data Classes

### Basic Data Class
```kotlin
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Usage
val user = User(1, "John Doe", "john@example.com")
println(user)  // User(id=1, name=John Doe, email=john@example.com)
println(user.copy(name = "Jane Doe"))  // Creates a copy with modified name
```

### Data Class with Default Values
```kotlin
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val inStock: Boolean = true,
    val category: String = "General"
)

// Usage
val product1 = Product("1", "Laptop", 999.99)
val product2 = Product("2", "Phone", 499.99, category = "Electronics")
```

### Data Class with Custom Methods
```kotlin
data class Point(val x: Int, val y: Int) {
    fun distanceFromOrigin(): Double {
        return Math.sqrt(x * x + y * y.toDouble())
    }
    
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

// Usage
val p1 = Point(3, 4)
val p2 = Point(1, 2)
println(p1.distanceFromOrigin())  // 5.0
println(p1 + p2)  // Point(x=4, y=6)
```

## Sealed Classes

### Basic Sealed Class
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// Usage
fun handleResult(result: Result<String>) {
    when (result) {
        is Result.Success -> println("Success: ${result.data}")
        is Result.Error -> println("Error: ${result.message}")
        Result.Loading -> println("Loading...")
    }
}

val success = Result.Success("Data loaded")
val error = Result.Error("Network error")
val loading = Result.Loading

handleResult(success)  // Success: Data loaded
handleResult(error)    // Error: Network error
handleResult(loading)  // Loading...
```

### Sealed Class with Nested Classes
```kotlin
sealed class NetworkResponse {
    data class Success(val data: String) : NetworkResponse()
    sealed class Error : NetworkResponse() {
        data class NetworkError(val message: String) : Error()
        data class ServerError(val code: Int, val message: String) : Error()
        object Timeout : Error()
    }
}

// Usage
fun handleNetworkResponse(response: NetworkResponse) {
    when (response) {
        is NetworkResponse.Success -> println("Success: ${response.data}")
        is NetworkResponse.Error.NetworkError -> println("Network Error: ${response.message}")
        is NetworkResponse.Error.ServerError -> println("Server Error ${response.code}: ${response.message}")
        is NetworkResponse.Error.Timeout -> println("Request timed out")
    }
}
```

## Real-world Examples

### 1. API Response Handling
```kotlin
sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val code: Int, val message: String) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}

data class User(
    val id: Int,
    val name: String,
    val email: String
)

class UserRepository {
    suspend fun fetchUser(id: Int): ApiResponse<User> {
        return try {
            // Simulate API call
            ApiResponse.Success(User(id, "John Doe", "john@example.com"))
        } catch (e: Exception) {
            ApiResponse.Error(500, e.message ?: "Unknown error")
        }
    }
}

// Usage
suspend fun main() {
    val repository = UserRepository()
    val response = repository.fetchUser(1)
    
    when (response) {
        is ApiResponse.Success -> println("User: ${response.data}")
        is ApiResponse.Error -> println("Error: ${response.message}")
        ApiResponse.Loading -> println("Loading...")
    }
}
```

### 2. Payment Processing System
```kotlin
sealed class PaymentResult {
    data class Success(
        val transactionId: String,
        val amount: Double,
        val timestamp: Long
    ) : PaymentResult()
    
    sealed class Failure : PaymentResult() {
        data class InsufficientFunds(val balance: Double) : Failure()
        data class InvalidCard(val reason: String) : Failure()
        object NetworkError : Failure()
    }
}

data class PaymentRequest(
    val amount: Double,
    val cardNumber: String,
    val expiryDate: String
)

class PaymentProcessor {
    fun processPayment(request: PaymentRequest): PaymentResult {
        return when {
            request.amount > 1000.0 -> PaymentResult.Failure.InsufficientFunds(500.0)
            request.cardNumber.length != 16 -> PaymentResult.Failure.InvalidCard("Invalid card number")
            else -> PaymentResult.Success(
                transactionId = "TXN-${System.currentTimeMillis()}",
                amount = request.amount,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}

// Usage
val processor = PaymentProcessor()
val request = PaymentRequest(500.0, "1234567890123456", "12/25")
val result = processor.processPayment(request)

when (result) {
    is PaymentResult.Success -> println("Payment successful: ${result.transactionId}")
    is PaymentResult.Failure.InsufficientFunds -> println("Insufficient funds. Balance: ${result.balance}")
    is PaymentResult.Failure.InvalidCard -> println("Invalid card: ${result.reason}")
    is PaymentResult.Failure.NetworkError -> println("Network error occurred")
}
```

### 3. File System Operations
```kotlin
sealed class FileOperation {
    data class Read(val path: String) : FileOperation()
    data class Write(val path: String, val content: String) : FileOperation()
    data class Delete(val path: String) : FileOperation()
}

sealed class FileResult {
    data class Success(val content: String) : FileResult()
    data class Error(val message: String) : FileResult()
}

class FileManager {
    fun executeOperation(operation: FileOperation): FileResult {
        return when (operation) {
            is FileOperation.Read -> {
                try {
                    // Simulate file reading
                    FileResult.Success("File content")
                } catch (e: Exception) {
                    FileResult.Error("Failed to read file: ${e.message}")
                }
            }
            is FileOperation.Write -> {
                try {
                    // Simulate file writing
                    FileResult.Success("File written successfully")
                } catch (e: Exception) {
                    FileResult.Error("Failed to write file: ${e.message}")
                }
            }
            is FileOperation.Delete -> {
                try {
                    // Simulate file deletion
                    FileResult.Success("File deleted successfully")
                } catch (e: Exception) {
                    FileResult.Error("Failed to delete file: ${e.message}")
                }
            }
        }
    }
}

// Usage
val fileManager = FileManager()
val readOperation = FileOperation.Read("example.txt")
val result = fileManager.executeOperation(readOperation)

when (result) {
    is FileResult.Success -> println("Operation successful: ${result.content}")
    is FileResult.Error -> println("Operation failed: ${result.message}")
}
```

## Practice Exercises

1. Create a data class for a `Book` with properties for title, author, and publication year
2. Implement a sealed class for handling different types of network requests
3. Create a data class for a `ShoppingCart` item with quantity and price
4. Design a sealed class hierarchy for different types of user authentication results
5. Implement a data class for a `Task` with priority and due date

Remember:
- Data classes automatically provide `equals()`, `hashCode()`, `toString()`, and `copy()`
- Sealed classes are great for representing restricted class hierarchies
- Use data classes for simple data holders
- Use sealed classes when you need to represent a fixed set of possible types
- Consider using sealed classes for API responses and error handling 