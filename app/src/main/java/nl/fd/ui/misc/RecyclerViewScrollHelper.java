package nl.fd.ui.misc;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import nl.fd.BuildConfig;
import nl.fd.data.entity.card.Card;
import nl.fd.data.entity.card.teaser.Teaser;
import nl.fd.ui.adapter.CardListAdapter;

public final class RecyclerViewScrollHelper {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RecyclerViewScrollHelper.class);

    private RecyclerViewScrollHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int findFirstVisibleItemPosition(RecyclerView recyclerView) {
        return Optional.ofNullable(recyclerView.getLayoutManager())
                .filter(LinearLayoutManager.class::isInstance)
                .map(LinearLayoutManager.class::cast)
                .map(LinearLayoutManager::findFirstVisibleItemPosition)
                .orElse(RecyclerView.NO_POSITION);
    }

    /**
     * Method to reliably scroll to the given position
     * @param recyclerView  The recycler view to scroll
     * @param pos   The position to scroll to
     */
    public static void scrollToPosition(RecyclerView recyclerView, int pos) {
        log.debug("Requesting recyclerView to scroll to position {}", pos);
        describeScrollWindow(recyclerView, "initial state");
        recyclerView.scrollToPosition(pos);
        waitForLayout(recyclerView, pos, vh -> centerItem(recyclerView, pos, vh));
    }

    /**
     * Method to reliably smooth scroll to the given position
     * @param recyclerView  The recycler view to scroll
     * @param pos   The position to scroll to
     */
    public static void smoothScrollToPosition(RecyclerView recyclerView, int pos) {
        log.debug("Requesting recyclerView to scroll to position {}", pos);
        describeScrollWindow(recyclerView, "initial state");
        recyclerView.smoothScrollToPosition(pos);
        waitForLayout(recyclerView, pos, vh -> centerItem(recyclerView, pos, vh));
    }

    private static void waitForLayout(RecyclerView recyclerView, int pos, Consumer<RecyclerView.ViewHolder> onLayout) {
        RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(pos);
        if (vh == null) {
            // VH not available, so items are not yet measured - resulting in wrong scroll position.
            log.debug("No viewHolder present at position {} - attaching operation onScrolled", pos);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    log.debug("RecyclerView detected scroll({}, {}) - attempting to scroll to correct position again", dx, dy);
                    recyclerView.removeOnScrollListener(this);
                    RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(pos);
                    if (vh != null) {
                        onLayout.accept(vh);
                    } else {
                        log.warn("Unable to find viewHolder on position {}, even after waiting for initial scroll. Giving up", pos);
                    }
                }
            });
        } else {
            onLayout.accept(vh);
        }
    }

    private static void centerItem(RecyclerView recyclerView, int pos, RecyclerView.ViewHolder vh) {
        int cardHeight = vh.itemView.getHeight();
        int screenHeight = recyclerView.getHeight();
        int verticalOffset = (screenHeight - cardHeight) / 2;
        int cardWidth = vh.itemView.getWidth();
        int screenWidth = recyclerView.getWidth();
        int horizontalOffset = (screenWidth - cardWidth) / 2;
        log.info("Attempting to center item; card height {}, screen height {}, vertical offset {}, card width {}, screen width {}, horizontal offset {}",
                cardHeight, screenHeight, verticalOffset, cardWidth, screenWidth, horizontalOffset);
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = ((LinearLayoutManager) lm);
            describeScrollWindow(recyclerView, "Before centering");
            if (llm.getOrientation() == RecyclerView.HORIZONTAL) {
                log.debug("Centering - scrollToPositionWithOffset({}, {})", pos, horizontalOffset);
                llm.scrollToPositionWithOffset(pos, horizontalOffset);
            } else {
                log.debug("Centering - scrollToPositionWithOffset({}, {})", pos, verticalOffset);
                llm.scrollToPositionWithOffset(pos, verticalOffset);
            }
            recyclerView.postDelayed(() -> describeScrollWindow(recyclerView, "After centering"), 200);
        } else {
            log.error("Unable to find a LinearLayoutManager on this recyclerView");
        }
    }

    private static void describeScrollWindow(RecyclerView recyclerView, String stateDescription){
        if (BuildConfig.DEBUG) {
            log.debug(stateDescription);
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (adapter instanceof CardListAdapter && lm instanceof LinearLayoutManager) {
                LinearLayoutManager llm = ((LinearLayoutManager) lm);
                CardListAdapter cardListAdapter = (CardListAdapter) adapter;
                int index = llm.findFirstVisibleItemPosition();
                log.debug("First visible item: {} {}", index, itemAt(cardListAdapter, index));
                index = llm.findFirstCompletelyVisibleItemPosition();
                log.debug("First completely visible item: {} {}", index, itemAt(cardListAdapter, index));
                index = llm.findLastVisibleItemPosition();
                log.debug("Last visible item: {} {}", index, itemAt(cardListAdapter, index));
                index = llm.findLastCompletelyVisibleItemPosition();
                log.debug("Last completely visible item: {} {}", index, itemAt(cardListAdapter, index));
            }
        }
    }

    private static String itemAt(CardListAdapter adapter, int pos) {
        List<Card> list = adapter.getCardList();
        if (pos >= 0 && pos < list.size()) {
            Card card = list.get(pos);
            if (card instanceof Teaser) {
                Teaser teaser = (Teaser) card;
                return Objects.toString(teaser.getTeaserTitle(), teaser.getTitle());
            }
            return String.valueOf(card);
        }
        return "(unknown)";
    }

    // Can be replaced with java.util.function.Consumer when minimum API level is raised to 24
    @FunctionalInterface
    private interface Consumer<T> {
        void accept(T viewHolder);
    }

}
