import com.github.xserxses.nqueensproblem.utils.formatGameTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class FormatGameTimeTest {

    @Test
    fun `formatGameTime with positive duration`() {
        // Given
        val duration = 1.hours + 23.minutes + 45.seconds

        // When
        val formattedTime = formatGameTime(duration)

        // Then
        assertEquals("01:23:45", formattedTime)
    }

    @Test
    fun `formatGameTime with negative duration`() {
        // Given
        val duration = (-1).hours

        // When
        val formattedTime = formatGameTime(duration)

        // Then
        assertEquals("00:00:00", formattedTime)
    }

    @Test
    fun `formatGameTime with zero duration`() {
        // Given
        val duration = 0.seconds

        // When
        val formattedTime = formatGameTime(duration)

        // Then
        assertEquals("00:00:00", formattedTime)
    }

    @Test
    fun `formatGameTime with exactly one hour`() {
        // Given
        val duration = 1.hours

        // When
        val formattedTime = formatGameTime(duration)

        // Then
        assertEquals("01:00:00", formattedTime)
    }

    @Test
    fun `formatGameTime with duration just under one hour`() {
        // Given
        val duration = 59.minutes + 59.seconds

        // When
        val formattedTime = formatGameTime(duration)

        // Then
        assertEquals("00:59:59", formattedTime)
    }

    @Test
    fun `formatGameTime with large number of hours`() {
        // Given
        val duration = 123.hours + 45.minutes + 6.seconds

        // When
        val formattedTime = formatGameTime(duration)

        // Then
        assertEquals("123:45:06", formattedTime)
    }

    @Test
    fun `formatGameTime with only seconds`() {
        // Given
        val duration = 30.seconds

        // When
        val formattedTime = formatGameTime(duration)

        // Then
        assertEquals("00:00:30", formattedTime)
    }

    @Test
    fun `formatGameTime with only minutes`() {
        // Given
        val duration = 15.minutes

        // When
        val formattedTime = formatGameTime(duration)

        // Then
        assertEquals("00:15:00", formattedTime)
    }
}
