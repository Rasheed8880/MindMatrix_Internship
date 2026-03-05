// ============================================
// SESSION 8 LAB
// Console Art Gallery Navigator
// ============================================

// Data class to store artwork information
data class Artwork(
    val title: String,
    val artist: String,
    val year: Int
)

// Function to display the current artwork
fun displayArtwork(artwork: Artwork, index: Int, total: Int) {
    println("\n--- Artwork ${index + 1} of $total ---")
    println("Title  : ${artwork.title}")
    println("Artist : ${artwork.artist}")
    println("Year   : ${artwork.year}")
}

// Function to move to the next artwork (wraps around)
fun nextArtwork(current: Int, size: Int): Int {
    return (current + 1) % size
}

// Function to move to the previous artwork (wraps around)
fun previousArtwork(current: Int, size: Int): Int {
    return if (current - 1 < 0) size - 1 else current - 1
}

fun main() {

    val gallery = listOf(
        Artwork("Starry Night", "Vincent van Gogh", 1889),
        Artwork("Mona Lisa", "Leonardo da Vinci", 1503),
        Artwork("The Persistence of Memory", "Salvador Dali", 1931),
        Artwork("The Scream", "Edvard Munch", 1893),
        Artwork("Girl with a Pearl Earring", "Johannes Vermeer", 1665)
    )

    var currentIndex = 0
    var running = true

    println("===== Console Art Gallery Navigator =====")

    while (running) {

        displayArtwork(gallery[currentIndex], currentIndex, gallery.size)

        println("\nOptions:")
        println("n - Next Artwork")
        println("p - Previous Artwork")
        println("q - Quit")

        print("Enter choice: ")
        when (readLine()?.lowercase()) {

            "n" -> currentIndex = nextArtwork(currentIndex, gallery.size)

            "p" -> currentIndex = previousArtwork(currentIndex, gallery.size)

            "q" -> {
                println("Exiting gallery. Goodbye!")
                running = false
            }

            else -> println("Invalid option. Please try again.")
        }
    }
}