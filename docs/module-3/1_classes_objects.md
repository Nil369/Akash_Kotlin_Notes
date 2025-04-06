# Classes and Objects in Kotlin

## Basic Class Declaration

### Simple Class
```kotlin
class Person {
    var name: String = ""
    var age: Int = 0
    
    fun introduce() {
        println("Hello, I'm $name and I'm $age years old")
    }
}

// Usage
val person = Person()
person.name = "John"
person.age = 25
person.introduce()  // Hello, I'm John and I'm 25 years old
```

### Primary Constructor
```kotlin
class Car(val brand: String, val model: String, var year: Int) {
    fun start() {
        println("Starting $brand $model ($year)")
    }
}

// Usage
val car = Car("Toyota", "Camry", 2020)
car.start()  // Starting Toyota Camry (2020)
```

### Secondary Constructor
```kotlin
class Student(val name: String) {
    var grade: Int = 0
    var major: String = ""
    
    constructor(name: String, grade: Int) : this(name) {
        this.grade = grade
    }
    
    constructor(name: String, grade: Int, major: String) : this(name, grade) {
        this.major = major
    }
    
    fun displayInfo() {
        println("Name: $name, Grade: $grade, Major: $major")
    }
}

// Usage
val student1 = Student("Alice")  // Only name
val student2 = Student("Bob", 12)  // Name and grade
val student3 = Student("Charlie", 12, "Computer Science")  // All parameters
```

## Properties

### Backing Fields
```kotlin
class BankAccount {
    private var _balance: Double = 0.0
    val balance: Double
        get() = _balance
    
    fun deposit(amount: Double) {
        if (amount > 0) {
            _balance += amount
        }
    }
    
    fun withdraw(amount: Double) {
        if (amount > 0 && amount <= _balance) {
            _balance -= amount
        }
    }
}

// Usage
val account = BankAccount()
account.deposit(1000.0)
println(account.balance)  // 1000.0
account.withdraw(500.0)
println(account.balance)  // 500.0
```

### Lazy Properties
```kotlin
class Database {
    val connection: String by lazy {
        println("Initializing database connection...")
        "Connected to database"
    }
}

// Usage
val db = Database()
println("Database created")
println(db.connection)  // Initializes and prints connection
println(db.connection)  // Uses cached value
```

## Real-world Examples

### 1. User Management System
```kotlin
class UserManager {
    private val users = mutableMapOf<String, User>()
    
    fun registerUser(username: String, email: String, password: String): Boolean {
        if (users.containsKey(username)) {
            return false
        }
        
        val user = User(username, email, password)
        users[username] = user
        return true
    }
    
    fun authenticateUser(username: String, password: String): Boolean {
        val user = users[username]
        return user?.password == password
    }
    
    fun getUserProfile(username: String): User? {
        return users[username]
    }
}

data class User(
    val username: String,
    val email: String,
    private val password: String
)

// Usage
val userManager = UserManager()
userManager.registerUser("john_doe", "john@example.com", "password123")
val isAuthenticated = userManager.authenticateUser("john_doe", "password123")
```

### 2. Shopping Cart System
```kotlin
class ShoppingCart {
    private val items = mutableListOf<CartItem>()
    
    fun addItem(product: Product, quantity: Int) {
        val existingItem = items.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            items.add(CartItem(product, quantity))
        }
    }
    
    fun removeItem(productId: String) {
        items.removeIf { it.product.id == productId }
    }
    
    fun getTotal(): Double {
        return items.sumOf { it.product.price * it.quantity }
    }
}

data class Product(
    val id: String,
    val name: String,
    val price: Double
)

data class CartItem(
    val product: Product,
    var quantity: Int
)

// Usage
val cart = ShoppingCart()
val product = Product("1", "Laptop", 999.99)
cart.addItem(product, 2)
println(cart.getTotal())  // 1999.98
```

### 3. Task Management System
```kotlin
class TaskManager {
    private val tasks = mutableListOf<Task>()
    
    fun addTask(title: String, description: String, priority: Priority) {
        val task = Task(
            id = generateTaskId(),
            title = title,
            description = description,
            priority = priority,
            status = Status.PENDING
        )
        tasks.add(task)
    }
    
    fun updateTaskStatus(taskId: String, status: Status) {
        tasks.find { it.id == taskId }?.status = status
    }
    
    fun getTasksByPriority(priority: Priority): List<Task> {
        return tasks.filter { it.priority == priority }
    }
    
    private fun generateTaskId(): String {
        return "TASK-${System.currentTimeMillis()}"
    }
}

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    var status: Status
)

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class Status {
    PENDING, IN_PROGRESS, COMPLETED
}

// Usage
val taskManager = TaskManager()
taskManager.addTask(
    "Implement login",
    "Create user authentication system",
    Priority.HIGH
)
taskManager.addTask(
    "Update documentation",
    "Update API documentation",
    Priority.MEDIUM
)
val highPriorityTasks = taskManager.getTasksByPriority(Priority.HIGH)
```

## Practice Exercises

1. Create a `Book` class with properties for title, author, and publication year
2. Implement a `Bank` class with methods for managing multiple accounts
3. Create a `Library` class that manages a collection of books
4. Implement a `Student` class with a grade calculation system
5. Create a `Restaurant` class that manages menu items and orders

Remember:
- Use appropriate access modifiers (public, private, protected)
- Implement proper encapsulation
- Use data classes when appropriate
- Consider using sealed classes for restricted hierarchies
- Always validate input data in constructors and methods 