// ==========================================================
// Session 12 Lab
// Theme Switching and Progress Animation Simulation
// ==========================================================

// Data class to represent a theme
data class Theme(
    val name: String,
    val background: String,
    val textColor: String
)

// Function to apply a theme
fun applyTheme(theme: Theme) {
    println("\nApplying Theme: ${theme.name}")
    println("Background: ${theme.background}")
    println("Text Color: ${theme.textColor}")
}

// Function to simulate progress animation
fun animateProgress(totalSteps: Int) {

    println("\nLoading Progress:")

    for (i in 1..totalSteps) {

        val progressBar = "#".repeat(i) + "-".repeat(totalSteps - i)

        println("[$progressBar] ${i * 100 / totalSteps}%")

        Thread.sleep(200) // pause to simulate animation
    }

    println("Loading Complete!")
}

fun main() {

    println("=== Theme Switcher Simulation ===")

    // Create themes
    val lightTheme = Theme("Light Theme", "White", "Black")
    val darkTheme = Theme("Dark Theme", "Black", "White")
    val blueTheme = Theme("Blue Theme", "Blue", "Yellow")

    // Apply themes
    applyTheme(lightTheme)
    applyTheme(darkTheme)
    applyTheme(blueTheme)

    // Run progress animation
    animateProgress(10)
}