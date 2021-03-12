package ontime.app.model.restaurantlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantExample {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responceData")
    @Expose
    private List<ResponceDatum> responceData = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResponceDatum> getResponceData() {
        return responceData;
    }

    public void setResponceData(List<ResponceDatum> responceData) {
        this.responceData = responceData;
    }
}
