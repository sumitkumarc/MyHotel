package ontime.app.restaurant.model.readerOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReaderRemovalId {
    @SerializedName("removal")
    @Expose
    private String removal;

    public String getRemoval() {
        return removal;
    }

    public void setRemoval(String removal) {
        this.removal = removal;
    }
}
