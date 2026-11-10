package cz.utb.meteodenik.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.utb.meteodenik.R

@Composable
fun DetailObrazovka(
    onZpet: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel()
) {
    val stav by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(modifier = modifier) { insety ->
        Column(Modifier.padding(insety).padding(16.dp)) {
            Text(
                stav.mesto?.nazev ?: stringResource(R.string.mesto_nezname),
                style = MaterialTheme.typography.headlineMedium
            )
            Button(onClick = onZpet, modifier = Modifier.padding(top = 24.dp)) {
                Text(stringResource(R.string.zpet))
            }
        }
    }
}
