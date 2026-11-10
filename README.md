# MeteoDenik

Semestrální Android aplikace Petra Grajciara pro zapisování počasí.
Seznam v Compose umožňuje přidávat a mazat města, odmítá duplicitní názvy bez ohledu
na velikost písmen a mezery okolo a ukazuje počet měst. Tlačítko Autor otevře
vizitku z cvičení 7.1. Texty jsou česky a anglicky, barvy vycházejí z tématu.

Od lekce 08 drží stav `MestaViewModel` v neměnném `MestaUiState` a vystavuje jej
jen jako `StateFlow`. Obrazovka jej odebírá přes `collectAsStateWithLifecycle()`;
obsah seznamu, karta a formulář jsou bezstavové a mají náhledy.
Rozepsaný název zůstává v UI v `rememberSaveable`, výběr města v `SavedStateHandle`.
Seznam i detail sdílejí dočasný zdroj `MestaZdroj` v paměti procesu.

Jediná aktivita hostí typované destinace `Seznam` a `Detail(mestoId: Int)`.
Argument detailu čte jeho ViewModel ze `SavedStateHandle`; obrazovky dostávají
navigační lambdy. Vybraná karta má rámeček. Seznam přežije rotaci, po smrti
procesu se vrátí ukázková data. Výběr i rozepsaný text lze obnovit i po smrti
procesu při návratu do původní úlohy. Trvalé ukládání zatím není implementováno.

Ověření (příkaz na jednom řádku):

```powershell
./gradlew.bat :app:assembleDebug :app:lintDebug :app:testDebugUnitTest :app:connectedDebugAndroidTest
```

Ověřováno na dostupném emulátoru Medium_Phone s API 37 (Android 17);
emulátor s API 36 uvedený v lekci zde není nainstalovaný.

Protokol rotace, smrti procesu a kontrolního pokusu je v
[docs/lekce-08-stav.md](docs/lekce-08-stav.md). Prošla kontrola sestavení, lint
bez chyb, jednotkový test a všech 11 instrumentovaných testů. Detail zobrazuje
lokalizovanou teplotu i stav neexistujícího města; náhledy pokrývají také tmavý
režim a zvětšené písmo.
