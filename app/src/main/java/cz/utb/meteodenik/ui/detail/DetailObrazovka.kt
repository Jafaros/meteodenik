package cz.utb.meteodenik.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.utb.meteodenik.R
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

@Composable
fun DetailObrazovka(
    onZpet: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel()
) {
    val stav by viewModel.uiState.collectAsStateWithLifecycle()
    DetailObsah(stav, onZpet, modifier)
}

@Composable
fun DetailObsah(stav: DetailUiState, onZpet: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { insety ->
        Column(Modifier.padding(insety).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text(
                stav.mesto?.nazev ?: stringResource(R.string.mesto_nezname),
                style = MaterialTheme.typography.headlineMedium
            )
            stav.mesto?.let { mesto ->
                Text(
                    text = mesto.teplotaC?.let { stringResource(R.string.teplota_format, it) }
                        ?: stringResource(R.string.mesto_teplota_neznama),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Button(onClick = onZpet, modifier = Modifier.padding(top = 24.dp)) {
                Text(stringResource(R.string.zpet))
            }
        }
    }
}

@Preview(showBackground = true, locale = "cs")
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailObsahPreview() {
    MeteoDenikTheme { DetailObsah(DetailUiState(Mesto(2, "Brno", 21.0)), {}) }
}

@Preview(showBackground = true, locale = "cs", fontScale = 1.5f, widthDp = 320)
@Composable
private fun DetailBezTeplotyPreview() {
    MeteoDenikTheme { DetailObsah(DetailUiState(Mesto(3, "Uherské Hradiště", null)), {}) }
}

@Preview(showBackground = true, locale = "cs")
@Composable
private fun DetailNenalezenPreview() {
    MeteoDenikTheme { DetailObsah(DetailUiState(), {}) }
}
