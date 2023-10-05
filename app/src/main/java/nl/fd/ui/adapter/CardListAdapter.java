package nl.fd.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.fd.BuildConfig;
import nl.fd.data.entity.WebLink;
import nl.fd.data.entity.article.ArticleMetaHeader;
import nl.fd.data.entity.card.Card;
import nl.fd.data.entity.card.CardType;
import nl.fd.data.entity.card.TeaserGroup;
import nl.fd.data.entity.card.TeaserHolder;
import nl.fd.data.entity.card.teaser.Teaser;
import nl.fd.ui.viewholder.card.AbstractCardViewHolder;
import nl.fd.ui.viewholder.card.TeaserGroupViewHolder;
import nl.fd.ui.viewholder.card.WhitespaceCardViewHolder;

public class CardListAdapter extends RefreshableViewHolderAdapter<AbstractCardViewHolder<? extends Card>> implements DefaultLifecycleObserver {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CardListAdapter.class);
    final List<Card> cardList;

    private OnChildAttachStateChangeListener onChildAttachStateChangeListener;

    private final Activity activity;
    private final List<WebLink> teasersWebLinkList;
    private final SparseArray<AbstractCardViewHolder<? extends Card>> boundViewHolders;

    private DateTime lastUpdated;

    protected CardListAdapter(Activity activity, boolean defaultVisible) {
        super(defaultVisible);
        this.activity = activity;
        teasersWebLinkList = new ArrayList<>();
        cardList = new ArrayList<>();
        boundViewHolders = new SparseArray<>();
        super.setHasStableIds(true);
    }

    public void attachToLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    public boolean shouldUpdate() {
        return lastUpdated == null ||
                (DateTime.now().isAfter(lastUpdated.plusSeconds(BuildConfig.REFRESH_FREQUENCY_IN_SECONDS)));
    }

    public void forceUpdate() {
        this.lastUpdated = null;
    }

    /**
     * Replaces the current contents with the new, updated list. Calculates and notifies recyclerView about the differences.
     * `lastUpdated` is modified.
     *
     * @param newCardList Replacement list of cards
     */
    public void updateCardList(List<Card> newCardList) {
        var diffResult = calculateDiff(newCardList);

        lastUpdated = DateTime.now();
        cardList.clear();
        cardList.addAll(newCardList);

        diffResult.dispatchUpdatesTo(this);
        identifyCards();
    }

    @Override
    public int getItemViewType(int position) {
        var card = cardList.get(position);
        if (card instanceof TeaserGroup) {
            return CardType.TEASER_GROUP;
        }
        // Default to empty card
        return CardType.WHITE_SPACE;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AbstractCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new viewHolderFD
        switch (viewType) {
            case CardType.TEASER_GROUP:
                return TeaserGroupViewHolder.create(parent);
            default:
                return WhitespaceCardViewHolder.create(parent);
        }
    }

    // Replace the contents of a AbstractTeaserViewHolder (invoked by the layout manager)
    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractCardViewHolder holder, int positionInRecyclerView) {
        // - get element from your dataset at this position
        // - replace the contents of the viewHolderFD with that element
        var card = cardList.get(positionInRecyclerView);

        holder.setValues(this.activity, card, teasersWebLinkList);

        log.trace("({}) bind [{}] -> {} - {}", getClass().getSimpleName(), positionInRecyclerView, holder.getClass().getSimpleName(), card);
        boundViewHolders.put(positionInRecyclerView, holder);
    }

    @Nullable
    public AbstractCardViewHolder getBoundViewHolder(int positionInRecyclerView) {
        return boundViewHolders.get(positionInRecyclerView);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public long getItemId(int position) {
        var card = cardList.get(position);
        return card.getUniqueRecyclerId();
    }


    @Override
    public void onViewAttachedToWindow(@NonNull AbstractCardViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Optional.ofNullable(onChildAttachStateChangeListener)
                .ifPresent(listener -> listener.onViewAttachedToWindow(holder));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull AbstractCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Optional.ofNullable(onChildAttachStateChangeListener)
                .ifPresent(listener -> listener.onViewDetachedFromWindow(holder));
    }

    @Override
    public void onViewRecycled(@NonNull AbstractCardViewHolder holder) {
        super.onViewRecycled(holder);
        AbstractCardViewHolder<? extends Card> vh = boundViewHolders.get(holder.getBindingAdapterPosition());
        if (vh == holder) {
            boundViewHolders.remove(holder.getBindingAdapterPosition());
        } else {
            log.warn("Unable to locate {} on last known position", holder);
        }
        holder.recycle();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        lastUpdated = null;
        cardList.clear();
        identifyCards();
    }

    public int getCardPositionFromTeaserPosition(int teaserPosition) {
        if (teaserPosition >= 0 && teaserPosition < teasersWebLinkList.size()) {
            var counter = 0;
            for (var i = 0; i < getCardList().size(); i++) {
                var card = getCardList().get(i);
                if (card instanceof TeaserHolder) {
                    counter += ((TeaserHolder) card).getTeasers().size();
                    if (counter > teaserPosition) {
                        return i;
                    }
                }
            }
        }
        return RecyclerView.NO_POSITION;
    }

    private void identifyCards() {
        teasersWebLinkList.clear();

        cardList.stream()
                .filter(TeaserHolder.class::isInstance)
                .map(TeaserHolder.class::cast)
                .flatMap(teaserHolder -> teaserHolder.getTeasers().stream())
                .forEach(this::addTeaserToList);
    }

    private void addTeaserToList(Teaser teaser) {
        WebLink webLink;
        if (TextUtils.isEmpty(teaser.getRedirectUrl())) {
            webLink = WebLink
                    .builder(teaser.getUrl())
                    .preview(teaser.getContents())
                    .articleMeta(ArticleMetaHeader.fromMeta(teaser))
                    .free(teaser.isFree())
                    .category(teaser.getCategory())
                    .build();
        } else {
            webLink = WebLink
                    .builder(teaser.getRedirectUrl())
                    .articleMeta(ArticleMetaHeader.builder().articleId(teaser.getArticleId()).build())
                    .free(teaser.isFree())
                    .build();
        }

        teasersWebLinkList.add(webLink);
    }

    @NonNull
    private DiffUtil.DiffResult calculateDiff(List<Card> newCardList) {
        return DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return cardList.size();
            }

            @Override
            public int getNewListSize() {
                return newCardList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                var oldItem = cardList.get(oldItemPosition);
                var newItem = newCardList.get(newItemPosition);

                boolean same = Optional.ofNullable(oldItem)
                            .map(Card::getId)
                            .flatMap(oldId ->
                                    Optional.ofNullable(newItem)
                                            .map(Card::getId)
                                            .filter(newId -> newId.equals(oldId))
                            ).isPresent();


                if (!same) {
                    log.trace("Items {}({}) and {}({}) are not the same!",
                            oldItemPosition,
                            nullSafeCardId(oldItem),
                            newItemPosition,
                            nullSafeCardId(newItem));
                }
                return same;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                var oldItem = cardList.get(oldItemPosition);
                var newItem = newCardList.get(newItemPosition);
                boolean same= oldItem != null && oldItem.equals(newItem);

                if (!same) {
                    log.trace("Item contents for {}({}) and {}({}) are not the same!",
                            oldItemPosition,
                            nullSafeCardId(oldItem),
                            newItemPosition,
                            nullSafeCardId(newItem));
                }
                return same;
            }
        });
    }

    private static String nullSafeCardId(Card card) {
        return card == null ? "null" : getCardIdOrNoId(card);
    }

    private static String getCardIdOrNoId(Card card) {
        return card.getId() == null ? "<no_id>" : card.getId();
    }

    public List<Card> getCardList() {
        return this.cardList;
    }


    public interface OnChildAttachStateChangeListener {

        void onViewAttachedToWindow(@NonNull AbstractCardViewHolder holder);

        void onViewDetachedFromWindow(@NonNull AbstractCardViewHolder holder);

    }
}