# Coroutines in Kotlin

## Basic Concepts

### Coroutine Basics
```kotlin
import kotlinx.coroutines.*

// Launch a coroutine in the GlobalScope
fun basic() {
    GlobalScope.launch {
        delay(1000L) // Non-blocking delay
        println("World!")
    }
    println("Hello,")
    Thread.sleep(2000L) // Blocking the main thread
}

// Using runBlocking to block the current thread
fun blocking() = runBlocking {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
}

// Using coroutineScope for structured concurrency
suspend fun structured() = coroutineScope {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
}
```

### Suspend Functions
```kotlin
// Basic suspend function
suspend fun fetchUserData(): User {
    delay(1000L) // Simulates network delay
    return User("John", "Doe")
}

// Calling suspend functions from coroutines
fun fetchData() = runBlocking {
    val user = fetchUserData()
    println("User: ${user.firstName} ${user.lastName}")
}

// Sequential execution of suspend functions
suspend fun loadData() {
    val user = fetchUserData()
    val profile = fetchUserProfile(user.id)
    val settings = fetchUserSettings(user.id)
    println("Data loaded")
}
```

### Coroutine Context and Dispatchers
```kotlin
// Using different dispatchers
fun dispatchers() = runBlocking {
    // Main dispatcher - for UI operations (not available in plain JVM)
    // launch(Dispatchers.Main) { ... }
    
    // Default dispatcher - CPU-intensive tasks
    launch(Dispatchers.Default) {
        println("Default: Running on thread ${Thread.currentThread().name}")
    }
    
    // IO dispatcher - IO-intensive tasks
    launch(Dispatchers.IO) {
        println("IO: Running on thread ${Thread.currentThread().name}")
    }
    
    // Unconfined - starts in the caller thread, but may switch
    launch(Dispatchers.Unconfined) {
        println("Unconfined: Running on thread ${Thread.currentThread().name}")
        delay(100)
        println("Unconfined after delay: Running on thread ${Thread.currentThread().name}")
    }
}

// Custom dispatcher with a thread pool
fun customDispatcher() = runBlocking {
    val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
    
    launch(dispatcher) {
        println("Running on custom thread pool")
    }
    
    dispatcher.close() // Don't forget to close the dispatcher
}
```

## Advanced Coroutine Features

### Job Management
```kotlin
// Handling Jobs
fun jobManagement() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("Job running: $i")
            delay(100L)
        }
    }
    
    delay(1000L)
    println("Canceling job...")
    job.cancel() // Cancel the job
    job.join()   // Wait for job completion
    println("Job canceled")
}

// Job hierarchy
fun jobHierarchy() = runBlocking {
    val parentJob = launch {
        val childJob = launch {
            repeat(10) { i ->
                println("Child job: $i")
                delay(100L)
            }
        }
        delay(500L)
        println("Canceling parent...")
        // Canceling parent also cancels children
    }
    delay(1000L)
    parentJob.cancel()
    parentJob.join()
    println("All jobs completed")
}
```

### Exception Handling
```kotlin
// Basic exception handling
fun exceptionHandling() = runBlocking {
    val job = launch {
        try {
            delay(100L)
            throw RuntimeException("Error in coroutine")
        } catch (e: Exception) {
            println("Caught exception: ${e.message}")
        }
    }
    job.join()
}

// SupervisorJob - failure of a child doesn't affect other children
fun supervisorJob() = runBlocking {
    val supervisor = SupervisorJob()
    
    with(CoroutineScope(coroutineContext + supervisor)) {
        val job1 = launch {
            delay(100L)
            throw RuntimeException("Error in job1")
        }
        
        val job2 = launch {
            delay(200L)
            println("Job2 still running")
        }
        
        delay(300L)
        println("job1 state: ${job1.isCancelled}, job2 state: ${job2.isActive}")
    }
}

// CoroutineExceptionHandler
fun exceptionHandler() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught exception: ${exception.message}")
    }
    
    val job = GlobalScope.launch(handler) {
        throw RuntimeException("Error in coroutine")
    }
    
    job.join()
}
```

### Async and Await
```kotlin
// Sequential execution
suspend fun sequential() {
    val time = measureTimeMillis {
        val user = fetchUserData()        // ~1000ms
        val profile = fetchUserProfile()  // ~1000ms
        println("User: $user, Profile: $profile")
    }
    println("Sequential execution took $time ms")
}

// Parallel execution with async/await
suspend fun parallel() = coroutineScope {
    val time = measureTimeMillis {
        val userDeferred = async { fetchUserData() }
        val profileDeferred = async { fetchUserProfile() }
        
        val user = userDeferred.await()
        val profile = profileDeferred.await()
        
        println("User: $user, Profile: $profile")
    }
    println("Parallel execution took $time ms")
}

// Lazy async execution
suspend fun lazyAsync() = coroutineScope {
    val time = measureTimeMillis {
        val userDeferred = async(start = CoroutineStart.LAZY) { fetchUserData() }
        val profileDeferred = async(start = CoroutineStart.LAZY) { fetchUserProfile() }
        
        // Start both operations
        userDeferred.start()
        profileDeferred.start()
        
        val user = userDeferred.await()
        val profile = profileDeferred.await()
        
        println("User: $user, Profile: $profile")
    }
    println("Lazy async execution took $time ms")
}
```

## Real-world Examples

### 1. Network API Client
```kotlin
class ApiClient(private val baseUrl: String) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()
    
    suspend fun fetchUser(userId: String): User = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("$baseUrl/users/$userId")
            .build()
            
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected response ${response.code}")
            }
            
            // Parse JSON response to User object
            parseUserResponse(response.body?.string() ?: "")
        }
    }
    
    suspend fun fetchMultipleUsers(userIds: List<String>): List<User> = coroutineScope {
        userIds.map { userId ->
            async(Dispatchers.IO) {
                fetchUser(userId)
            }
        }.awaitAll()
    }
    
    private fun parseUserResponse(json: String): User {
        // JSON parsing logic here
        return User("John", "Doe")
    }
}

// Usage
suspend fun loadUsersExample() {
    val client = ApiClient("https://api.example.com")
    
    try {
        val user = client.fetchUser("123")
        println("Loaded user: ${user.firstName} ${user.lastName}")
        
        val users = client.fetchMultipleUsers(listOf("123", "456", "789"))
        println("Loaded ${users.size} users")
    } catch (e: Exception) {
        println("Error loading users: ${e.message}")
    }
}
```

### 2. Background Task Manager
```kotlin
class BackgroundTaskManager {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val tasks = mutableMapOf<String, Job>()
    
    fun startTask(id: String, task: suspend () -> Unit) {
        val job = scope.launch {
            try {
                task()
            } catch (e: CancellationException) {
                // Task was canceled, clean up
                println("Task $id was canceled")
                throw e
            } catch (e: Exception) {
                println("Task $id failed: ${e.message}")
            } finally {
                tasks.remove(id)
                println("Task $id completed")
            }
        }
        
        tasks[id] = job
    }
    
    fun cancelTask(id: String) {
        tasks[id]?.cancel()
    }
    
    fun cancelAllTasks() {
        tasks.forEach { (_, job) -> job.cancel() }
    }
    
    suspend fun waitForTask(id: String) {
        tasks[id]?.join()
    }
    
    suspend fun waitForAllTasks() {
        tasks.forEach { (_, job) -> job.join() }
    }
    
    fun isTaskActive(id: String): Boolean {
        return tasks[id]?.isActive == true
    }
    
    fun shutdown() {
        scope.cancel()
    }
}

// Usage
suspend fun backgroundTaskExample() {
    val taskManager = BackgroundTaskManager()
    
    taskManager.startTask("task1") {
        println("Task 1 started")
        delay(1000L)
        println("Task 1 completed")
    }
    
    taskManager.startTask("task2") {
        println("Task 2 started")
        delay(2000L)
        println("Task 2 completed")
    }
    
    delay(500L)
    taskManager.cancelTask("task2")
    
    taskManager.waitForAllTasks()
    println("All tasks completed or canceled")
}
```

### 3. Data Cache with Coroutines
```kotlin
class DataCache<K, V> {
    private val cache = ConcurrentHashMap<K, Deferred<V>>()
    private val mutex = Mutex()
    
    suspend fun get(key: K, dataProvider: suspend () -> V): V = coroutineScope {
        // Check if data exists in cache
        cache[key]?.let { 
            return@coroutineScope it.await() 
        }
        
        // Prevent multiple coroutines from fetching same data
        mutex.withLock {
            // Check again after acquiring lock
            cache[key]?.let { 
                return@coroutineScope it.await() 
            }
            
            // Create new deferred and store in cache
            val deferred = async { dataProvider() }
            cache[key] = deferred
            deferred.await()
        }
    }
    
    suspend fun refresh(key: K, dataProvider: suspend () -> V): V = coroutineScope {
        mutex.withLock {
            val deferred = async { dataProvider() }
            cache[key] = deferred
            deferred.await()
        }
    }
    
    suspend fun clear() {
        mutex.withLock {
            cache.clear()
        }
    }
    
    suspend fun remove(key: K) {
        mutex.withLock {
            cache.remove(key)
        }
    }
}

// Usage
suspend fun cacheExample() {
    val userCache = DataCache<String, User>()
    
    val user1 = userCache.get("user123") {
        println("Fetching user data from network")
        delay(1000L) // Simulate network fetch
        User("John", "Doe")
    }
    
    val user2 = userCache.get("user123") {
        println("This won't be printed because data is cached")
        delay(1000L)
        User("Jane", "Smith")
    }
    
    println("User from cache: ${user2.firstName} ${user2.lastName}")
}
```

## Practice Exercises

1. Create a parallel file processor that uses coroutines to process multiple files
2. Build a web scraper that uses coroutines to fetch and parse multiple web pages
3. Implement a task scheduler with coroutines, priorities, and dependencies
4. Create a coroutine-based caching system with time-based expiration
5. Build a parallel image processing pipeline using coroutines

Remember:
- Use structured concurrency for better error handling and resource management
- Choose appropriate dispatchers based on the task (CPU or IO bound)
- Leverage coroutine scopes for lifecycle management
- Prefer suspend functions over blocking calls
- Use async/await for parallel execution
- Handle exceptions properly with try/catch or exception handlers
- Consider using flow for asynchronous streams of data 