package cz.utb.meteodenik.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cz.utb.meteodenik.R
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

internal val ukazkovaMesta = listOf(
    Mesto(1, "Zlín", 18.5),
    Mesto(2, "Brno", 21.0),
    Mesto(3, "Uherské Hradiště", null)
)

@Composable
fun MestaObrazovka(modifier: Modifier = Modifier) {
    // Jediné místo se stavem. Seznam zatím nepřežije rotaci; v lekci 08 jej převezme ViewModel.
    var novyNazev by rememberSaveable { mutableStateOf("") }
    var mesta by remember { mutableStateOf(ukazkovaMesta) }
    var jeDuplikat by rememberSaveable { mutableStateOf(false) }
    var zobrazitVizitku by rememberSaveable { mutableStateOf(false) }

    MestaObsah(
        mesta = mesta,
        novyNazev = novyNazev,
        onNovyNazevChange = {
            novyNazev = it
            jeDuplikat = false
        },
        onPridatMesto = {
            val nazev = novyNazev.trim()
            if (mesta.any { it.nazev.equals(nazev, ignoreCase = true) }) {
                jeDuplikat = true
            } else if (nazev.isNotEmpty()) {
                mesta = mesta + Mesto((mesta.maxOfOrNull { it.id } ?: 0) + 1, nazev, null)
                novyNazev = ""
                jeDuplikat = false
            }
        },
        modifier = modifier,
        onZobrazitVizitku = { zobrazitVizitku = true },
        onSmazatMesto = { id -> mesta = mesta.filterNot { it.id == id } },
        jeChyba = jeDuplikat
    )
    if (zobrazitVizitku) {
        Dialog(
            onDismissRequest = { zobrazitVizitku = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            VizitkaKarta(
                jmeno = stringResource(R.string.vizitka_jmeno),
                obor = stringResource(R.string.vizitka_obor),
                email = stringResource(R.string.vizitka_email),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MestaObsah(
    mesta: List<Mesto>,
    novyNazev: String,
    onNovyNazevChange: (String) -> Unit,
    onPridatMesto: () -> Unit,
    onSmazatMesto: (Int) -> Unit,
    modifier: Modifier = Modifier,
    jeChyba: Boolean = false,
    onZobrazitVizitku: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.mesta_titulek)) },
                actions = {
                    TextButton(onClick = onZobrazitVizitku) {
                        Text(stringResource(R.string.vizitka_otevrit))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            Modifier.fillMaxSize().padding(innerPadding)
                .consumeWindowInsets(innerPadding).imePadding().padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            PridatMestoRadek(novyNazev, onNovyNazevChange, onPridatMesto, jeChyba = jeChyba)
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text(
                text = pluralStringResource(R.plurals.mesta_pocet, mesta.size, mesta.size),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            MestaSeznam(mesta, onSmazatMesto, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun PridatMestoRadek(
    nazev: String,
    onNazevChange: (String) -> Unit,
    onPridat: () -> Unit,
    modifier: Modifier = Modifier,
    jeChyba: Boolean = false
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = nazev,
            onValueChange = onNazevChange,
            label = { Text(stringResource(R.string.mesto_pole_label)) },
            singleLine = true,
            isError = jeChyba,
            supportingText = if (jeChyba) {
                { Text(stringResource(R.string.mesto_duplikat)) }
            } else null,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(12.dp))
        Button(onClick = onPridat, enabled = nazev.isNotBlank()) {
            Text(stringResource(R.string.mesto_pridat))
        }
    }
}

@Composable
fun MestaSeznam(
    mesta: List<Mesto>,
    onSmazatMesto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (mesta.isEmpty()) {
        Text(
            text = stringResource(R.string.mesta_prazdno),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(vertical = 32.dp)
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mesta, key = { it.id }) { mesto ->
                MestoKarta(mesto, onSmazat = { onSmazatMesto(mesto.id) })
            }
        }
    }
}

@Composable
fun MestoKarta(mesto: Mesto, onSmazat: () -> Unit, modifier: Modifier = Modifier) {
    val avatarPopis = stringResource(R.string.mesto_avatar_popis, mesto.nazev)
    val smazatPopis = stringResource(R.string.mesto_smazat_popis, mesto.nazev)
    Card(modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clearAndSetSemantics { contentDescription = avatarPopis },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    mesto.nazev.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    mesto.nazev,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = mesto.teplotaC?.let { stringResource(R.string.mesto_teplota, it) }
                        ?: stringResource(R.string.mesto_teplota_neznama),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(
                onClick = onSmazat,
                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .semantics { contentDescription = smazatPopis }
            ) {
                Text(stringResource(R.string.mesto_smazat))
            }
        }
    }
}

@Preview(showBackground = true, locale = "cs")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "cs")
@Composable
private fun MestoKartaPreview() {
    MeteoDenikTheme { MestoKarta(ukazkovaMesta.first(), {}, Modifier.padding(16.dp)) }
}

@Preview(showBackground = true, locale = "cs")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "en")
@Composable
private fun MestaObsahPreview() {
    MeteoDenikTheme { MestaObsah(ukazkovaMesta, "Otrokovice", {}, {}, {}) }
}

@Preview(showBackground = true, locale = "cs")
@Composable
private fun MestaObsahPrazdnaPreview() {
    MeteoDenikTheme { MestaObsah(emptyList(), "", {}, {}, {}) }
}

@Preview(showBackground = true, locale = "cs")
@Composable
private fun PridatMestoRadekPreview() {
    MeteoDenikTheme { PridatMestoRadek("Otrokovice", {}, {}) }
}

@Preview(showBackground = true, locale = "cs")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "en")
@Composable
private fun MestaObsahChybaPreview() {
    MeteoDenikTheme { MestaObsah(ukazkovaMesta, " brno ", {}, {}, {}, jeChyba = true) }
}
