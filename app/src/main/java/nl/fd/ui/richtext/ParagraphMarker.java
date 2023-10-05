package nl.fd.ui.richtext;

import android.os.Parcel;
import android.text.Editable;
import android.text.Spanned;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ParagraphMarker implements RichTextParcelableSpan {
    private static final String PARAGRAPH_MARKER_STRING = "\n\n";
    private static final int MARKER_LENGTH = PARAGRAPH_MARKER_STRING.length();

    // NOT the class hashCode, as that will create a different id in different classloaders
    // So just an int high enoug to be different from the ids of the span types in TextUtils
    public static final int PARAGRAPH_MARKER_ID = 1000 + 1;

    @Getter
    private final String annotation;

    private ParagraphMarker(Parcel in) {
        annotation = in.readString();
    }

    @Override
    public int getSpanTypeId() {
        return PARAGRAPH_MARKER_ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(annotation);
    }

    public static void markParagraphs(Editable richText) {
        int lastPos = 0 - MARKER_LENGTH;
        int pos;
        String plainText = richText.toString();
        while (lastPos + MARKER_LENGTH < plainText.length() && (pos = plainText.indexOf(PARAGRAPH_MARKER_STRING, lastPos + MARKER_LENGTH)) >= 0) {
            richText.setSpan(new ParagraphMarker("p" + pos + "->" + lastPos), lastPos + MARKER_LENGTH, pos, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            lastPos = pos;
        }

        if (lastPos + MARKER_LENGTH != plainText.length()) {
            richText.setSpan(new ParagraphMarker("(last) p" + lastPos + "->" + plainText.length()), lastPos + MARKER_LENGTH, plainText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
    }

    public static final Creator<ParagraphMarker> CREATOR = new Creator<ParagraphMarker>() {
        @Override
        public ParagraphMarker createFromParcel(Parcel in) {
            return new ParagraphMarker(in);
        }

        @Override
        public ParagraphMarker[] newArray(int size) {
            return new ParagraphMarker[size];
        }
    };

}
