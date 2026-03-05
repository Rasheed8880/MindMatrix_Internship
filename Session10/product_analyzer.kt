// ==========================================================
// SESSION 10 LAB
// Product Analyzer using Higher-Order Functions
// ==========================================================

// 1) Data class for product
data class Product(
    val name: String,
    val category: String,
    val price: Double
)

// 2) Function to print product list
fun printProducts(title: String, products: List<Product>) {

    println("\n$title")

    if (products.isEmpty()) {
        println("No products found.")
        return
    }

    for (p in products) {
        println("Name: ${p.name} | Category: ${p.category} | Price: ₹%.2f".format(p.price))
    }
}

// 3) Higher-order function to apply discounts
fun applyDiscount(
    products: List<Product>,
    discountFunction: (Double) -> Double
): List<Product> {

    return products.map {
        it.copy(price = discountFunction(it.price))
    }
}

// 4) Generic filter function
fun <T> filterList(list: List<T>, condition: (T) -> Boolean): List<T> {
    return list.filter(condition)
}

fun main() {

    // 5) Sample product list
    val products = listOf(
        Product("Laptop", "Electronics", 75000.0),
        Product("Headphones", "Electronics", 2500.0),
        Product("Chair", "Furniture", 4000.0),
        Product("Table", "Furniture", 7000.0),
        Product("Notebook", "Stationery", 50.0)
    )

    // Print all products
    printProducts("--- All Products ---", products)

    // Apply 10% discount
    val discountedProducts = applyDiscount(products) { price -> price * 0.9 }

    printProducts("--- Products After 10% Discount ---", discountedProducts)

    // Filter expensive products
    val expensiveProducts = filterList(products) { it.price > 5000 }

    printProducts("--- Expensive Products (>5000) ---", expensiveProducts)

    // Filter electronics
    val electronics = filterList(products) { it.category == "Electronics" }

    printProducts("--- Electronics Category ---", electronics)
}