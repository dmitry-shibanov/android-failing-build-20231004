package nl.fd.ui.richtext;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;

import org.xml.sax.XMLReader;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RichTextTagHandler implements Html.TagHandler {

    public static final RichTextTagHandler INSTANCE = new RichTextTagHandler();

    private static class RemoveContents implements ContentHandler {

        @Override
        public void handleContent(Editable output) {
            removeTagAndContents(output, RemoveContents.class);
        }
    }

    private static class IgnoreTag implements ContentHandler {

        @Override
        public void handleContent(Editable output) {
            IgnoreTag instance = getLast(output, IgnoreTag.class);
            output.removeSpan(instance);
        }

        static ContentType<IgnoreTag> contentType() {
            return new ContentType<IgnoreTag>(IgnoreTag.class) {
                @Override
                protected IgnoreTag instance() {
                    return new IgnoreTag();
                }
            };
        }
    }

    @RequiredArgsConstructor
    protected static abstract class AttributeConsumer<P, T> implements ContentHandler {

        private final Class<P> parentKind;
        private final Class<T> attributeKind;

        abstract void handleAttribute(P parent, CharSequence attributeContent);

        public void handleContent(Editable output) {
            CharSequence content = getValue(output, attributeKind);
            P parent = getLast(output, parentKind);
            removeTagAndContents(output, attributeKind);

            handleAttribute(parent, content);
        }
    }

    private static final ContentType<RemoveContents> REMOVE_CONTENTS_CONTENT_TYPE = new ContentType<RemoveContents>(RemoveContents.class) {
        @Override
        protected RemoveContents instance() {
            return new RemoveContents();
        }
    };

    private static final Map<String, ContentType<?>> SUPPORTED_TYPES;

    static {
        SUPPORTED_TYPES = new HashMap<>();
        SUPPORTED_TYPES.put("body", IgnoreTag.contentType());
        SUPPORTED_TYPES.put("html", IgnoreTag.contentType());
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        ContentType<?> contentType = SUPPORTED_TYPES.get(tag);
        if (contentType == null) {
            contentType = REMOVE_CONTENTS_CONTENT_TYPE;
        }

        if (opening) {
            start(output, contentType.instance());
        } else {
            ContentHandler contentHandler = getLast(output, contentType.getType());
            if (contentHandler == null) {
                log.warn("No span of type {} found for closing tag '{}'.", contentType.getType().getSimpleName(), tag);
                return;
            }
            contentHandler.handleContent(output);
        }

    }

    private static void start(Editable output, Object tag) {
        int len = output.length();
        output.setSpan(tag, len, len, Spanned.SPAN_MARK_MARK);

    }

    private static <T> void removeTagAndContents(Editable output, Class<T> tagType) {
        T obj = getLast(output, tagType);
        // start of the tag
        int where = output.getSpanStart(obj);
        // removeTagAndContents of the tag
        int len = output.length();

        output.removeSpan(obj);

        if (where != len) {
            output.delete(where, len);
        }
    }

    private static <T> CharSequence getValue(Editable output, Class<T> tagType) {
        Object obj = getLast(output, tagType);
        // start of the tag
        int where = output.getSpanStart(obj);
        // removeTagAndContents of the tag
        int len = output.length();

        return output.subSequence(where, len);
    }

    /**
     * Get last marked position of a specific tag kind (private class)
     */
    private static <T> T getLast(Editable text, Class<T> kind) {
        T[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        } else {
            for (int i = objs.length; i > 0; i--) {
                if (text.getSpanFlags(objs[i - 1]) == Spanned.SPAN_MARK_MARK) {
                    return objs[i - 1];
                }
            }
            return null;
        }
    }
}
