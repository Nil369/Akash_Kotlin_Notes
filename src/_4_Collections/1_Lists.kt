package _4_Collections

/**
 * This file demonstrates list collections in Kotlin
 * including creation, manipulation, and various operations.
 */
fun main() {
    println("===== LIST CREATION =====")
    
    // Immutable list (List)
    val immutableList = listOf("Apple", "Banana", "Cherry", "Date")
    println("Immutable list: $immutableList")
    
    // Empty list
    val emptyList = emptyList<String>()
    println("Empty list: $emptyList")
    
    // List with different element types
    val mixedList = listOf("Kotlin", 1, 2.5, true)
    println("Mixed list: $mixedList")
    
    // Mutable list (MutableList)
    val mutableList = mutableListOf("Red", "Green", "Blue")
    println("Original mutable list: $mutableList")
    
    // ArrayList (Java compatibility)
    val arrayList = ArrayList<Int>()
    arrayList.add(1)
    arrayList.add(2)
    arrayList.add(3)
    println("ArrayList: $arrayList")
    
    println("\n===== LIST OPERATIONS =====")
    
    // Accessing elements
    println("First element: ${immutableList[0]}")
    println("Last element: ${immutableList[immutableList.size - 1]}")
    println("Element at index 2: ${immutableList[2]}")
    
    // Size
    println("List size: ${immutableList.size}")
    
    // Contains
    println("Contains 'Banana': ${immutableList.contains("Banana")}")
    println("Contains 'Orange': ${immutableList.contains("Orange")}")
    
    // IndexOf
    println("Index of 'Cherry': ${immutableList.indexOf("Cherry")}")
    println("Index of 'Orange': ${immutableList.indexOf("Orange")}") // Returns -1 if not found
    
    // Sublist
    val subList = immutableList.subList(1, 3) // From index 1 to 2 (exclusive 3)
    println("Sublist (1-2): $subList")
    
    println("\n===== MUTABLE LIST OPERATIONS =====")
    
    // Adding elements
    mutableList.add("Yellow")
    println("After adding 'Yellow': $mutableList")
    
    mutableList.add(1, "Purple") // Add at specific index
    println("After adding 'Purple' at index 1: $mutableList")
    
    // Removing elements
    mutableList.remove("Green")
    println("After removing 'Green': $mutableList")
    
    mutableList.removeAt(0) // Remove at specific index
    println("After removing element at index 0: $mutableList")
    
    // Updating elements
    mutableList[0] = "Orange"
    println("After updating element at index 0: $mutableList")
    
    // Clear
    val tempList = mutableListOf(1, 2, 3, 4, 5)
    println("Temp list before clear: $tempList")
    tempList.clear()
    println("Temp list after clear: $tempList")
    
    println("\n===== LIST ITERATION =====")
    
    // For loop
    println("For loop:")
    for (fruit in immutableList) {
        println("- $fruit")
    }
    
    // ForEach
    println("\nForEach:")
    immutableList.forEach { println("- $it") }
    
    // ForEachIndexed
    println("\nForEachIndexed:")
    immutableList.forEachIndexed { index, value -> println("$index: $value") }
    
    println("\n===== LIST TRANSFORMATION =====")
    
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
    // Map
    val squared = numbers.map { it * it }
    println("Original numbers: $numbers")
    println("Squared numbers: $squared")
    
    // Filter
    val evenNumbers = numbers.filter { it % 2 == 0 }
    println("Even numbers: $evenNumbers")
    
    // Partition
    val (evens, odds) = numbers.partition { it % 2 == 0 }
    println("Evens: $evens")
    println("Odds: $odds")
    
    // Sort
    val unsortedList = listOf(3, 1, 4, 1, 5, 9, 2, 6)
    val sortedList = unsortedList.sorted()
    println("Unsorted: $unsortedList")
    println("Sorted: $sortedList")
    println("Sorted descending: ${unsortedList.sortedDescending()}")
    
    // Distinct
    val duplicatesList = listOf(1, 2, 2, 3, 4, 4, 5)
    println("With duplicates: $duplicatesList")
    println("Distinct: ${duplicatesList.distinct()}")
    
    println("\n===== REAL-WORLD EXAMPLE: TASK MANAGER =====")
    
    // Task manager implementation
    val taskManager = TaskManager()
    
    // Adding tasks
    taskManager.addTask("Complete project proposal", "Work", "High")
    taskManager.addTask("Buy groceries", "Personal", "Medium")
    taskManager.addTask("Schedule meeting with team", "Work", "Medium")
    taskManager.addTask("Pay bills", "Personal", "High")
    taskManager.addTask("Research new technologies", "Work", "Low")
    
    // Display all tasks
    taskManager.displayTasks()
    
    // Find tasks by category
    println("\nWork Tasks:")
    taskManager.findTasksByCategory("Work").forEach {
        println("- ${it.title} (${it.priority})")
    }
    
    // Find tasks by priority
    println("\nHigh Priority Tasks:")
    taskManager.findTasksByPriority("High").forEach {
        println("- ${it.title} (${it.category})")
    }
    
    // Complete a task
    taskManager.completeTask("Buy groceries")
    
    // Display tasks after completion
    println("\nAfter completing 'Buy groceries':")
    taskManager.displayTasks()
    
    // Display statistics
    taskManager.displayStatistics()
}

// Task class for the real-world example
data class Task(
    val title: String,
    val category: String,
    val priority: String,
    var isCompleted: Boolean = false,
    val creationDate: Long = System.currentTimeMillis()
)

// Task manager class
class TaskManager {
    private val tasks = mutableListOf<Task>()
    
    fun addTask(title: String, category: String, priority: String) {
        tasks.add(Task(title, category, priority))
        println("Task added: $title")
    }
    
    fun completeTask(title: String) {
        val task = tasks.find { it.title == title }
        if (task != null) {
            task.isCompleted = true
            println("Task completed: $title")
        } else {
            println("Task not found: $title")
        }
    }
    
    fun findTasksByCategory(category: String): List<Task> {
        return tasks.filter { it.category == category }
    }
    
    fun findTasksByPriority(priority: String): List<Task> {
        return tasks.filter { it.priority == priority }
    }
    
    fun displayTasks() {
        println("\nAll Tasks:")
        if (tasks.isEmpty()) {
            println("No tasks found.")
            return
        }
        
        tasks.forEachIndexed { index, task ->
            val status = if (task.isCompleted) "[X]" else "[ ]"
            println("${index + 1}. $status ${task.title} - ${task.category} (${task.priority})")
        }
    }
    
    fun displayStatistics() {
        println("\nTask Statistics:")
        println("Total tasks: ${tasks.size}")
        println("Completed tasks: ${tasks.count { it.isCompleted }}")
        println("Pending tasks: ${tasks.count { !it.isCompleted }}")
        
        // Category breakdown
        val categoryStats = tasks.groupBy { it.category }
            .mapValues { it.value.size }
        
        println("\nTasks by Category:")
        categoryStats.forEach { (category, count) ->
            println("- $category: $count tasks")
        }
        
        // Priority breakdown
        val priorityStats = tasks.groupBy { it.priority }
            .mapValues { it.value.size }
        
        println("\nTasks by Priority:")
        priorityStats.forEach { (priority, count) ->
            println("- $priority: $count tasks")
        }
    }
} 