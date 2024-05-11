import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object TextWidthUtils {

    @Composable
    fun findWidestText(currentDate: LocalDate): String {
        return (0 until 7).map { index ->
            val day = currentDate.plusDays(index.toLong())
            day.format(DateTimeFormatter.ofPattern("E"))
        }.maxByOrNull { it.length } ?: ""
    }

    @Composable
    fun calculateMinWidth(widestText: String): Float {
        return with(LocalDensity.current) {
            (64.dp * widestText.length).toPx() // Adjust the factor as needed
        }
    }
}
