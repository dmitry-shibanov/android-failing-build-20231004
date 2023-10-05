package nl.fd.ui.richtext;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract class ContentType<T extends ContentHandler> {
    @Getter
    private final Class<T> type;


    protected abstract T instance();

}
