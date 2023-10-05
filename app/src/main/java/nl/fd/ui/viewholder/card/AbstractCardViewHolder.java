package nl.fd.ui.viewholder.card;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.fd.data.entity.WebLink;
import nl.fd.data.entity.card.Card;

public abstract class AbstractCardViewHolder<T extends Card> extends RecyclerView.ViewHolder {

    protected AbstractCardViewHolder(View view) {
        super(view);
    }

    /**
     * Sets up the viewholder for this card:
     * @param activity          The activity to use as context
     * @param card              The card (teaser, ad-unit, etc) to display
     * @param articleLinkList   The list of articles this teaser belongs to
     */
    public abstract void setValues(@NonNull Activity activity, T card, List<WebLink> articleLinkList);


    public void recycle() {

    }

}
