// ==========================================================
// SESSION 7 LAB
// Console-Based Tip Calculator
// ==========================================================

import kotlin.math.ceil

// Function to calculate tip
fun calculateTip(amount: Double, percent: Double, roundUp: Boolean): Double {
    var tip = amount * percent / 100

    if (roundUp) {
        tip = ceil(tip)
    }

    return tip
}

// Function to format amount to 2 decimal places
fun formatAmount(value: Double): String {
    return "%.2f".format(value)
}

fun main() {

    println("=== Tip Calculator ===")

    print("Enter bill amount: ")
    val amount = readLine()?.toDoubleOrNull() ?: 0.0

    print("Enter tip percentage: ")
    val percent = readLine()?.toDoubleOrNull() ?: 0.0

    print("Round up tip? (yes/no): ")
    val roundInput = readLine()?.lowercase()

    val roundUp = roundInput == "yes"

    val tip = calculateTip(amount, percent, roundUp)
    val total = amount + tip

    println("\n--- Calculation Result ---")
    println("Tip Amount : ₹${formatAmount(tip)}")
    println("Total Bill : ₹${formatAmount(total)}")
}