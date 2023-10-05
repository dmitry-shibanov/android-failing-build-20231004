package nl.fd.data.entity.card.teaser;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import nl.fd.BR;
import nl.fd.data.entity.ArticleCategory;
import nl.fd.data.entity.article.ArticleMeta;
import nl.fd.data.entity.article.Content;
import nl.fd.data.entity.card.Card;
import nl.fd.data.entity.card.TeaserHolder;
import nl.fd.ui.misc.TeaserUtils;

@EqualsAndHashCode(callSuper = false, exclude = "markedAsRead")
@ToString(exclude = {"teaserIntro", "intro", "contents"})
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Teaser extends BaseObservable implements Card, ArticleMeta, TeaserHolder, TeaserInterface {

    @Expose
    private String id;

    @Expose
    private String title;

    @Expose
    private String intro;

    @Expose
    private String url;

    @Expose
    private String shareUrl;

    @Expose
    private String redirectUrl;

    @Expose
    private String teaserIntro;

    @Expose
    private String teaserTitle;

    @Expose
    @Getter(onMethod = @__(@Bindable))
    @Bindable
    private DateTime publicationDate;

    @Expose
    private String label;

    @Expose
    private Integer readingTime;

    @Expose
    private boolean shortArticle;

    @Expose
    private ArticleCategory category;

    @Expose
    private Update update;

    private Content[] contents;

    @SerializedName("content")
    @Expose
    private String rawContent;

    @Expose
    private Boolean requiresWebView;

    @Expose
    private Boolean commentsEnabled;

    @Expose
    private boolean free;

    @Expose
    @Bindable
    private boolean markedAsRead;

    @Expose
    private boolean specialCard;

    @Expose
    private int publicationNumber;
    @Expose
    private int pageNumber;

    public boolean isSpecialCard() {
        return specialCard;
    }

    public void publicationDateUpdated(){
        notifyPropertyChanged(BR.publicationDate);
    }

    public ArticleCategory getCategory() {
        return category;
    }

    @Override
    public long getArticleId() {
        return TeaserUtils.safeParseLong(id);
    }

    @Override
    public long getUniqueRecyclerId() {
        return ((category != null ? category.name() : "") + id).hashCode();
    }

    @Override
    public List<Teaser> getTeasers() {
        return List.of(this);
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getIntro() {
        return this.intro;
    }

    public String getUrl() {
        return this.url;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public String getTeaserIntro() {
        return this.teaserIntro;
    }

    public String getTeaserTitle() {
        return this.teaserTitle;
    }

    public String getLabel() {
        return this.label;
    }

    public Update getUpdate() {
        return this.update;
    }

    public Content[] getContents() {
        if (contents == null && this.rawContent != null) {
            this.contents = getContentArray(rawContent);
        }
        return this.contents;
    }

    public Boolean getRequiresWebView() {
        return this.requiresWebView;
    }

    public Boolean getCommentsEnabled() {
        return this.commentsEnabled;
    }

    public boolean isFree() {
        return this.free;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTeaserIntro(String teaserIntro) {
        this.teaserIntro = teaserIntro;
    }

    private Content[] getContentArray(String rawContent) {
        return new Content[0];
    }
}
