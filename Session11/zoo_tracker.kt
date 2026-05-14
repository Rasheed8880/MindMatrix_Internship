// ==========================================================
// Zoo Animal Tracker
// Demonstrates Inheritance, Abstract Classes, Interfaces,
// and Polymorphism in Kotlin
// ==========================================================

// Abstract class
abstract class Animal(
    val name: String
) {
    abstract fun makeSound()

    fun eat() {
        println("$name is eating.")
    }
}

// Interface for animals that can perform tricks
interface TrickPerformer {
    fun performTrick()
}

// Lion class (inherits Animal)
class Lion(name: String) : Animal(name) {

    override fun makeSound() {
        println("$name roars loudly!")
    }
}

// Elephant class (inherits Animal)
class Elephant(name: String) : Animal(name) {

    override fun makeSound() {
        println("$name trumpets!")
    }

    fun sprayWater() {
        println("$name sprays water with its trunk!")
    }
}

// Parrot class (inherits Animal and implements interface)
class Parrot(name: String) : Animal(name), TrickPerformer {

    override fun makeSound() {
        println("$name squawks!")
    }

    override fun performTrick() {
        println("$name mimics human words!")
    }
}

fun main() {

    // Create animals
    val lion = Lion("Leo")
    val elephant = Elephant("Dumbo")
    val parrot = Parrot("Polly")

    // Store in a list of type Animal (Polymorphism)
    val zooAnimals: List<Animal> = listOf(lion, elephant, parrot)

    println("=== Zoo Animal Tracker ===")

    for (animal in zooAnimals) {
        animal.makeSound()
        animal.eat()

        // Check specific behaviors
        if (animal is Elephant) {
            animal.sprayWater()
        }

        if (animal is TrickPerformer) {
            animal.performTrick()
        }

        println()
    }
}