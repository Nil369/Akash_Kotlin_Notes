package _3_OOP

/**
 * This file demonstrates polymorphism in Kotlin
 * including method overriding, overloading, and runtime polymorphism.
 */
fun main() {
    println("===== COMPILE-TIME POLYMORPHISM (METHOD OVERLOADING) =====")
    
    // Method overloading example
    val calculator = Calculator()
    
    // Using different versions of the same method
    println("Adding two integers: ${calculator.add(5, 7)}")
    println("Adding three integers: ${calculator.add(5, 7, 3)}")
    println("Adding two doubles: ${calculator.add(5.2, 7.8)}")
    println("Adding a string and an integer: ${calculator.add("5", 7)}")
    
    println("\n===== RUNTIME POLYMORPHISM (METHOD OVERRIDING) =====")
    
    // Creating different shapes
    val circle = Circle(5.0)
    val rectangle = Rectangle(4.0, 6.0)
    val triangle = Triangle(3.0, 4.0, 5.0)
    
    // Using polymorphism with a list of different shapes
    val shapes = listOf<Shape>(circle, rectangle, triangle)
    
    println("Shape details:")
    for (shape in shapes) {
        // Same method call, different implementations (polymorphism)
        println("- ${shape.name}: Area = ${shape.calculateArea()}, Perimeter = ${shape.calculatePerimeter()}")
    }
    
    println("\n===== POLYMORPHISM WITH INTERFACES =====")
    
    // Creating objects that implement the Drawable interface
    val canvas = Canvas()
    canvas.addElement(CircleElement(10.0, 20.0, 15.0))
    canvas.addElement(RectangleElement(30.0, 40.0, 25.0, 35.0))
    canvas.addElement(TextElement(50.0, 60.0, "Hello, Kotlin!"))
    
    // Drawing all elements - polymorphic behavior
    canvas.drawAllElements()
    
    println("\n===== POLYMORPHISM WITH ABSTRACT CLASSES =====")
    
    // Creating different vehicles
    val car = Car("Toyota", "Corolla", 180.0)
    val bicycle = Bicycle("Trek", "Mountain Bike", 25.0)
    val boat = Boat("Yamaha", "Speedboat", 120.0)
    
    // Using polymorphism with a list of different vehicles
    val vehicles = listOf<Vehicle>(car, bicycle, boat)
    
    println("Vehicle details:")
    for (vehicle in vehicles) {
        println("- ${vehicle.brand} ${vehicle.model}")
        vehicle.startEngine()
        println("  Max Speed: ${vehicle.maxSpeed} km/h")
        println("  Travel method: ${vehicle.move()}")
        vehicle.stopEngine()
        println()
    }
    
    println("\n===== REAL-WORLD EXAMPLE: NOTIFICATION SYSTEM =====")
    
    // Creating different types of notifications
    val emailNotification = EmailNotification("Welcome to our service!", "john@example.com")
    val smsNotification = SMSNotification("Your verification code is 1234", "+123456789")
    val pushNotification = PushNotification("New message received", "chatapp://messages/123")
    
    // Using polymorphism to send different types of notifications
    val notificationService = NotificationService()
    notificationService.sendNotification(emailNotification)
    notificationService.sendNotification(smsNotification)
    notificationService.sendNotification(pushNotification)
    
    // Batch sending with polymorphism
    val notifications = listOf<Notification>(
        EmailNotification("Your order is confirmed", "alice@example.com"),
        SMSNotification("Your order #A123 has shipped", "+987654321"),
        PushNotification("Special offer just for you!", "storeapp://offers/75")
    )
    
    notificationService.sendBatchNotifications(notifications)
    
    println("\n===== REAL-WORLD EXAMPLE: PAYMENT PROCESSING =====")
    
    // Creating different payment methods
    val creditCardPayment = CreditCardPayment(125.50, "1234-5678-9012-3456")
    val payPalPayment = PayPalPayment(75.25, "user@example.com")
    val cryptoPayment = CryptoPayment(250.00, "0x742d35Cc6634C0532925a3b844Bc454e4438f44e")
    
    // Process different payment types polymorphically
    val paymentProcessor = PaymentProcessor()
    
    val transactions = listOf<Payment>(creditCardPayment, payPalPayment, cryptoPayment)
    var totalProcessed = 0.0
    
    println("Processing payments:")
    for (transaction in transactions) {
        if (paymentProcessor.processPayment(transaction)) {
            totalProcessed += transaction.amount
        }
    }
    
    println("\nTotal payment processed: $${totalProcessed}")
}

// Method overloading example (Compile-time polymorphism)
class Calculator {
    // Multiple methods with the same name but different parameters
    fun add(a: Int, b: Int): Int {
        return a + b
    }
    
    fun add(a: Int, b: Int, c: Int): Int {
        return a + b + c
    }
    
    fun add(a: Double, b: Double): Double {
        return a + b
    }
    
    fun add(a: String, b: Int): String {
        return a + b
    }
}

// Runtime polymorphism example with an abstract class
abstract class Shape {
    abstract val name: String
    
    // Abstract methods that subclasses must implement
    abstract fun calculateArea(): Double
    abstract fun calculatePerimeter(): Double
    
    // Concrete method that can be inherited
    fun getInfo(): String {
        return "This is a $name with area ${calculateArea()} and perimeter ${calculatePerimeter()}"
    }
}

// Circle implementation
class Circle(private val radius: Double) : Shape() {
    override val name = "Circle"
    
    override fun calculateArea(): Double {
        return Math.PI * radius * radius
    }
    
    override fun calculatePerimeter(): Double {
        return 2 * Math.PI * radius
    }
}

// Rectangle implementation
class Rectangle(private val width: Double, private val height: Double) : Shape() {
    override val name = "Rectangle"
    
    override fun calculateArea(): Double {
        return width * height
    }
    
    override fun calculatePerimeter(): Double {
        return 2 * (width + height)
    }
}

// Triangle implementation
class Triangle(private val a: Double, private val b: Double, private val c: Double) : Shape() {
    override val name = "Triangle"
    
    override fun calculateArea(): Double {
        // Using Heron's formula
        val s = (a + b + c) / 2
        return Math.sqrt(s * (s - a) * (s - b) * (s - c))
    }
    
    override fun calculatePerimeter(): Double {
        return a + b + c
    }
}

// Polymorphism with interfaces
interface Drawable {
    val x: Double
    val y: Double
    
    fun draw()
}

// Circle element implementing Drawable
class CircleElement(
    override val x: Double, 
    override val y: Double,
    private val radius: Double
) : Drawable {
    override fun draw() {
        println("Drawing a circle at ($x, $y) with radius $radius")
    }
}

// Rectangle element implementing Drawable
class RectangleElement(
    override val x: Double, 
    override val y: Double,
    private val width: Double,
    private val height: Double
) : Drawable {
    override fun draw() {
        println("Drawing a rectangle at ($x, $y) with width $width and height $height")
    }
}

// Text element implementing Drawable
class TextElement(
    override val x: Double, 
    override val y: Double,
    private val text: String
) : Drawable {
    override fun draw() {
        println("Drawing text at ($x, $y): \"$text\"")
    }
}

// Canvas that works with drawable elements
class Canvas {
    private val elements = mutableListOf<Drawable>()
    
    fun addElement(element: Drawable) {
        elements.add(element)
    }
    
    fun drawAllElements() {
        println("Drawing all elements:")
        for (element in elements) {
            element.draw() // Polymorphic call
        }
    }
}

// Abstract Vehicle class for polymorphism example
abstract class Vehicle(val brand: String, val model: String, val maxSpeed: Double) {
    // Abstract methods
    abstract fun startEngine()
    abstract fun stopEngine()
    abstract fun move(): String
}

// Car implementation
class Car(brand: String, model: String, maxSpeed: Double) : Vehicle(brand, model, maxSpeed) {
    override fun startEngine() {
        println("  Car engine started with ignition key")
    }
    
    override fun stopEngine() {
        println("  Car engine stopped")
    }
    
    override fun move(): String {
        return "Driving on roads"
    }
}

// Bicycle implementation
class Bicycle(brand: String, model: String, maxSpeed: Double) : Vehicle(brand, model, maxSpeed) {
    override fun startEngine() {
        println("  Bicycle doesn't have an engine - ready to pedal")
    }
    
    override fun stopEngine() {
        println("  Bicycle stopped pedaling")
    }
    
    override fun move(): String {
        return "Pedaling on roads or trails"
    }
}

// Boat implementation
class Boat(brand: String, model: String, maxSpeed: Double) : Vehicle(brand, model, maxSpeed) {
    override fun startEngine() {
        println("  Boat engine started with pull cord")
    }
    
    override fun stopEngine() {
        println("  Boat engine stopped")
    }
    
    override fun move(): String {
        return "Sailing on water"
    }
}

// Real-world example: Notification system
abstract class Notification {
    abstract val message: String
    abstract fun send(): Boolean
    
    // Common functionality
    fun prepareMessage(): String {
        return "NOTIFICATION: $message"
    }
}

class EmailNotification(
    override val message: String,
    private val recipient: String
) : Notification() {
    override fun send(): Boolean {
        println("Sending email to $recipient:")
        println("Subject: New Notification")
        println("Body: ${prepareMessage()}")
        // Simulate email sending
        return true
    }
}

class SMSNotification(
    override val message: String,
    private val phoneNumber: String
) : Notification() {
    override fun send(): Boolean {
        println("Sending SMS to $phoneNumber:")
        println("Content: ${prepareMessage()}")
        // Simulate SMS sending
        return true
    }
}

class PushNotification(
    override val message: String,
    private val deviceToken: String
) : Notification() {
    override fun send(): Boolean {
        println("Sending push notification to device:")
        println("Token: $deviceToken")
        println("Alert: ${prepareMessage()}")
        // Simulate push notification
        return true
    }
}

class NotificationService {
    fun sendNotification(notification: Notification): Boolean {
        println("\nPreparing to send notification...")
        return notification.send()
    }
    
    fun sendBatchNotifications(notifications: List<Notification>) {
        println("\nSending batch notifications:")
        for (notification in notifications) {
            notification.send()
            println("---")
        }
        println("Batch sending completed")
    }
}

// Real-world example: Payment processing
abstract class Payment(val amount: Double) {
    abstract fun validate(): Boolean
    abstract fun process(): Boolean
    abstract fun getReceipt(): String
}

class CreditCardPayment(
    amount: Double,
    private val cardNumber: String
) : Payment(amount) {
    override fun validate(): Boolean {
        // Simulated validation for credit card
        val isValid = cardNumber.length >= 16
        println("Validating credit card: ${if (isValid) "valid" else "invalid"}")
        return isValid
    }
    
    override fun process(): Boolean {
        println("Processing credit card payment of $$amount")
        println("Card: ${maskCardNumber(cardNumber)}")
        return true
    }
    
    override fun getReceipt(): String {
        return "Credit Card Payment Receipt\nAmount: $$amount\nCard: ${maskCardNumber(cardNumber)}"
    }
    
    private fun maskCardNumber(number: String): String {
        val visible = 4
        return if (number.length > visible) {
            "*".repeat(number.length - visible) + number.takeLast(visible)
        } else {
            number
        }
    }
}

class PayPalPayment(
    amount: Double,
    private val email: String
) : Payment(amount) {
    override fun validate(): Boolean {
        // Simulated validation for PayPal
        val isValid = email.contains("@")
        println("Validating PayPal account: ${if (isValid) "valid" else "invalid"}")
        return isValid
    }
    
    override fun process(): Boolean {
        println("Processing PayPal payment of $$amount")
        println("Account: $email")
        return true
    }
    
    override fun getReceipt(): String {
        return "PayPal Payment Receipt\nAmount: $$amount\nAccount: $email"
    }
}

class CryptoPayment(
    amount: Double,
    private val walletAddress: String
) : Payment(amount) {
    override fun validate(): Boolean {
        // Simulated validation for crypto wallet
        val isValid = walletAddress.startsWith("0x") && walletAddress.length > 10
        println("Validating crypto wallet: ${if (isValid) "valid" else "invalid"}")
        return isValid
    }
    
    override fun process(): Boolean {
        println("Processing cryptocurrency payment of $$amount")
        println("Wallet: ${walletAddress.take(6)}...${walletAddress.takeLast(4)}")
        return true
    }
    
    override fun getReceipt(): String {
        return "Crypto Payment Receipt\nAmount: $$amount\nWallet: ${walletAddress.take(6)}...${walletAddress.takeLast(4)}"
    }
}

class PaymentProcessor {
    fun processPayment(payment: Payment): Boolean {
        // Polymorphic behavior - each payment type processed differently
        if (!payment.validate()) {
            println("Payment validation failed")
            return false
        }
        
        val success = payment.process()
        if (success) {
            println("Payment successful!")
            println("Receipt:\n${payment.getReceipt()}")
        } else {
            println("Payment processing failed")
        }
        
        return success
    }
} 