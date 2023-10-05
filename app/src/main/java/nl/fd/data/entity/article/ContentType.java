package nl.fd.data.entity.article;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

@StringDef({ContentType.META, ContentType.FOOTER, ContentType.PARAGRAPH, ContentType.IMAGE, ContentType.INFOGRAPHIC,
            ContentType.INFOGRAPHIC_XT, ContentType.HEADING, ContentType.SUBHEADING, ContentType.STACK_FRAME, ContentType.TEXT_FRAME,
            ContentType.NUMBER_FRAME, ContentType.QUOTE, ContentType.STOCK_QUOTE, ContentType.RELATED_LINK, ContentType.READ_MORE, ContentType.READ_MORE_V2, ContentType.TWITTER,
            ContentType.YOUTUBE, ContentType.VIMEO, ContentType.SUMMARY, ContentType.BULLET_LIST, ContentType.PDF, ContentType.HTML_EMBED, ContentType.TOPICS})
@Retention(RetentionPolicy.SOURCE)
public @interface ContentType {
    String META           = "article-meta";
    String FOOTER         = "article-meta-footer";
    String TOPICS         = "topics";
    String PARAGRAPH      = "p";
    String IMAGE          = "fdmg-image";
    String INFOGRAPHIC    = "fdmg-infographic";
    String INFOGRAPHIC_XT = "fdmg-infographic-extended";
    String HEADING        = "h2";
    String SUBHEADING     = "h3";
    String STACK_FRAME    = "fdmg-stack-frame";
    String TEXT_FRAME     = "fdmg-text-frame";
    String NUMBER_FRAME   = "fdmg-number-frame";
    String QUOTE          = "fdmg-quote";
    String STOCK_QUOTE    = "fdmg-stock-quote";
    String RELATED_LINK   = "fdmg-related-link";
    String READ_MORE      = "fdmg-readmore";
    String READ_MORE_V2   = "fdmg-readmore-v2";
    String PDF            = "fdmg-pdf";
    String TWITTER        = "fdmg-twitter";
    String YOUTUBE        = "fdmg-youtube";
    String VIMEO          = "fdmg-vimeo";
    String SUMMARY        = "fdmg-summary";
    String BULLET_LIST    = "fdmg-bulletpoint";
    String HTML_EMBED     = "fdmg-html-embed";
}
