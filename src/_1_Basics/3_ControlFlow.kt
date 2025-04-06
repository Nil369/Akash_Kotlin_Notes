package _1_Basics

/**
 * This file demonstrates different control flow statements in Kotlin
 * including if-else, when, and various loop constructs.
*/

fun main() {
    println("===== IF-ELSE STATEMENTS =====")
    

    // Basic if-else
    print("Enter Your age: ")
    val age = readln().toInt() // Taking Input from user and Typecasting to Integer

    if (age >= 18) {
        println("You are an adult")
    } else {
        println("You are a minor")
    }
    

    // If-else as an expression (returns a value)
    val message = if (age >= 21) {
        "You can drink in the US"
    } else {
        "You are too young to drink in the US"
    }
    println(message)
    

    // Multiple branches with if-else if-else
    val score = 85
    val grade = if (score >= 90) {
        "A"
    } else if (score >= 80) {
        "B"
    } else if (score >= 70) {
        "C"
    } else if (score >= 60) {
        "D"
    } else {
        "F"
    }
    println("Score: $score, Grade: $grade")
    



    println("\n===== WHEN EXPRESSION =====")
    
    // Basic when expression (similar to switch in other languages)
    val dayOfWeek = 3
    val day = when (dayOfWeek) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> "Invalid day"
    }
    println("Day $dayOfWeek is $day")
    
    // When with multiple values per branch
    val number = 15
    when (number) {
        0 -> println("Zero")
        1, 2, 3 -> println("Small number")
        in 4..9 -> println("Medium number")
        in 10..99 -> println("Large number")
        else -> println("Very large number")
    }
    
    // When with arbitrary conditions
    val temperature = 28
    when {
        temperature < 0 -> println("Freezing")
        temperature in 0..10 -> println("Very cold")
        temperature in 11..20 -> println("Cool")
        temperature in 21..30 -> println("Warm")
        else -> println("Hot")
    }
    



    println("\n===== FOR LOOPS =====")
    
    // Loop through a range
    for (i in 1..5) {
        print("$i ")
    }
    println()
    
    // Loop with step
    for (i in 1..10 step 2) {
        print("$i ")
    }
    println()
    
    // Loop with until (exclusive upper bound)
    for (i in 1 until 5) {  // equivalent to 1..4
        print("$i ")
    }
    println()
    
    // Reverse loop
    for (i in 5 downTo 1) {
        print("$i ")
    }
    println()
    
    // Loop through arrays
    val fruits = arrayOf("Apple", "Banana", "Cherry", "Durian", "Elderberry")
    for (fruit in fruits) {
        print("$fruit ")
    }
    println()
    
    // Loop with index
    for ((index, value) in fruits.withIndex()) {
        println("Item at $index is $value")
    }
    


    println("\n===== WHILE AND DO-WHILE LOOPS =====")
    
    // While loop
    var counter = 0
    while (counter < 5) {
        print("${counter++} ")
    }
    println()
    
    // Do-while loop (always executes at least once)
    counter = 0
    do {
        print("${counter++} ")
    } while (counter < 5)
    println()
    
} 