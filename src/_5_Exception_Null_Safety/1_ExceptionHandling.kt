package _5_Exception_Null_Safety

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * This file demonstrates exception handling in Kotlin
 * including try-catch blocks, custom exceptions, and best practices.
 */
fun main() {
    println("===== BASIC EXCEPTION HANDLING =====")
    
    // Simple try-catch
    try {
        val result = 10 / 0
        println("Result: $result") // This line will not be executed
    } catch (e: ArithmeticException) {
        println("Error: Division by zero")
    }
    
    // Multiple catch blocks
    try {
        val numbers = listOf(1, 2, 3)
        println(numbers[5]) // This will throw IndexOutOfBoundsException
    } catch (e: IndexOutOfBoundsException) {
        println("Error: Index out of bounds")
    } catch (e: Exception) {
        println("Some other error occurred: ${e.message}")
    }
    
    // Try-catch-finally
    try {
        val str = "abc"
        val num = str.toInt() // This will throw NumberFormatException
        println("Parsed number: $num") // This line will not be executed
    } catch (e: NumberFormatException) {
        println("Error: Cannot convert string to number")
    } finally {
        println("This block always executes, regardless of exceptions")
    }
    
    // Try as an expression
    val result = try {
        "123".toInt()
    } catch (e: NumberFormatException) {
        0 // Default value in case of exception
    }
    println("Result of try expression: $result")
    
    // Nested try-catch
    try {
        println("Outer try block")
        try {
            val arr = arrayOf(1, 2, 3)
            arr[5] = 10 // This will throw ArrayIndexOutOfBoundsException
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Inner catch: Array index out of bounds")
            throw IllegalStateException("Re-throwing as IllegalStateException")
        }
    } catch (e: IllegalStateException) {
        println("Outer catch: ${e.message}")
    }
    
    println("\n===== CHECKED VS UNCHECKED EXCEPTIONS =====")
    
    // Kotlin doesn't have checked exceptions like Java,
    // but we can still handle them similarly
    try {
        readFileContent("nonexistent.txt")
    } catch (e: FileNotFoundException) {
        println("File not found: ${e.message}")
    } catch (e: IOException) {
        println("IO error: ${e.message}")
    }
    
    // Using the runCatching extension function
    val fileContent = runCatching {
        readFileContent("nonexistent.txt")
    }.getOrElse {
        when (it) {
            is FileNotFoundException -> "File not found"
            is IOException -> "IO error: ${it.message}"
            else -> "Unknown error: ${it.message}"
        }
    }
    println("File content (or error message): $fileContent")
    
    println("\n===== CUSTOM EXCEPTIONS =====")
    
    // Using a custom exception
    try {
        validateAge(-5)
    } catch (e: InvalidAgeException) {
        println("Invalid age: ${e.message}")
    }
    
    // Another custom exception
    try {
        val date = "2023-13-45" // Invalid date format
        parseDateOrThrow(date)
    } catch (e: InvalidDateException) {
        println("Date error: ${e.message}")
        println("Original exception: ${e.cause?.message}")
    }
    
    println("\n===== REAL-WORLD EXAMPLE: BANKING SYSTEM =====")
    
    val bank = Bank()
    val account1 = bank.createAccount("John Doe", 1000.0)
    val account2 = bank.createAccount("Jane Smith", 500.0)
    
    // Successful transaction
    try {
        bank.transfer(account1, account2, 300.0)
        println("Transfer successful")
        println("Account 1 balance: $${account1.balance}")
        println("Account 2 balance: $${account2.balance}")
    } catch (e: BankException) {
        println("Transfer failed: ${e.message}")
    }
    
    // Failed transaction - insufficient funds
    try {
        bank.transfer(account1, account2, 2000.0)
        println("Transfer successful")
    } catch (e: BankException) {
        println("Transfer failed: ${e.message}")
        println("Account 1 balance: $${account1.balance}")
        println("Account 2 balance: $${account2.balance}")
    }
    
    // Failed transaction - invalid amount
    try {
        bank.transfer(account1, account2, -100.0)
        println("Transfer successful")
    } catch (e: BankException) {
        println("Transfer failed: ${e.message}")
    }
    
    println("\n===== REAL-WORLD EXAMPLE: HTTP API CLIENT =====")
    
    val apiClient = ApiClient()
    
    // Get user - success case
    try {
        val user = apiClient.getUser(123)
        println("User retrieved: $user")
    } catch (e: ApiException) {
        println("API error: ${e.message}")
    }
    
    // Get user - not found case
    try {
        val user = apiClient.getUser(456)
        println("User retrieved: $user")
    } catch (e: ApiException) {
        println("API error: ${e.message}")
    }
    
    // Get user - server error case
    try {
        val user = apiClient.getUser(500)
        println("User retrieved: $user")
    } catch (e: ApiException) {
        println("API error: ${e.message}")
    }
    
    // Get user - network error case
    try {
        val user = apiClient.getUser(0)
        println("User retrieved: $user")
    } catch (e: ApiException) {
        println("API error: ${e.message}")
        println("Caused by: ${e.cause?.message}")
    }
}

// Function that may throw an IOException
fun readFileContent(filePath: String): String {
    val file = File(filePath)
    if (!file.exists()) {
        throw FileNotFoundException("File not found: $filePath")
    }
    return file.readText()
}

// Custom exceptions
class InvalidAgeException(message: String) : IllegalArgumentException(message)

class InvalidDateException(message: String, cause: Throwable? = null) : 
    Exception(message, cause)

// Function that throws a custom exception
fun validateAge(age: Int) {
    if (age < 0) {
        throw InvalidAgeException("Age cannot be negative: $age")
    } else if (age > 150) {
        throw InvalidAgeException("Age cannot be greater than 150: $age")
    }
    println("Age is valid: $age")
}

// Function that handles parsing exceptions and rethrows as custom exceptions
fun parseDateOrThrow(dateString: String): LocalDate {
    return try {
        LocalDate.parse(dateString)
    } catch (e: DateTimeParseException) {
        throw InvalidDateException("Invalid date format: $dateString", e)
    }
}

// Real-world example: Banking system
class BankException(message: String) : Exception(message)

class InsufficientFundsException(message: String) : BankException(message)
class InvalidAmountException(message: String) : BankException(message)
class AccountNotFoundException(message: String) : BankException(message)

class BankAccount(val id: String, val owner: String, var balance: Double)

class Bank {
    private val accounts = mutableMapOf<String, BankAccount>()
    private var nextAccountId = 1
    
    fun createAccount(owner: String, initialBalance: Double): BankAccount {
        val id = "ACC" + nextAccountId++.toString().padStart(3, '0')
        val account = BankAccount(id, owner, initialBalance)
        accounts[id] = account
        println("Account created: $id, Owner: $owner, Initial balance: $$initialBalance")
        return account
    }
    
    fun getAccount(accountId: String): BankAccount {
        return accounts[accountId] ?: throw AccountNotFoundException("Account not found: $accountId")
    }
    
    fun deposit(account: BankAccount, amount: Double) {
        if (amount <= 0) {
            throw InvalidAmountException("Deposit amount must be positive: $amount")
        }
        account.balance += amount
    }
    
    fun withdraw(account: BankAccount, amount: Double) {
        if (amount <= 0) {
            throw InvalidAmountException("Withdrawal amount must be positive: $amount")
        }
        if (account.balance < amount) {
            throw InsufficientFundsException("Insufficient funds. Balance: $${account.balance}, Requested: $$amount")
        }
        account.balance -= amount
    }
    
    fun transfer(fromAccount: BankAccount, toAccount: BankAccount, amount: Double) {
        try {
            if (amount <= 0) {
                throw InvalidAmountException("Transfer amount must be positive: $amount")
            }
            
            if (fromAccount.balance < amount) {
                throw InsufficientFundsException("Insufficient funds. Balance: $${fromAccount.balance}, Requested: $$amount")
            }
            
            // Perform the transfer
            fromAccount.balance -= amount
            toAccount.balance += amount
        } catch (e: BankException) {
            // Re-throw the exception
            throw e
        } catch (e: Exception) {
            // Wrap any other exception
            throw BankException("Transfer failed: ${e.message}")
        }
    }
}

// Real-world example: API Client
data class User(val id: Int, val name: String, val email: String)

class ApiException(message: String, val statusCode: Int? = null, cause: Throwable? = null) : 
    Exception(message, cause)

class ApiClient {
    fun getUser(userId: Int): User {
        return try {
            // Simulate API call
            when (userId) {
                0 -> throw IOException("Network error")
                123 -> User(123, "John Doe", "john.doe@example.com")
                in 400..499 -> throw ApiException("User not found", 404)
                in 500..599 -> throw ApiException("Server error", 500)
                else -> throw ApiException("User not found", 404)
            }
        } catch (e: IOException) {
            throw ApiException("Network error: ${e.message}", cause = e)
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException("Unexpected error: ${e.message}", cause = e)
        }
    }
    
    fun createUser(name: String, email: String): User {
        if (name.isBlank()) {
            throw ApiException("Name cannot be blank", 400)
        }
        if (!email.contains("@")) {
            throw ApiException("Invalid email format", 400)
        }
        
        // Simulate API call - just return a new user with a random ID
        return User((100..999).random(), name, email)
    }
    
    fun makeHttpRequest(url: String): String {
        return try {
            // This would normally make an actual HTTP request
            val connection = URL(url).openConnection()
            connection.connect()
            "Response data"
        } catch (e: IOException) {
            throw ApiException("Failed to connect to $url", cause = e)
        }
    }
} 