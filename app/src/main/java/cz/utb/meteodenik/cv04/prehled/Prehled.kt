package cz.utb.meteodenik.cv04.prehled

data class Mesto(val nazev: String, val teplota: Double)

fun Double.jakoCelsia(): String = "${this} C"

fun Mesto.popis(): String = "$nazev: ${teplota.jakoCelsia()}"

sealed interface StavPrehledu {
    data object Nacita : StavPrehledu
    data object Chyba : StavPrehledu
    data object Prazdno : StavPrehledu
    data object Hotovo : StavPrehledu
}

class Prehled(private val mesta: List<Mesto>) {
    private val stav: StavPrehledu = StavPrehledu.Hotovo;

    fun textStavu(): String = when (stav) {
        StavPrehledu.Nacita -> "Načítám…"
        StavPrehledu.Chyba -> "Chyba"
        StavPrehledu.Prazdno -> "Nic tu není"
        is StavPrehledu.Hotovo -> "Měst: ${mesta.size}" }

    fun nejteplejsi(): Mesto? = mesta.maxByOrNull { it.teplota }

    fun vypis() {
        if (mesta.isEmpty()) {
            return;
        }

        mesta.forEach { println(it.popis()) }
        println(textStavu())

        val nej = nejteplejsi()
        if (nej != null) {
            println("Nejtepleji: " + nej.nazev + " (" + nej.teplota + " C)")
        }
    }
}

fun main() {
    val p = Prehled( listOf( Mesto("Zlín", 21.4), Mesto("Brno", 23.0), Mesto("Otrokovice", 20.9)))
    p.vypis()
}