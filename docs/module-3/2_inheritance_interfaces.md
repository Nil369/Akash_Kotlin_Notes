# Inheritance and Interfaces in Kotlin

## Inheritance

### Basic Inheritance
```kotlin
open class Animal(val name: String) {
    open fun makeSound() {
        println("Some sound")
    }
}

class Dog(name: String) : Animal(name) {
    override fun makeSound() {
        println("Woof!")
    }
}

// Usage
val dog = Dog("Rex")
dog.makeSound()  // Woof!
```

### Multiple Constructors in Inheritance
```kotlin
open class Vehicle(val brand: String, val model: String) {
    var year: Int = 2020
    
    constructor(brand: String, model: String, year: Int) : this(brand, model) {
        this.year = year
    }
}

class Car(brand: String, model: String, year: Int) : Vehicle(brand, model, year) {
    fun start() {
        println("Starting $brand $model ($year)")
    }
}

// Usage
val car = Car("Toyota", "Camry", 2020)
car.start()  // Starting Toyota Camry (2020)
```

### Abstract Classes
```kotlin
abstract class Shape {
    abstract fun calculateArea(): Double
    abstract fun calculatePerimeter(): Double
    
    fun printInfo() {
        println("Area: ${calculateArea()}")
        println("Perimeter: ${calculatePerimeter()}")
    }
}

class Circle(private val radius: Double) : Shape() {
    override fun calculateArea(): Double = Math.PI * radius * radius
    override fun calculatePerimeter(): Double = 2 * Math.PI * radius
}

// Usage
val circle = Circle(5.0)
circle.printInfo()
```

## Interfaces

### Basic Interface
```kotlin
interface Drawable {
    fun draw()
    fun erase()
}

class Circle : Drawable {
    override fun draw() {
        println("Drawing a circle")
    }
    
    override fun erase() {
        println("Erasing a circle")
    }
}

// Usage
val circle = Circle()
circle.draw()   // Drawing a circle
circle.erase()  // Erasing a circle
```

### Interface with Default Implementation
```kotlin
interface Logger {
    fun log(message: String)
    fun logError(error: String) {
        log("ERROR: $error")
    }
}

class FileLogger : Logger {
    override fun log(message: String) {
        println("Writing to file: $message")
    }
}

// Usage
val logger = FileLogger()
logger.log("Application started")
logger.logError("Connection failed")  // Uses default implementation
```

## Real-world Examples

### 1. Payment Processing System
```kotlin
abstract class PaymentMethod {
    abstract fun processPayment(amount: Double): Boolean
    abstract fun refund(transactionId: String): Boolean
}

class CreditCardPayment(
    private val cardNumber: String,
    private val expiryDate: String
) : PaymentMethod() {
    override fun processPayment(amount: Double): Boolean {
        println("Processing credit card payment of $amount")
        return true
    }
    
    override fun refund(transactionId: String): Boolean {
        println("Refunding transaction $transactionId")
        return true
    }
}

class PayPalPayment(
    private val email: String
) : PaymentMethod() {
    override fun processPayment(amount: Double): Boolean {
        println("Processing PayPal payment of $amount")
        return true
    }
    
    override fun refund(transactionId: String): Boolean {
        println("Refunding transaction $transactionId")
        return true
    }
}

// Usage
val creditCard = CreditCardPayment("1234-5678-9012-3456", "12/25")
val paypal = PayPalPayment("user@example.com")

creditCard.processPayment(100.0)
paypal.processPayment(50.0)
```

### 2. Media Player System
```kotlin
interface MediaPlayer {
    fun play()
    fun pause()
    fun stop()
    fun setVolume(level: Int)
}

abstract class AudioPlayer : MediaPlayer {
    protected var isPlaying = false
    protected var volume = 50
    
    override fun play() {
        isPlaying = true
        println("Playing audio")
    }
    
    override fun pause() {
        isPlaying = false
        println("Paused audio")
    }
    
    override fun stop() {
        isPlaying = false
        println("Stopped audio")
    }
    
    override fun setVolume(level: Int) {
        volume = level.coerceIn(0, 100)
        println("Volume set to $volume")
    }
}

class MP3Player : AudioPlayer() {
    fun loadMP3(file: String) {
        println("Loading MP3 file: $file")
    }
}

class WAVPlayer : AudioPlayer() {
    fun loadWAV(file: String) {
        println("Loading WAV file: $file")
    }
}

// Usage
val mp3Player = MP3Player()
mp3Player.loadMP3("song.mp3")
mp3Player.play()
mp3Player.setVolume(75)
```

### 3. Database Access System
```kotlin
interface Database {
    fun connect()
    fun disconnect()
    fun query(sql: String): List<Map<String, Any>>
}

abstract class SQLDatabase : Database {
    protected var isConnected = false
    
    override fun connect() {
        isConnected = true
        println("Connected to database")
    }
    
    override fun disconnect() {
        isConnected = false
        println("Disconnected from database")
    }
    
    override fun query(sql: String): List<Map<String, Any>> {
        if (!isConnected) {
            throw IllegalStateException("Not connected to database")
        }
        println("Executing query: $sql")
        return emptyList()
    }
}

class MySQLDatabase : SQLDatabase() {
    fun setCharacterSet(charset: String) {
        println("Setting character set to $charset")
    }
}

class PostgreSQLDatabase : SQLDatabase() {
    fun setSchema(schema: String) {
        println("Setting schema to $schema")
    }
}

// Usage
val mysql = MySQLDatabase()
mysql.connect()
mysql.setCharacterSet("utf8mb4")
mysql.query("SELECT * FROM users")
mysql.disconnect()
```

## Practice Exercises

1. Create a hierarchy of `Vehicle` classes (Car, Motorcycle, Bicycle)
2. Implement a `Shape` interface with various geometric shapes
3. Create a `Database` interface with different database implementations
4. Design a `Payment` system with various payment methods
5. Implement a `Media` player system with different media types

Remember:
- Use `open` keyword for classes that can be inherited
- Use `override` keyword for overridden methods
- Interfaces can have default implementations
- Abstract classes can have both abstract and concrete methods
- Consider using sealed classes for restricted inheritance hierarchies
- Always follow the Liskov Substitution Principle 