package nl.fd.ui;

import android.content.Context;

import nl.fd.BuildConfig;
import nl.fd.data.entity.WebLink;

public class PageRouter {

    private PageRouter() {
    }

    public static void open(Context context, String url) {
        var page = PageUrlParser.parse(url);
        open(context, page);
    }

    public static void open(Context context, PageUrlParser.Page page) {
        switch (page.getPageType()) {
            case SUBSCRIBE:
                WebLink
                        .builder(BuildConfig.BASE_URL + "abonneren?utm_source=fd-app&utm_medium=referral&utm_campaign=betaald-toegang&utm_term=" + page.getValue())
                        .external(true)
                        .build().openExternally(context);
                break;
            case EXTERNAL:
                WebLink
                        .builder(page.getValue())
                        .external(true)
                        .build().openExternally(context);
                break;
            default:
                // This page type is not handled by the PageParser
        }

    }
}
