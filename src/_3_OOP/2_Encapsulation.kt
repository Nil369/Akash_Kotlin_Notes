package _3_OOP

/**
 * This file demonstrates encapsulation in Kotlin
 * including visibility modifiers, getters, setters, and best practices.
 */
fun main() {
    println("===== BASIC ENCAPSULATION =====")
    
    // Creating a bank account with encapsulation
    val account = BankAccount("12345", "John Doe", 1000.0)
    
    // Accessing public properties
    println("Account Number: ${account.accountNumber}")
    println("Account Holder: ${account.accountHolder}")
    
    // Cannot directly access balance (private property)
    // println(account.balance) // This would cause a compilation error
    
    // Instead, use public methods to interact with private data
    println("Current Balance: $${account.getBalance()}")
    
    // Deposit money
    account.deposit(500.0)
    println("Balance after deposit: $${account.getBalance()}")
    
    // Withdraw money
    val withdrawalSuccess = account.withdraw(200.0)
    if (withdrawalSuccess) {
        println("Withdrawal successful")
    } else {
        println("Withdrawal failed")
    }
    println("Balance after withdrawal: $${account.getBalance()}")
    
    // Try to withdraw more than balance
    val largeWithdrawalSuccess = account.withdraw(2000.0)
    if (largeWithdrawalSuccess) {
        println("Large withdrawal successful")
    } else {
        println("Large withdrawal failed")
    }
    println("Final balance: $${account.getBalance()}")
    
    println("\n===== ENCAPSULATION WITH PROPERTIES =====")
    
    // Creating a user with property encapsulation
    val user = User("johndoe", "password123", "john@example.com")
    
    // Access properties (getters are automatically called)
    println("Username: ${user.username}")
    println("Email: ${user.email}")
    
    // Try to print password - only gets the masked version
    println("Password: ${user.password}") // Shows masked password
    
    // Update email using setter
    user.email = "john.doe@company.com"
    println("Updated email: ${user.email}")
    
    // Try to set invalid email
    user.email = "invalid"
    println("Email after invalid update attempt: ${user.email}")
    
    // Try to set invalid username
    // user.username = "a" // This would cause an error as username is read-only

    println("\n===== BACKING PROPERTIES =====")
    
    // Using a class with backing properties
    val profile = UserProfile("Alice", 25)
    println("Name: ${profile.name}")
    println("Age: ${profile.age}")
    
    profile.age = 26
    println("Updated age: ${profile.age}")
    
    profile.age = -5 // Invalid age
    println("Age after invalid update: ${profile.age}")
    
    println("\n===== REAL-WORLD EXAMPLE: E-COMMERCE PRODUCT =====")
    
    // Creating a product with encapsulation
    val product = Product("P123", "Smartphone", 799.99, 10)
    
    // Display product details
    product.displayDetails()
    
    // Update stock
    product.updateStock(5) // Add 5 units
    product.updateStock(-3) // Remove 3 units
    
    // Try to set negative stock
    product.updateStock(-20) // Should fail
    
    // Apply discount
    product.applyDiscount(10.0) // 10% discount
    product.displayDetails()
    
    // Try to apply invalid discount
    product.applyDiscount(110.0) // More than 100% discount should fail
    product.displayDetails()
    
    println("\n===== REAL-WORLD EXAMPLE: TEMPERATURE CONVERTER =====")
    
    // Using a temperature converter with encapsulation
    val tempConverter = TemperatureConverter(32.0, TemperatureUnit.FAHRENHEIT)
    
    // Display temperature in both units
    tempConverter.displayTemperature()
    
    // Update temperature
    tempConverter.celsiusValue = 25.0 // Set in Celsius
    tempConverter.displayTemperature()
    
    // Update unit preference
    tempConverter.unit = TemperatureUnit.CELSIUS
    tempConverter.displayTemperature()
    
    // Update to Fahrenheit again
    tempConverter.fahrenheitValue = 68.0 // Set in Fahrenheit
    tempConverter.displayTemperature()
}

// Basic encapsulation example
class BankAccount(
    val accountNumber: String,  // Public (immutable)
    val accountHolder: String,  // Public (immutable)
    private var balance: Double // Private (mutable)
) {
    // Public methods to access and modify private data
    fun getBalance(): Double {
        return balance
    }
    
    fun deposit(amount: Double) {
        if (amount > 0) {
            balance += amount
            println("Deposited $${amount}")
        } else {
            println("Cannot deposit negative amount")
        }
    }
    
    fun withdraw(amount: Double): Boolean {
        return if (amount > 0 && amount <= balance) {
            balance -= amount
            println("Withdrew $${amount}")
            true
        } else {
            println("Cannot withdraw $${amount}")
            false
        }
    }
}

// Encapsulation with Kotlin properties
class User(
    // Read-only property (immutable)
    val username: String,
    
    // Private property with custom getter (data hiding)
    private val _password: String,
    
    // Mutable property with custom setter (validation)
    private var _email: String
) {
    // Custom getter for password that masks the actual value
    val password: String
        get() = "*".repeat(_password.length)
    
    // Property with custom getter and setter
    var email: String
        get() = _email
        set(value) {
            if (value.contains("@") && value.contains(".")) {
                _email = value
                println("Email updated successfully")
            } else {
                println("Invalid email format")
            }
        }
    
    // Private method for internal use
    private fun validatePassword(input: String): Boolean {
        return input == _password
    }
    
    // Public method using private data
    fun login(inputUsername: String, inputPassword: String): Boolean {
        return inputUsername == username && validatePassword(inputPassword)
    }
}

// Using backing properties for encapsulation
class UserProfile(
    nameInput: String,
    ageInput: Int
) {
    // Backing property with validation
    private var _name = nameInput
    val name: String
        get() = _name
    
    // Backing property with custom setter and validation
    private var _age = if (ageInput > 0) ageInput else 0
    var age: Int
        get() = _age
        set(value) {
            if (value > 0) {
                _age = value
                println("Age updated to $value")
            } else {
                println("Age must be positive")
            }
        }
}

// Real-world example: E-commerce product
class Product(
    val id: String,
    val name: String,
    private var _price: Double,
    private var _stockQuantity: Int
) {
    // Property with custom getter but no direct setter
    val price: Double
        get() = _price
    
    // Property with custom getter but no direct setter
    val stockQuantity: Int
        get() = _stockQuantity
    
    // Property to check if in stock
    val isInStock: Boolean
        get() = _stockQuantity > 0
    
    // Display product details
    fun displayDetails() {
        println("Product: $name (ID: $id)")
        println("Price: $${price}")
        println("In stock: ${if (isInStock) "Yes ($stockQuantity units)" else "No"}")
    }
    
    // Update stock quantity
    fun updateStock(quantityChange: Int) {
        val newQuantity = _stockQuantity + quantityChange
        if (newQuantity >= 0) {
            _stockQuantity = newQuantity
            println("Stock updated. New quantity: $_stockQuantity")
        } else {
            println("Cannot reduce stock below 0")
        }
    }
    
    // Apply discount
    fun applyDiscount(percentageDiscount: Double) {
        if (percentageDiscount in 0.0..100.0) {
            val discountAmount = _price * (percentageDiscount / 100.0)
            _price -= discountAmount
            println("${percentageDiscount}% discount applied. New price: $${_price}")
        } else {
            println("Invalid discount percentage")
        }
    }
    
    // Private helper method
    private fun calculateTax(taxRate: Double): Double {
        return _price * (taxRate / 100.0)
    }
    
    // Public method using private helper
    fun getPriceWithTax(taxRate: Double): Double {
        return _price + calculateTax(taxRate)
    }
}

// Real-world example: Temperature converter
enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT
}

class TemperatureConverter(initialTemp: Double, initialUnit: TemperatureUnit) {
    // Backing property for the current temperature
    private var _temperature: Double = initialTemp
    
    // Backing property for the unit
    private var _unit: TemperatureUnit = initialUnit
    
    // Public property for the unit with custom setter
    var unit: TemperatureUnit
        get() = _unit
        set(value) {
            _unit = value
            println("Unit changed to ${_unit.name}")
        }
    
    // Property for Celsius with custom getter and setter
    var celsiusValue: Double
        get() = if (_unit == TemperatureUnit.CELSIUS) _temperature else fahrenheitToCelsius(_temperature)
        set(value) {
            _temperature = if (_unit == TemperatureUnit.CELSIUS) value else celsiusToFahrenheit(value)
            println("Temperature set to ${_temperature}° ${_unit.name}")
        }
    
    // Property for Fahrenheit with custom getter and setter
    var fahrenheitValue: Double
        get() = if (_unit == TemperatureUnit.FAHRENHEIT) _temperature else celsiusToFahrenheit(_temperature)
        set(value) {
            _temperature = if (_unit == TemperatureUnit.FAHRENHEIT) value else fahrenheitToCelsius(value)
            println("Temperature set to ${_temperature}° ${_unit.name}")
        }
    
    // Private conversion methods
    private fun celsiusToFahrenheit(celsius: Double): Double {
        return celsius * 9.0 / 5.0 + 32.0
    }
    
    private fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32.0) * 5.0 / 9.0
    }
    
    // Display temperature in both units
    fun displayTemperature() {
        val celsius = celsiusValue
        val fahrenheit = fahrenheitValue
        println("Current temperature:")
        println("- ${celsius.format(1)}°C")
        println("- ${fahrenheit.format(1)}°F")
        println("Current unit: ${_unit.name}")
    }
    
    // Helper extension function to format doubles
    private fun Double.format(digits: Int): String = "%.${digits}f".format(this)
} 