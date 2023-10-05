package nl.fd.data.entity;

import androidx.annotation.NonNull;

import org.parceler.Parcel;

import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.fd.data.entity.card.CardType;

@Parcel(Parcel.Serialization.BEAN)
@RequiredArgsConstructor
@Getter
public enum ArticleCategory {

    UNKNOWN_CATEGORY,
    BREAKING_NEWS(CardType.BREAKING),
    SPECIAL(CardType.SECTION_TEASER),
    FAVORITES(CardType.SECTION_TEASER);

    @CardType
    private final int teaserType;

    ArticleCategory() {
        this(CardType.TEASER);
    }

    public static @NonNull
    ArticleCategory of(@NonNull String category) {
        return Stream.of(ArticleCategory.values())
                .filter(articleCategory -> articleCategory.name().equals(category))
                .findFirst()
                .orElse(UNKNOWN_CATEGORY);
    }
}
