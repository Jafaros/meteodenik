package cz.utb.meteodenik

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.hasText
import android.content.pm.ActivityInfo
import android.os.ParcelFileDescriptor
import org.junit.Assert.assertFalse
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class MestaObrazovkaTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    private fun text(id: Int) = compose.activity.getString(id)
    private fun pocet(pocet: Int) = compose.activity.resources
        .getQuantityString(R.plurals.mesta_pocet, pocet, pocet)

    @Test
    fun pridaniValidaceAMazani() {
        val pole = compose.onNodeWithText(text(R.string.mesto_pole_label))
        val pridat = compose.onNodeWithText(text(R.string.mesto_pridat))
        pridat.assertIsNotEnabled()
        pole.performTextReplacement("   ")
        pridat.assertIsNotEnabled()

        // Duplicitní název musí být odmítnut i s mezerami a jinou velikostí písmen.
        pole.performTextReplacement(" bRnO ")
        pridat.performClick()
        compose.onNodeWithText(text(R.string.mesto_duplikat)).assertIsDisplayed()
        compose.onNodeWithText(pocet(3)).assertIsDisplayed()
        compose.onAllNodesWithText("Brno").assertCountEquals(1)

        val novaMesta = listOf("Otrokovice", "Praha", "Ostrava", "Olomouc", "Liberec")
        novaMesta.forEach { nazev ->
            pole.performTextReplacement(" $nazev ")
            compose.onNodeWithText(text(R.string.mesto_duplikat)).assertDoesNotExist()
            pridat.performClick()
            pridat.assertIsNotEnabled() // Úspěšné přidání zároveň vyprázdní pole.
        }
        compose.onNodeWithText(pocet(8)).assertIsDisplayed()

        fun smazat(nazev: String) {
            compose.onNodeWithContentDescription(
                compose.activity.getString(R.string.mesto_smazat_popis, nazev)
            ).performScrollTo().performClick()
        }
        smazat("Otrokovice")
        compose.onNodeWithText("Otrokovice").assertDoesNotExist()
        compose.onNodeWithText(pocet(7)).assertIsDisplayed()
        compose.onNodeWithText("Brno").assertIsDisplayed()
        novaMesta.drop(1).forEach(::smazat)
        compose.onNodeWithText(pocet(3)).assertIsDisplayed()
    }

    @Test
    fun rozepsanyNazevPrezijeObnoveniAktivity() {
        compose.onNodeWithText(text(R.string.mesto_pole_label)).performTextReplacement("Rozepsano")
        InstrumentationRegistry.getInstrumentation().runOnMainSync { compose.activity.recreate() }
        compose.waitForIdle()
        compose.onNodeWithText("Rozepsano").assertIsDisplayed()
    }

    @Test
    fun pridaneMestoATextPrezijiSkutecnouRotaciBezOnCleared() {
        compose.onNodeWithText(text(R.string.mesto_pole_label)).performTextReplacement("Kromeriz")
        compose.onNodeWithText(text(R.string.mesto_pridat)).performClick()
        compose.onNodeWithText(text(R.string.mesto_pole_label)).performTextReplacement("Rozepsano")
        val puvodniAktivita = compose.activity
        fun shell(prikaz: String): String = ParcelFileDescriptor.AutoCloseInputStream(
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(prikaz)
        ).bufferedReader().use { it.readText() }
        shell("logcat -c")
        compose.activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        compose.waitUntil(10_000) { compose.activity !== puvodniAktivita }
        compose.waitForIdle()
        compose.onNodeWithText("Rozepsano").assertIsDisplayed()
        compose.onNodeWithTag("mesta_seznam").performScrollToNode(hasText("Kromeriz"))
        compose.onNodeWithText("Kromeriz").assertIsDisplayed()
        compose.onNodeWithText(pocet(4)).assertIsDisplayed()
        assertFalse(shell("logcat -d -s Stav:D *:S").contains("onCleared"))
        compose.onNodeWithContentDescription(
            compose.activity.getString(R.string.mesto_smazat_popis, "Kromeriz")
        ).performClick()
        compose.activityRule.scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    @Test
    fun autorOtevreVizitku() {
        compose.onNodeWithText(text(R.string.vizitka_otevrit)).performClick()
        compose.onNodeWithText(text(R.string.vizitka_jmeno)).assertIsDisplayed()
        compose.onNodeWithContentDescription(
            compose.activity.getString(R.string.vizitka_email_popis, text(R.string.vizitka_email))
        ).assertIsDisplayed()
    }
}
