package nl.fd.ui;

import android.net.Uri;

import java.util.List;

import lombok.Data;
import nl.fd.BuildConfig;
import nl.fd.R;
import nl.fd.data.entity.ArticleSection;
import nl.fd.data.entity.WebLink;

public class PageUrlParser {

    public enum PageType {
        HOME,
        ARTICLE,
        SECTION,
        INTERNAL,
        EXTERNAL,
        NEWSPAPER,
        SUBSCRIBE,
        COPYRIGHT_PRIVACY,
        TAG,
        AUTHOR,
        BEURS,
        TOPIC,
        TOPIC_SELECT
    }

    public static final String BASE_URL_HTTP = BuildConfig.BASE_URL.replace("https://", "http://");

    @Data
    public static class Page {

        private PageType pageType;
        private ArticleSection section;
        private String value;

        public Page(PageType pageType, ArticleSection section) {
            this.pageType = pageType;
            this.section = section;
        }

        public Page(PageType pageType, String value) {
            this.pageType = pageType;
            this.value = value;
        }
    }

    public static Page parse(String url) {
        return new Page(PageType.EXTERNAL, url);
    }



}
