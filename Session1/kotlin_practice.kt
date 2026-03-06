// Function to display a greeting
fun greetUser(name: String) {
    println("Hello, $name! Welcome to Kotlin practice.")
}

// Function to display a list of hobbies
fun showHobbies(hobbies: List<String>) {
    println("\nYour hobbies are:")
    for (hobby in hobbies) {
        println("- $hobby")
    }
}

// Function to count hobbies
fun countHobbies(hobbies: List<String>) {
    println("\nTotal hobbies: ${hobbies.size}")
}

fun main() {

    // Variables
    val userName = "Rasheed"
    val age = 22

    // String template
    println("My name is $userName and I am $age years old.")

    // Call greeting function
    greetUser(userName)

    // List of hobbies
    val hobbies = listOf(
        "Coding",
        "Reading",
        "Gaming",
        "Watching Tech Videos"
    )

    // Call functions
    showHobbies(hobbies)
    countHobbies(hobbies)
}