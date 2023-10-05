package nl.fd.data.entity.card;

import android.content.Context;

import androidx.annotation.DimenRes;
import androidx.annotation.Px;

import java.util.Random;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
public class Whitespace implements Card {

    @Getter
    @EqualsAndHashCode.Exclude
    final long uniqueRecyclerId = new Random().nextLong();

    private String id;

    private final int heightPx;

    private Whitespace(int heightPx) {
        this.heightPx = heightPx;
        id = "ws" + heightPx;
    }

    public static Whitespace px(@Px int heightPx) {
        return new Whitespace(heightPx);
    }

    public static Whitespace res(@DimenRes int res, Context context) {
        return px((int) context.getResources().getDimension(res));
    }
}
