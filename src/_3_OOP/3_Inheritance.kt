package _3_OOP

/**
 * This file demonstrates inheritance and interfaces in Kotlin
 * including abstract classes, interfaces, and their implementations.
 */
fun main() {
    println("===== INHERITANCE BASICS =====")
    
    // Creating and using derived class objects
    val cat = Cat("Whiskers", 3)
    cat.makeSound()
    cat.eat()
    cat.sleep()
    
    val dog = Dog("Rex", 5, "Labrador")
    dog.makeSound()
    dog.eat()
    dog.sleep()
    println("Dog breed: ${dog.breed}")
    
    println("\n===== ABSTRACT CLASSES =====")
    
    // Using abstract class implementations
    val circle = Circle(5.0)
    println("Circle area: ${circle.calculateArea()}")
    println("Circle perimeter: ${circle.calculatePerimeter()}")
    circle.draw()
    
    val rectangle = Rectangle(4.0, 6.0)
    println("Rectangle area: ${rectangle.calculateArea()}")
    println("Rectangle perimeter: ${rectangle.calculatePerimeter()}")
    rectangle.draw()
    
    println("\n===== INTERFACES =====")
    
    // Using interface implementations
    val car = Car("Toyota", "Corolla", 2020)
    car.start()
    car.drive()
    car.stop()
    
    val bicycle = Bicycle("Trek", "Mountain Bike", 21)
    bicycle.start()
    bicycle.drive()
    bicycle.stop()
    
    println("\n===== MULTIPLE INTERFACES =====")
    
    // Using a class that implements multiple interfaces
    val smartphone = Smartphone("Samsung", "Galaxy S21", "Android")
    smartphone.call("555-123-4567")
    smartphone.takePicture()
    smartphone.browse("https://www.example.com")
    
    println("\n===== INTERFACE DEFAULT METHODS =====")
    
    // Using interface with default implementations
    val simpleLogger = SimpleLogger()
    simpleLogger.debug("This is a debug message")
    simpleLogger.info("This is an info message")
    simpleLogger.warn("This is a warning message")
    simpleLogger.error("This is an error message")
    
    val fileLogger = FileLogger()
    fileLogger.debug("This is a debug message")
    fileLogger.info("This is an info message")
    fileLogger.warn("This is a warning message")
    fileLogger.error("This is an error message")
    
    println("\n===== REAL-WORLD EXAMPLE: PAYMENT SYSTEM =====")
    
    // Create payment processor
    val paymentProcessor = PaymentProcessor()
    
    // Process different types of payments
    val creditCardPayment = CreditCardPayment(
        amount = 99.99,
        cardNumber = "4111-1111-1111-1111",
        cardHolder = "John Doe",
        expirationDate = "12/25",
        cvv = "123"
    )
    paymentProcessor.processPayment(creditCardPayment)
    
    val payPalPayment = PayPalPayment(
        amount = 49.99,
        email = "john.doe@example.com",
        password = "****"
    )
    paymentProcessor.processPayment(payPalPayment)
    
    val bankTransferPayment = BankTransferPayment(
        amount = 199.99,
        accountNumber = "1234567890",
        bankCode = "ABCDEF"
    )
    paymentProcessor.processPayment(bankTransferPayment)
    
    // Show payment history
    paymentProcessor.showPaymentHistory()
}

// Base class
open class Animal(val name: String, val age: Int) {
    // Open function that can be overridden by derived classes
    open fun makeSound() {
        println("The animal makes a sound")
    }
    
    // Functions that are not open cannot be overridden
    fun sleep() {
        println("$name is sleeping")
    }
    
    open fun eat() {
        println("$name is eating")
    }
}

// Derived class
class Cat(name: String, age: Int) : Animal(name, age) {
    // Override functions from the base class
    override fun makeSound() {
        println("$name meows")
    }
    
    override fun eat() {
        println("$name is eating fish")
    }
}

// Another derived class with additional properties
class Dog(name: String, age: Int, val breed: String) : Animal(name, age) {
    // Override functions from the base class
    override fun makeSound() {
        println("$name barks")
    }
    
    override fun eat() {
        println("$name is eating bone")
    }
}

// Abstract class
abstract class Shape {
    // Abstract properties
    abstract val name: String
    
    // Abstract methods
    abstract fun calculateArea(): Double
    abstract fun calculatePerimeter(): Double
    
    // Concrete method
    fun draw() {
        println("Drawing a $name")
    }
}

// Concrete implementation of the abstract class
class Circle(val radius: Double) : Shape() {
    override val name: String = "Circle"
    
    override fun calculateArea(): Double {
        return Math.PI * radius * radius
    }
    
    override fun calculatePerimeter(): Double {
        return 2 * Math.PI * radius
    }
}

// Another concrete implementation
class Rectangle(val width: Double, val height: Double) : Shape() {
    override val name: String = "Rectangle"
    
    override fun calculateArea(): Double {
        return width * height
    }
    
    override fun calculatePerimeter(): Double {
        return 2 * (width + height)
    }
}

// Interface definition
interface Vehicle {
    val make: String
    val model: String
    
    fun start()
    fun stop()
    fun drive()
}

// Interface implementation
class Car(
    override val make: String,
    override val model: String,
    val year: Int
) : Vehicle {
    override fun start() {
        println("$make $model: Starting the engine")
    }
    
    override fun stop() {
        println("$make $model: Stopping the engine")
    }
    
    override fun drive() {
        println("$make $model: Driving on the road")
    }
}

// Another interface implementation
class Bicycle(
    override val make: String,
    override val model: String,
    val gears: Int
) : Vehicle {
    override fun start() {
        println("$make $model: Getting on the bicycle")
    }
    
    override fun stop() {
        println("$make $model: Getting off the bicycle")
    }
    
    override fun drive() {
        println("$make $model: Pedaling with $gears gears")
    }
}

// Multiple interfaces
interface Phone {
    fun call(number: String)
}

interface Camera {
    fun takePicture()
}

interface WebBrowser {
    fun browse(url: String)
}

// Class implementing multiple interfaces
class Smartphone(
    val brand: String,
    val model: String,
    val os: String
) : Phone, Camera, WebBrowser {
    override fun call(number: String) {
        println("$brand $model: Calling $number")
    }
    
    override fun takePicture() {
        println("$brand $model: Taking a picture")
    }
    
    override fun browse(url: String) {
        println("$brand $model: Browsing $url on $os")
    }
}

// Interface with default method implementations
interface Logger {
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
    
    // Default method
    fun log(level: String, message: String) {
        println("[$level] $message")
    }
}

// Minimal implementation - uses default behavior
class SimpleLogger : Logger {
    override fun debug(message: String) {
        log("DEBUG", message)
    }
    
    override fun info(message: String) {
        log("INFO", message)
    }
    
    override fun warn(message: String) {
        log("WARNING", message)
    }
    
    override fun error(message: String) {
        log("ERROR", message)
    }
}

// Extended implementation - overrides the default behavior
class FileLogger : Logger {
    override fun debug(message: String) {
        // In a real implementation, this would write to a file
        println("[FILE:DEBUG] $message")
    }
    
    override fun info(message: String) {
        println("[FILE:INFO] $message")
    }
    
    override fun warn(message: String) {
        println("[FILE:WARNING] $message")
    }
    
    override fun error(message: String) {
        println("[FILE:ERROR] $message")
    }
    
    override fun log(level: String, message: String) {
        // Override the default implementation
        println("[FILE:$level] $message")
    }
}

// Real-world example: Payment system
abstract class Payment(val amount: Double) {
    abstract fun process(): Boolean
    abstract fun getReceipt(): String
    
    fun formatAmount(): String {
        return "%.2f".format(amount)
    }
}

class CreditCardPayment(
    amount: Double,
    private val cardNumber: String,
    private val cardHolder: String,
    private val expirationDate: String,
    private val cvv: String
) : Payment(amount) {
    override fun process(): Boolean {
        // In a real system, this would call a payment gateway API
        println("Processing credit card payment of $${formatAmount()}")
        println("Card: ${maskCardNumber(cardNumber)}, Holder: $cardHolder, Exp: $expirationDate")
        return true
    }
    
    override fun getReceipt(): String {
        return "Credit Card Payment - Amount: $${formatAmount()}, Card: ${maskCardNumber(cardNumber)}"
    }
    
    private fun maskCardNumber(cardNumber: String): String {
        val cleaned = cardNumber.replace("-", "").replace(" ", "")
        return "**** **** **** " + cleaned.takeLast(4)
    }
}

class PayPalPayment(
    amount: Double,
    private val email: String,
    private val password: String
) : Payment(amount) {
    override fun process(): Boolean {
        // In a real system, this would call PayPal API
        println("Processing PayPal payment of $${formatAmount()}")
        println("Account: $email")
        return true
    }
    
    override fun getReceipt(): String {
        return "PayPal Payment - Amount: $${formatAmount()}, Account: $email"
    }
}

class BankTransferPayment(
    amount: Double,
    private val accountNumber: String,
    private val bankCode: String
) : Payment(amount) {
    override fun process(): Boolean {
        // In a real system, this would call bank API
        println("Processing bank transfer payment of $${formatAmount()}")
        println("Account: $accountNumber, Bank: $bankCode")
        return true
    }
    
    override fun getReceipt(): String {
        return "Bank Transfer Payment - Amount: $${formatAmount()}, Account: ${maskAccountNumber(accountNumber)}"
    }
    
    private fun maskAccountNumber(accountNumber: String): String {
        val cleaned = accountNumber.replace("-", "").replace(" ", "")
        return "******" + cleaned.takeLast(4)
    }
}

class PaymentProcessor {
    private val payments = mutableListOf<Payment>()
    
    fun processPayment(payment: Payment) {
        println("\nProcessing payment...")
        val success = payment.process()
        
        if (success) {
            payments.add(payment)
            println("Payment successful!")
            println("Receipt: ${payment.getReceipt()}")
        } else {
            println("Payment failed!")
        }
    }
    
    fun showPaymentHistory() {
        println("\nPayment History:")
        if (payments.isEmpty()) {
            println("No payments processed yet.")
            return
        }
        
        var totalAmount = 0.0
        payments.forEachIndexed { index, payment ->
            println("${index + 1}. ${payment.getReceipt()}")
            totalAmount += payment.amount
        }
        
        println("\nTotal payments: ${payments.size}")
        println("Total amount: $${String.format("%.2f", totalAmount)}")
    }
} 