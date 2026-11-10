package cz.utb.meteodenik.ui.mesta

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.utb.meteodenik.data.MestaZdroj
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val KLIC_VYBRANE_MESTO = "vybraneMestoId"

class MestaViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState = MutableStateFlow(
        MestaUiState(MestaZdroj.mesta.value, savedStateHandle[KLIC_VYBRANE_MESTO])
    )
    val uiState: StateFlow<MestaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                MestaZdroj.mesta,
                savedStateHandle.getStateFlow<Int?>(KLIC_VYBRANE_MESTO, null)
            ) { mesta, vybraneId -> mesta to vybraneId }.collect { (mesta, vybraneId) ->
                _uiState.update { it.copy(mesta = mesta, vybraneMestoId = vybraneId) }
            }
        }
    }

    fun vyberMesto(id: Int) {
        savedStateHandle[KLIC_VYBRANE_MESTO] = id
    }

    fun pridejMesto(nazev: String): Boolean {
        if (nazev.isBlank()) return false
        val pridano = MestaZdroj.pridej(nazev)
        _uiState.update { it.copy(jeDuplikat = !pridano) }
        return pridano
    }

    fun zrusChybu() {
        _uiState.update { it.copy(jeDuplikat = false) }
    }

    fun smazMesto(id: Int) {
        MestaZdroj.smaz(id)
        if (savedStateHandle.get<Int?>(KLIC_VYBRANE_MESTO) == id) {
            savedStateHandle[KLIC_VYBRANE_MESTO] = null
        }
    }
}
