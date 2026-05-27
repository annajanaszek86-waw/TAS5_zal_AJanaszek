package pl.pepco.testdata;

import org.junit.jupiter.params.provider.Arguments;
import pl.pepco.pages.PepcoClubPage;

import java.util.stream.Stream;

public final class PepcoClubData {
    private PepcoClubData() {
    }

    public static Stream<Arguments> appStoreLinks() {
        return Stream.of(
                Arguments.of(
                        "app_store",
                        PepcoClubPage.APP_STORE,
                        "apps.apple.com/pl/app/pepco-deals-and-vouchers/id6757158188",
                        "pepco_club_app_store_page"
                ),
                Arguments.of(
                        "google_play",
                        PepcoClubPage.GOOGLE_PLAY,
                        "play.google.com/store/apps/details?id=eu.pepco.app",
                        "pepco_club_google_play_page"
                )
        );
    }
}
