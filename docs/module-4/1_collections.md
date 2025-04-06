# Collections in Kotlin

## Basic Collections

### Lists
```kotlin
// Immutable List
val immutableList = listOf(1, 2, 3, 4, 5)

// Mutable List
val mutableList = mutableListOf("Apple", "Banana", "Orange")

// List Operations
val numbers = listOf(1, 2, 3, 4, 5)
println(numbers.first())  // 1
println(numbers.last())   // 5
println(numbers.size)     // 5
println(numbers[2])       // 3
```

### Sets
```kotlin
// Immutable Set
val immutableSet = setOf(1, 2, 3, 3, 4)  // [1, 2, 3, 4]

// Mutable Set
val mutableSet = mutableSetOf("A", "B", "C")

// Set Operations
val set1 = setOf(1, 2, 3)
val set2 = setOf(3, 4, 5)
println(set1.union(set2))      // [1, 2, 3, 4, 5]
println(set1.intersect(set2))  // [3]
println(set1.subtract(set2))   // [1, 2]
```

### Maps
```kotlin
// Immutable Map
val immutableMap = mapOf(
    "name" to "John",
    "age" to 30,
    "city" to "New York"
)

// Mutable Map
val mutableMap = mutableMapOf(
    1 to "One",
    2 to "Two",
    3 to "Three"
)

// Map Operations
val userMap = mapOf(
    "id" to 1,
    "name" to "Alice",
    "email" to "alice@example.com"
)
println(userMap["name"])  // Alice
println(userMap.getOrDefault("phone", "N/A"))  // N/A
```

## Collection Operations

### Filtering
```kotlin
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

// Filter even numbers
val evenNumbers = numbers.filter { it % 2 == 0 }  // [2, 4, 6, 8, 10]

// Filter with index
val numbersAtEvenIndices = numbers.filterIndexed { index, _ -> index % 2 == 0 }

// Take first N elements
val firstFive = numbers.take(5)  // [1, 2, 3, 4, 5]

// Drop first N elements
val afterFive = numbers.drop(5)  // [6, 7, 8, 9, 10]
```

### Mapping
```kotlin
val numbers = listOf(1, 2, 3, 4, 5)

// Map numbers to their squares
val squares = numbers.map { it * it }  // [1, 4, 9, 16, 25]

// Map with index
val indexedSquares = numbers.mapIndexed { index, number -> 
    "Index $index: ${number * number}"
}

// Flatten nested lists
val nestedList = listOf(
    listOf(1, 2, 3),
    listOf(4, 5),
    listOf(6)
)
val flattened = nestedList.flatten()  // [1, 2, 3, 4, 5, 6]
```

### Sorting
```kotlin
val numbers = listOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)

// Natural sorting
val sorted = numbers.sorted()  // [1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9]

// Reverse sorting
val reverseSorted = numbers.sortedDescending()

// Custom sorting
data class Person(val name: String, val age: Int)
val people = listOf(
    Person("Alice", 25),
    Person("Bob", 30),
    Person("Charlie", 20)
)
val sortedByAge = people.sortedBy { it.age }
```

## Real-world Examples

### 1. Product Inventory Management
```kotlin
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val inStock: Boolean
)

class InventoryManager {
    private val products = mutableListOf<Product>()

    fun addProduct(product: Product) {
        products.add(product)
    }

    fun getProductsByCategory(category: String): List<Product> {
        return products.filter { it.category == category }
    }

    fun getLowStockProducts(threshold: Double): List<Product> {
        return products.filter { it.price < threshold }
    }

    fun getTotalValue(): Double {
        return products.sumOf { it.price }
    }

    fun getCategories(): Set<String> {
        return products.map { it.category }.toSet()
    }
}

// Usage
val manager = InventoryManager()
manager.addProduct(Product("1", "Laptop", 999.99, "Electronics", true))
manager.addProduct(Product("2", "Phone", 499.99, "Electronics", true))
manager.addProduct(Product("3", "Book", 29.99, "Books", true))

val electronics = manager.getProductsByCategory("Electronics")
val lowStock = manager.getLowStockProducts(500.0)
val categories = manager.getCategories()
```

### 2. User Analytics System
```kotlin
data class UserActivity(
    val userId: String,
    val action: String,
    val timestamp: Long,
    val duration: Int
)

class AnalyticsSystem {
    private val activities = mutableListOf<UserActivity>()

    fun recordActivity(activity: UserActivity) {
        activities.add(activity)
    }

    fun getUserActivitySummary(userId: String): Map<String, Int> {
        return activities
            .filter { it.userId == userId }
            .groupBy { it.action }
            .mapValues { it.value.size }
    }

    fun getMostActiveUsers(limit: Int): List<String> {
        return activities
            .groupBy { it.userId }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }

    fun getAverageSessionDuration(): Double {
        return activities
            .map { it.duration }
            .average()
    }
}

// Usage
val analytics = AnalyticsSystem()
analytics.recordActivity(UserActivity("user1", "login", System.currentTimeMillis(), 300))
analytics.recordActivity(UserActivity("user1", "view_profile", System.currentTimeMillis(), 120))
analytics.recordActivity(UserActivity("user2", "login", System.currentTimeMillis(), 240))

val user1Summary = analytics.getUserActivitySummary("user1")
val topUsers = analytics.getMostActiveUsers(2)
val avgDuration = analytics.getAverageSessionDuration()
```

### 3. Task Management System
```kotlin
data class Task(
    val id: String,
    val title: String,
    val priority: Priority,
    val dueDate: Long,
    val completed: Boolean
)

enum class Priority {
    LOW, MEDIUM, HIGH
}

class TaskManager {
    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun getTasksByPriority(priority: Priority): List<Task> {
        return tasks.filter { it.priority == priority }
    }

    fun getOverdueTasks(): List<Task> {
        val now = System.currentTimeMillis()
        return tasks.filter { !it.completed && it.dueDate < now }
    }

    fun getCompletedTasks(): List<Task> {
        return tasks.filter { it.completed }
    }

    fun getTaskStatistics(): Map<Priority, Int> {
        return tasks
            .groupBy { it.priority }
            .mapValues { it.value.size }
    }
}

// Usage
val taskManager = TaskManager()
taskManager.addTask(Task("1", "Implement login", Priority.HIGH, System.currentTimeMillis() + 86400000, false))
taskManager.addTask(Task("2", "Update docs", Priority.MEDIUM, System.currentTimeMillis() + 172800000, false))
taskManager.addTask(Task("3", "Code review", Priority.LOW, System.currentTimeMillis() - 3600000, true))

val highPriorityTasks = taskManager.getTasksByPriority(Priority.HIGH)
val overdueTasks = taskManager.getOverdueTasks()
val statistics = taskManager.getTaskStatistics()
```

## Practice Exercises

1. Create a `Student` class and implement a grade management system using collections
2. Build a `Recipe` management system with filtering and sorting capabilities
3. Implement a `Weather` data analysis system using various collection operations
4. Create a `Library` management system with book tracking and statistics
5. Build a `Sales` reporting system using collections and aggregation functions

Remember:
- Use immutable collections by default
- Choose appropriate collection types based on your needs
- Leverage collection operations for clean and concise code
- Consider performance implications of collection operations
- Use sequences for large collections to improve performance 