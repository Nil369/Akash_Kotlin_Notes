# File I/O in Kotlin

## Basic File Operations

### Reading Files
```kotlin
// Read entire file as String
val content = File("data.txt").readText()

// Read file as List of lines
val lines = File("data.txt").readLines()

// Reading with buffered reader
File("data.txt").bufferedReader().use { reader ->
    val content = reader.readText()
}

// Process file line by line
File("data.txt").forEachLine { line ->
    println(line)
}

// Use sequence for large files
File("large_data.txt").useLines { lines ->
    lines.filter { it.contains("important") }
         .map { it.uppercase() }
         .forEach { println(it) }
}
```

### Writing Files
```kotlin
// Write String to file
File("output.txt").writeText("Hello, World!")

// Write lines to file
File("output.txt").writeLines(listOf("Line 1", "Line 2", "Line 3"))

// Append to file
File("log.txt").appendText("New log entry at ${System.currentTimeMillis()}\n")

// Using buffered writer
File("data.txt").bufferedWriter().use { writer ->
    writer.write("First line")
    writer.newLine()
    writer.write("Second line")
}

// Print writer
File("data.txt").printWriter().use { writer ->
    writer.println("Line with println")
    writer.print("Text without newline")
}
```

## Path Operations

### Working with Paths
```kotlin
// Creating Path objects
val filePath = Path("users/documents/file.txt")
val absolutePath = Path("/home/user/documents/file.txt")

// Path operations
val fileName = filePath.fileName  // file.txt
val parent = filePath.parent      // users/documents

// Path resolution
val basePath = Path("documents")
val resolvedPath = basePath.resolve("invoices/invoice-2023.pdf")

// Path normalization
val normalizedPath = Path("documents/../downloads/./file.txt").normalize()
```

## Real-world Examples

### 1. CSV File Processor
```kotlin
class CsvProcessor {
    fun processCsvFile(filePath: String, delimiter: String = ","): List<Map<String, String>> {
        val file = File(filePath)
        if (!file.exists()) {
            throw FileNotFoundException("CSV file not found: $filePath")
        }

        return file.useLines { lines ->
            val iterator = lines.iterator()
            
            // Read header and rows
            if (!iterator.hasNext()) return emptyList()
            val headers = iterator.next().split(delimiter)
            
            // Process each line
            iterator.asSequence().map { line ->
                val values = line.split(delimiter)
                // Create map of header to value
                headers.zip(values).toMap()
            }.toList()
        }
    }
}

// Usage
val processor = CsvProcessor()
val records = processor.processCsvFile("users.csv")
records.forEach { user ->
    println("User: ${user["name"]}, Email: ${user["email"]}")
}
```

### 2. Configuration File Manager
```kotlin
class ConfigManager {
    private val configFile = File("app_config.properties")
    private val properties = Properties()

    init {
        loadProperties()
    }

    private fun loadProperties() {
        if (configFile.exists()) {
            configFile.inputStream().use { stream ->
                properties.load(stream)
            }
        }
    }

    fun getProperty(key: String, defaultValue: String = ""): String {
        return properties.getProperty(key, defaultValue)
    }

    fun setProperty(key: String, value: String) {
        properties.setProperty(key, value)
        saveProperties()
    }

    private fun saveProperties() {
        configFile.outputStream().use { stream ->
            properties.store(stream, "Application Configuration")
        }
    }
}

// Usage
val config = ConfigManager()
val serverUrl = config.getProperty("server.url", "http://localhost:8080")
config.setProperty("api.key", "abc123xyz")
```

### 3. Log File Analyzer
```kotlin
class LogAnalyzer {
    fun analyzeLogFile(filePath: String, pattern: Regex): Map<String, Int> {
        val file = File(filePath)
        val results = mutableMapOf<String, Int>()

        file.useLines { lines ->
            lines.filter { it.contains(pattern) }
                 .forEach { line ->
                     val match = pattern.find(line)?.value ?: return@forEach
                     results[match] = results.getOrDefault(match, 0) + 1
                 }
        }

        return results
    }

    fun findErrorsInLog(filePath: String): List<String> {
        return File(filePath)
            .useLines { lines ->
                lines.filter { it.contains("ERROR", ignoreCase = true) }
                     .toList()
            }
    }
}

// Usage
val analyzer = LogAnalyzer()
val ipAddresses = analyzer.analyzeLogFile("access.log", Regex("\\d+\\.\\d+\\.\\d+\\.\\d+"))
val errors = analyzer.findErrorsInLog("application.log")
```

## Binary Files

### Reading and Writing Binary Data
```kotlin
// Write binary data
File("data.bin").outputStream().use { stream ->
    val bytes = byteArrayOf(1, 2, 3, 4, 5)
    stream.write(bytes)
}

// Read binary data
val bytes = File("data.bin").readBytes()

// Data input/output streams
File("numbers.dat").outputStream().use { fileOut ->
    DataOutputStream(BufferedOutputStream(fileOut)).use { dataOut ->
        dataOut.writeInt(42)
        dataOut.writeDouble(3.14)
        dataOut.writeUTF("Hello")
    }
}

File("numbers.dat").inputStream().use { fileIn ->
    DataInputStream(BufferedInputStream(fileIn)).use { dataIn ->
        val int = dataIn.readInt()
        val double = dataIn.readDouble()
        val string = dataIn.readUTF()
    }
}
```

## Practice Exercises

1. Create a file backup utility that copies files to a backup directory
2. Implement a text file merger that combines multiple files
3. Build a CSV to JSON converter
4. Create a log rotation system that archives log files
5. Implement a file encryption/decryption utility

Remember:
- Always use `use` to handle resource cleanup
- For large files, process line by line or use sequences
- Consider character encodings when working with text files
- Handle exceptions appropriately
- Use appropriate buffer sizes for binary operations
- Consider thread safety when writing to shared files 