fun main(){
    println("\n===== REAL-WORLD EXAMPLE: WEATHER APP =====")
    
    // Weather forecast simulator
    val temperatures = arrayOf(22, 25, 27, 30, 28, 26, 24)
    val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    
    println("Weekly Weather Forecast:")
    for (i in temperatures.indices) {
        val temp = temperatures[i]
        val weatherCondition = when {
            temp < 20 -> "Cool"
            temp in 20..25 -> "Pleasant"
            temp in 26..30 -> "Warm"
            else -> "Hot"
        }
        
        val recommendation = when (weatherCondition) {
            "Cool" -> "Bring a jacket"
            "Pleasant" -> "Perfect for outdoor activities"
            "Warm" -> "Stay hydrated"
            "Hot" -> "Avoid extended sun exposure"
            else -> ""
        }
        
        println("${days[i]}: $temp°C - $weatherCondition. $recommendation")
    }
    
    // Calculate average temperature
    var sum = 0
    for (temp in temperatures) {
        sum += temp
    }
    val average = sum.toDouble() / temperatures.size
    println("\nAverage temperature: %.1f°C".format(average))
    
    // Find warmest day
    var maxTemp = temperatures[0]
    var maxTempDay = days[0]
    for (i in 1 until temperatures.size) {
        if (temperatures[i] > maxTemp) {
            maxTemp = temperatures[i]
            maxTempDay = days[i]
        }
    }
    println("Warmest day: $maxTempDay with $maxTemp°C")
}