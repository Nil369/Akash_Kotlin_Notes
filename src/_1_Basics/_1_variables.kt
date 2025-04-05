package _1_Basics

fun main(){

    // 1. Printing in kotlin
    print("Hello World.")
    print(" Hello from Kotlin!\n")
    println("This is Akash.")
    println("He is a Full Stack Developer")



    // 2. Variables in Kotlin
    /*
    • Variables are containers for storing data in our program
    • In Kotlin we declare variable (the value which is mutable/changeable)
      using the 'var' keyword & constant value (immutable) using tha 'val' keyword

     ++++ Naming the Variables ++++
        → Names can contain letters, digits, underscores, and dollar signs
        → Names should start with a letter
        → Names can also begin with $ and _
        → Names are case-sensitive ("myVar" and "myvar" are different variables)
        → Names should start with a lowercase letter, and it cannot contain whitespace
        → Reserved words (like Kotlin keywords, such as var or String) cannot be used as names
    */


    var a = 20
    val b = 40
    // b = 69 // This will produce error as var is immutable


    // var userName; // This produces error as kotlin don't know the type of Data. If we assign it as soon as
    // we declare variable it works fine. For declaring variables like this we should specify its type first.
    val userName:String;
    userName = "Akash";
    println("Username: "+userName) // This is string Concatenation

    
    val name = "Akash Halder"
    val profession = "Programmer"
    println("Hello! My name is: $name and I'm a $profession") // This is String Interpolation


    val x = 10
    val y = 20
    println("The sum of $x and $y is: ${x+y}")

}