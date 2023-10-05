package nl.fd.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.AllArgsConstructor;
import nl.fd.data.entity.WebLink;
import nl.fd.data.entity.card.teaser.Teaser;
import nl.fd.ui.viewholder.card.AbstractTeaserViewHolder;
import nl.fd.ui.viewholder.card.SectionVerticalTeaserViewHolder;

@AllArgsConstructor
public class TeaserGroupAdapter extends RecyclerView.Adapter<AbstractTeaserViewHolder> {

    private Activity activity;

    private List<WebLink> webLinkList;

    private List<Teaser> teaserList;

    private int cardWidth;

    private boolean verticalOnly;

    private String analyticsSectionId;

    @NonNull
    @Override
    public AbstractTeaserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SectionVerticalTeaserViewHolder.create(parent, cardWidth, false, analyticsSectionId);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractTeaserViewHolder holder, int position) {
        holder.setValues(activity, teaserList.get(position), webLinkList);
    }

    @Override
    public int getItemCount() {
        return teaserList.size();
    }

    @Override
    public int getItemViewType(int position) {
        var teaser = teaserList.get(position);
        if (teaser.isSpecialCard()) {
            return TeaserStyle.SPECIAL;
        } else {
            return TeaserStyle.DEFAULT;
        }
    }
}
