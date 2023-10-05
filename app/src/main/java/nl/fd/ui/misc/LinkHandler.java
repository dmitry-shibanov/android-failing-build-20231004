package nl.fd.ui.misc;

import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;
import nl.fd.data.entity.WebLink;

@Slf4j
public class LinkHandler {

    public static void linkify(TextView textView) {
        BetterLinkMovementMethod.linkifyHtml(textView)
                .setOnLinkClickListener((view, link) -> {
                    WebLink webLink = WebLink.of(link);
                        webLink.openExternally(view.getContext());
                    return true;
                });
    }

    public static boolean needsStrippedUnderlines(Spannable spannable) {
        return spannable.getSpans(0, spannable.length(), URLSpan.class).length !=
                spannable.getSpans(0, spannable.length(), URLSpanNoUnderline.class).length;
    }

    public static void stripUnderlines(Spannable spannable) {
        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan span : spans) {
            // Only replace normal URLSpan, no child classes
            if (span.getClass() == URLSpan.class) {
                int start = spannable.getSpanStart(span);
                int end = spannable.getSpanEnd(span);
                if (end > start) {
                    spannable.removeSpan(span);
                    span = new URLSpanNoUnderline(span.getURL());
                    spannable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                log.debug("Not replacing customized {}", span.getClass().getSimpleName());
            }
        }
    }

    private static class URLSpanNoUnderline extends URLSpan {

        @Nullable
        @ColorInt
        final Integer linkColor;

        URLSpanNoUnderline(String url) {
            super(url);
            linkColor = null;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            Optional.ofNullable(linkColor)
                    .ifPresent(ds::setColor);
        }
    }
}
