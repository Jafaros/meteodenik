package cz.utb.meteodenik.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import cz.utb.meteodenik.data.MestaZdroj
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.navigace.Detail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DetailUiState(val mesto: Mesto? = null)

class DetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val argumenty = savedStateHandle.toRoute<Detail>()
    private val _uiState = MutableStateFlow(
        DetailUiState(mesto = MestaZdroj.podleId(argumenty.mestoId))
    )
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
}
