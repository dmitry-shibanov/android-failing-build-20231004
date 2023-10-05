package nl.fd.ui.viewholder.card;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Optional;

import nl.fd.R;
import nl.fd.data.entity.WebLink;
import nl.fd.data.entity.card.Card;

public class WhitespaceCardViewHolder extends AbstractCardViewHolder<Card> {

    private final View whitespace;

    private WhitespaceCardViewHolder(View whitespace) {
        super(whitespace);
        this.whitespace = whitespace;
    }

    @Override
    public void setValues(@NonNull Activity activity, @NonNull Card card, @NonNull List<WebLink> articleLinkList) {
        // Since this is the default card, don't assume it's a whitespace card
        int heightPx = 0;
        setHeight(whitespace, heightPx);
    }

    public static WhitespaceCardViewHolder create(ViewGroup parent) {
        Context context = parent.getContext();
        View whitespace = new View(context);
        setHeight(whitespace, 0);
        whitespace.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        return new WhitespaceCardViewHolder(whitespace);
    }

    private static void setHeight(View view, int height) {
        ViewGroup.LayoutParams params = Optional.ofNullable(view.getLayoutParams()).orElseGet(() ->
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height;
        view.setLayoutParams(params);
    }
}
