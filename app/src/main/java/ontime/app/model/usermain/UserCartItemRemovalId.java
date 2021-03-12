package ontime.app.model.usermain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCartItemRemovalId {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("removal")
    @Expose
    private String removal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemoval() {
        return removal;
    }

    public void setRemoval(String removal) {
        this.removal = removal;
    }
}
