// ==========================================================
// SESSION 5 LAB
// Console-Based Contact Manager
// ==========================================================

// 1) Data class
data class Contact(
    val name: String,
    val phone: String?,
    val email: String?,
    val isFavorite: Boolean
)

// 2) Function to display contacts
fun displayContacts(title: String, contacts: List<Contact>) {
    println("\n==== $title ====")

    if (contacts.isEmpty()) {
        println("No contacts found.")
        return
    }

    contacts.forEach { contact ->
        println("Name      : ${contact.name}")
        println("Phone     : ${contact.phone ?: "Not Available"}")
        println("Email     : ${contact.email ?: "Not Available"}")
        println("Favorite  : ${if (contact.isFavorite) "Yes" else "No"}")
        println("------------------------------------")
    }
}

// 3) Higher-order function for filtering
fun filterContacts(
    contacts: List<Contact>,
    condition: (Contact) -> Boolean
): List<Contact> {
    return contacts.filter(condition)
}

// 4) Main function
fun main() {

    val contactList = listOf(
        Contact("Mohammed Rasheed", "8880397689", "rasheedhutti888@gmail.com", true),
        Contact("Android Learner", null, "learner@email.com", false),
        Contact("Compose Developer", "9876543210", null, true),
        Contact("Kotlin Student", null, null, false)
    )

    // Display all contacts
    displayContacts("All Contacts", contactList)

    // Filter favorite contacts
    val favoriteContacts = filterContacts(contactList) { it.isFavorite }
    displayContacts("Favorite Contacts", favoriteContacts)

    // Filter contacts with email
    val emailContacts = filterContacts(contactList) { it.email != null }
    displayContacts("Contacts With Email", emailContacts)
}