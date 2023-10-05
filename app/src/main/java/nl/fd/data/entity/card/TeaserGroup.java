package nl.fd.data.entity.card;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.fd.data.entity.card.teaser.Teaser;
import nl.fd.data.entity.home.HomeSectionType;

@Data
@AllArgsConstructor
public class TeaserGroup implements Card, TeaserHolder {

    private String id;

    private HomeSectionType sectionType;

    private List<Teaser> teasers;

    @Nullable
    private String analyticsSectionId;

    @NonNull
    @Override
    public long getUniqueRecyclerId() {
        return (this.getClass().getName() + id).hashCode();
    }
}
