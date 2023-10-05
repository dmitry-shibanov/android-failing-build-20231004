package nl.fd.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AttachStateAwareViewHolderAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (recyclerView == this.recyclerView) {
            this.recyclerView = null;
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull T holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof AttachStateAwareViewHolder) {
            ((AttachStateAwareViewHolder) holder).onAttach();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull T holder) {
        if (holder instanceof AttachStateAwareViewHolder) {
            ((AttachStateAwareViewHolder) holder).onDetach();
        }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull T holder) {
        if (holder instanceof AttachStateAwareViewHolder) {
            ((AttachStateAwareViewHolder) holder).onRecycle();
        }
        super.onViewRecycled(holder);
    }

    void cycleVisible(Consumer<RecyclerView.ViewHolder> consumer) {
        Optional.ofNullable(recyclerView.getLayoutManager())
                .filter(layoutManager -> layoutManager instanceof LinearLayoutManager)
                .map(layoutManager -> (LinearLayoutManager) layoutManager)
                .ifPresent(llm -> {
                    for (int i = llm.findFirstVisibleItemPosition(); i < llm.findLastVisibleItemPosition(); i++) {
                        consumer.accept(recyclerView.findViewHolderForLayoutPosition(i));
                    }
                });

    }

}
