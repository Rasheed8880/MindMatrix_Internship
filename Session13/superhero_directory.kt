// ==========================================================
// Superhero Directory & 30-Day Schedule
// ==========================================================

// 1) Data class
data class Superhero(
    val name: String,
    val alias: String,
    val power: String,
    val favorite: Boolean = false,
    val accessibleDescription: String
)

// 2) Format hero information
fun formatHero(hero: Superhero): String {

    val favTag = if (hero.favorite) "[Fav]" else ""

    return "${hero.name} (${hero.alias}) - ${hero.power} $favTag (A11y: ${hero.accessibleDescription})"
}

// 3) Filter heroes by power
fun filterByPower(heroes: List<Superhero>, powerQuery: String): List<Superhero> {

    return heroes.filter {
        it.power.lowercase().contains(powerQuery.lowercase())
    }
}

// 4) Get favorite heroes
fun getFavorites(heroes: List<Superhero>): List<Superhero> {

    return heroes.filter { it.favorite }
}

// 5) Generate schedule
fun generateSchedule(heroes: List<Superhero>, days: Int): List<String> {

    val favorites = getFavorites(heroes)

    val sourceList = if (favorites.isNotEmpty()) favorites else heroes

    val schedule = mutableListOf<String>()

    for (day in 1..days) {

        val hero = sourceList[(day - 1) % sourceList.size]

        schedule.add("Day $day: ${hero.name} (${hero.alias})")
    }

    return schedule
}

fun main() {

    // 6) Create superhero list
    val heroes = listOf(
        Superhero(
            "Clark Kent",
            "Superman",
            "Flight",
            true,
            "Clark Kent, alias Superman, power Flight"
        ),
        Superhero(
            "Diana Prince",
            "Wonder Woman",
            "Strength",
            true,
            "Diana Prince, alias Wonder Woman, power Strength"
        ),
        Superhero(
            "Bruce Wayne",
            "Batman",
            "Intelligence",
            false,
            "Bruce Wayne, alias Batman, power Intelligence"
        ),
        Superhero(
            "Barry Allen",
            "Flash",
            "Speed",
            false,
            "Barry Allen, alias Flash, power Speed"
        ),
        Superhero(
            "Arthur Curry",
            "Aquaman",
            "Water Control",
            false,
            "Arthur Curry, alias Aquaman, power Water Control"
        ),
        Superhero(
            "Hal Jordan",
            "Green Lantern",
            "Energy Ring",
            false,
            "Hal Jordan, alias Green Lantern, power Energy Ring"
        )
    )

    // Print directory
    println("--- Superhero Directory ---")
    heroes.forEachIndexed { index, hero ->
        println("${index + 1}. ${formatHero(hero)}")
    }

    // Filter by power
    println("\n--- Filter: flight ---")

    val filtered = filterByPower(heroes, "flight")

    filtered.forEachIndexed { index, hero ->
        println("${index + 1}. ${formatHero(hero)}")
    }

    // Favorites
    println("\n--- Favorites ---")

    val favorites = getFavorites(heroes)

    favorites.forEachIndexed { index, hero ->
        println("${index + 1}. ${formatHero(hero)}")
    }

    // Generate schedule
    println("\n--- 30-Day Schedule ---")

    val schedule = generateSchedule(heroes, 30)

    schedule.forEach {
        println(it)
    }
}