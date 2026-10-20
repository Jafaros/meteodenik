package cz.utb.meteodenik.model

data class Mesto(
    val nazev: String,
    val teplota: Double
)

// Názvy měst jsou data; v lekci 11 je nahradí odpověď Open-Meteo API.
val UKAZKOVA_MESTA = listOf(
    Mesto("Zlín", 21.4),
    Mesto("Brno", 23.1),
    Mesto("Ostrava", 19.8)
)
