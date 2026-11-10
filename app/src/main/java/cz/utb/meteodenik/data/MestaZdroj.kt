package cz.utb.meteodenik.data

import cz.utb.meteodenik.model.Mesto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Společný zdroj seznamu i detailu v paměti procesu; trvalé úložiště přijde později.
object MestaZdroj {
    val ukazkovaMesta = listOf(
        Mesto(1, "Zlín", 18.5),
        Mesto(2, "Brno", 21.0),
        Mesto(3, "Uherské Hradiště", null)
    )
    private val _mesta = MutableStateFlow(ukazkovaMesta)
    val mesta: StateFlow<List<Mesto>> = _mesta.asStateFlow()

    fun pridej(nazev: String): Boolean {
        val upravenyNazev = nazev.trim()
        if (upravenyNazev.isEmpty()) return false
        // UI volá metodu z hlavního vlákna; ověření a zápis jsou jedna operace.
        synchronized(this) {
            if (_mesta.value.any { it.nazev.equals(upravenyNazev, ignoreCase = true) }) return false
            _mesta.update { seznam ->
                seznam + Mesto((seznam.maxOfOrNull { it.id } ?: 0) + 1, upravenyNazev, null)
            }
        }
        return true
    }

    fun smaz(id: Int) {
        _mesta.update { seznam -> seznam.filterNot { it.id == id } }
    }

    fun podleId(id: Int): Mesto? = _mesta.value.firstOrNull { it.id == id }
}
