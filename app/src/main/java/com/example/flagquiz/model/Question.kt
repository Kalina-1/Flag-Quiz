
// This class holds all the info for a quiz question:
// the flag image resource, a list of answer options,
// the correct answer itself, and a little hint about the capital city.
// Makes it easy to pass around all question details together.
data class Question(
    val flagImage: Int,
    val options: List<String>,
    val correctAnswer: String,
    val capitalHint: String
)
