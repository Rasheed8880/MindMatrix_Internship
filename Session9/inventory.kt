// ==========================================================
// SESSION 9 LAB
// Inventory Management System
// ==========================================================

// 1) Data class for inventory items
data class Item(
    val name: String,
    val category: String,
    val stock: Int,
    val price: Double
)

// 2) Generic filter function
fun <T> filterItems(items: List<T>, predicate: (T) -> Boolean): List<T> {
    return items.filter(predicate)
}

// 3) Function to compute totals by category
fun computeCategoryTotals(items: List<Item>): Map<String, Int> {
    val totals = mutableMapOf<String, Int>()

    for (item in items) {
        totals[item.category] = totals.getOrDefault(item.category, 0) + item.stock
    }

    return totals
}

// 4) Function to display items
fun displayItems(title: String, items: List<Item>) {
    println("\n$title")
    for (item in items) {
        println("${item.name} | Category: ${item.category} | Stock: ${item.stock} | Price: ${item.price}")
    }
}

// 5) Main function
fun main() {

    val inventory = listOf(
        Item("Laptop", "Electronics", 10, 75000.0),
        Item("Mouse", "Electronics", 25, 500.0),
        Item("Notebook", "Stationery", 50, 40.0),
        Item("Pen", "Stationery", 100, 10.0),
        Item("Water Bottle", "Accessories", 15, 250.0)
    )

    // Display all items
    displayItems("--- All Inventory Items ---", inventory)

    // Filter low stock items
    val lowStock = filterItems(inventory) { it.stock < 20 }
    displayItems("--- Low Stock Items ---", lowStock)

    // Filter electronics category
    val electronics = filterItems(inventory) { it.category == "Electronics" }
    displayItems("--- Electronics Category ---", electronics)

    // Compute category totals
    println("\n--- Category Totals ---")
    val totals = computeCategoryTotals(inventory)

    for ((category, total) in totals) {
        println("$category : $total items")
    }
}