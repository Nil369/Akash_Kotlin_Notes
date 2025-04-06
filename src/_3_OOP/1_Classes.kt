package _3_OOP

/**
 * This file demonstrates classes and objects in Kotlin
 * including constructors, properties, methods, and more.
 */
fun main() {
    println("===== BASIC CLASS CREATION =====")
    
    // Creating an instance of a class
    val person = Person("John", "Doe", 30)
    println("Person: ${person.firstName} ${person.lastName}, Age: ${person.age}")
    
    // Using class methods
    person.introduce()
    person.celebrateBirthday()
    println("After birthday: ${person.firstName} ${person.lastName}, Age: ${person.age}")
    
    println("\n===== PROPERTIES AND GETTERS/SETTERS =====")
    
    // Using custom getters and setters
    val employee = Employee("Alice", "Smith", 35, 50000.0)
    println("Employee: ${employee.fullName}, Age: ${employee.age}")
    println("Current salary: $${employee.salary}")
    
    // Using setter
    employee.salary = 55000.0
    println("After raise: $${employee.salary}")
    
    // Trying to set invalid value
    employee.salary = -5000.0 // This will be rejected by the setter
    println("After invalid salary change: $${employee.salary}")
    
    println("\n===== SECONDARY CONSTRUCTORS =====")
    
    // Using primary constructor
    val student1 = Student("Bob", "Johnson", 20, "Computer Science")
    student1.displayInfo()
    
    // Using secondary constructor
    val student2 = Student("Charlie", "Williams", 22)
    student2.displayInfo()
    
    // Using another secondary constructor
    val student3 = Student("12345")
    student3.displayInfo()
    
    println("\n===== DATA CLASSES =====")
    
    // Creating a data class instance
    val product1 = Product("Laptop", "Electronics", 999.99)
    val product2 = Product("Laptop", "Electronics", 999.99)
    val product3 = product1.copy(price = 899.99)
    
    println("Product1: $product1")
    println("Product2: $product2")
    println("Product3: $product3")
    
    // Automatic equals, hashCode, and toString
    println("product1 == product2: ${product1 == product2}")
    println("product1 == product3: ${product1 == product3}")
    
    // Destructuring declarations
    val (name, category, price) = product1
    println("Destructured: name=$name, category=$category, price=$price")
    
    println("\n===== OBJECT KEYWORD =====")
    
    // Singleton object
    println("Database URL: ${Database.url}")
    Database.connect()
    Database.executeQuery("SELECT * FROM users")
    Database.disconnect()
    
    // Companion object
    val userFromFactory = User.create("David", "Brown", "david@example.com")
    println("User created: ${userFromFactory.name}, Email: ${userFromFactory.email}")
    println("User count: ${User.userCount}")
    
    println("\n===== REAL-WORLD EXAMPLE: E-COMMERCE SYSTEM =====")
    
    // Create the e-commerce system
    val ecommerceSystem = ECommerceSystem()
    
    // Add some products to inventory
    ecommerceSystem.addProduct("P001", "Smartphone", "Electronics", 599.99, 10)
    ecommerceSystem.addProduct("P002", "Headphones", "Electronics", 99.99, 20)
    ecommerceSystem.addProduct("P003", "T-shirt", "Clothing", 24.99, 50)
    
    // Create customers
    ecommerceSystem.registerCustomer("C001", "John Doe", "john@example.com")
    ecommerceSystem.registerCustomer("C002", "Jane Smith", "jane@example.com")
    
    // Place orders
    ecommerceSystem.placeOrder("C001", mapOf("P001" to 1, "P002" to 2))
    ecommerceSystem.placeOrder("C002", mapOf("P003" to 3, "P002" to 1))
    
    // Try to place an order with insufficient stock
    ecommerceSystem.placeOrder("C001", mapOf("P001" to 20))
    
    // Display order history
    ecommerceSystem.displayOrderHistory("C001")
    
    // Display inventory status
    ecommerceSystem.displayInventory()
}

// Basic class with primary constructor
class Person(val firstName: String, val lastName: String, var age: Int) {
    
    // Initialization block
    init {
        println("Person object created: $firstName $lastName")
    }
    
    // Method
    fun introduce() {
        println("Hi, my name is $firstName $lastName, and I am $age years old.")
    }
    
    // Method that modifies a property
    fun celebrateBirthday() {
        age++
        println("Happy Birthday! Now I am $age years old.")
    }
}

// Class with custom getters and setters
class Employee(
    val firstName: String,
    val lastName: String,
    var age: Int,
    private var _salary: Double
) {
    // Custom getter for a computed property
    val fullName: String
        get() = "$firstName $lastName"
    
    // Custom getter and setter
    var salary: Double
        get() = _salary
        set(value) {
            if (value > 0) {
                _salary = value
                println("Salary updated to: $value")
            } else {
                println("Invalid salary value: $value")
            }
        }
}

// Class with secondary constructors
class Student(val firstName: String, val lastName: String, var age: Int, var major: String) {
    private var studentId: String = "Unknown"
    
    // Secondary constructor
    constructor(firstName: String, lastName: String, age: Int) : this(firstName, lastName, age, "Undeclared") {
        println("Created student with undeclared major")
    }
    
    // Another secondary constructor
    constructor(studentId: String) : this("New", "Student", 18, "Undeclared") {
        this.studentId = studentId
        println("Created student with ID: $studentId")
    }
    
    // Method
    fun displayInfo() {
        println("Student: $firstName $lastName, Age: $age, Major: $major, ID: $studentId")
    }
}

// Data class
data class Product(val name: String, val category: String, val price: Double)

// Object declaration (singleton)
object Database {
    val url: String = "jdbc:mysql://localhost:3306/mydb"
    private var connected: Boolean = false
    
    fun connect() {
        connected = true
        println("Connected to database at $url")
    }
    
    fun disconnect() {
        connected = false
        println("Disconnected from database")
    }
    
    fun executeQuery(query: String) {
        if (connected) {
            println("Executing query: $query")
        } else {
            println("Error: Not connected to database")
        }
    }
}

// Class with companion object
class User(val name: String, val email: String) {
    
    companion object Factory {
        var userCount: Int = 0
        
        fun create(firstName: String, lastName: String, email: String): User {
            userCount++
            return User("$firstName $lastName", email)
        }
    }
}

// Real-world example: E-commerce system
class ECommerceSystem {
    private val inventory = mutableMapOf<String, ECommerceProduct>()
    private val customers = mutableMapOf<String, Customer>()
    private val orders = mutableListOf<Order>()
    
    // Add product to inventory
    fun addProduct(id: String, name: String, category: String, price: Double, quantity: Int) {
        inventory[id] = ECommerceProduct(id, name, category, price, quantity)
        println("Added product: $name to inventory (Qty: $quantity)")
    }
    
    // Register customer
    fun registerCustomer(id: String, name: String, email: String) {
        customers[id] = Customer(id, name, email)
        println("Registered customer: $name ($id)")
    }
    
    // Place an order
    fun placeOrder(customerId: String, items: Map<String, Int>) {
        val customer = customers[customerId]
        if (customer == null) {
            println("Error: Customer not found")
            return
        }
        
        val orderItems = mutableListOf<OrderItem>()
        var orderTotal = 0.0
        var allItemsAvailable = true
        
        // Check inventory and create order items
        for ((productId, quantity) in items) {
            val product = inventory[productId]
            if (product == null) {
                println("Error: Product $productId not found")
                allItemsAvailable = false
                break
            }
            
            if (product.quantity < quantity) {
                println("Error: Insufficient stock for ${product.name}. Requested: $quantity, Available: ${product.quantity}")
                allItemsAvailable = false
                break
            }
            
            val itemTotal = product.price * quantity
            orderItems.add(OrderItem(productId, product.name, quantity, product.price, itemTotal))
            orderTotal += itemTotal
        }
        
        // Process order if all items are available
        if (allItemsAvailable && orderItems.isNotEmpty()) {
            val orderId = "ORD" + (orders.size + 1).toString().padStart(3, '0')
            val order = Order(orderId, customerId, orderItems, orderTotal)
            orders.add(order)
            
            // Update inventory
            for (item in orderItems) {
                val product = inventory[item.productId]!!
                product.quantity -= item.quantity
            }
            
            println("Order placed successfully: $orderId, Total: $${orderTotal}")
        } else {
            println("Order could not be processed")
        }
    }
    
    // Display order history for a customer
    fun displayOrderHistory(customerId: String) {
        val customer = customers[customerId]
        if (customer == null) {
            println("Error: Customer not found")
            return
        }
        
        val customerOrders = orders.filter { it.customerId == customerId }
        if (customerOrders.isEmpty()) {
            println("No orders found for customer: ${customer.name}")
            return
        }
        
        println("\nOrder History for ${customer.name}:")
        customerOrders.forEach { order ->
            println("Order ${order.orderId} - Total: $${order.total}")
            println("Items:")
            order.items.forEach { item ->
                println("  ${item.name} - Qty: ${item.quantity}, Price: $${item.price}, Total: $${item.total}")
            }
            println()
        }
    }
    
    // Display inventory status
    fun displayInventory() {
        println("\nCurrent Inventory:")
        inventory.values.forEach { product ->
            println("${product.name} (${product.id}) - ${product.category} - $${product.price} - In stock: ${product.quantity}")
        }
    }
    
    // E-commerce related classes
    data class ECommerceProduct(
        val id: String,
        val name: String,
        val category: String,
        val price: Double,
        var quantity: Int
    )
    
    data class Customer(
        val id: String,
        val name: String,
        val email: String
    )
    
    data class OrderItem(
        val productId: String,
        val name: String,
        val quantity: Int,
        val price: Double,
        val total: Double
    )
    
    data class Order(
        val orderId: String,
        val customerId: String,
        val items: List<OrderItem>,
        val total: Double
    )
} 