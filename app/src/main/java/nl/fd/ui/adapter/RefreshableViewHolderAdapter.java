package nl.fd.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import nl.fd.ui.viewholder.Refreshable;

public abstract class RefreshableViewHolderAdapter<T extends RecyclerView.ViewHolder> extends AttachStateAwareViewHolderAdapter<T> {

    private final Set<T> currentlyVisibleCards;

    private final Flowable<Long> refreshInterval;

    private boolean currentlyVisible;

    RefreshableViewHolderAdapter(boolean defaultVisible) {
        currentlyVisible = defaultVisible;
        currentlyVisibleCards = new HashSet<>();
        refreshInterval = Flowable
                .interval(1, TimeUnit.MINUTES)
                .throttleLast(1, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .publish()
                .refCount();
    }

    public void inView(){
        currentlyVisible = true;
        currentlyVisibleCards.stream()
                .filter(viewHolder -> viewHolder instanceof Refreshable)
                .map(viewHolder -> (Refreshable) viewHolder)
                .forEach(refreshable -> refreshable.subscribe(refreshInterval));
    }

    public void outOfView(){
        currentlyVisible = false;
        currentlyVisibleCards.stream()
                .filter(viewHolder -> viewHolder instanceof Refreshable)
                .map(viewHolder -> (Refreshable) viewHolder)
                .forEach(Refreshable::unsubscribe);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull T holder) {
        super.onViewAttachedToWindow(holder);
        currentlyVisibleCards.add(holder);
        if (currentlyVisible && holder instanceof Refreshable) {
            ((Refreshable) holder).subscribe(refreshInterval);
        }
    }

    @Override
    @java.lang.SuppressWarnings("java:S2175")
    public void onViewDetachedFromWindow(@NonNull T holder) {
        super.onViewDetachedFromWindow(holder);
        currentlyVisibleCards.remove(holder);
        if (holder instanceof Refreshable) {
            ((Refreshable) holder).unsubscribe();
        }
    }
}
