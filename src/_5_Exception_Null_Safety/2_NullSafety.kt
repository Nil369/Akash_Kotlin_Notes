package _5_Exception_Null_Safety

/**
 * This file demonstrates null safety features in Kotlin
 * including nullable types, safe calls, Elvis operator, and more.
 */
fun main() {
    println("===== NULLABLE AND NON-NULLABLE TYPES =====")
    
    // Non-nullable type - cannot be null
    var nonNullable: String = "Hello"
    // nonNullable = null // Compilation error
    
    // Nullable type - can be null
    var nullable: String? = "World"
    nullable = null // OK
    
    println("Non-nullable: $nonNullable")
    println("Nullable: $nullable")
    
    // Type inference
    val inferredNonNull = "Inferred as non-nullable String"
    // inferredNonNull = null // Compilation error
    
    val inferredNullable = null // Inferred as Nothing?
    println("Inferred nullable: $inferredNullable")
    
    println("\n===== SAFE CALLS =====")
    
    // Safe call operator (?.)
    val length1 = nullable?.length
    println("Length of nullable (using safe call): $length1")
    
    val name: String? = "John"
    val nameLength = name?.length
    println("Length of name: $nameLength")
    
    // Chained safe calls
    data class Address(val street: String?, val city: String, val zipCode: String)
    data class Person(val name: String, val address: Address?)
    
    val person1 = Person("Alice", Address("123 Main St", "New York", "10001"))
    val person2 = Person("Bob", null)
    
    val street1 = person1.address?.street
    val street2 = person2.address?.street
    
    println("Person 1 street: $street1")
    println("Person 2 street: $street2")
    
    println("\n===== ELVIS OPERATOR =====")
    
    // Elvis operator (?:) - provide a default value when null
    val length2 = nullable?.length ?: 0
    println("Length of nullable (using Elvis): $length2")
    
    // Elvis with function call
    fun getDefaultStreet(): String {
        println("Providing default street")
        return "Unknown Street"
    }
    
    val aliceStreet = person1.address?.street ?: getDefaultStreet()
    val bobStreet = person2.address?.street ?: getDefaultStreet()
    
    println("Alice's street: $aliceStreet")
    println("Bob's street: $bobStreet")
    
    // Elvis with throw
    fun getStreetSafe(p: Person): String {
        return p.address?.street ?: throw IllegalArgumentException("Address or street is missing")
    }
    
    try {
        val aliceStreetSafe = getStreetSafe(person1)
        println("Alice's street (safe): $aliceStreetSafe")
        
        val bobStreetSafe = getStreetSafe(person2)
        println("Bob's street (safe): $bobStreetSafe") // This line won't be executed
    } catch (e: IllegalArgumentException) {
        println("Exception: ${e.message}")
    }
    
    println("\n===== NOT-NULL ASSERTION =====")
    
    // Not-null assertion operator (!!) - throws NPE if null
    var nonNullName: String? = "Charlie"
    val length3 = nonNullName!!.length // Safe because nonNullName is not null
    println("Length using !! (safe): $length3")
    
    try {
        nonNullName = null
        val length4 = nonNullName!!.length // Will throw NullPointerException
        println("This won't be printed")
    } catch (e: NullPointerException) {
        println("NullPointerException was thrown when using !!")
    }
    
    println("\n===== SAFE CASTS =====")
    
    // Safe cast operator (as?)
    val obj1: Any = "Hello World"
    val obj2: Any = 123
    
    val str1: String? = obj1 as? String
    val str2: String? = obj2 as? String
    
    println("Safe cast of string: $str1")
    println("Safe cast of integer: $str2")
    
    println("\n===== PLATFORM TYPES =====")
    
    // Platform types come from Java code
    // For example, java.util.Date.toString() returns a platform type (String!)
    val javaDate = java.util.Date()
    val dateStr = javaDate.toString() // Platform type String!, treated as non-null
    println("Date string: $dateStr")
    
    println("\n===== COLLECTIONS WITH NULLS =====")
    
    // List with nullable elements
    val listWithNulls: List<String?> = listOf("A", null, "B", null, "C")
    println("List with nulls: $listWithNulls")
    
    // Filter out nulls
    val nonNullList = listWithNulls.filterNotNull()
    println("List without nulls: $nonNullList")
    
    // Map with nullable values
    val mapWithNulls: Map<String, Int?> = mapOf(
        "a" to 1,
        "b" to null,
        "c" to 3,
        "d" to null
    )
    println("Map with nulls: $mapWithNulls")
    
    // Filter out null values
    val nonNullMap = mapWithNulls.filterValues { it != null }
    println("Map without nulls: $nonNullMap")
    
    println("\n===== LET WITH NULLABLE VALUES =====")
    
    // Using let with safe call to execute code only if value is not null
    nullable?.let {
        println("Nullable is not null, its value is: $it")
    } ?: println("Nullable is null")
    
    val nullValue: String? = null
    nullValue?.let {
        println("This won't be printed because nullValue is null")
    } ?: println("nullValue is null")
    
    // let with safe call on collection elements
    listWithNulls.forEach { item ->
        item?.let {
            println("Processing non-null item: $it")
        }
    }
    
    println("\n===== REAL-WORLD EXAMPLE: USER MANAGEMENT =====")
    
    val userManager = UserManager()
    
    // Test with different scenarios
    processUser(userManager, 1)  // User with all fields
    processUser(userManager, 2)  // User with null email
    processUser(userManager, 3)  // User with null phone and address
    processUser(userManager, 4)  // User with incomplete address
    processUser(userManager, 10) // Non-existent user
    
    println("\n===== REAL-WORLD EXAMPLE: CONFIGURATION MANAGER =====")
    
    val configManager = ConfigurationManager()
    
    // Get settings with defaults when null
    val port = configManager.getIntSetting("server.port") ?: 8080
    val host = configManager.getStringSetting("server.host") ?: "localhost"
    val enableLogging = configManager.getBooleanSetting("logging.enabled") ?: false
    val maxConnections = configManager.getIntSetting("server.maxConnections") ?: 50
    
    println("Server configuration:")
    println("Host: $host")
    println("Port: $port")
    println("Max connections: $maxConnections")
    println("Logging enabled: $enableLogging")
    
    // Check if all required settings are available
    val requiredSettings = listOf("server.host", "server.port", "database.url")
    val missingSettings = requiredSettings.filter { configManager.getSetting(it) == null }
    
    if (missingSettings.isNotEmpty()) {
        println("\nWarning: Missing required settings: $missingSettings")
    } else {
        println("\nAll required settings are present")
    }
}

// Real-world example: User management
data class UserAddress(
    val street: String?,
    val city: String?,
    val state: String?,
    val zipCode: String?
)

data class UserProfile(
    val id: Int,
    val name: String,
    val email: String?,
    val phone: String?,
    val address: UserAddress?
)

class UserManager {
    private val users = mapOf(
        1 to UserProfile(
            1, 
            "John Doe", 
            "john.doe@example.com", 
            "555-1234", 
            UserAddress("123 Main St", "New York", "NY", "10001")
        ),
        2 to UserProfile(
            2, 
            "Jane Smith", 
            null, 
            "555-5678", 
            UserAddress("456 Oak Ave", "Los Angeles", "CA", "90001")
        ),
        3 to UserProfile(
            3, 
            "Bob Johnson", 
            "bob.johnson@example.com", 
            null, 
            null
        ),
        4 to UserProfile(
            4, 
            "Alice Brown", 
            "alice.brown@example.com", 
            "555-9012", 
            UserAddress("789 Pine St", null, "TX", null)
        )
    )
    
    fun getUserById(id: Int): UserProfile? {
        return users[id]
    }
    
    fun getFullAddress(user: UserProfile): String {
        // Using safe calls and Elvis operator for comprehensive null handling
        val address = user.address ?: return "No address provided"
        
        val street = address.street ?: "No street provided"
        val city = address.city ?: "No city provided"
        val state = address.state ?: "No state provided"
        val zipCode = address.zipCode ?: "No ZIP code provided"
        
        return "$street, $city, $state $zipCode"
    }
    
    fun sendEmailNotification(user: UserProfile, message: String): Boolean {
        // Using let with safe call
        return user.email?.let {
            println("Sending email to $it: $message")
            true // Email sent successfully
        } ?: run {
            println("Cannot send email to ${user.name} - email address is missing")
            false // Failed to send email
        }
    }
    
    fun getContact(user: UserProfile): String {
        // Preference: email, then phone, then "No contact info"
        return user.email ?: user.phone ?: "No contact information available"
    }
}

// Helper function to test UserManager
fun processUser(userManager: UserManager, userId: Int) {
    println("\nProcessing user with ID: $userId")
    
    // Using Elvis with early return
    val user = userManager.getUserById(userId) ?: run {
        println("User not found")
        return
    }
    
    println("Name: ${user.name}")
    
    // Safe access to nullable properties
    println("Email: ${user.email ?: "Not provided"}")
    println("Phone: ${user.phone ?: "Not provided"}")
    
    // More complex null handling
    println("Address: ${userManager.getFullAddress(user)}")
    
    // Using a function that itself handles nullables
    println("Primary contact: ${userManager.getContact(user)}")
    
    // Using let to perform an action only if email is present
    val emailResult = userManager.sendEmailNotification(user, "Your account has been updated.")
    println("Email notification result: ${if (emailResult) "Success" else "Failed"}")
}

// Real-world example: Configuration manager
class ConfigurationManager {
    // Simulate a configuration with potentially missing values
    private val settings = mapOf(
        "server.host" to "192.168.1.100",
        "logging.enabled" to "true",
        "database.url" to "jdbc:mysql://localhost:3306/mydb",
        "database.username" to "admin",
        // "server.port" is intentionally missing
        // "server.maxConnections" is intentionally missing
        "app.name" to "MyApp"
    )
    
    fun getSetting(key: String): String? {
        return settings[key]
    }
    
    fun getStringSetting(key: String): String? {
        return getSetting(key)
    }
    
    fun getIntSetting(key: String): Int? {
        // Using safe call and let for conversion
        return getSetting(key)?.let {
            try {
                it.toInt()
            } catch (e: NumberFormatException) {
                println("Warning: Setting '$key' is not a valid integer: '$it'")
                null
            }
        }
    }
    
    fun getBooleanSetting(key: String): Boolean? {
        return getSetting(key)?.let {
            when (it.lowercase()) {
                "true", "yes", "1", "on" -> true
                "false", "no", "0", "off" -> false
                else -> {
                    println("Warning: Setting '$key' is not a valid boolean: '$it'")
                    null
                }
            }
        }
    }
    
    fun checkRequiredSettings(keys: List<String>): Boolean {
        return keys.all { getSetting(it) != null }
    }
} 