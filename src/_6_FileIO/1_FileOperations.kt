package _6_FileIO

import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

/**
 * This file demonstrates file I/O operations in Kotlin
 * including reading, writing, and managing files and directories.
 * 
 * Note: This example creates temporary files in the system's temp directory.
 * These files should be automatically cleaned up, but you can manually delete
 * them if needed.
 */
fun main() {
    // Create a temporary directory for our examples
    val tempDir = Files.createTempDirectory("kotlin_file_io_example")
    println("Created temporary directory: $tempDir")
    
    try {
        println("\n===== BASIC FILE OPERATIONS =====")
        
        // Create a simple text file
        val sampleFilePath = tempDir.resolve("sample.txt")
        Files.write(
            sampleFilePath, 
            listOf("Hello, Kotlin File I/O!", "This is a sample file.", "With multiple lines."),
            StandardCharsets.UTF_8
        )
        println("Created sample file: $sampleFilePath")
        
        // Check if file exists
        println("File exists: ${Files.exists(sampleFilePath)}")
        
        // Get file information
        val attributes = Files.readAttributes(sampleFilePath, BasicFileAttributes::class.java)
        println("File size: ${attributes.size()} bytes")
        println("Creation time: ${attributes.creationTime()}")
        println("Last modified: ${attributes.lastModifiedTime()}")
        
        // Rename file
        val renamedFilePath = tempDir.resolve("renamed.txt")
        Files.move(sampleFilePath, renamedFilePath)
        println("Renamed file to: $renamedFilePath")
        
        // Copy file
        val copiedFilePath = tempDir.resolve("copied.txt")
        Files.copy(renamedFilePath, copiedFilePath)
        println("Copied file to: $copiedFilePath")
        
        // Delete a file
        Files.delete(copiedFilePath)
        println("Deleted file: $copiedFilePath")
        println("Copied file still exists: ${Files.exists(copiedFilePath)}")
        
        println("\n===== READING FILES =====")
        
        // Read entire file as a single string
        val content = Files.readString(renamedFilePath)
        println("File content as string:")
        println(content)
        
        // Read file as a list of lines
        val lines = Files.readAllLines(renamedFilePath)
        println("\nFile content as lines:")
        lines.forEachIndexed { index, line -> println("${index + 1}: $line") }
        
        // Read file using BufferedReader
        println("\nReading with BufferedReader:")
        BufferedReader(FileReader(renamedFilePath.toFile())).use { reader ->
            reader.lines().forEach { println(it) }
        }
        
        // Read file byte by byte (for binary files)
        println("\nReading file byte-by-byte (first 20 bytes as integers):")
        FileInputStream(renamedFilePath.toFile()).use { input ->
            val bytes = ByteArray(20)
            val bytesRead = input.read(bytes)
            for (i in 0 until bytesRead) {
                print("${bytes[i].toInt()} ")
            }
            println()
        }
        
        println("\n===== WRITING FILES =====")
        
        // Create a new file using BufferedWriter
        val newFilePath = tempDir.resolve("new_file.txt")
        BufferedWriter(FileWriter(newFilePath.toFile())).use { writer ->
            writer.write("This is a new file.\n")
            writer.write("Created using BufferedWriter.\n")
            writer.write("In Kotlin File I/O example.")
        }
        println("Created a new file using BufferedWriter: $newFilePath")
        
        // Append to existing file
        Files.write(
            newFilePath,
            listOf("\nThis line is appended."),
            StandardOpenOption.APPEND
        )
        println("Appended text to the file")
        
        // Display the content of the new file
        println("\nContent of the new file:")
        Files.readAllLines(newFilePath).forEach { println(it) }
        
        // Write binary data
        val binaryFilePath = tempDir.resolve("binary_file.bin")
        FileOutputStream(binaryFilePath.toFile()).use { output ->
            for (b in 0..255) {
                output.write(b)
            }
        }
        println("\nCreated binary file: $binaryFilePath")
        println("Binary file size: ${Files.size(binaryFilePath)} bytes")
        
        println("\n===== DIRECTORY OPERATIONS =====")
        
        // Create directories
        val nestedDirsPath = tempDir.resolve("parent/child/grandchild")
        Files.createDirectories(nestedDirsPath)
        println("Created nested directories: $nestedDirsPath")
        
        // Create files in nested directories
        for (i in 1..3) {
            val filePath = nestedDirsPath.resolve("file$i.txt")
            Files.write(filePath, listOf("Content of file $i"))
            println("Created file: $filePath")
        }
        
        // List directory contents
        println("\nListing directory contents:")
        Files.list(tempDir).forEach { println(it) }
        
        // Walk directory tree (recursive listing)
        println("\nWalking directory tree (recursive):")
        Files.walk(tempDir).forEach { println(it) }
        
        // Find files matching a pattern
        println("\nFinding .txt files:")
        Files.newDirectoryStream(tempDir, "*.txt").use { stream ->
            stream.forEach { println(it) }
        }
        
        println("\n===== KOTLIN SPECIFIC FILE OPERATIONS =====")
        
        // Kotlin's File extensions
        val kotlinFile = File(tempDir.toFile(), "kotlin_specific.txt")
        
        // Write text with Kotlin extension
        kotlinFile.writeText("This file was created with Kotlin extensions.\nIt's simpler!")
        println("Created file with Kotlin extension: $kotlinFile")
        
        // Read text with Kotlin extension
        val kotlinFileContent = kotlinFile.readText()
        println("\nFile content read with Kotlin extension:")
        println(kotlinFileContent)
        
        // Read lines with Kotlin extension
        val kotlinFileLines = kotlinFile.readLines()
        println("\nLines read with Kotlin extension:")
        kotlinFileLines.forEachIndexed { index, line -> println("${index + 1}: $line") }
        
        // Append text with Kotlin extension
        kotlinFile.appendText("\nThis line was appended with Kotlin extension.")
        println("\nAppended text to file with Kotlin extension")
        println("Updated content: ${kotlinFile.readText()}")
        
        // Use kotlin.io.path extensions (Kotlin 1.5+)
        val kotlinPath = Path(tempDir.toString(), "kotlin_path.txt")
        if (!kotlinPath.exists()) {
            kotlinPath.parent.createDirectories()
            Files.writeString(kotlinPath, "Using kotlin.io.path extensions")
            println("\nCreated file with kotlin.io.path: $kotlinPath")
        }
        
        println("\n===== REAL-WORLD EXAMPLE: SIMPLE LOGGER =====")
        
        // Create a simple logger
        val logger = SimpleLogger(tempDir.resolve("application.log").toString())
        
        // Log some messages
        logger.info("Application started")
        logger.debug("Initializing components...")
        logger.info("User logged in: user123")
        logger.error("Failed to connect to database", IOException("Connection refused"))
        logger.info("Application closed")
        
        // Display log content
        println("\nLog file content:")
        Files.readAllLines(tempDir.resolve("application.log")).forEach { println(it) }
        
        println("\n===== REAL-WORLD EXAMPLE: FILE-BASED CONFIGURATION =====")
        
        // Create a configuration file
        val configFile = tempDir.resolve("config.properties").toFile()
        val config = FileBasedConfig(configFile)
        
        // Set configuration values
        config.setProperty("app.name", "Kotlin File I/O Demo")
        config.setProperty("app.version", "1.0.0")
        config.setProperty("database.url", "jdbc:mysql://localhost:3306/mydb")
        config.setProperty("database.username", "admin")
        config.setProperty("database.password", "secret")
        
        // Save the configuration
        config.save()
        println("Created configuration file: $configFile")
        
        // Reload the configuration
        val loadedConfig = FileBasedConfig(configFile)
        loadedConfig.load()
        
        // Display configuration values
        println("\nConfiguration values:")
        println("App name: ${loadedConfig.getProperty("app.name")}")
        println("App version: ${loadedConfig.getProperty("app.version")}")
        println("Database URL: ${loadedConfig.getProperty("database.url")}")
        println("Database username: ${loadedConfig.getProperty("database.username")}")
        println("Database password: ${loadedConfig.getProperty("database.password", "******")}")
        
        println("\n===== REAL-WORLD EXAMPLE: CSV PROCESSOR =====")
        
        // Create a CSV file
        val csvFilePath = tempDir.resolve("data.csv")
        Files.write(
            csvFilePath,
            listOf(
                "id,name,age,email",
                "1,John Doe,30,john.doe@example.com",
                "2,Jane Smith,25,jane.smith@example.com",
                "3,Bob Johnson,45,bob.johnson@example.com",
                "4,Alice Brown,35,alice.brown@example.com"
            )
        )
        println("Created CSV file: $csvFilePath")
        
        // Process the CSV file
        val csvProcessor = CsvProcessor()
        val records = csvProcessor.readCsv(csvFilePath.toString())
        
        // Display parsed records
        println("\nParsed CSV records:")
        records.forEach { println(it) }
        
        // Update some records
        records.find { it["name"] == "John Doe" }?.let { it["age"] = "31" }
        records.find { it["name"] == "Jane Smith" }?.let { it["email"] = "jane.updated@example.com" }
        
        // Save the updated CSV
        val updatedCsvPath = tempDir.resolve("updated_data.csv")
        csvProcessor.writeCsv(updatedCsvPath.toString(), records)
        println("\nUpdated and saved CSV file: $updatedCsvPath")
        
        // Display the updated CSV content
        println("\nUpdated CSV content:")
        Files.readAllLines(updatedCsvPath).forEach { println(it) }
        
    } finally {
        // Clean up: delete the temporary directory and all its contents
        println("\n===== CLEANUP =====")
        println("Deleting temporary directory and its contents: $tempDir")
        Files.walk(tempDir)
            .sorted(Comparator.reverseOrder())
            .forEach { Files.delete(it) }
        println("Cleanup complete")
    }
}

// Real-world example: Simple Logger
class SimpleLogger(private val logFilePath: String) {
    private val logFile = File(logFilePath)
    
    init {
        // Create the log file if it doesn't exist
        if (!logFile.exists()) {
            logFile.parentFile?.mkdirs()
            logFile.createNewFile()
        }
    }
    
    private fun log(level: String, message: String) {
        val timestamp = java.time.LocalDateTime.now()
        val logEntry = "[$timestamp] $level: $message"
        logFile.appendText("$logEntry\n")
    }
    
    fun debug(message: String) {
        log("DEBUG", message)
    }
    
    fun info(message: String) {
        log("INFO", message)
    }
    
    fun warn(message: String) {
        log("WARN", message)
    }
    
    fun error(message: String, exception: Throwable? = null) {
        if (exception != null) {
            log("ERROR", "$message - ${exception.javaClass.simpleName}: ${exception.message}")
            
            // Also log the stack trace
            val stackTrace = StringWriter().apply {
                exception.printStackTrace(PrintWriter(this))
            }.toString()
            
            logFile.appendText(stackTrace)
        } else {
            log("ERROR", message)
        }
    }
}

// Real-world example: File-based Configuration
class FileBasedConfig(private val configFile: File) {
    private val properties = java.util.Properties()
    
    fun load() {
        if (configFile.exists()) {
            FileInputStream(configFile).use { input ->
                properties.load(input)
            }
        }
    }
    
    fun save() {
        configFile.parentFile?.mkdirs()
        FileOutputStream(configFile).use { output ->
            properties.store(output, "Configuration saved on " + java.time.LocalDateTime.now())
        }
    }
    
    fun setProperty(key: String, value: String) {
        properties.setProperty(key, value)
    }
    
    fun getProperty(key: String, defaultValue: String = ""): String {
        return properties.getProperty(key, defaultValue)
    }
    
    fun getAllProperties(): Map<String, String> {
        val result = mutableMapOf<String, String>()
        for (key in properties.stringPropertyNames()) {
            result[key] = properties.getProperty(key)
        }
        return result
    }
}

// Real-world example: CSV Processor
class CsvProcessor {
    fun readCsv(filePath: String): List<MutableMap<String, String>> {
        val file = File(filePath)
        if (!file.exists()) {
            return emptyList()
        }
        
        val lines = file.readLines()
        if (lines.isEmpty()) {
            return emptyList()
        }
        
        val headers = lines[0].split(",")
        return lines.drop(1).map { line ->
            val values = line.split(",")
            val record = mutableMapOf<String, String>()
            
            headers.forEachIndexed { index, header ->
                record[header] = if (index < values.size) values[index] else ""
            }
            
            record
        }
    }
    
    fun writeCsv(filePath: String, records: List<Map<String, String>>) {
        if (records.isEmpty()) {
            return
        }
        
        val file = File(filePath)
        file.parentFile?.mkdirs()
        
        // Get all unique headers from all records
        val headers = records.flatMap { it.keys }.distinct()
        
        file.bufferedWriter().use { writer ->
            // Write headers
            writer.write(headers.joinToString(","))
            writer.newLine()
            
            // Write records
            records.forEach { record ->
                val line = headers.map { header -> record[header] ?: "" }.joinToString(",")
                writer.write(line)
                writer.newLine()
            }
        }
    }
    
    fun filterByColumn(records: List<Map<String, String>>, column: String, value: String): List<Map<String, String>> {
        return records.filter { it[column] == value }
    }
} 