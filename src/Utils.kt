import java.math.BigInteger
import java.security.MessageDigest
import java.util.regex.Pattern
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Returns all matched strings of this pattern for a given input string.
 */
fun Pattern.findAllIncludeOverlapping(input: CharSequence, startIndex: Int = 0): List<String> {
    if (startIndex < 0 || startIndex > input.length) {
        throw IndexOutOfBoundsException("Start index out of bounds: $startIndex, input length: ${input.length}")
    }
    val m = matcher(input)
    val matches = mutableListOf<String>()
    if (m.find(startIndex)) {
        do {
            matches.add(m.group())
        } while (m.find(m.start(1) + 1))
    }
    return matches
}
