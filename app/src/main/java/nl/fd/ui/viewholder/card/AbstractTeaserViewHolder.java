package nl.fd.ui.viewholder.card;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import nl.fd.data.entity.WebLink;
import nl.fd.data.entity.card.teaser.Teaser;
import nl.fd.ui.viewholder.Refreshable;

@Slf4j
public abstract class AbstractTeaserViewHolder extends AbstractCardViewHolder<Teaser> implements Refreshable {

    int parentWidth;

    @Nullable
    private Teaser teaser;
    @Nullable
    private Disposable refreshSubscription;

    @Nullable
    private String analyticsSectionId;

    /**
     * Constructor for teaser view holders that have already created their own view
     *
     * @param parent Parent-view (used to properly dimension the teaser image)
     * @param view   The (inflated) view
     */
    AbstractTeaserViewHolder(ViewGroup parent, View view) {
        super(view);
        parentWidth = parent.getWidth();
        log.debug("calculated width: {}", parentWidth);
    }

    AbstractTeaserViewHolder(ViewGroup parent, View view, @Nullable String analyticsSectionId) {
        super(view);
        parentWidth = parent.getWidth();
        log.debug("calculated width: {}", parentWidth);
        this.analyticsSectionId = analyticsSectionId;
    }

    private void refresh() {
        Optional.ofNullable(teaser)
                .map(teaser1 -> {
                    teaser1.publicationDateUpdated();
                    return teaser1.getTitle();
                })
                .ifPresent(title -> log.trace("Refreshing publication date for {}", title));
    }

    @Override
    public void subscribe(Flowable<Long> refreshInterval) {
        unsubscribe();
        refreshSubscription = refreshInterval.subscribe(i -> refresh(),
                throwable -> log.error("refreshInterval received an exception", throwable));
    }

    @Override
    public void unsubscribe() {
        Optional.ofNullable(refreshSubscription)
                .filter(disposable -> !disposable.isDisposed())
                .ifPresent(Disposable::dispose);
    }

    public void setValues(@NonNull Activity activity, @NonNull Teaser teaser, @NonNull List<WebLink> articleLinkList) {
        this.teaser = teaser;
    }

}
