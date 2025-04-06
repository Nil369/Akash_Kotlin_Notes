package _1_Basics

class User(
    val name:String,
    val phone:Number,
    val address: String
){
    fun getDetails(){
        println("Name: $name")
        println("Phone No. : $phone")
        println("Address: $address")
    }
}

fun main(){
    // Number Types:
    val number1:Byte = 127 // Highest no. byte can store
    val number2:Short = 12345 // Up to 5 digits no. Short can hold
    val number3:Int = 1234567890 // Can hold up to 10 digits in Int
    val number4:Long = 1234567899999999900 // It can hold up to 20 digits
    println("Number 1: $number1")
    println("Number 2: $number2")
    println("Number 3: $number3")
    println("Number 4: $number4\n")

    val marks: Float = 34.67F
    val price: Double = 1232.46
    println("Marks: $marks")
    println("Price: $price\n")


    // Char & String:
    val ch: Char = 'Q'
    val userName: String = "Akash Halder"
    val message: String = "I love kotlin and i am going to program android apps"


    // Boolean:
    val isActive: Boolean = false


    // Array:
    val favActivities: Array<String> = arrayOf("cricket", "chess", "music", "programming")
    println(favActivities[1])
    favActivities[2] = "Listen Music"
    println(favActivities[2])


    // NOTE:: In kotlin all DataTypes are Objects => Non-Primitive Data Types / Reference Data Types / User Defined Data Types
    println("${number1.toFloat()} \n")


    // User Defined Data Types:
    val user1: User = User("Akash", 1234567890, "Somewhere on Earth")
    user1.getDetails()

}