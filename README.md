* [Główne informacje](#Główne informacje)
* [Spodziewany wyniki](#spodziewany-wynik)
* [Główne klasy i metody](#główne-klasy-i-metody)
* [Docker](#docker)

## Główne informacje
Głównym celem tej bibilioteki jest podział przedmitów znajdujących się w koszyku klienta
na grupy dostaw.
Bibliotek korzysta z wcześniej zdefiniowanego API do realizacji tego zadania.
Kluczowym elementem działania biblioteki jest wczytanie pliku konfiguracyjnego,
który zawiera możliwe sposoby dostaw wszystkich oferowanych w sklepie produktów.

## Spodziewany wynik
Algorytm w wyniku ma zwrócić podział produktów na grupy dostawy jako mapę. Kluczem w mapie będzie sposób dostawy, a wartością lista produktów. Oczekujemy, że:
<ul>
<li>Algorytm dzieli produkty na możliwie minimalną liczbę grup dostaw</li>
<li>Największa grupa zawiera możliwie najwięcej produktów</li>
</ul>

## Główne klasy i metody

### BasketSplitter

Główna klasa biblioteki, odpowiedzialna za podział przedmiotów w koszyku na grupy dostaw

#### `split`

- Metoda wykonująca podział produktów na grupy dostaw na podstawie przekazanej listy przedmiotów
- Argumenty:
    - `items`: Lista przedmiotów w koszyku

#### `fetchDataFromFile`

- Metoda do wczytywania danych konfiguracyjnych z pliku
- Argumenty:
    - `filePath`: Ścieżka do pliku z danymi konfiguracyjnymi

#### `findMatchingProviders`

- Metoda do znalezienia dostawców odpowiadających produktom
- Argumenty:
    - `items`: Lista przedmiotów w koszyku
    - `itemProviders`: Mapa dostawców jako klucz i przedmitów(które mogą dostarczyć) jako wartość

#### `findBestProvider`

- Znajduje najlepszego dostawcę dla zestawu produktów
- Argumenty:
    - `products`: Zbiór produktów
    - `providersList`: Lista dostawców

### ProviderSolver

Zawiera logikę wyboru najlepszego dostawcy


#### Metoda `findMostCoveringProvider`

- Znajduje dostawcę, który jest w stanie przewieźć najwięcej elementów

#### Metoda `providersIntersection`

- Zwraca pookrywające się produkty w koszyku z produktami dostawcy
    - `items`: Lista produktów w koszyku
    - `products`: Produkty które może przewieźć dostawca

#### Metoda `foundMoreCoveredElements`

-  Sprawdza, czy kolejny dostawca jest w stanie przewieźć więcej przedmiotów od obecnie najlepszego
- Argumenty:
    - `intersection`: Zbiór pokrywających się elementów dla obecnego dostawcy
    - `coveredElements`: Zbiór już pokrytych elementów
- Zwracana wartość: Wartość logiczna określająca, czy nowy dostawca może przewieźć więcej przedmiotów

### Provider

- Reprezentuje dostawcę w systemie

  ##### Pola:
    - `name`: Nazwa dostawcy
    - `products`: Zbiór produktów dostarczanych przez dostawcę

## Docker

Projekt zawiera plik Dockerfile, który umożliwia uruchomienie aplikacji w kontenerze Docker
