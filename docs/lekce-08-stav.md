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

## Rozdělení odpovědnosti

`MestaViewModel` drží neměnný snímek `MestaUiState`, mění jej přes `copy()`
a veřejně vystavuje pouze `StateFlow` přes `asStateFlow()`. Operace přidání,
mazání a validace jsou mimo composable funkce. `SeznamMestObrazovka` odebírá
stav přes `collectAsStateWithLifecycle()` a předává data a události bezstavovému
obsahu. Rozepsaný název je malý lokální stav vstupního pole.

Navigace předává pouze `Int` id města. `DetailViewModel` jej čte přes
`SavedStateHandle.toRoute<Detail>()`. NavController existuje pouze v NavHostu;
jednotlivé obrazovky dostávají navigační lambdy.
