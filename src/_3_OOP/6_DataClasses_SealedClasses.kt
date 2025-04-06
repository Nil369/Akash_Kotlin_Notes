package _3_OOP

/**
 * This file demonstrates data classes and sealed classes in Kotlin
 * including their creation, usage, and practical applications.
 */
fun main() {
    println("===== DATA CLASSES =====")
    
    // Basic data class example
    val user1 = User(1, "John Doe", "john.doe@example.com")
    val user2 = User(2, "Jane Smith", "jane.smith@example.com")
    val user3 = User(1, "John Doe", "john.doe@example.com")
    
    println("User 1: $user1")
    println("User 2: $user2")
    println("User 1 == User 3: ${user1 == user3}") // true: structural equality
    println("User 1 === User 3: ${user1 === user3}") // false: referential equality
    
    // Copy function
    val updatedUser = user1.copy(email = "john.new@example.com")
    println("\nOriginal user: $user1")
    println("Updated user: $updatedUser")
    
    // Destructuring declarations
    val (id, name, email) = user1
    println("\nDestructured user - ID: $id, Name: $name, Email: $email")
    
    // Data class with default values
    val defaultProduct = Product("P100", "Default Product")
    val customProduct = Product("P101", "Custom Product", 99.99, "Electronics", true)
    
    println("\nDefault product: $defaultProduct")
    println("Custom product: $customProduct")
    
    // Data class with custom methods
    val point1 = Point(3.0, 4.0)
    val point2 = Point(1.0, 2.0)
    val distance = point1.distanceFromOrigin()
    val sumPoint = point1 + point2
    
    println("\nPoint 1: $point1")
    println("Distance from origin: $distance")
    println("Point 1 + Point 2: $sumPoint")
    
    println("\n===== SEALED CLASSES =====")
    
    // Basic sealed class example
    val results = listOf(
        Result.Success(User(101, "Alice Cooper", "alice@example.com")),
        Result.Error("Network connection failed"),
        Result.Loading
    )
    
    // Processing different result types
    for (result in results) {
        processResult(result)
    }
    
    // Sealed class with nested classes
    val responses = listOf(
        NetworkResponse.Success(200, "Operation successful"),
        NetworkResponse.Error.ClientError(404, "Resource not found"),
        NetworkResponse.Error.ServerError(500, "Internal server error"),
        NetworkResponse.Loading(75)
    )
    
    // Processing different response types
    for (response in responses) {
        processNetworkResponse(response)
    }
    
    println("\n===== REAL-WORLD EXAMPLE: API RESPONSE HANDLING =====")
    
    // Simulating API calls
    val userApi = UserApi()
    println("\nFetching user with ID 123:")
    processApiResponse(userApi.getUser(123))
    
    println("\nFetching user with ID 999:")
    processApiResponse(userApi.getUser(999))
    
    println("\nFetching user with ID 456:")
    processApiResponse(userApi.getUser(456))
    
    println("\n===== REAL-WORLD EXAMPLE: PAYMENT SYSTEM =====")
    
    // Payment processor
    val paymentProcessor = PaymentProcessor()
    
    // Process different payments
    val payments = listOf(
        Payment.CreditCard("4111111111111111", "John Doe", "123", "12/25", 100.50),
        Payment.PayPal("john.doe@example.com", 75.25),
        Payment.BankTransfer("GB29NWBK60161331926819", "John Doe", 250.00)
    )
    
    for (payment in payments) {
        val result = paymentProcessor.processPayment(payment)
        displayPaymentResult(payment, result)
    }
}

// Basic data class
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Data class with default values
data class Product(
    val id: String,
    val name: String,
    val price: Double = 0.0,
    val category: String = "Uncategorized",
    val inStock: Boolean = false
)

// Data class with custom methods
data class Point(val x: Double, val y: Double) {
    fun distanceFromOrigin(): Double {
        return Math.sqrt(x * x + y * y)
    }
    
    // Operator overloading
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

// Basic sealed class
sealed class Result {
    data class Success(val data: User) : Result()
    data class Error(val message: String) : Result()
    object Loading : Result()
}

// Processing different result types
fun processResult(result: Result) {
    when (result) {
        is Result.Success -> println("\nSuccess: ${result.data}")
        is Result.Error -> println("\nError: ${result.message}")
        is Result.Loading -> println("\nLoading...")
    }
}

// Sealed class with nested classes
sealed class NetworkResponse {
    data class Success(val statusCode: Int, val data: String) : NetworkResponse()
    
    sealed class Error : NetworkResponse() {
        data class ClientError(val statusCode: Int, val message: String) : Error()
        data class ServerError(val statusCode: Int, val message: String) : Error()
    }
    
    data class Loading(val progress: Int) : NetworkResponse()
}

// Processing different network response types
fun processNetworkResponse(response: NetworkResponse) {
    when (response) {
        is NetworkResponse.Success -> 
            println("\nSuccessful response (${response.statusCode}): ${response.data}")
        
        is NetworkResponse.Error.ClientError -> 
            println("\nClient error (${response.statusCode}): ${response.message}")
        
        is NetworkResponse.Error.ServerError -> 
            println("\nServer error (${response.statusCode}): ${response.message}")
        
        is NetworkResponse.Loading -> 
            println("\nLoading... ${response.progress}% complete")
    }
}

// Real-world example: API response handling
sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val code: Int, val message: String) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}

// User API class
class UserApi {
    private val users = mapOf(
        123 to User(123, "John Doe", "john.doe@example.com"),
        456 to User(456, "Jane Smith", "jane.smith@example.com")
    )
    
    fun getUser(id: Int): ApiResponse<User> {
        // Simulate loading
        println("Loading user data...")
        
        // Simulate network delay
        Thread.sleep(500)
        
        // Simulate response
        return if (id == 456) {
            // Simulate network error
            ApiResponse.Error(500, "Server timeout")
        } else if (users.containsKey(id)) {
            ApiResponse.Success(users[id]!!)
        } else {
            ApiResponse.Error(404, "User not found")
        }
    }
}

// Process API response
fun <T> processApiResponse(response: ApiResponse<T>) {
    when (response) {
        is ApiResponse.Success -> println("Success! Data: ${response.data}")
        is ApiResponse.Error -> println("Error ${response.code}: ${response.message}")
        is ApiResponse.Loading -> println("Loading...")
    }
}

// Real-world example: Payment system
sealed class Payment {
    data class CreditCard(
        val cardNumber: String,
        val cardHolderName: String,
        val cvv: String,
        val expiryDate: String,
        val amount: Double
    ) : Payment()
    
    data class PayPal(
        val email: String,
        val amount: Double
    ) : Payment()
    
    data class BankTransfer(
        val accountNumber: String,
        val accountHolderName: String,
        val amount: Double
    ) : Payment()
}

sealed class PaymentResult {
    data class Success(
        val transactionId: String,
        val amount: Double,
        val timestamp: Long = System.currentTimeMillis()
    ) : PaymentResult()
    
    sealed class Failure : PaymentResult() {
        data class InsufficientFunds(val message: String) : Failure()
        data class InvalidDetails(val message: String) : Failure()
        data class GeneralError(val message: String) : Failure()
    }
}

class PaymentProcessor {
    fun processPayment(payment: Payment): PaymentResult {
        // Simulate payment processing
        return when (payment) {
            is Payment.CreditCard -> {
                if (payment.cardNumber.length != 16) {
                    PaymentResult.Failure.InvalidDetails("Invalid card number")
                } else {
                    PaymentResult.Success("CC-${System.currentTimeMillis()}", payment.amount)
                }
            }
            
            is Payment.PayPal -> {
                if (!payment.email.contains("@")) {
                    PaymentResult.Failure.InvalidDetails("Invalid email address")
                } else {
                    PaymentResult.Success("PP-${System.currentTimeMillis()}", payment.amount)
                }
            }
            
            is Payment.BankTransfer -> {
                // Simulate a rare insufficient funds error
                if (payment.amount > 200) {
                    PaymentResult.Failure.InsufficientFunds("Insufficient funds in account")
                } else {
                    PaymentResult.Success("BT-${System.currentTimeMillis()}", payment.amount)
                }
            }
        }
    }
}

fun displayPaymentResult(payment: Payment, result: PaymentResult) {
    val paymentType = when (payment) {
        is Payment.CreditCard -> "Credit Card"
        is Payment.PayPal -> "PayPal"
        is Payment.BankTransfer -> "Bank Transfer"
    }
    
    val amount = when (payment) {
        is Payment.CreditCard -> payment.amount
        is Payment.PayPal -> payment.amount
        is Payment.BankTransfer -> payment.amount
    }
    
    println("\nProcessing $paymentType payment of $$amount:")
    
    when (result) {
        is PaymentResult.Success -> {
            println("Payment successful!")
            println("Transaction ID: ${result.transactionId}")
            println("Amount: $${result.amount}")
            println("Timestamp: ${result.timestamp}")
        }
        
        is PaymentResult.Failure.InsufficientFunds -> {
            println("Payment failed: Insufficient funds")
            println("Details: ${result.message}")
        }
        
        is PaymentResult.Failure.InvalidDetails -> {
            println("Payment failed: Invalid details")
            println("Details: ${result.message}")
        }
        
        is PaymentResult.Failure.GeneralError -> {
            println("Payment failed: General error")
            println("Details: ${result.message}")
        }
    }
} 