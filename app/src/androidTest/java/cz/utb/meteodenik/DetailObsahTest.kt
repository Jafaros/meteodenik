package cz.utb.meteodenik

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.test.platform.app.InstrumentationRegistry
import cz.utb.meteodenik.ui.detail.DetailObsah
import cz.utb.meteodenik.ui.detail.DetailViewModel
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DetailObsahTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun neexistujiciIdZobraziChybuAStaleUmozniZpet() {
        val viewModel = DetailViewModel(SavedStateHandle(mapOf("mestoId" to -1)))
        assertNull(viewModel.uiState.value.mesto)
        val stav = viewModel.uiState.value
        var zpet = false
        compose.setContent {
            MeteoDenikTheme { DetailObsah(stav, onZpet = { zpet = true }) }
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        compose.onNodeWithText(context.getString(R.string.mesto_nezname)).assertIsDisplayed()
        compose.onNodeWithText(context.getString(R.string.zpet)).performClick()
        assertTrue(zpet)
    }

    @Test
    fun viewModelCteTypovyArgumentZeSavedStateHandle() {
        val viewModel = DetailViewModel(SavedStateHandle(mapOf("mestoId" to 2)))
        assertEquals("Brno", viewModel.uiState.value.mesto?.nazev)
    }
}
