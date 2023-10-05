package nl.fd.ui.misc;

import android.text.TextUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FdUrlUtils {

    public static String stripLeadingSlash(String url) {
        if (url != null && url.startsWith("/")) {
            return url.substring(1);
        }
        return url;
    }
}
