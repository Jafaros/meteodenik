package cz.utb.meteodenik.model

data class Mesto(
    val id: Int,
    val nazev: String,
    val teplotaC: Double? // null = teplotu zatím neznáme, doplní ji API v lekci 11.
)
