# Cvičení 6.2 — Žádost o polohu se správným UX

## Čtyři stavy

| Stav | Provedený krok | Pozorovaný výsledek | Screenshot |
| --- | --- | --- | --- |
| `NEZNAMY` | Reset oprávnění a příznaků, nové spuštění a stisk Zjistit polohu. | První systémový dialog s přibližnou polohou. | [První žádost](lekce-06-opravneni-neznamy.png) |
| `ODMITNUTO` | V dialogu Don't allow a poté znovu Zjistit polohu. | Vlastní vysvětlení přínosu polohy a tlačítko Pokračovat; systémový dialog se ukáže až po potvrzení. | [Vysvětlení](lekce-06-opravneni-odmitnuto.png) |
| `ODMITNUTO_NATRVALO` | Pokračovat a podruhé Don't allow. | Text o fungování bez polohy a jediné tlačítko Otevřít nastavení. Žádná další žádost o oprávnění. | [Trvalé zamítnutí](lekce-06-opravneni-natrvalo.png) |
| `UDELENO` | Otevřít nastavení → Permissions → Location → Allow only while using the app, návrat Zpět. | Karta se sama přepne při `ON_RESUME`. Po simulaci síťové polohy zobrazuje zeměpisnou šířku a délku. | [Povolená poloha](lekce-06-opravneni-udeleno.png) |

## Proč trvalé zamítnutí vyhodnocujeme v callbacku

`shouldShowRequestPermissionRationale()` vrací `false` před první žádostí i po
trvalém zamítnutí. Při startu proto tyto situace nerozlišíme. Až po zamítnutí
v callbacku rozhodujeme podle této hodnoty, zda příště nabídnout vysvětlení
před další žádostí, nebo cestu do Nastavení.

## Proč ACCESS_COARSE_LOCATION místo ACCESS_FINE_LOCATION

Pro nabídku počasí v okolním městě stačí přibližná poloha; přesnou adresu aplikace
nepotřebuje. Podle [dokumentace Androidu k přesnosti polohy](https://developer.android.com/develop/sensors-and-location/location/permissions#accuracy)
poskytuje `ACCESS_COARSE_LOCATION` odhad v oblasti přibližně 3 km²,
zatímco `ACCESS_FINE_LOCATION` umožňuje přesnost obvykle do 50 metrů,
někdy i několika metrů. Takovou přesnost pro výběr města nepotřebujeme.
