package _3_OOP

/**
 * This file demonstrates abstraction in Kotlin
 * including abstract classes, interfaces, and hiding implementation details.
 */
fun main() {
    println("===== ABSTRACTION WITH ABSTRACT CLASSES =====")
    
    // Cannot instantiate abstract class directly
    // val shape = Shape() // This would cause a compilation error
    
    // Create concrete implementations
    val circle = CircleShape(5.0)
    val rectangle = RectangleShape(4.0, 6.0)
    
    // Use abstract methods through concrete implementations
    println("Circle area: ${circle.calculateArea()}")
    println("Rectangle area: ${rectangle.calculateArea()}")
    
    // Use concrete methods from abstract class
    println("Circle description: ${circle.getDescription()}")
    println("Rectangle description: ${rectangle.getDescription()}")
    
    println("\n===== ABSTRACTION WITH INTERFACES =====")
    
    // Cannot instantiate interface directly
    // val device = ElectronicDevice() // This would cause a compilation error
    
    // Create concrete implementations
    val smartphone = Smartphone("Samsung Galaxy S21")
    val laptop = Laptop("MacBook Pro")
    val smartTV = SmartTV("LG OLED 65\"")
    
    // Use device operations through interface
    val devices = listOf<ElectronicDevice>(smartphone, laptop, smartTV)
    
    for (device in devices) {
        device.turnOn()
        device.performTask()
        device.turnOff()
        println()
    }
    
    println("\n===== MULTIPLE INTERFACE IMPLEMENTATION =====")
    
    val musicPlayer = MusicPlayer("iPod")
    musicPlayer.turnOn()
    musicPlayer.playMedia("Bohemian Rhapsody")
    musicPlayer.adjustVolume(8)
    musicPlayer.turnOff()
    
    println("\n===== IMPLEMENTATION HIDING =====")
    
    // Using a database manager that hides its implementation
    val dbManager = DatabaseManager()
    
    // Client code only uses the simple public interface
    dbManager.connect()
    
    val userData = dbManager.fetchData("SELECT * FROM users")
    println("Retrieved ${userData.size} user records")
    
    dbManager.saveData("INSERT INTO users (name) VALUES ('John')")
    
    dbManager.disconnect()
    
    println("\n===== REAL-WORLD EXAMPLE: COFFEE MACHINE =====")
    
    // Using a coffee machine abstraction
    val coffeeMachine = BasicCoffeeMachine()
    
    // Customer interface
    coffeeMachine.makeCoffee("Espresso")
    coffeeMachine.makeCoffee("Latte")
    
    // Premium coffee machine with more features but same basic abstraction
    val premiumCoffeeMachine = PremiumCoffeeMachine()
    
    // Same interface, different implementation
    premiumCoffeeMachine.makeCoffee("Cappuccino")
    premiumCoffeeMachine.makeCoffee("Mocha")
    
    println("\n===== REAL-WORLD EXAMPLE: BANKING APP =====")
    
    // Bank account abstraction hides the complex implementation
    // Customer only interacts with the account via a simple interface
    val bankingApp = BankingApp()
    
    // Customer can use simple operations without knowing the complexities
    bankingApp.login("user123", "password")
    
    bankingApp.checkBalance()
    bankingApp.deposit(500.0)
    bankingApp.withdraw(200.0)
    bankingApp.transfer(150.0, "friend@bank.com")
    
    bankingApp.logout()
}

// Abstraction with abstract classes
abstract class AbstractShape {
    // Abstract property
    abstract val name: String
    
    // Abstract method that must be implemented by subclasses
    abstract fun calculateArea(): Double
    
    // Concrete method that uses abstract properties/methods
    fun getDescription(): String {
        return "This is a $name with area ${calculateArea()}"
    }
}

class CircleShape(private val radius: Double) : AbstractShape() {
    override val name = "Circle"
    
    override fun calculateArea(): Double {
        return Math.PI * radius * radius
    }
}

class RectangleShape(private val width: Double, private val height: Double) : AbstractShape() {
    override val name = "Rectangle"
    
    override fun calculateArea(): Double {
        return width * height
    }
}

// Abstraction with interfaces
interface ElectronicDevice {
    val brand: String
    
    fun turnOn()
    fun turnOff()
    fun performTask()
}

class Smartphone(override val brand: String) : ElectronicDevice {
    override fun turnOn() {
        println("$brand smartphone turning on - showing boot animation")
    }
    
    override fun performTask() {
        println("$brand smartphone performing task - opening applications")
    }
    
    override fun turnOff() {
        println("$brand smartphone turning off - saving state")
    }
}

class Laptop(override val brand: String) : ElectronicDevice {
    override fun turnOn() {
        println("$brand laptop turning on - running BIOS checks")
    }
    
    override fun performTask() {
        println("$brand laptop performing task - running software")
    }
    
    override fun turnOff() {
        println("$brand laptop turning off - closing programs")
    }
}

class SmartTV(override val brand: String) : ElectronicDevice {
    override fun turnOn() {
        println("$brand smart TV turning on - initializing display")
    }
    
    override fun performTask() {
        println("$brand smart TV performing task - streaming content")
    }
    
    override fun turnOff() {
        println("$brand smart TV turning off - display going to sleep")
    }
}

// Multiple interface implementation
interface MediaPlayer {
    fun playMedia(mediaName: String)
    fun pauseMedia()
}

interface VolumeControl {
    fun adjustVolume(level: Int)
    fun mute()
}

// Class implementing multiple interfaces
class MusicPlayer(override val brand: String) : ElectronicDevice, MediaPlayer, VolumeControl {
    private var isPlaying = false
    private var volumeLevel = 5
    private var isMuted = false
    
    override fun turnOn() {
        println("$brand music player turning on - showing welcome screen")
    }
    
    override fun performTask() {
        println("$brand music player ready to play music")
    }
    
    override fun turnOff() {
        println("$brand music player turning off - saving playlists")
    }
    
    override fun playMedia(mediaName: String) {
        isPlaying = true
        println("$brand music player now playing: $mediaName")
    }
    
    override fun pauseMedia() {
        isPlaying = false
        println("$brand music player paused")
    }
    
    override fun adjustVolume(level: Int) {
        if (level in 0..10) {
            volumeLevel = level
            isMuted = false
            println("$brand music player volume set to $volumeLevel")
        } else {
            println("$brand music player volume level must be between 0 and 10")
        }
    }
    
    override fun mute() {
        isMuted = true
        println("$brand music player muted")
    }
}

// Implementation hiding example
class DatabaseManager {
    // Public interface - simple methods for clients to use
    fun connect() {
        println("Database connected successfully")
        // Hiding the complex connection logic
        setupConnection()
        authenticateConnection()
        initializeCache()
    }
    
    fun fetchData(query: String): List<Map<String, Any>> {
        println("Executing query: $query")
        // Hide the complex query parsing, optimization, execution
        validateQuery(query)
        val result = executeQueryInternal(query)
        return transformResultSet(result)
    }
    
    fun saveData(statement: String): Boolean {
        println("Executing statement: $statement")
        // Hide transaction management, constraint validation, etc.
        validateStatement(statement)
        beginTransaction()
        val success = executeUpdateInternal(statement)
        if (success) commitTransaction() else rollbackTransaction()
        return success
    }
    
    fun disconnect() {
        println("Database disconnected successfully")
        // Hide cleanup operations
        clearCache()
        closeConnection()
    }
    
    // Private implementation details - hidden from client code
    private fun setupConnection() {
        // Complex connection pool management
    }
    
    private fun authenticateConnection() {
        // Security checks and authentication
    }
    
    private fun initializeCache() {
        // Setting up query cache
    }
    
    private fun validateQuery(query: String): Boolean {
        // SQL injection prevention and syntax validation
        return true
    }
    
    private fun executeQueryInternal(query: String): Any {
        // Actual database interaction
        return listOf(
            mapOf("id" to 1, "name" to "Alice"),
            mapOf("id" to 2, "name" to "Bob"),
            mapOf("id" to 3, "name" to "Charlie")
        )
    }
    
    private fun transformResultSet(result: Any): List<Map<String, Any>> {
        // Convert raw database result to usable objects
        @Suppress("UNCHECKED_CAST")
        return result as List<Map<String, Any>>
    }
    
    private fun validateStatement(statement: String): Boolean {
        // Validate data modification statement
        return true
    }
    
    private fun beginTransaction() {
        // Start database transaction
    }
    
    private fun executeUpdateInternal(statement: String): Boolean {
        // Execute the update statement
        return true
    }
    
    private fun commitTransaction() {
        // Commit changes
    }
    
    private fun rollbackTransaction() {
        // Roll back changes if error
    }
    
    private fun clearCache() {
        // Clear query cache
    }
    
    private fun closeConnection() {
        // Return connection to pool
    }
}

// Real-world example: Coffee Machine
abstract class CoffeeMachine {
    // Abstract method defines what a coffee machine must do
    abstract fun makeCoffee(type: String)
    
    // Concrete methods with implementation
    protected fun grindBeans(amount: Int) {
        println("Grinding $amount grams of coffee beans")
    }
    
    protected fun heatWater(temperature: Int) {
        println("Heating water to $temperature°C")
    }
    
    protected fun brewCoffee() {
        println("Brewing coffee")
    }
    
    protected fun pourInCup() {
        println("Pouring coffee into cup")
    }
}

// Basic implementation of the coffee machine
class BasicCoffeeMachine : CoffeeMachine() {
    override fun makeCoffee(type: String) {
        println("\nPreparing $type with basic coffee machine:")
        
        when (type.lowercase()) {
            "espresso" -> {
                grindBeans(7)
                heatWater(92)
                brewCoffee()
                pourInCup()
                println("Your $type is ready!")
            }
            "latte" -> {
                grindBeans(7)
                heatWater(90)
                brewCoffee()
                println("Adding milk")
                pourInCup()
                println("Your $type is ready!")
            }
            else -> {
                println("Sorry, this coffee machine only makes Espresso and Latte")
            }
        }
    }
}

// Premium implementation of the coffee machine
class PremiumCoffeeMachine : CoffeeMachine() {
    override fun makeCoffee(type: String) {
        println("\nPreparing $type with premium coffee machine:")
        
        when (type.lowercase()) {
            "espresso" -> {
                grindBeans(7)
                heatWater(94)
                brewCoffee()
                pourInCup()
                println("Your $type is ready!")
            }
            "latte" -> {
                grindBeans(7)
                heatWater(90)
                brewCoffee()
                steamMilk(60)
                pourInCup()
                println("Your $type is ready!")
            }
            "cappuccino" -> {
                grindBeans(7)
                heatWater(90)
                brewCoffee()
                steamMilk(65)
                addFoamedMilk()
                pourInCup()
                println("Your $type is ready!")
            }
            "mocha" -> {
                grindBeans(7)
                heatWater(90)
                addChocolateSyrup()
                brewCoffee()
                steamMilk(65)
                pourInCup()
                addWhippedCream()
                println("Your $type is ready!")
            }
            else -> {
                println("Sorry, this coffee machine doesn't make that type of coffee")
            }
        }
    }
    
    // Additional premium features
    private fun steamMilk(temperature: Int) {
        println("Steaming milk to $temperature°C")
    }
    
    private fun addFoamedMilk() {
        println("Adding foamed milk")
    }
    
    private fun addChocolateSyrup() {
        println("Adding chocolate syrup")
    }
    
    private fun addWhippedCream() {
        println("Topping with whipped cream")
    }
}

// Real-world example: Banking App
class BankingApp {
    // Public interface for the app - simple methods for users
    fun login(username: String, password: String) {
        println("\nAttempting to log in user: $username")
        // Hide complex authentication process
        if (authenticateUser(username, password)) {
            println("Login successful")
            fetchAccountData()
        } else {
            println("Login failed")
        }
    }
    
    fun checkBalance() {
        // Hide the complex data retrieval
        val balance = getAccountBalance()
        println("Current balance: $${balance}")
    }
    
    fun deposit(amount: Double) {
        println("Depositing $${amount}")
        // Hide the complex transaction processing
        if (processTransaction(TransactionType.DEPOSIT, amount)) {
            println("Deposit successful. New balance: $${getAccountBalance()}")
        } else {
            println("Deposit failed")
        }
    }
    
    fun withdraw(amount: Double) {
        println("Withdrawing $${amount}")
        // Hide the complex transaction processing
        if (processTransaction(TransactionType.WITHDRAWAL, amount)) {
            println("Withdrawal successful. New balance: $${getAccountBalance()}")
        } else {
            println("Withdrawal failed - insufficient funds")
        }
    }
    
    fun transfer(amount: Double, recipient: String) {
        println("Transferring $${amount} to $recipient")
        // Hide the complex transfer processing
        if (processTransfer(amount, recipient)) {
            println("Transfer successful. New balance: $${getAccountBalance()}")
        } else {
            println("Transfer failed")
        }
    }
    
    fun logout() {
        println("Logging out...")
        // Hide cleanup operations
        saveAccountState()
        clearSession()
        println("Logout successful")
    }
    
    // Private implementation details - hidden from users
    private var accountBalance = 1000.0 // Simulated balance
    private var loggedIn = false
    private val transactions = mutableListOf<Transaction>()
    
    private fun authenticateUser(username: String, password: String): Boolean {
        // Complex authentication logic hidden
        // In a real app, this would validate against a database
        loggedIn = true
        return true
    }
    
    private fun fetchAccountData() {
        // Complex data retrieval from multiple sources
        // Could include accounts, transactions, user preferences
    }
    
    private fun getAccountBalance(): Double {
        // Could involve complex calculations including pending transactions
        return accountBalance
    }
    
    private fun processTransaction(type: TransactionType, amount: Double): Boolean {
        // Transaction validation and processing
        when (type) {
            TransactionType.DEPOSIT -> {
                if (amount <= 0) return false
                accountBalance += amount
                recordTransaction(type, amount)
                return true
            }
            TransactionType.WITHDRAWAL -> {
                if (amount <= 0 || amount > accountBalance) return false
                accountBalance -= amount
                recordTransaction(type, amount)
                return true
            }
        }
    }
    
    private fun processTransfer(amount: Double, recipient: String): Boolean {
        // Complex transfer logic hidden
        if (amount <= 0 || amount > accountBalance) return false
        
        // Validate recipient, check for fraud, etc.
        
        accountBalance -= amount
        recordTransaction(TransactionType.WITHDRAWAL, amount, "Transfer to $recipient")
        return true
    }
    
    private fun recordTransaction(type: TransactionType, amount: Double, description: String = "") {
        // Add to transaction history
        transactions.add(Transaction(type, amount, System.currentTimeMillis(), description))
    }
    
    private fun saveAccountState() {
        // Save account data to persistent storage
    }
    
    private fun clearSession() {
        // Clear sensitive data from memory
        loggedIn = false
    }
    
    // Helper classes
    private enum class TransactionType {
        DEPOSIT, WITHDRAWAL
    }
    
    private data class Transaction(
        val type: TransactionType,
        val amount: Double,
        val timestamp: Long,
        val description: String = ""
    )
} 