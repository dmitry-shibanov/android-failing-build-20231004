package nl.fd.data.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class HomeSectionsResponse {

    @SerializedName("sections")
    @Expose
    private List<HomeSection> sections = new ArrayList<>();
}
