# TAS5 - testy automatyczne Selenium

Projekt zawiera testy automatyczne strony Pepco wykonane w Javie z uzyciem Selenium WebDriver, JUnit 5 i Maven.
Testy zostaly przygotowane w oparciu o Page Object Model, PageFactory, jawne oczekiwania `WebDriverWait` oraz dane testowe z `MethodSource`, pliku CSV i biblioteki DataFaker.

## Wymagania

- Java 17 lub nowsza
- Maven
- Google Chrome

## Uruchamianie testow

Wszystkie testy:

```powershell
./mvnw test
```

Pojedyncza klasa testowa:

```powershell
./mvnw -Dtest=CollectionTest test
```

W systemie Windows mozna tez uzyc:

```powershell
.\mvnw.cmd test
```

Screenshoty z testow sa zapisywane w katalogu `screenshots/`.

## Zakres testow

Projekt zawiera nastepujace klasy testowe:

- `CollectionTest` - przejscie z kolekcji produktow do produktu oraz do informacji o zwrotach.
- `DmpEventsTest` - diagnostyka requestow Mediarithmics po zaakceptowaniu cookies. Test ma charakter diagnostyczny: wypisuje informacje o requestach i pikselach, ale brak zewnetrznego requestu DMP w pojedynczym przebiegu nie przerywa calej suity.
- `CurrentLeafletPinnedProductTest` - sciezka z aktualnej oferty gazetkowej przez pin produktu do strony produktu.
- `NewsletterFakerTest` - wpisanie do formularza newslettera adresu e-mail wygenerowanego przez DataFaker oraz weryfikacja wartosci pola, bez wysylania formularza.
- `PepcoClubTest` - sprawdzenie linkow do aplikacji Pepco w App Store i Google Play.
- `RandomCityFromFakerTest` - wyszukiwanie sklepu dla losowego miasta z DataFaker.
- `StoreLocatorCsvTest` - wyszukiwanie sklepow dla miast pobieranych z pliku CSV.
- `TrendyTest` - sprawdzenie strony oferty gazetkowej i przejscia do produktu.

## Przykladowe komendy

```powershell
./mvnw -Dtest=CollectionTest test
./mvnw -Dtest=DmpEventsTest test
./mvnw -Dtest=CurrentLeafletPinnedProductTest test
./mvnw -Dtest=NewsletterFakerTest test
./mvnw -Dtest=PepcoClubTest test
./mvnw -Dtest=RandomCityFromFakerTest test
./mvnw -Dtest=StoreLocatorCsvTest test
./mvnw -Dtest=TrendyTest test
```

## Struktura projektu

```text
src/test/java/pl/pepco/pages      - klasy Page Object
src/test/java/pl/pepco/testdata   - dane testowe dla testow parametryzowanych
src/test/java/pl/pepco/tests      - klasy testowe JUnit 5
src/test/resources/test_data      - pliki danych, np. CSV
screenshots                       - screenshoty tworzone podczas testow
```

## Najwazniejsze techniki

- Selenium WebDriver
- JUnit 5
- Maven
- Page Object Model
- PageFactory
- WebDriverWait
- testy parametryzowane przez `MethodSource`
- testy data driven z pliku CSV
- DataFaker
- screenshoty po waznych akcjach
- logi performance Chrome do diagnostyki requestow Mediarithmics
