package nl.fd.data.entity;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.ParcelConverter;

import java.util.Optional;
import java.util.function.Consumer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.fd.ui.richtext.ParagraphMarker;
import nl.fd.ui.richtext.RichTextParcelableSpan;

@EqualsAndHashCode(of = "staticText")
@Parcel(converter = RichText.RichTextConverter.class)
@Slf4j
public class RichText implements CharSequence {

    /**
     * Parsed rich text spanned string. Although the implementation might be {@link Editable},
     * don't attempt to make modifications. Modifications not dependent on view state should use
     * {@linkplain #fromXmlString(String, Consumer)} to modify the {@code Spanned staticText}, so
     * that the result is persisted. Modifications that do depend on view state or context should
     * use {@linkplain #edit()} instead.
     */
    @Getter
    private Spanned staticText;

    private Editable editText;

    private RichText(Spanned staticText) {
        this.staticText = staticText;
    }

    /**
     * The Editable contains modifications made after initial parsing, such as adding of Spans that
     * require a Context or laid out TextView to be instantiated. Modifications will be kept in
     * memory, but not persisted when parcelled. Before making modifications to this Editable,
     * make sure those same modifications weren't already made by a previous invocation.
     *
     * @return Modifiable copy of the parsed Rich Text
     */
    public Editable edit() {
        editText = Optional.ofNullable(editText)
                .orElseGet(() -> new SpannableStringBuilder(staticText));
        return editText;
    }

    @Override
    public int length() {
        return toString().length();
    }

    @Override
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @NonNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    @NonNull
    @Override
    public String toString() {
        return staticText.toString();
    }

    public static class RichTextConverter implements ParcelConverter<RichText> {
        @Override
        public void toParcel(RichText input, android.os.Parcel parcel) {
            Spanned spanned = input.staticText;
            TextUtils.writeToParcel(spanned, parcel, 0);
            RichTextParcelableSpan[] customSpans = spanned.getSpans(0, spanned.length(), RichTextParcelableSpan.class);
            parcel.writeInt(customSpans.length);
            for (RichTextParcelableSpan customSpan : customSpans) {
                parcel.writeInt(customSpan.getSpanTypeId());
                customSpan.writeToParcel(parcel, 0);
                parcel.writeInt(spanned.getSpanStart(customSpan));
                parcel.writeInt(spanned.getSpanEnd(customSpan));
                parcel.writeInt(spanned.getSpanFlags(customSpan));
            }
        }

        @Override
        public RichText fromParcel(android.os.Parcel parcel) {
            CharSequence text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            Spannable spanned;
            if (text instanceof Spannable) {
                spanned = (Spannable) text;
            } else {
                spanned = new SpannableStringBuilder(text);
            }

            int length = parcel.readInt();
            for (int count = 0; count < length; count++) {
                int type = parcel.readInt();
                if (type == ParagraphMarker.PARAGRAPH_MARKER_ID) {
                    RichTextParcelableSpan span = ParagraphMarker.CREATOR.createFromParcel(parcel);
                    int start = parcel.readInt();
                    int end = parcel.readInt();
                    int flags = parcel.readInt();
                    spanned.setSpan(span, start, end, flags);
                } else {
                    log.error("Unsupported span type {} (on position {})", type, parcel.dataPosition());
                }
            }
            return new RichText(spanned);
        }
    }
}
