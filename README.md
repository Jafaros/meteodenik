# MeteoDenik

Semestrální Android aplikace Petra Grajciara pro zapisování počasí.
Obrazovka v Compose zobrazuje ukázková města, umožňuje přidávat další a mazat je.
Odmítá duplicitní názvy bez ohledu na velikost písmen a mezery okolo a ukazuje
počet měst. Tlačítko Autor otevře vizitku (cvičení 7.1).
Stav drží pouze `MestaObrazovka`, její obsah i jednotlivé komponenty jsou
bezstavové a mají náhledy. Texty jsou česky a anglicky, barvy vycházejí z tématu.
Rozepsaný název přežije otočení zařízení, seznam se zatím obnoví na ukázková data
(v lekci 08 jeho stav převezme ViewModel). Trvalé ukládání zatím není implementováno.

Ověření lekce 07: `gradlew.bat :app:assembleDebug :app:lintDebug
:app:testDebugUnitTest :app:connectedDebugAndroidTest` (příkaz na jednom řádku).
UI testy kontrolují prázdný vstup, duplicity, přidání pěti měst, mazání,
prázdný seznam, obnovu rozepsaného textu a otevření vizitky.
Ověřeno na dostupném emulátoru Medium_Phone s API 37 (Android 17);
emulátor s API 36 uvedený v lekci zde není nainstalovaný.
AndroidX Test je aktualizovaný, protože původní Espresso 3.5.1 na API 37
selhávalo při inicializaci vstupního systému před spuštěním UI testů.
