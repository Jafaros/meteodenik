# Lekce 08 – ověření stavu

Ověřeno 10. 11. 2026 na Medium_Phone, Android 17 / API 37.

## Smrt procesu (náplň hodiny, blok G)

1. Přidána Praha, v poli ponecháno `Uherske Hradiste` (bez diakritiky kvůli
   `adb input text`), otevřen detail Brna a návrat systémovým Zpět.
2. Před testem: 4 města, Brno označené, rozepsaný text přítomný; PID `6898`.
3. Použité příkazy (`adb` je `Android/Sdk/platform-tools/adb.exe`):

   ```text
   adb shell pidof cz.utb.meteodenik
   adb shell input keyevent 3
   adb shell am kill cz.utb.meteodenik
   adb shell pidof cz.utb.meteodenik
   adb shell input keyevent 187
   ```

4. Po `am kill` druhé `pidof` nic nevrátilo. V přehledu byla otevřena původní
   úloha MeteoDeníku; po obnově byl PID `7177`.
5. Výsledek: text `Uherske Hradiste` zůstal, Brno bylo stále označené (checked
   v přístupnostním stromu), seznam obsahoval původní 3 města. Praha zmizela.

`rememberSaveable` obnoví lokální text, `SavedStateHandle` obnoví id výběru.
`MestaZdroj` žije pouze v paměti procesu, takže přidaná města smrt procesu
nepřežijí. To odpovídá této lekci; trvalé úložiště zatím není součástí aplikace.

## Kontrolní pokus (blok H)

Dočasně byl pouze rozepsaný název změněn na `remember`. Po výběru Brna a návratu
na seznam byl do pole zapsán text `Kontrola`. PID před ukončením byl `9015`.
Následovalo Home → `am kill` → prázdný výstup `pidof` → návrat z přehledu.
Nový PID byl `9232`, pole bylo prázdné a Brno zůstalo označené.
Tím se odlišilo ukládání výběru od lokálního stavu pole. Poté byl kód vrácen
na `rememberSaveable` a znovu sestaven; kontrolní verze se necommituje.

## Rozdělení odpovědnosti

Cvičení 8.1: instrumentovaný test přidá Kroměříž (`Kromeriz`), ponechá text
`Rozepsano`, vyžádá orientaci na šířku a počká na novou instanci aktivity.
Ověří zachování textu i 4 měst, odscrolluje na přidanou kartu a zkontroluje,
že Logcat s tagem `Stav` neobsahuje `onCleared`. Test prošel na API 37.
Samostatný test vykreslí prázdný obsah bez ViewModelu a potvrdí prázdný stav.
Celkem po cvičení 8.1 prošlo 6 instrumentovaných testů.

`MestaViewModel` drží neměnný snímek `MestaUiState`, mění jej přes `copy()`
a veřejně vystavuje pouze `StateFlow` přes `asStateFlow()`. Operace přidání,
mazání a validace jsou mimo composable funkce. `SeznamMestObrazovka` odebírá
stav přes `collectAsStateWithLifecycle()` a předává data a události bezstavovému
obsahu. Rozepsaný název je malý lokální stav vstupního pole.

Navigace předává pouze `Int` id města. `DetailViewModel` jej čte přes
`SavedStateHandle.toRoute<Detail>()`. NavController existuje pouze v NavHostu;
jednotlivé obrazovky dostávají navigační lambdy.

## Cvičení 8.2 – navigace a detail

Detail má bezstavový obsah a náhledy pro světlý/tmavý režim, chybějící teplotu,
velké písmo a neexistující město. Teplota používá lokalizovaný `teplota_format`.
V manifestu je právě jedna aktivita a NavController je pouze v navigačním grafu.

Instrumentované testy ověřily:

- Otevření Brna s teplotou 21,0 °C (formát podle jazyka emulátoru), skutečnou
  rotaci na detailu a návrat tlačítkem Zpět se zachováním rozepsaného textu a výběru.
- Dvojklik na Brno a jediný návrat systémovým Zpět na seznam.
- Detail nově přidané Jihlavy ze stejného zdroje dat, s neznámou teplotou.
- Čtení id pomocí `SavedStateHandle.toRoute<Detail>()`, neexistující id a funkční
  tlačítko Zpět i při nenalezeném městě.

Finální kontrola: sestavení APK, jednotkový test, 11 instrumentovaných testů
a lint (0 chyb). Varování lintu zůstávají především u starších závislostí,
nepoužitých zdrojů šablony a původního targetSdk. Testováno na API 37, ne API 36.
