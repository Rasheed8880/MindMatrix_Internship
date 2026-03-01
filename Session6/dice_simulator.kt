// ==========================================================
// SESSION 6 LAB
// Dice Rolling Simulator
// ==========================================================

import kotlin.random.Random

// Function to roll a single dice
fun rollDice(): Int {
    return Random.nextInt(1, 7) // Generates number between 1 and 6
}

// Function to roll two dice and print result
fun rollPair() {
    val dice1 = rollDice()
    val dice2 = rollDice()

    println("Dice 1: $dice1")
    println("Dice 2: $dice2")

    if (dice1 == dice2) {
        println("Double! You rolled two ${dice1}s!")
    } else {
        println("No match this time. Try again!")
    }

    println("----------------------------------")
}

// Main function
fun main() {

    println("🎲 Welcome to Dice Roller 🎲")
    println()

    // Roll dice 5 times
    repeat(5) {
        println("Roll #${it + 1}")
        rollPair()
    }

    println("Thanks for playing!")
}