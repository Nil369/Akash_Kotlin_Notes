package _2_Functions_lambdas

/**
 * This file demonstrates different types of functions in Kotlin
 * including standard functions, parameters, return types, and default arguments.
 */
fun main() {
    println("===== BASIC FUNCTIONS =====")
    
    // Calling a simple function
    greet()
    
    // Function with parameters
    greetPerson("Alice")
    
    // Function with return value
    val sum = addNumbers(5, 7)
    println("Sum: $sum")
    
    // Single-expression function
    val product = multiply(4, 3)
    println("Product: $product")
    
    println("\n===== FUNCTION PARAMETERS =====")
    
    // Default parameter values
    printInfo("Bob")
    printInfo("Charlie", 30)
    printInfo("David", 45, "Manager")
    
    // Named arguments
    printInfo(
        age = 25,
        name = "Eve",
        job = "Designer"
    )
    
    // Variable number of arguments (varargs)
    printNumbers(1, 2, 3, 4, 5)
    
    // Mix of regular and varargs parameters
    val numbers = arrayOf(1, 2, 3)
    calculateSum("Sum of numbers", *numbers) // Spread operator
    
    println("\n===== FUNCTION OVERLOADING =====")
    
    // Function overloading
    println("Area of square: ${calculateArea(5.0)}")
    println("Area of rectangle: ${calculateArea(5.0, 3.0)}")
    println("Area of circle: ${calculateArea(radius = 3.0)}")
    
    println("\n===== LOCAL FUNCTIONS =====")
    
    processData(arrayOf(14, 7, 21, 3, 8, 18))
    
    println("\n===== REAL-WORLD EXAMPLE: BANKING SYSTEM =====")
    
    // Creating bank accounts
    val savingsAccount = BankAccount("SA-12345", "Alice Johnson", 1000.0, 0.03)
    val checkingAccount = BankAccount("CH-67890", "Bob Smith", 500.0)
    
    // Performing transactions
    savingsAccount.deposit(500.0)
    savingsAccount.printBalance()
    
    val withdrawalSuccess = checkingAccount.withdraw(200.0)
    if (withdrawalSuccess) {
        println("Withdrawal successful")
    } else {
        println("Withdrawal failed due to insufficient funds")
    }
    checkingAccount.printBalance()
    
    // Apply monthly interest to savings account
    savingsAccount.applyInterest()
    savingsAccount.printBalance()
    
    // Transfer funds between accounts
    transferFunds(savingsAccount, checkingAccount, 300.0)
    
    // Print final balances
    println("\nFinal account states:")
    savingsAccount.printBalance()
    checkingAccount.printBalance()
}

// Basic function without parameters or return value
fun greet() {
    println("Hello, World!")
}

// Function with a parameter
fun greetPerson(name: String) {
    println("Hello, $name!")
}

// Function with parameters and return value
fun addNumbers(a: Int, b: Int): Int {
    return a + b
}

// Single-expression function
fun multiply(a: Int, b: Int): Int = a * b

// Function with default parameter values
fun printInfo(name: String, age: Int = 0, job: String = "Unemployed") {
    println("Name: $name, Age: $age, Job: $job")
}

// Function with variable number of arguments
fun printNumbers(vararg numbers: Int) {
    println("Numbers: ${numbers.joinToString()}")
}

// Function with a mix of regular and varargs parameters
fun calculateSum(message: String, vararg numbers: Int) {
    val sum = numbers.sum()
    println("$message: $sum")
}

// Overloaded functions
fun calculateArea(side: Double): Double {
    return side * side
}

fun calculateArea(length: Double, width: Double): Double {
    return length * width
}

fun calculateArea(radius: Double, isCircle: Boolean = true): Double {
    return if (isCircle) Math.PI * radius * radius else radius * radius
}

// Function with a local function
fun processData(data: Array<Int>) {
    // Local function
    fun isEven(n: Int): Boolean {
        return n % 2 == 0
    }
    
    println("Processing ${data.size} data points")
    
    val evenCount = data.count { isEven(it) }
    val oddCount = data.size - evenCount
    
    println("Even numbers: $evenCount")
    println("Odd numbers: $oddCount")
    
    // Another local function
    fun findMax(): Int {
        return data.maxOrNull() ?: 0
    }
    
    println("Maximum value: ${findMax()}")
}

// Bank Account class for the real-world example
class BankAccount(
    val accountNumber: String,
    val ownerName: String,
    private var balance: Double,
    private val interestRate: Double = 0.0
) {
    // Deposit function
    fun deposit(amount: Double) {
        if (amount > 0) {
            balance += amount
            println("Deposited $${amount} to account $accountNumber")
        } else {
            println("Invalid deposit amount")
        }
    }
    
    // Withdraw function
    fun withdraw(amount: Double): Boolean {
        return if (amount > 0 && amount <= balance) {
            balance -= amount
            println("Withdrew $${amount} from account $accountNumber")
            true
        } else {
            println("Cannot withdraw $${amount} from account $accountNumber")
            false
        }
    }
    
    // Apply monthly interest (for savings accounts)
    fun applyInterest() {
        if (interestRate > 0) {
            val interest = balance * interestRate
            balance += interest
            println("Applied $${interest} interest to account $accountNumber")
        }
    }
    
    // Get current balance
    fun getBalance(): Double {
        return balance
    }
    
    // Print current balance
    fun printBalance() {
        println("Account $accountNumber balance: $${balance}")
    }
}

// Function that uses other functions
fun transferFunds(from: BankAccount, to: BankAccount, amount: Double) {
    println("\nAttempting to transfer $${amount} from ${from.accountNumber} to ${to.accountNumber}")
    
    if (from.withdraw(amount)) {
        to.deposit(amount)
        println("Transfer completed")
    } else {
        println("Transfer failed")
    }
} 