package nl.fd.ui.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import nl.fd.data.entity.card.Card;

/**
 * Taken from: https://github.com/shandilyaaman/SampleEndlessRecyclerView
 */
@Slf4j
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    /**
     * The total number of items in the dataset after the last load
     */
    private int mPreviousTotal = 0;
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean mLoading = true;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = Objects.requireNonNull(recyclerView.getLayoutManager()).getItemCount();
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        log.trace("visible: {}, totalItems: {}, firstVisible: {}}, mLoading: {}, mPreviousTotal: {}",
                visibleItemCount,
                totalItemCount,
                firstVisibleItem,
                mLoading,
                mPreviousTotal);

        if (mLoading && (totalItemCount > mPreviousTotal)) {
                mLoading = false;
                mPreviousTotal = totalItemCount;

        }
        var visibleThreshold = 5;
        if (!mLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            onLoadMore();

            mLoading = true;
        }
    }

    public void reset() {
        mLoading = true;
        mPreviousTotal = 0;
    }

    public abstract void onLoadMore();

    public static List<Card> removeOrderedDuplicatesFromTail(final List<Card> original, List<Card> tail) {
        // We assume both lists to be ordered, and the 'extra' to be placed after original.
        // Duplicates can thus only occur between the head of 'extra' and the tail of 'original' ; traverse both list in opposite order to find them
        Iterator<Card> head = tail.iterator();
        var lastTailFoundPos = 0;
        while (head.hasNext()) {
            Card newItem = head.next();

            var headRemoved = false;
            for (int tailPos = original.size() - 1; tailPos >= lastTailFoundPos; tailPos--) {
                Card oldItem = original.get(tailPos);
                if (TextUtils.equals(oldItem.getId(), newItem.getId())) {
                    head.remove();
                    headRemoved = true;
                    lastTailFoundPos = tailPos;
                    break;
                }
            }

            if (!headRemoved) {
                break;
            }

        }
        return tail;
    }
}