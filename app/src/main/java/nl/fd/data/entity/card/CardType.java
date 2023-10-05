package nl.fd.data.entity.card;

import androidx.annotation.IntDef;

@IntDef({
        CardType.TEASER,
        CardType.SHORT_ARTICLE,
        CardType.SECTION_TEASER,
        CardType.SECTION_HEADER,
        CardType.SECTION_OVERVIEW_TEASER,
        CardType.LATEST_NEWS_TEASER,
        CardType.EMPTY_TEASER,
        CardType.EMPTY_TEASER_HORIZONTAL,
        CardType.STOCK_TICKERS,
        CardType.WHITE_SPACE,
        CardType.AD_UNIT,
        CardType.TEASER_GROUP,
        CardType.SECTION_BLOCK,
        CardType.SECTION_BLOCK_NEWSLETTER,
        CardType.BREAKING,
        CardType.INFO_TEXT,
        CardType.OVERVIEW_MESSAGE,
        CardType.MY_NEWS_TEASER,
        CardType.MY_INTERESTS_EDITOR,
        CardType.PREDEFINED_INTERESTS,
        CardType.TAG_HEADER,
        CardType.AUTHOR_HEADER,
        CardType.PARTNERS_BLOCK,
        CardType.TOPIC_HEADER,
        CardType.LAST_SEEN_MARKER})
public @interface CardType {

    int TEASER = 0;
    int LATEST_NEWS_TEASER = 1;
    int SHORT_ARTICLE = 2;
    int SECTION_TEASER = 3;
    int SECTION_HEADER = 4;
    int SECTION_OVERVIEW_TEASER = 6;
    int TEASER_GROUP = 7;
    int SECTION_BLOCK = 8;
    int BREAKING = 9;
    int INFO_TEXT = 10;
    int MY_NEWS_TEASER = 11;
    int MY_INTERESTS_EDITOR = 12;
    int PREDEFINED_INTERESTS = 13;
    int TAG_HEADER = 14;
    int AUTHOR_HEADER = 15;
    int SECTION_BLOCK_NEWSLETTER = 16;
    int PARTNERS_BLOCK = 17;
    int TOPIC_HEADER = 18;
    int LAST_SEEN_MARKER = 19;

    int OVERVIEW_MESSAGE = 100;
    int EMPTY_TEASER = 1000;
    int WHITE_SPACE =  1001;
    int EMPTY_TEASER_HORIZONTAL = 1002;
    int STOCK_TICKERS = 2000;

    int AD_UNIT = 666;
}
