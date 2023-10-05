package nl.fd.ui.richtext;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Arrays;
import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.fd.data.entity.RichText;
import nl.fd.data.entity.article.FontScaleHolder;
import nl.fd.ui.misc.LinkHandler;

@RequiredArgsConstructor
@Slf4j
public class ContextAwareSpansBindingAdapter implements LifecycleObserver {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindingAdapter(value = {"android:text", "enableStockQuotes", "enableLinks", "enableDropCap",
            "scalable"}, requireAll = false)
    @SuppressWarnings("java:S107")
    public void enrich(TextView textView, RichText richText, boolean stockQuotes, boolean links,
                       boolean dropCap, boolean scalable) {

        if (richText != null && !stockQuotes && !links && !dropCap) {
            // The spannable won't be changed
            textView.setText(richText.getStaticText(), TextView.BufferType.SPANNABLE);
        } else {
            Editable editable = createEditable(textView, richText, scalable);

            var wasTextSet = false;

            wasTextSet = handleLinks(textView, links, editable, wasTextSet);

            if (!wasTextSet) {
                setText(textView, editable);
            }

        }
    }

    private boolean handleLinks(TextView textView, boolean links, Editable editable, boolean wasTextSet) {
        if (links) {
            if (!wasTextSet) {
                wasTextSet = setText(textView, editable);
            }
            LinkHandler.linkify(textView);

            if (LinkHandler.needsStrippedUnderlines(editable)) {
                LinkHandler.stripUnderlines(editable);
                setText(textView, editable);
            }
        }
        return wasTextSet;
    }

    private Editable createEditable(TextView textView, RichText richText, boolean scalable) {
        Editable editable;
        if (richText == null) {
            editable = new SpannableStringBuilder(textView.getText());
        } else {
            editable = richText.edit();
        }
        if (scalable) {
            Arrays.stream(editable.getSpans(0, editable.length(),
                    RelativeSizeSpan.class)).forEach(editable::removeSpan);
            editable.setSpan(new RelativeSizeSpan(FontScaleHolder.getScalingFactor()/FontScaleHolder.getSystemFontScale(textView.getContext().getContentResolver())), 0, editable.length(), 0);
        }
        return editable;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        compositeDisposable.clear();
    }

    public boolean setText(TextView textView, Editable editable) {
        textView.setText(editable, TextView.BufferType.EDITABLE);
        return true;
    }
}
