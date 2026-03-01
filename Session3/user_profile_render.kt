// =========================================================
// SESSION 3 LAB
// Dynamic User Profile Console App
// =========================================================

data class UserProfile(
    val name: String,
    val age: Int,
    val email: String,
    val showAvatar: Boolean
)

// Function to render avatar box
fun renderAvatar(name: String) {
    val initials = name.split(" ")
        .map { it.first().uppercaseChar() }
        .joinToString("")

    println("+----------------+")
    println("|      $initials         |")
    println("+----------------+")
}

// Function to display user profile
fun displayProfile(user: UserProfile) {

    println("===================================")

    if (user.showAvatar) {
        renderAvatar(user.name)
    }

    println("Name  : ${user.name}")
    println("Age   : ${user.age}")
    println("Email : ${user.email}")

    println("===================================\n")
}

fun main() {

    val users = listOf(
        UserProfile("Mohammed Rasheed", 21, "rasheed@email.com", true),
        UserProfile("Android Learner", 20, "learner@email.com", false),
        UserProfile("Compose Developer", 22, "compose@email.com", true)
    )

    println("========== User Profiles ==========\n")

    for (user in users) {
        displayProfile(user)
    }

    println("========== End of Profiles ==========")
}