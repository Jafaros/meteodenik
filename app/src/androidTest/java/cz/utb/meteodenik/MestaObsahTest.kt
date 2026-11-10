package cz.utb.meteodenik

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import cz.utb.meteodenik.ui.mesta.MestaUiState
import cz.utb.meteodenik.ui.mesta.SeznamMestObsah
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme
import org.junit.Rule
import org.junit.Test

class MestaObsahTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun prazdnyStavLzeZobrazitBezViewModelu() {
        compose.setContent {
            MeteoDenikTheme { SeznamMestObsah(MestaUiState(), "", {}, {}, {}) }
        }
        compose.onNodeWithText(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.mesta_prazdno)
        ).assertIsDisplayed()
    }
}
