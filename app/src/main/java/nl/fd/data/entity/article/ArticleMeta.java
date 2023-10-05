package nl.fd.data.entity.article;

import org.joda.time.DateTime;

import java.util.List;

import nl.fd.data.entity.card.teaser.Update;

public interface ArticleMeta {

    long getArticleId();

    String getTitle();

    String getUrl();

    DateTime getPublicationDate();

    String getIntro();

    String getLabel();

    Boolean getRequiresWebView();

    Boolean getCommentsEnabled();

    Update getUpdate();
}
