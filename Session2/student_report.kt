fun main() {

    println("===== Student Performance Report =====")

    print("Enter student name: ")
    val studentName = readLine()!!

    print("Enter number of subjects: ")
    val numberOfSubjects = readLine()!!.toInt()

    var totalMarks = 0

    for (i in 1..numberOfSubjects) {
        print("Enter marks for subject $i: ")
        val marks = readLine()!!.toInt()

        // Condition to validate marks
        if (marks in 0..100) {
            totalMarks += marks
        } else {
            println("Invalid marks entered! Please enter marks between 0 and 100.")
            return
        }
    }

    val average = totalMarks / numberOfSubjects

    val grade = calculateGrade(average)

    printReport(studentName, totalMarks, average, grade)
}

fun calculateGrade(average: Int): String {
    return when {
        average >= 90 -> "A+"
        average >= 75 -> "A"
        average >= 60 -> "B"
        average >= 50 -> "C"
        else -> "Fail"
    }
}

fun printReport(name: String, total: Int, avg: Int, grade: String) {
    println("\n===== Report Card =====")
    println("Student Name: $name")
    println("Total Marks: $total")
    println("Average Marks: $avg")
    println("Grade: $grade")

    if (grade == "Fail") {
        println("Status: Needs Improvement")
    } else {
        println("Status: Passed")
    }
}