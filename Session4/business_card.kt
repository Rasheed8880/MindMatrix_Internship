// ==========================================================
// SESSION 4 LAB
// Console-Based Business Card using Kotlin
// ==========================================================

fun renderLogo(width: Int): String {
    val contentWidth = width - 4
    val logoText = "MM"
    val padding = (contentWidth - logoText.length) / 2
    val centeredLogo =
        " ".repeat(padding) + logoText + " ".repeat(contentWidth - padding - logoText.length)

    return buildString {
        append("|  " + " ".repeat(contentWidth) + "  |\n")
        append("|  $centeredLogo  |\n")
        append("|  " + " ".repeat(contentWidth) + "  |\n")
    }
}

fun renderHeader(name: String, title: String, width: Int): String {
    val contentWidth = width - 4

    fun formatLine(text: String): String {
        val trimmed = if (text.length > contentWidth)
            text.take(contentWidth)
        else
            text

        val padded = trimmed + " ".repeat(contentWidth - trimmed.length)
        return "|  $padded  |\n"
    }

    return formatLine(name) + formatLine(title)
}

fun renderContact(phone: String, email: String, width: Int): String {
    val contentWidth = width - 4

    fun formatLine(label: String, value: String): String {
        val fullText = "$label: $value"
        val trimmed = if (fullText.length > contentWidth)
            fullText.take(contentWidth)
        else
            fullText

        val padded = trimmed + " ".repeat(contentWidth - trimmed.length)
        return "|  $padded  |\n"
    }

    return formatLine("Phone", phone) +
           formatLine("Email", email)
}

fun renderBusinessCard(
    name: String,
    title: String,
    phone: String,
    email: String,
    width: Int = 50
): String {

    val border = "+" + "-".repeat(width - 2) + "+\n"

    return buildString {
        append(border)
        append(renderLogo(width))
        append(renderHeader(name, title, width))
        append(renderContact(phone, email, width))
        append(border)
    }
}

fun main() {
    val card = renderBusinessCard(
        name = "Mohammed Rasheed",
        title = "Android Developer",
        phone = "8880397689",
        email = "rasheedhutti888@gmail.com"
    )

    println(card)
}