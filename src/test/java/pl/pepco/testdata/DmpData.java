package pl.pepco.testdata;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class DmpData {
    private DmpData() {
    }

    public static Stream<Arguments> pages() {
        return Stream.of(
                Arguments.of("home_page", "/"),
                Arguments.of("collection_page", "/collections/dom/meble/kosze-i-pudelka")
        );
    }
}
