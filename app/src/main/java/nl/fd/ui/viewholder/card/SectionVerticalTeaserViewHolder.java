package nl.fd.ui.viewholder.card;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import nl.fd.data.entity.WebLink;
import nl.fd.data.entity.card.teaser.Teaser;
import nl.fd.databinding.CardTeaserVerticalBinding;
import nl.fd.ui.misc.TeaserUtils;

@Slf4j
public class SectionVerticalTeaserViewHolder extends AbstractTeaserViewHolder {

    private final CardTeaserVerticalBinding binding;

    private SectionVerticalTeaserViewHolder(ViewGroup parent, CardTeaserVerticalBinding binding, int cardWidth, boolean darkBackground, String analyticsSectionId) {
        super(parent, binding.getRoot(), analyticsSectionId);
        this.binding = binding;
        parentWidth = TeaserUtils.calculateImageWidth(cardWidth, parent, binding.getRoot());
    }

    @Override
    public void setValues(@NonNull Activity activity, @NonNull Teaser teaser, @NonNull List<WebLink> articleLinkList) {
        super.setValues(activity, teaser, articleLinkList);
        binding.setTeaser(teaser);
    }

    public static SectionVerticalTeaserViewHolder create(ViewGroup parent) {
        return create(parent, 0, false, null);
    }

    public static SectionVerticalTeaserViewHolder create(ViewGroup parent, int cardWidth, boolean darkBackground, String analyticsSectionId) {
        var binding = CardTeaserVerticalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SectionVerticalTeaserViewHolder(parent, binding, cardWidth, darkBackground, analyticsSectionId);
    }

}