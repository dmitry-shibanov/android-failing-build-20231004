package nl.fd.ui.viewholder.card;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.List;

import nl.fd.R;
import nl.fd.data.entity.card.TeaserGroup;
import nl.fd.data.entity.home.HomeSectionType;
import nl.fd.databinding.CardTeaserGroupBinding;
import nl.fd.ui.adapter.TeaserGroupAdapter;
import nl.fd.ui.misc.TeaserUtils;

public class TeaserGroupViewHolder extends AbstractCardViewHolder<TeaserGroup> {

    private final CardTeaserGroupBinding binding;

    private TeaserGroupViewHolder(CardTeaserGroupBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void setValues(@NonNull Activity activity, TeaserGroup card, List articleLinkList) {
        var gridLayoutManager = (GridLayoutManager) binding.teaserList.getLayoutManager();
        switch (card.getSectionType()) {
            case OPENING:
                gridLayoutManager.setSpanCount(card.getTeasers().size() > 1 ? activity.getResources().getInteger(R.integer.homeOpeningColumns) : 1);
                break;
            default:
                gridLayoutManager.setSpanCount(activity.getResources().getInteger(R.integer.homeHighlightedColumns));
        }
        binding.teaserList.setAdapter(new TeaserGroupAdapter(activity, articleLinkList, card.getTeasers(), TeaserUtils.calculateCardWidth(binding.teaserList, binding.getRoot()), forceVerticalCards(card), card.getAnalyticsSectionId()));
    }

    private boolean forceVerticalCards(TeaserGroup card) {
        return card.getSectionType() == HomeSectionType.BREAKING || card.getSectionType() == HomeSectionType.OPENING;
    }

    public static TeaserGroupViewHolder create(ViewGroup parent) {
        var binding = CardTeaserGroupBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TeaserGroupViewHolder(binding);
    }
}
