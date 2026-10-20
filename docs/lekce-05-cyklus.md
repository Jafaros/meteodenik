# Lekce 05 – tabulka životního cyklu

| Scénář | Můj předpoklad | Skutečnost z Logcatu | Proč se to liší |
|---|---|---|---|
| 1. Start aplikace z ikony | onCreate (savedInstanceState = null) → onStart → onResume | onCreate (savedInstanceState = null) → onStart → onResume | Shoda. |
| 2. Rotace displeje | onPause → onStop → onDestroy (isFinishing = false) → onCreate (savedInstanceState != null) → onStart → onResume | onPause → onStop → onDestroy (isFinishing = false) → onCreate (savedInstanceState != null) → onStart → onResume | Shoda. Změna konfigurace zruší instanci a vytvoří novou ve stejném procesu; uložený Bundle není null. |
| 3. Stisk Home | onPause → onStop | onPause → onStop | Shoda. Aktivita přestane být viditelná, ale není dokončena; její instance zůstane v paměti. |
| 4. Návrat do aplikace | onRestart → onStart → onResume | onRestart → onStart → onResume | Shoda. Návrat přes přehled spuštěných aplikací obnoví existující zastavenou instanci; onRestart sám data neobnovuje. |
| 5. Stisk Zpět na MainActivity | onPause → onStop, bez onDestroy | onPause → onStop, bez onDestroy | Shoda. Po skutečném spuštění z ikony launcheru systém přesune task s kořenovou aktivitou na pozadí. |
| 6. adb shell am kill na pozadí + návrat | Home: onPause → onStop; zabití: bez callbacků; návrat: onCreate (savedInstanceState != null) → onStart → onResume | Home: onPause → onStop; zabití: žádný callback; návrat přes přehled: onCreate (savedInstanceState != null) → onStart → onResume | Shoda. Po zabití proces zanikl bez onDestroy; návrat vytvořil nový proces a aktivitu s uloženým stavem. isFinishing se při zabití vůbec nezalogovalo. |
