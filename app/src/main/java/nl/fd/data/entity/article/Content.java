package nl.fd.data.entity.article;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.Objects;

import lombok.Getter;

/**
 * Base class for the content.
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
@org.parceler.Parcel(converter = Content.ContentConverter.class)
public abstract class Content extends BaseObservable implements ContentComparable {
    @Getter(onMethod = @__({@ContentType, @NonNull}))
    @ContentType
    final String type;

    protected Content(@ContentType String type) {
        this.type = type;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Content)) return false;
        final Content other = (Content) o;
        if (!other.canEqual((Object) this)) return false;
        return Objects.equals(this.type, other.type);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Content;
    }

    public int hashCode() {
        final var PRIME = 59;
        var result = 1;
        final Object hashType = this.type;
        result = result * PRIME + (hashType == null ? 43 : hashType.hashCode());
        return result;
    }

    public static class ContentConverter implements ParcelConverter<Content> {
        @Override
        public void toParcel(Content input, Parcel parcel) {
            parcel.writeParcelable(Parcels.wrap(input), 0);
        }

        @Override
        public Content fromParcel(Parcel parcel) {
            return Parcels.unwrap(parcel.readParcelable(Content.class.getClassLoader()));
        }
    }

    @Override
    public boolean sameItem(Object other) {
        return equals(other);
    }

    @Override
    public boolean sameContents(Object other) {
        return sameItem(other);
    }
}