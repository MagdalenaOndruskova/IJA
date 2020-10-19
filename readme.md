# IJA projekt
## MHD simulátor 
**Autor**: 
- Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>

**Programovací jazyk**: Java

**Hodnotenie:** 80/80b

## Implementačné detaily: 
Cielom projektu bolo implementovať MHD simulátor, ktorý po načítaní údajov zo súboru začne pohybovať vozidlá po mape. Vozidlá ktoré patria k tej istej linke majú tú istú farbu. Po kliknutí na vozidlo sa vyfarbí ich trasa na mape a v ľavom bočnom paneli sa ukáže jeho aktuálna trasa s názvom zastávok a časom odchodu z jednotlivých zástavok. Vozidlá sa postupne v čase objavujú na mape a miznú keď prídu na koniec trasy. 

**Skok v čase:** Skok v čase je implementovaný vo viacerých verziách: 
- Skok v čase na konci dňa: keď na hodinách tikne 23:00, automaticky sa simulácia "reštartuje" a skočí sa v čase znovu na začiatok: 08:00
- Po stlačení tlačítka Reset Time: Skočí sa na čas 08:00, čo je východzí čas pre simuláciu 
- Po stlačení tlačítka Jump in Time: Skočí sa na čas, ktorý je zadaný vyššie v time Edite. 

**Traffic:** Zvýšená dopravná situácia je implementovaná pomocou zachytenia kliknutím myši pravým tlačítkom -- jedno kliknutie zvýši traffic o jeden bod a prejazd vozidiel sa spomalí o 0.5bodu ich rýchlosti. 

**Obchádzky:** Obchádzky neboli implementované. Implementované bolo iba uzavretie ulíc ktoré nemá žiadny efekt, okrem sfarbenia ulice na čierno.

### Tlačítka: 
- Jump in Time - po stlačení sa zoberie čas v timeEdit (nad tlačítkom) a skočí sa v čase na daný čas 
- Faster / Slower - po zadaní čísla do text editu vedľa sa toľkokrát zrýchly/spomalý čas
- Reset Time - po stlačení sa simulácia vráti do času 08:00 (východzí čas)
- Reset Lines - po stlačení sa odstránia všetky zvýraznené linky a aj výpis linky v bočnom paneli 
- Reset Traffic - po stlačení sa všetká traffic vráti do pôvodného stavu 
- Reset Detours - po stlačení sa všetky zatvorené ulice otvoria 

## Príklad spustenia: 
- ./get-libs.sh - skript z priečinka lib na stiahnutie všetkých potrebných knižníc - nutno spraviť ešte pred spustením antu
- ant compile - pre skompilovanie programu 
- ant run - pre spustenie programu

## Odovzdané súbory:
- ./src - priečinok so zdrojovými súbormi
- ./doc - priečinok ktorý obsahuje vygenerovanú Javadoc dokumentáciu
- ./build - priečinok kam sa vygenerujú všetky buildovacie súbory 
- ./dest - priečinok kam sa vygeneruje spustiteľný jar 
- ./examples - priečinok ktorý obsahuje podklad mapy v YAML súbore 
- ./lib - priečinok so skriptom, ktorý stiahne knižnice do tohto súboru
- build.xml - súbor pre spustenie ant 
- readme.md - základný popis projektu
