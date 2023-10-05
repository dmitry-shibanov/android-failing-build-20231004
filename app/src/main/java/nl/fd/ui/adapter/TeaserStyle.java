package nl.fd.ui.adapter;

import androidx.annotation.IntDef;

@IntDef({
        TeaserStyle.DEFAULT,
        TeaserStyle.SPECIAL})
public @interface TeaserStyle {
    int DEFAULT = 0;
    int SPECIAL = 1;
}