package nl.fd.data.entity.article;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import org.joda.time.DateTime;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nl.fd.data.entity.card.teaser.Update;


@Getter
@Parcel(Parcel.Serialization.BEAN)
@EqualsAndHashCode(callSuper = true)
@ToString
public class ArticleMetaHeader extends Content implements ArticleMeta {

    private final long articleId;

    private final String title;

    private final String url;

    private final DateTime publicationDate;

    private final String intro;

    private final String label;

    private final Update update;

    private final Boolean requiresWebView;

    private final Boolean commentsEnabled;

    @ParcelConstructor
    @SuppressWarnings("java:S107")
    public ArticleMetaHeader(long articleId, String title, String url, DateTime publicationDate, String intro, String label,
                             Boolean requiresWebView,
                             Boolean commentsEnabled, Update update) {
        super(ContentType.META);
        this.articleId = articleId;
        this.title = title;
        this.url = url;
        this.publicationDate = publicationDate;
        this.intro = intro;
        this.label = label;
        this.requiresWebView = requiresWebView;
        this.commentsEnabled = commentsEnabled;
        this.update = update;
    }

    public static ArticleMetaHeaderBuilder builder() {
        return new ArticleMetaHeaderBuilder();
    }

    @Override
    public boolean sameItem(Object other) {
        if (other instanceof ArticleMetaHeader) {
            ArticleMetaHeader otherMeta = (ArticleMetaHeader) other;
            return articleId == otherMeta.articleId;
        }
        return false;
    }

    @Override
    public boolean sameContents(Object other) {
        return equals(other);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public DateTime getPublicationDate() {
        return publicationDate;
    }

    @Bindable
    public String getIntro() {
        return intro;
    }

    @Bindable
    public String getLabel() {
        return label;
    }

    @Bindable
    public DateTime getUpdateDate() {
        if (update != null) {
            return update.getDate();
        }
        return null;
    }

    public static ArticleMetaHeader fromMeta(ArticleMeta meta) {
        return ArticleMetaHeader
                .builder()
                .articleId(meta.getArticleId())
                .title(meta.getTitle())
                .url(meta.getUrl())
                .publicationDate(meta.getPublicationDate())
                .intro(meta.getIntro())
                .label(meta.getLabel())
                .requiresWebView(meta.getRequiresWebView())
                .commentsEnabled(meta.getCommentsEnabled())
                .update(meta.getUpdate())
                .build();
    }

    public static class ArticleMetaHeaderBuilder {
        private long articleId;
        private String title;
        private String url;
        private DateTime publicationDate;
        private String intro;
        private String label;
        private Boolean requiresWebView;
        private Boolean commentsEnabled;
        private Update update;

        ArticleMetaHeaderBuilder() {
        }

        public ArticleMetaHeaderBuilder articleId(long articleId) {
            this.articleId = articleId;
            return this;
        }

        public ArticleMetaHeaderBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ArticleMetaHeaderBuilder url(String url) {
            this.url = url;
            return this;
        }

        public ArticleMetaHeaderBuilder publicationDate(DateTime publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public ArticleMetaHeaderBuilder intro(String intro) {
            this.intro = intro;
            return this;
        }

        public ArticleMetaHeaderBuilder label(String label) {
            this.label = label;
            return this;
        }

        public ArticleMetaHeaderBuilder requiresWebView(Boolean requiresWebView) {
            this.requiresWebView = requiresWebView;
            return this;
        }

        public ArticleMetaHeaderBuilder commentsEnabled(Boolean commentsEnabled) {
            this.commentsEnabled = commentsEnabled;
            return this;
        }

        public ArticleMetaHeaderBuilder update(Update update) {
            this.update = update;
            return this;
        }

        public ArticleMetaHeader build() {
            return new ArticleMetaHeader(articleId, title, url, publicationDate, intro, label, requiresWebView, commentsEnabled, update);
        }

        @NonNull
        public String toString() {
            return "ArticleMetaHeader.ArticleMetaHeaderBuilder(articleId=" + this.articleId + ", title=" + this.title + ", url=" + this.url + ", publicationDate=" + this.publicationDate + ", intro=" + this.intro + ", label=" + this.label + ", requiresWebView=" + this.requiresWebView + ", commentsEnabled=" + this.commentsEnabled + ", update=" + this.update + ")";
        }
    }
}
