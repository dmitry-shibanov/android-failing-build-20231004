package nl.fd.data.entity.home;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import nl.fd.data.entity.card.Card;

@Data
public class HomeSection {

    @Expose
    private HomeSectionType type;

    @SerializedName("items")
    @Expose
    private List<Card> cardList = new ArrayList<>();

    @Nullable
    @Expose
    private String title;

    @Nullable
    @Expose
    private String moreUrl;

    @Nullable
    @Expose
    private String moreTitle;

    @Nullable
    @Expose
    private String analyticsSectionId;
}
