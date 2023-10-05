package nl.fd.data.entity.card.teaser;

import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;
import org.parceler.Parcel;

import lombok.Data;

@Data
@Parcel(Parcel.Serialization.BEAN)
public class Update {

    @Expose
    private DateTime date;

    @Expose
    private Integer expireSeconds;
}