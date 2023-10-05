package nl.fd.ui.misc;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.fd.R;
import nl.fd.data.entity.ArticleCategory;
import nl.fd.data.entity.card.teaser.Teaser;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class TeaserUtils {

    public static final int MAX_AUTHOR_IMAGES_COUNT = 3;

    public static CharSequence teaserTitle(@Nullable Teaser teaser) {
        if (teaser == null) {
            return "null";
        } else {
            return TextUtils.isEmpty(teaser.getTeaserTitle()) ? teaser.getTitle() : teaser.getTeaserTitle();
        }
    }

    public static long safeParseLong(String string) {
        long res = 0;
        if (string != null && !string.isEmpty()) {
            try {
                res = Long.parseLong(string);
            } catch (NumberFormatException e) {
                log.debug("Not a valid number: {}", string);
            }
        }
        return res;
    }

    @BindingAdapter("viewLabel")
    public static void setLabel(View view, String label) {
        view.setTag(R.id.label, label);
    }

    @BindingAdapter("parentWidth")
    public static void setGradientWidthAndHeight(View view, int parentWidth) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = parentWidth;
        params.height = parentWidth;
        view.setLayoutParams(params);
    }

    private static final Map<String, Integer> NONSTANDARD_TEASER_HEIGHTS = new HashMap<>();

    public static int calculateCardWidth(ViewGroup parent, View view) {
        int cardWidth;
        if (parent instanceof RecyclerView && ((RecyclerView) parent).getLayoutManager() instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) Objects.requireNonNull(((RecyclerView) parent).getLayoutManager())).getSpanCount();
            log.debug("parent width: {}, parent measuredWidth: {} , ", parent.getWidth(), parent.getMeasuredWidth() );
            int recyclerWidth = parent.getWidth();
            if (recyclerWidth == 0) {
                recyclerWidth = parent.getMeasuredWidth();
            }
            if (recyclerWidth == 0) {
                recyclerWidth = getWidthFromDevice(view);
            }
            cardWidth = recyclerWidth / spanCount;
        } else {
            cardWidth = parent.getWidth();
        }
        return cardWidth;
    }

    private static int getWidthFromDevice(View view) {
        int recyclerWidth;
        var displayMetrics = view.getContext().getResources().getDisplayMetrics();
        var deviceWidth = displayMetrics.widthPixels;

        var homeMargins = (int) view.getContext().getResources().getDimension(R.dimen.homeHorizontalGridMargin) * 2;
        recyclerWidth = deviceWidth - homeMargins;
        return recyclerWidth;
    }


    public static int calculateImageWidth(int cardWidth, ViewGroup parent, View view) {
        if (cardWidth > 0 && parent instanceof RecyclerView && ((RecyclerView) parent).getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            var cardMargins = lp.leftMargin + lp.rightMargin;
            return cardWidth - cardMargins;
        }
        return 0;
    }
}
