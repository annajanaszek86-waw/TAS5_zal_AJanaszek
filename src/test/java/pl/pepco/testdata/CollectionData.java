package pl.pepco.testdata;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class CollectionData {
    private CollectionData() {
    }

    public static Stream<Arguments> collections() {
        return Stream.of(
                Arguments.of("kolekcja_dom", "/collections/dom", "Dom"),
                Arguments.of("kolekcja_oswietlenie", "/collections/dom/oswietlenie", "Oswietlenie"),
                Arguments.of("kolekcja_dziecko", "/collections/dziecko", "Dziecko"),
                Arguments.of("kolekcja_niemowle_chl", "/collections/niemowle/chlopiec-56-98", "chlopiec"),
                Arguments.of("kolekcja_dziewczynka", "/collections/dziecko/dziewczynka-134-176", "dziewczeta")
        );
    }
}
