package pl.pepco.testdata;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class CollectionData {
    private CollectionData() {
    }


    public static Stream<Arguments> returnPolicyCollections() {
        return Stream.of(
                Arguments.of("zwroty_oswietlenie", "/collections/dom/oswietlenie", "/centrum-pomocy/zwroty"),
                Arguments.of("zwroty_chlopiec", "/collections/dziecko/chlopiec-92-134", "pepco-privacy.my.onetrust.com/policies")
        );
    }
}
