# Null Safety in Kotlin

## Basic Null Safety

### Nullable Types
```kotlin
// Nullable String
var nullableString: String? = null

// Non-null String
var nonNullString: String = "Hello"  // Cannot be null

// Nullable Int
var nullableInt: Int? = null

// Nullable List
var nullableList: List<String>? = null
```

### Safe Call Operator
```kotlin
// Safe call operator (?.)
val length: Int? = nullableString?.length

// Chained safe calls
val streetName: String? = user?.address?.street?.name

// Safe call with let
nullableString?.let { 
    println("String is not null: $it")
}
```

### Elvis Operator
```kotlin
// Elvis operator (?:)
val length: Int = nullableString?.length ?: 0

// Elvis with throw
val user: User = userRepository.findById(id) ?: 
    throw UserNotFoundException("User not found: $id")

// Elvis with return
fun processUser(user: User?) {
    val name = user?.name ?: return
    println("Processing user: $name")
}
```

### Not-null Assertion
```kotlin
// Not-null assertion (!!)
val length: Int = nullableString!!.length  // Throws NPE if null

// Safe not-null assertion
val length: Int = nullableString?.length ?: throw IllegalArgumentException("String cannot be null")
```

## Real-world Examples

### 1. User Profile Management
```kotlin
data class UserProfile(
    val id: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val address: Address?
)

data class Address(
    val street: String,
    val city: String,
    val country: String,
    val postalCode: String?
)

class UserProfileManager {
    private val profiles = mutableMapOf<String, UserProfile>()

    fun getUserContactInfo(userId: String): ContactInfo? {
        val profile = profiles[userId] ?: return null
        
        return ContactInfo(
            email = profile.email,
            phone = profile.phone,
            address = profile.address?.let { 
                "${it.street}, ${it.city}, ${it.country}"
            }
        )
    }

    fun updateUserEmail(userId: String, newEmail: String): Boolean {
        val profile = profiles[userId] ?: return false
        profiles[userId] = profile.copy(email = newEmail)
        return true
    }

    fun getUsersInCity(city: String): List<UserProfile> {
        return profiles.values.filter { 
            it.address?.city?.equals(city, ignoreCase = true) == true 
        }
    }
}

// Usage
val manager = UserProfileManager()
val contactInfo = manager.getUserContactInfo("user123")
val email = contactInfo?.email ?: "No email provided"
```

### 2. Configuration Management
```kotlin
data class DatabaseConfig(
    val host: String,
    val port: Int,
    val username: String,
    val password: String?,
    val database: String
)

class ConfigurationManager {
    private var config: DatabaseConfig? = null

    fun loadConfig(env: String): DatabaseConfig {
        return when (env) {
            "development" -> DatabaseConfig(
                host = "localhost",
                port = 5432,
                username = "dev_user",
                password = null,
                database = "dev_db"
            )
            "production" -> DatabaseConfig(
                host = System.getenv("DB_HOST") ?: throw IllegalStateException("DB_HOST not set"),
                port = System.getenv("DB_PORT")?.toIntOrNull() ?: 5432,
                username = System.getenv("DB_USER") ?: throw IllegalStateException("DB_USER not set"),
                password = System.getenv("DB_PASSWORD"),
                database = System.getenv("DB_NAME") ?: throw IllegalStateException("DB_NAME not set")
            )
            else -> throw IllegalArgumentException("Unknown environment: $env")
        }.also { config = it }
    }

    fun getConnectionString(): String {
        val currentConfig = config ?: throw IllegalStateException("Config not loaded")
        return buildString {
            append("jdbc:postgresql://")
            append(currentConfig.host)
            append(":")
            append(currentConfig.port)
            append("/")
            append(currentConfig.database)
            append("?user=")
            append(currentConfig.username)
            currentConfig.password?.let {
                append("&password=")
                append(it)
            }
        }
    }
}

// Usage
val configManager = ConfigurationManager()
try {
    val config = configManager.loadConfig("development")
    val connectionString = configManager.getConnectionString()
    println("Connected to: $connectionString")
} catch (e: Exception) {
    println("Failed to load configuration: ${e.message}")
}
```

### 3. API Response Handling
```kotlin
sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String, val code: Int) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}

data class User(
    val id: String,
    val name: String,
    val email: String?,
    val profileImage: String?
)

class UserApiClient {
    suspend fun fetchUser(userId: String): ApiResponse<User> {
        return try {
            // Simulated API call
            val response = makeApiCall("users/$userId")
            val user = parseUser(response)
            ApiResponse.Success(user)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Unknown error", 500)
        }
    }

    private suspend fun makeApiCall(endpoint: String): String {
        // Simulated API call
        return "{\"id\":\"123\",\"name\":\"John Doe\",\"email\":\"john@example.com\",\"profileImage\":null}"
    }

    private fun parseUser(json: String): User {
        // Simulated JSON parsing
        return User(
            id = "123",
            name = "John Doe",
            email = "john@example.com",
            profileImage = null
        )
    }
}

// Usage
val client = UserApiClient()
runBlocking {
    when (val response = client.fetchUser("123")) {
        is ApiResponse.Success -> {
            val user = response.data
            println("User: ${user.name}")
            user.email?.let { println("Email: $it") }
            user.profileImage?.let { println("Profile image: $it") }
        }
        is ApiResponse.Error -> println("Error: ${response.message}")
        is ApiResponse.Loading -> println("Loading...")
    }
}
```

## Best Practices

### 1. Null Safety in Data Classes
```kotlin
data class Product(
    val id: String,
    val name: String,
    val description: String? = null,  // Optional field with default value
    val price: Double,
    val category: String? = null,     // Optional field with default value
    val tags: List<String> = emptyList()  // Non-null with default empty list
)

class ProductManager {
    fun createProduct(
        id: String,
        name: String,
        price: Double,
        description: String? = null,
        category: String? = null
    ): Product {
        return Product(
            id = id,
            name = name,
            description = description,
            price = price,
            category = category
        )
    }
}
```

### 2. Null Safety in Collections
```kotlin
class CollectionManager {
    // Nullable list of nullable items
    private var nullableItems: List<String?>? = null

    // Non-null list of nullable items
    private var itemsWithNulls: List<String?> = emptyList()

    // Non-null list of non-null items
    private var nonNullItems: List<String> = emptyList()

    fun processItems() {
        // Filter out nulls
        val nonNullOnly = itemsWithNulls.filterNotNull()

        // Map with null safety
        val lengths = itemsWithNulls.mapNotNull { it?.length }

        // Group by with null safety
        val groupedByLength = itemsWithNulls.groupBy { it?.length ?: 0 }
    }
}
```

## Practice Exercises

1. Create a `BankAccount` class with nullable fields and proper null safety
2. Implement a `FileManager` class that handles nullable file paths and content
3. Build a `Cache` system with nullable values and expiration
4. Create a `WeatherService` that handles nullable weather data
5. Implement a `UserPreferences` manager with optional settings

Remember:
- Use nullable types when a value can be null
- Prefer safe call operator over not-null assertion
- Use Elvis operator for default values
- Consider using sealed classes for better null handling
- Use `let` for null-safe code blocks
- Provide default values in data classes
- Use `filterNotNull()` for collections
- Consider using `lateinit` for properties initialized after construction
- Use `by lazy` for nullable properties that are expensive to compute 