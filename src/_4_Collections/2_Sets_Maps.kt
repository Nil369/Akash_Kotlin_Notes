package _4_Collections

/**
 * This file demonstrates set and map collections in Kotlin
 * including creation, manipulation, and various operations.
 */
fun main() {
    println("===== SET CREATION =====")
    
    // Immutable set (Set)
    val immutableSet = setOf("Apple", "Banana", "Cherry", "Apple") // Note: duplicates are removed
    println("Immutable set: $immutableSet")
    
    // Empty set
    val emptySet = emptySet<String>()
    println("Empty set: $emptySet")
    
    // Mutable set (MutableSet)
    val mutableSet = mutableSetOf("Red", "Green", "Blue")
    println("Original mutable set: $mutableSet")
    
    // HashSet (Java compatibility)
    val hashSet = HashSet<Int>()
    hashSet.add(1)
    hashSet.add(2)
    hashSet.add(3)
    hashSet.add(1) // Duplicate won't be added
    println("HashSet: $hashSet")
    
    // LinkedHashSet preserves insertion order
    val linkedHashSet = linkedSetOf(3, 1, 4, 1, 5, 9) // Duplicates removed, order preserved
    println("LinkedHashSet: $linkedHashSet")
    
    // SortedSet (TreeSet) maintains natural ordering
    val sortedSet = sortedSetOf(5, 2, 9, 1, 3)
    println("SortedSet: $sortedSet")
    
    println("\n===== SET OPERATIONS =====")
    
    // Size
    println("Set size: ${immutableSet.size}")
    
    // Contains
    println("Contains 'Banana': ${immutableSet.contains("Banana")}")
    println("Contains 'Orange': ${immutableSet.contains("Orange")}")
    
    // Set operations
    val set1 = setOf(1, 2, 3, 4)
    val set2 = setOf(3, 4, 5, 6)
    
    // Union
    println("Union: ${set1.union(set2)}")
    
    // Intersection
    println("Intersection: ${set1.intersect(set2)}")
    
    // Difference
    println("Difference (set1 - set2): ${set1.subtract(set2)}")
    println("Difference (set2 - set1): ${set2.subtract(set1)}")
    
    // Symmetrical difference (elements in either set but not in both)
    val symmetricDifference = set1.union(set2).subtract(set1.intersect(set2))
    println("Symmetric difference: $symmetricDifference")
    
    println("\n===== MUTABLE SET OPERATIONS =====")
    
    // Adding elements
    mutableSet.add("Yellow")
    println("After adding 'Yellow': $mutableSet")
    
    // Adding multiple elements
    mutableSet.addAll(listOf("Purple", "Orange"))
    println("After adding multiple elements: $mutableSet")
    
    // Removing elements
    mutableSet.remove("Green")
    println("After removing 'Green': $mutableSet")
    
    // Clear
    val tempSet = mutableSetOf(1, 2, 3, 4, 5)
    println("Temp set before clear: $tempSet")
    tempSet.clear()
    println("Temp set after clear: $tempSet")
    
    println("\n===== MAP CREATION =====")
    
    // Immutable map (Map)
    val immutableMap = mapOf(
        "name" to "John",
        "age" to 30,
        "city" to "New York"
    )
    println("Immutable map: $immutableMap")
    
    // Another way to create a map
    val anotherMap = mapOf(
        Pair("country", "USA"),
        Pair("language", "English"),
        Pair("currency", "USD")
    )
    println("Another map: $anotherMap")
    
    // Empty map
    val emptyMap = emptyMap<String, Int>()
    println("Empty map: $emptyMap")
    
    // Mutable map (MutableMap)
    val mutableMap = mutableMapOf(
        "red" to "#FF0000",
        "green" to "#00FF00",
        "blue" to "#0000FF"
    )
    println("Original mutable map: $mutableMap")
    
    // HashMap (Java compatibility)
    val hashMap = HashMap<String, Int>()
    hashMap["one"] = 1
    hashMap["two"] = 2
    hashMap["three"] = 3
    println("HashMap: $hashMap")
    
    // LinkedHashMap preserves insertion order
    val linkedHashMap = linkedMapOf(
        3 to "three",
        1 to "one",
        4 to "four"
    )
    println("LinkedHashMap: $linkedHashMap")
    
    // SortedMap (TreeMap) sorts keys by natural ordering
    val sortedMap = sortedMapOf(
        5 to "five",
        2 to "two",
        9 to "nine",
        1 to "one"
    )
    println("SortedMap: $sortedMap")
    
    println("\n===== MAP OPERATIONS =====")
    
    // Size
    println("Map size: ${immutableMap.size}")
    
    // Accessing values
    println("Name: ${immutableMap["name"]}")
    println("Age: ${immutableMap["age"]}")
    
    // Default value if key not found
    println("Gender: ${immutableMap.getOrDefault("gender", "Not specified")}")
    
    // Keys and values
    println("Keys: ${immutableMap.keys}")
    println("Values: ${immutableMap.values}")
    
    // Contains
    println("Contains key 'name': ${immutableMap.containsKey("name")}")
    println("Contains value 'John': ${immutableMap.containsValue("John")}")
    
    println("\n===== MUTABLE MAP OPERATIONS =====")
    
    // Adding/updating entries
    mutableMap["yellow"] = "#FFFF00"
    println("After adding 'yellow': $mutableMap")
    
    mutableMap["red"] = "#FF0101" // Update existing value
    println("After updating 'red': $mutableMap")
    
    // Removing entries
    mutableMap.remove("green")
    println("After removing 'green': $mutableMap")
    
    // Clear
    val tempMap = mutableMapOf(1 to "one", 2 to "two", 3 to "three")
    println("Temp map before clear: $tempMap")
    tempMap.clear()
    println("Temp map after clear: $tempMap")
    
    println("\n===== MAP ITERATION =====")
    
    // Iterating over entries
    println("Map entries:")
    for (entry in immutableMap) {
        println("${entry.key} = ${entry.value}")
    }
    
    // Using destructuring
    println("\nUsing destructuring:")
    for ((key, value) in immutableMap) {
        println("$key = $value")
    }
    
    // Iterating over keys
    println("\nMap keys:")
    for (key in immutableMap.keys) {
        println(key)
    }
    
    // Iterating over values
    println("\nMap values:")
    for (value in immutableMap.values) {
        println(value)
    }
    
    println("\n===== REAL-WORLD EXAMPLE: PRODUCT INVENTORY =====")
    
    // Product inventory implementation
    val inventory = ProductInventory()
    
    // Adding products
    inventory.addProduct("P001", "Smartphone", 799.99, "Electronics", 10)
    inventory.addProduct("P002", "Laptop", 1299.99, "Electronics", 5)
    inventory.addProduct("P003", "Headphones", 199.99, "Electronics", 15)
    inventory.addProduct("P004", "T-shirt", 29.99, "Clothing", 50)
    inventory.addProduct("P005", "Jeans", 59.99, "Clothing", 30)
    
    // Display all products
    inventory.displayAllProducts()
    
    // Find product by ID
    val product = inventory.findProductById("P003")
    if (product != null) {
        println("\nFound product: ${product.name} - $${product.price} (${product.stockQuantity} in stock)")
    }
    
    // Update product stock
    inventory.updateStock("P001", 5)
    inventory.updateStock("P002", -2)
    
    // Products by category
    println("\nProducts by category:")
    val categories = inventory.getProductsByCategory()
    categories.forEach { (category, products) ->
        println("\n$category:")
        products.forEach { product ->
            println("- ${product.name}: $${product.price} (${product.stockQuantity} in stock)")
        }
    }
    
    // Low stock alert
    println("\nLow stock alert (less than 10 items):")
    inventory.getLowStockProducts(10).forEach { product ->
        println("- ${product.name}: ${product.stockQuantity} remaining")
    }
    
    // Inventory statistics
    inventory.displayStatistics()
}

// Product class for the real-world example
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    var stockQuantity: Int
)

// Product inventory class
class ProductInventory {
    private val products = mutableMapOf<String, Product>()
    
    fun addProduct(id: String, name: String, price: Double, category: String, stock: Int) {
        products[id] = Product(id, name, price, category, stock)
        println("Product added: $name ($id)")
    }
    
    fun findProductById(id: String): Product? {
        return products[id]
    }
    
    fun updateStock(id: String, quantityChange: Int) {
        val product = products[id]
        if (product != null) {
            val newQuantity = product.stockQuantity + quantityChange
            if (newQuantity < 0) {
                println("Error: Cannot reduce stock below 0 for ${product.name}")
                return
            }
            
            product.stockQuantity = newQuantity
            println("Updated stock for ${product.name}: ${product.stockQuantity} (${if (quantityChange > 0) "+" else ""}$quantityChange)")
        } else {
            println("Error: Product not found with ID $id")
        }
    }
    
    fun getProductsByCategory(): Map<String, List<Product>> {
        return products.values.groupBy { it.category }
    }
    
    fun getLowStockProducts(threshold: Int): List<Product> {
        return products.values.filter { it.stockQuantity < threshold }
    }
    
    fun displayAllProducts() {
        println("\nAll Products:")
        if (products.isEmpty()) {
            println("No products in inventory.")
            return
        }
        
        products.values.forEach { product ->
            println("${product.id}: ${product.name} - $${product.price} - ${product.category} (${product.stockQuantity} in stock)")
        }
    }
    
    fun displayStatistics() {
        println("\nInventory Statistics:")
        println("Total products: ${products.size}")
        
        // Total stock value
        val totalValue = products.values.sumOf { it.price * it.stockQuantity }
        println("Total inventory value: $${String.format("%.2f", totalValue)}")
        
        // Categories count
        val categoryCounts = products.values
            .groupBy { it.category }
            .mapValues { it.value.size }
        
        println("\nProducts by Category:")
        categoryCounts.forEach { (category, count) ->
            println("- $category: $count products")
        }
        
        // Price statistics
        val prices = products.values.map { it.price }
        if (prices.isNotEmpty()) {
            val averagePrice = prices.average()
            val minPrice = prices.minOrNull()
            val maxPrice = prices.maxOrNull()
            println("\nPrice Statistics:")
            println("- Average price: $${String.format("%.2f", averagePrice)}")
            println("- Minimum price: $${String.format("%.2f", minPrice)}")
            println("- Maximum price: $${String.format("%.2f", maxPrice)}")
        }
        
        // Stock statistics
        val totalStock = products.values.sumOf { it.stockQuantity }
        println("\nStock Statistics:")
        println("- Total items in stock: $totalStock")
        println("- Average items per product: ${totalStock / products.size.toDouble()}")
    }
} 